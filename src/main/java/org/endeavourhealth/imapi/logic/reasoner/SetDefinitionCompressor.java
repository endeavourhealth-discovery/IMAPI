package org.endeavourhealth.imapi.logic.reasoner;

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
 */
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

    private final Map<String, Set<String>> ancestorCache = new HashMap<>();
    private final Map<String, List<Term>> nodeMemo = new HashMap<>();
    private final Map<String, Term> liftMemo = new HashMap<>();
    private final Set<String> inProgress = new HashSet<>();

    public void compressSetDefinition(String setIri){

  }

    /**
     * @param maxLiftDepth how many levels above a group of tops to look for a non-member
     *                     ancestor that can be used as "< ancestor" (0 disables lifting).
     */
    public SetDefinitionCompressor(Hierarchy h, Set<String> members, int maxLiftDepth) {
        this.h = h;
        this.members = members;
        this.maxLiftDepth = maxLiftDepth;
    }

    /** The definition: a union of terms. */
    public List<Term> compress() {
        List<String> tops = new ArrayList<>();
        for (String m : members) {
            boolean hasMemberAncestor = false;
            for (String a : ancestors(m)) {
                if (members.contains(a)) { hasMemberAncestor = true; break; }
            }
            if (!hasMemberAncestor) tops.add(m);
        }
        Collections.sort(tops);
        return cover(null, tops, true);
    }

    // ------------------------------------------------------------------ core

    private boolean in(String c) { return members.contains(c); }

    /** Plan for node t: the nodes with t's polarity in descendantsOrSelf(t). */
    private List<Term> planNode(String t) {
        List<Term> cached = nodeMemo.get(t);
        if (cached != null) return cached;
        if (!inProgress.add(t)) throw new IllegalStateException("subClassOf cycle at " + t);

        boolean want = in(t);

        // Option A:  << t  MINUS (everything of the other polarity beneath it)
        Term a = new Term(Op.DESC_OR_SELF, t, cover(t, frontier(t, !want), !want));

        // Option B:  t (self only)  OR  (same-polarity nodes beneath it, covered separately)
        List<Term> b = new ArrayList<>();
        b.add(new Term(Op.SELF, t, List.of()));
        b.addAll(cover(t, frontier(t, want), want));

        List<Term> best = a.cost() <= totalCost(b) ? List.of(a) : List.copyOf(b);
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
                    if (isStrictDescendant(t, a)) { covered.add(t); current += totalCost(planNode(t)); }
                }
                if (covered.size() < 2) continue;
                int saving = current - lift(a).cost();
                if (saving > bestSaving) { bestSaving = saving; bestA = a; bestCovered = covered; }
            }
            if (bestA == null) break;
            result.add(lift(bestA));
            lifted.add(bestA);
            remaining.removeAll(bestCovered);
        }

        // Multiple inheritance: a node beneath t may already be covered by a sibling term in this
        // same union. Then "t (self) OR <the rest>" can beat the memoised context-free plan.
        // Tops another top relies on are frozen (must keep full coverage); partial ones can't be relied on.
        Set<String> relied = new HashSet<>();
        Set<String> partial = new HashSet<>();
        for (String t : remaining) {
            List<Term> plan = planNode(t);
            if (!relied.contains(t)) {
                List<String> full = frontier(t, want);
                List<String> needed = new ArrayList<>();
                Set<String> usedTops = new HashSet<>();
                for (String f : full) {
                    String coveringTop = null;
                    for (String t2 : remaining) {
                        if (!t2.equals(t) && !partial.contains(t2) && (f.equals(t2) || isStrictDescendant(f, t2))) {
                            coveringTop = t2; break;
                        }
                    }
                    boolean byLift = false;
                    for (String a : lifted) if (isStrictDescendant(f, a)) { byLift = true; break; }
                    if (coveringTop != null) usedTops.add(coveringTop);
                    else if (!byLift) needed.add(f);
                }
                if (needed.size() < full.size()) {
                    List<Term> alt = new ArrayList<>();
                    alt.add(new Term(Op.SELF, t, List.of()));
                    alt.addAll(cover(t, needed, want));
                    if (totalCost(alt) < totalCost(plan)) {
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
        List<Term> out = new ArrayList<>();
        for (Term x : terms) {
            boolean subsumed = false;
            for (Term y : terms) {
                if (y != x && y.op() != Op.SELF && isStrictDescendant(x.iri(), y.iri())) { subsumed = true; break; }
            }
            if (!subsumed && !out.contains(x)) out.add(x);
        }
        return out;
    }

    /**
     * Topmost nodes of polarity {@code want} strictly beneath root: walk down through nodes of
     * the other polarity, stop at the first {@code want} node on each path, then drop any found
     * node that has a {@code want}-polarity ancestor inside root's subtree (that one covers it).
     */
    private List<String> frontier(String root, boolean want) {
        Set<String> found = new LinkedHashSet<>();
        Set<String> seen = new HashSet<>();
        Deque<String> queue = new ArrayDeque<>();
        for (String c : h.children(root)) if (!c.equals(root) && seen.add(c)) queue.add(c);
        while (!queue.isEmpty()) {
            String n = queue.poll();
            if (in(n) == want) { found.add(n); continue; }
            for (String c : h.children(n)) if (seen.add(c)) queue.add(c);
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
        return tops;
    }

    // ------------------------------------------------------------- helpers

    private static int totalCost(List<Term> terms) {
        int c = 0;
        for (Term t : terms) c += t.cost();
        return c;
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

    public static String toEcl(List<Term> terms, Function<String, String> label) {
        List<String> parts = new ArrayList<>();
        for (Term t : terms) parts.add(toEcl(t, label));
        return String.join("\nOR ", parts);
    }

    private static String toEcl(Term t, Function<String, String> label) {
        String base = switch (t.op()) {
            case SELF -> label.apply(t.iri());
            case DESC -> "< " + label.apply(t.iri());
            case DESC_OR_SELF -> "<< " + label.apply(t.iri());
        };
        if (t.minus().isEmpty()) return base;
        List<String> parts = new ArrayList<>();
        for (Term m : t.minus()) parts.add(toEcl(m, label));
        String minus = parts.size() == 1 ? parts.get(0) : "(" + String.join(" OR ", parts) + ")";
        return "(" + base + " MINUS " + minus + ")";
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
