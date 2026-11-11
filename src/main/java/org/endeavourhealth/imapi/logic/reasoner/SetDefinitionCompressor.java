package org.endeavourhealth.imapi.logic.reasoner;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.endeavourhealth.imapi.dataaccess.databases.IMDB;
import org.endeavourhealth.imapi.vocabulary.NAMESPACE;

import java.util.*;
import java.util.function.Function;

/**
 * Turns an enumerated set of concepts into a compact intensional definition using
 * descendants-or-self (<<), descendants-only (<), self, and nested MINUS (exclusions).
 *
 * Invariant used throughout: for a node n whose membership is X, plan(n) denotes exactly
 * the nodes of polarity X in descendantsOrSelf(n). "Polarity" = member / non-member.
 * An inclusion's MINUS clause is therefore a plan over the non-members beneath it, whose
 * own MINUS clauses re-include members, and so on, to whatever depth the data needs.
 *
 * {@link #compressSetDefinition(String)} is the database entry point: it takes a set IRI, reads its
 * im:hasMember members and the surrounding hierarchy, and returns ECL that avoids high level
 * ancestors whose MINUS list would be long.
 */
@Slf4j
public final class SetDefinitionCompressor {

    public interface Hierarchy {
        Collection<String> parents(String iri);   // direct rdfs:subClassOf targets
        Collection<String> children(String iri);  // direct subclasses
    }

    public enum Op { SELF, DESC, DESC_OR_SELF }

    /** op(iri) MINUS (minus[0] OR minus[1] ...) */
    public record Term(Op op, String iri, List<Term> minus) {
        public int cost() {
            int c = 1;
            for (Term m : minus) c += m.cost();
            return c;
        }
    }

    private final Hierarchy h;
    private final Set<String> members;
    private final int maxLiftDepth;

    /** Cost of one MINUS line relative to one inclusion line (1 = plain shortest definition). */
    private final int minusWeight;

    private final Map<String, Set<String>> ancestorCache;
    /**
     * Members and all their ancestors. A node outside this set has no member at or beneath it,
     * so its whole subtree is non-member and never needs walking or planning.
     */
    private final Set<String> memberOrAbove;
    private final Map<String, List<Term>> nodeMemo = new HashMap<>();
    private final Map<String, Term> liftMemo = new HashMap<>();
    private final Map<CoverKey, List<Term>> coverMemo = new HashMap<>();
    private final Map<String, List<String>> memberFrontierMemo = new HashMap<>();
    private final Map<String, List<String>> nonMemberFrontierMemo = new HashMap<>();
    private final Set<String> inProgress = new HashSet<>();

    /**
     * @param maxLiftDepth how many levels above a group of tops to look for a non-member
     *                     ancestor that can be used as "< ancestor" (0 disables lifting).
     */
    public SetDefinitionCompressor(Hierarchy h, Set<String> members, int maxLiftDepth) {
        this(h, members, maxLiftDepth, 1, new HashMap<>());
    }

    /**
     * @param minusWeight how many inclusion lines one MINUS line is worth. 1 gives the plain shortest
     *                    definition; higher values prefer listing inclusions, so a high level
     *                    "<< ancestor" is only used when its MINUS list is short compared with
     *                    the inclusion lines it replaces.
     */
    public SetDefinitionCompressor(Hierarchy h, Set<String> members, int maxLiftDepth, int minusWeight) {
        this(h, members, maxLiftDepth, minusWeight, new HashMap<>());
    }

    private SetDefinitionCompressor(Hierarchy h, Set<String> members, int maxLiftDepth, int minusWeight,
                                    Map<String, Set<String>> ancestorCache) {
        if (minusWeight < 1) throw new IllegalArgumentException("minusWeight must be at least 1");
        this.h = h;
        this.members = members;
        this.maxLiftDepth = maxLiftDepth;
        this.minusWeight = minusWeight;
        this.ancestorCache = ancestorCache;
        this.memberOrAbove = new HashSet<>(members);
        for (String m : members) this.memberOrAbove.addAll(ancestors(m));
    }

    /** Concept lines that add members: top-level terms and re-inclusions nested at even depth. */
    public static int inclusionLines(List<Term> terms) { return countLines(terms, true, true); }

    /** Concept lines that remove members: terms nested at odd depth. */
    public static int minusLines(List<Term> terms) { return countLines(terms, true, false); }

    private static int countLines(List<Term> terms, boolean inclusion, boolean countInclusions) {
        int n = 0;
        for (Term t : terms) {
            if (inclusion == countInclusions) n++;
            n += countLines(t.minus(), !inclusion, countInclusions);
        }
        return n;
    }

    // ------------------------------------------------------------ database

    /** Every relationship the im:isA closure is built from. */
    private static final String SUBTYPE = "(rdfs:subClassOf|rdfs:subPropertyOf|im:localSubClassOf|im:previousEntityOf)";
    private static final String UNSAFE_IRI_CHARS = "<>\"{}|^`\\";
    private static final int LIFT_DEPTH = 2;
    /** A MINUS line counts as this many inclusion lines, so ancestors needing long MINUS lists are not used. */
    private static final int MINUS_WEIGHT = 2;
    private static final int BATCH = 500;

    /**
     * Reverse engineers ECL for a set from its im:hasMember members.
     *
     * @param setIri iri of the set
     * @return ECL, one concept per line, whose expansion (<< and < evaluated with im:isA) is exactly
     * the members. MINUS is used only where it is clearly shorter than listing the inclusions.
     */

    /**
     * The definition must expand to exactly the members, both in the loaded hierarchy and in the
     * database's own im:isA closure (which is what << and < are evaluated against).
     */
    private static void verify(IMDB conn, InMemoryHierarchy h, Set<String> members, List<Term> terms, String setIri) {
        if (!evaluate(h, terms).equals(members))
            throw new IllegalStateException("Compressed definition of " + setIri + " does not expand to its members");
        Set<String> subsumed = new LinkedHashSet<>();
        collectSubsumptionRoots(terms, subsumed);
        List<String> list = new ArrayList<>(subsumed);
        for (int i = 0; i < list.size(); i += BATCH) {
            for (String[] row : select(conn, "SELECT ?a (COUNT(?d) AS ?size) WHERE { "
                + values("a", list.subList(i, Math.min(i + BATCH, list.size())))
                + " ?d im:isA ?a } GROUP BY ?a", "a", "size")) {
                long loaded = 1L + descendants(h, row[0]).size();
                if (Long.parseLong(row[1]) != loaded)
                    throw new IllegalStateException("im:isA closure of " + row[0] + " has " + row[1]
                        + " entries but its subtype hierarchy has " + loaded + "; cannot compress " + setIri + " reliably");
            }
        }
    }

    private static Set<String> evaluate(Hierarchy h, List<Term> terms) {
        Set<String> out = new HashSet<>();
        for (Term t : terms) {
            Set<String> s = new HashSet<>();
            if (t.op() != Op.DESC) s.add(t.iri());
            if (t.op() != Op.SELF) s.addAll(descendants(h, t.iri()));
            s.removeAll(evaluate(h, t.minus()));
            out.addAll(s);
        }
        return out;
    }

    private static Set<String> descendants(Hierarchy h, String root) {
        Set<String> out = new HashSet<>();
        Deque<String> queue = new ArrayDeque<>(h.children(root));
        while (!queue.isEmpty()) {
            String n = queue.poll();
            if (!n.equals(root) && out.add(n)) queue.addAll(h.children(n));
        }
        return out;
    }

    private static void collectIris(List<Term> terms, Set<String> out) {
        for (Term t : terms) {
            out.add(t.iri());
            collectIris(t.minus(), out);
        }
    }

    private static void collectSubsumptionRoots(List<Term> terms, Set<String> out) {
        for (Term t : terms) {
            if (t.op() != Op.SELF) out.add(t.iri());
            collectSubsumptionRoots(t.minus(), out);
        }
    }

    private static void addEdges(IMDB conn, String sparql, Map<String, Set<String>> parentsOf) {
        for (String[] row : select(conn, sparql, "c", "p")) {
            Set<String> parents = parentsOf.computeIfAbsent(row[0], k -> new HashSet<>());
            if (row[1] != null && !row[1].equals(row[0])) parents.add(row[1]);
        }
    }

    private static String values(String variable, List<String> iris) {
        StringBuilder sb = new StringBuilder("VALUES ?").append(variable).append(" {");
        for (String iri : iris) sb.append(" <").append(iri).append('>');
        return sb.append(" }").toString();
    }

    /** Rows of the named variables as strings; an unbound variable is null. Rows without the first variable are dropped. */
    private static List<String[]> select(IMDB conn, String sparql, String... variables) {
        List<String[]> rows = new ArrayList<>();
        try (TupleQueryResult rs = conn.prepareTupleSparql(sparql).evaluate()) {
            while (rs.hasNext()) {
                BindingSet bs = rs.next();
                String[] row = new String[variables.length];
                for (int i = 0; i < variables.length; i++) {
                    Value v = bs.getValue(variables[i]);
                    row[i] = v == null ? null : v.stringValue();
                }
                if (row[0] != null) rows.add(row);
            }
        }
        return rows;
    }

    /** Snomed concepts by code, anything else by iri, with the name as the ECL term. */
    private static String eclReference(String iri, String name) {
        String id = iri.startsWith(NAMESPACE.SNOMED.toString()) ? iri.substring(iri.lastIndexOf('#') + 1) : iri;
        return name == null ? id : id + " |" + name.replace('|', '/').trim() + "|";
    }

    /** The definition: a union of terms. */
    public String compress() {
        List<String> tops = new ArrayList<>();
        for (String m : members) {
            boolean hasMemberAncestor = false;
            for (String a : ancestors(m)) {
                if (members.contains(a)) { hasMemberAncestor = true; break; }
            }
            if (!hasMemberAncestor) tops.add(m);
        }
        Collections.sort(tops);
        List<Term> terms = cover(null, tops, true);
      return toEcl(terms, iri -> iri.substring(iri.lastIndexOf('#') + 1));
    }

    // ------------------------------------------------------------------ core

    private boolean in(String c) { return members.contains(c); }

    /** Plan for node t: the nodes with t's polarity in descendantsOrSelf(t). */
    private List<Term> planNode(String t) {
        List<Term> cached = nodeMemo.get(t);
        if (cached != null) return cached;
        // No member at or beneath t: the subtree is uniformly non-member, so "<< t" is the plan.
        if (!memberOrAbove.contains(t)) return List.of(new Term(Op.DESC_OR_SELF, t, List.of()));
        if (!inProgress.add(t)) throw new IllegalStateException("subClassOf cycle at " + t);

        boolean want = in(t);

        // Option A:  << t  MINUS (everything of the other polarity beneath it)
        Term a = new Term(Op.DESC_OR_SELF, t, cover(t, frontier(t, !want), !want));

        // Option B:  t (self only)  OR  (same-polarity nodes beneath it, covered separately)
        List<Term> b = new ArrayList<>();
        b.add(new Term(Op.SELF, t, List.of()));
        b.addAll(cover(t, frontier(t, want), want));

        List<Term> best = cost(a, want) <= totalCost(b, want) ? List.of(a) : List.copyOf(b);
        inProgress.remove(t);
        nodeMemo.put(t, best);
        return best;
    }

    /** "< a MINUS ..." : the nodes of polarity opposite to a, strictly beneath a. */
    private Term lift(String a) {
        Term cached = liftMemo.get(a);
        if (cached != null) return cached;
        boolean aPolarity = in(a);
        Term t = new Term(Op.DESC, a, cover(a, frontier(a, aPolarity), aPolarity));
        liftMemo.put(a, t);
        return t;
    }

    /**
     * Cover the given top nodes (all of polarity {@code want}, all strictly beneath scope,
     * or anywhere when scope is null). Greedily replaces groups of tops with "< ancestor"
     * when that is cheaper, then plans each remaining top.
     */
    private List<Term> cover(String scope, List<String> tops, boolean want) {
        if (tops.isEmpty()) return List.of();
        // The result depends only on the arguments, and the same sub-problem recurs many times
        // beneath nodes with several parents.
        CoverKey key = new CoverKey(scope, List.copyOf(tops), want);
        List<Term> cached = coverMemo.get(key);
        if (cached == null) {
            cached = List.copyOf(computeCover(scope, tops, want));
            coverMemo.put(key, cached);
        }
        return cached;
    }

    private record CoverKey(String scope, List<String> tops, boolean want) {}

    private List<Term> computeCover(String scope, List<String> tops, boolean want) {
        List<String> remaining = new ArrayList<>(tops);
        List<Term> result = new ArrayList<>();
        List<String> lifted = new ArrayList<>();

        while (maxLiftDepth > 0 && remaining.size() >= 2) {
            Set<String> candidates = new TreeSet<>();
            for (String t : remaining) {
                for (String a : ancestorsUpTo(t, maxLiftDepth)) {
                    if (in(a) != want && (scope == null || isStrictDescendant(a, scope))) {
                        candidates.add(a);
                    }
                }
            }
            String bestA = null;
            List<String> bestCovered = null;
            int bestSaving = 0;
            for (String a : candidates) {
                List<String> covered = new ArrayList<>();
                int current = 0;
                for (String t : remaining) {
                    if (isStrictDescendant(t, a)) { covered.add(t); current += totalCost(planNode(t), want); }
                }
                if (covered.size() < 2) continue;
                int saving = current - cost(lift(a), want);
                if (saving > bestSaving) { bestSaving = saving; bestA = a; bestCovered = covered; }
            }
            if (bestA == null) break;
            result.add(lift(bestA));
            lifted.add(bestA);
            remaining.removeAll(new HashSet<>(bestCovered));
        }

        Map<String, Integer> position = new HashMap<>();
        for (int i = 0; i < remaining.size(); i++) position.putIfAbsent(remaining.get(i), i);

        // Multiple inheritance: a node beneath t may already be covered by a sibling term in this
        // same union. Then "t (self) OR <the rest>" can beat the memoised context-free plan.
        // Tops another top relies on are frozen (must keep full coverage); partial ones can't be relied on.
        Set<String> relied = new HashSet<>();
        Set<String> partial = new HashSet<>();
        for (String t : remaining) {
            List<Term> plan = planNode(t);
            // A single line with no MINUS is already the cheapest possible plan; nothing to improve.
            boolean minimal = plan.size() == 1 && plan.get(0).minus().isEmpty();
            if (!minimal && !relied.contains(t)) {
                List<String> full = frontier(t, want);
                List<String> needed = new ArrayList<>();
                Set<String> usedTops = new HashSet<>();
                for (String f : full) {
                    // First remaining top (other than t, not partial) that is f or an ancestor of f.
                    int first = coveringPosition(f, t, position, partial, Integer.MAX_VALUE);
                    for (String x : ancestors(f)) first = coveringPosition(x, t, position, partial, first);
                    String coveringTop = first == Integer.MAX_VALUE ? null : remaining.get(first);
                    boolean byLift = false;
                    for (String a : lifted) if (isStrictDescendant(f, a)) { byLift = true; break; }
                    if (coveringTop != null) usedTops.add(coveringTop);
                    else if (!byLift) needed.add(f);
                }
                if (needed.size() < full.size()) {
                    List<Term> alt = new ArrayList<>();
                    alt.add(new Term(Op.SELF, t, List.of()));
                    alt.addAll(cover(t, needed, want));
                    if (totalCost(alt, want) < totalCost(plan, want)) {
                        plan = alt;
                        partial.add(t);
                        relied.addAll(usedTops);
                    }
                }
            }
            result.addAll(plan);
        }
        return dropSubsumed(result);
    }

    /**
     * Every term in one union has the same polarity, and a << or < term covers ALL nodes of that
     * polarity beneath its root. So any term whose root lies strictly beneath such a term's root
     * is redundant (happens with multiple inheritance, where flattened sub-plans overlap).
     */
    private List<Term> dropSubsumed(List<Term> terms) {
        Set<String> roots = new HashSet<>();
        for (Term y : terms) if (y.op() != Op.SELF) roots.add(y.iri());
        Set<Term> out = new LinkedHashSet<>();
        for (Term x : terms) {
            if (roots.isEmpty() || Collections.disjoint(ancestors(x.iri()), roots)) out.add(x);
        }
        return new ArrayList<>(out);
    }

    private static int coveringPosition(String candidate, String self, Map<String, Integer> position,
                                        Set<String> partial, int best) {
        Integer i = position.get(candidate);
        return i != null && i < best && !candidate.equals(self) && !partial.contains(candidate) ? i : best;
    }

    /**
     * Topmost nodes of polarity {@code want} strictly beneath root: walk down through nodes of
     * the other polarity, stop at the first {@code want} node on each path, then drop any found
     * node that has a {@code want}-polarity ancestor inside root's subtree (that one covers it).
     */
    private List<String> frontier(String root, boolean want) {
        return (want ? memberFrontierMemo : nonMemberFrontierMemo).computeIfAbsent(root, r -> computeFrontier(r, want));
    }

    private List<String> computeFrontier(String root, boolean want) {
        Set<String> found = new LinkedHashSet<>();
        Set<String> seen = new HashSet<>();
        Deque<String> queue = new ArrayDeque<>();
        // Looking for members: subtrees with no member in them are never entered.
        for (String c : h.children(root))
            if (!c.equals(root) && (!want || memberOrAbove.contains(c)) && seen.add(c)) queue.add(c);
        while (!queue.isEmpty()) {
            String n = queue.poll();
            if (in(n) == want) { found.add(n); continue; }
            for (String c : h.children(n))
                if ((!want || memberOrAbove.contains(c)) && seen.add(c)) queue.add(c);
        }
        List<String> tops = new ArrayList<>();
        for (String f : found) {
            boolean covered = false;
            for (String a : ancestors(f)) {
                if (in(a) == want && isStrictDescendant(a, root)) { covered = true; break; }
            }
            if (!covered) tops.add(f);
        }
        Collections.sort(tops);
        return Collections.unmodifiableList(tops);
    }

    // ------------------------------------------------------------- helpers

    /**
     * Weighted size of a union whose terms select nodes of polarity {@code member}: a line that
     * selects members is an inclusion line (cost 1), one that selects non-members is a MINUS
     * line (cost minusWeight). Polarity flips at each level of nesting.
     */
    private int totalCost(List<Term> terms, boolean member) {
        int c = 0;
        for (Term t : terms) c += cost(t, member);
        return c;
    }

    private int cost(Term t, boolean member) {
        return (member ? 1 : minusWeight) + totalCost(t.minus(), !member);
    }

    private Set<String> ancestors(String c) {
        Set<String> cached = ancestorCache.get(c);
        if (cached != null) return cached;
        Set<String> anc = new HashSet<>();
        Deque<String> queue = new ArrayDeque<>(h.parents(c));
        while (!queue.isEmpty()) {
            String p = queue.poll();
            if (!p.equals(c) && anc.add(p)) queue.addAll(h.parents(p));
        }
        ancestorCache.put(c, anc);
        return anc;
    }

    private boolean isStrictDescendant(String node, String root) {
        return !node.equals(root) && ancestors(node).contains(root);
    }

    private Set<String> ancestorsUpTo(String c, int depth) {
        Set<String> out = new LinkedHashSet<>();
        Set<String> level = Set.of(c);
        for (int d = 0; d < depth && !level.isEmpty(); d++) {
            Set<String> next = new LinkedHashSet<>();
            for (String n : level) for (String p : h.parents(n)) if (!p.equals(n) && out.add(p)) next.add(p);
            level = next;
        }
        return out;
    }

    // ------------------------------------------------------------- output

    /** One concept per line: top-level inclusions joined by OR, exclusions indented under MINUS. */
    public static String toEcl(List<Term> terms, Function<String, String> label) {
        if (terms.size() == 1) return toEcl(terms.get(0), label, "", false);
        List<String> parts = new ArrayList<>();
        for (Term t : terms) parts.add(toEcl(t, label, "", true));
        return String.join("\nOR ", parts);
    }

    private static String toEcl(Term t, Function<String, String> label, String indent, boolean bracket) {
        String base = switch (t.op()) {
            case SELF -> label.apply(t.iri());
            case DESC -> "< " + label.apply(t.iri());
            case DESC_OR_SELF -> "<< " + label.apply(t.iri());
        };
        if (t.minus().isEmpty()) return base;
        String minus;
        if (t.minus().size() == 1) {
            minus = toEcl(t.minus().get(0), label, indent + "  ", true);
        } else {
            List<String> parts = new ArrayList<>();
            for (Term m : t.minus()) parts.add(toEcl(m, label, indent + "    ", true));
            minus = "(" + String.join("\n" + indent + "    OR ", parts) + ")";
        }
        String ecl = base + "\n" + indent + "  MINUS " + minus;
        return bracket ? "(" + ecl + ")" : ecl;
    }

    /** Simple in-memory hierarchy; fill it from SELECT ?c ?p WHERE { ?c rdfs:subClassOf ?p }. */
    public static final class InMemoryHierarchy implements Hierarchy {
        private final Map<String, List<String>> parents = new HashMap<>();
        private final Map<String, List<String>> children = new HashMap<>();

        public void addEdge(String child, String parent) {
            if (child.equals(parent)) return;
            parents.computeIfAbsent(child, k -> new ArrayList<>()).add(parent);
            children.computeIfAbsent(parent, k -> new ArrayList<>()).add(child);
        }

        @Override public Collection<String> parents(String iri) { return parents.getOrDefault(iri, List.of()); }
        @Override public Collection<String> children(String iri) { return children.getOrDefault(iri, List.of()); }
    }
}
