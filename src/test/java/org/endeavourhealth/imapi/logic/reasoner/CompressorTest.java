package org.endeavourhealth.imapi.logic.reasoner;

import java.util.*;

public class CompressorTest {

    static Set<String> desc(SetDefinitionCompressor.Hierarchy h, String root) {
        Set<String> out = new HashSet<>();
        Deque<String> q = new ArrayDeque<>(h.children(root));
        while (!q.isEmpty()) { String n = q.poll(); if (out.add(n)) q.addAll(h.children(n)); }
        return out;
    }

    static Set<String> eval(SetDefinitionCompressor.Hierarchy h, SetDefinitionCompressor.Term t) {
        Set<String> s = new HashSet<>();
        switch (t.op()) {
            case SELF -> s.add(t.iri());
            case DESC -> s.addAll(desc(h, t.iri()));
            case DESC_OR_SELF -> { s.add(t.iri()); s.addAll(desc(h, t.iri())); }
        }
        for (SetDefinitionCompressor.Term m : t.minus()) s.removeAll(eval(h, m));
        return s;
    }

    static Set<String> eval(SetDefinitionCompressor.Hierarchy h, List<SetDefinitionCompressor.Term> terms) {
        Set<String> s = new HashSet<>();
        for (SetDefinitionCompressor.Term t : terms) s.addAll(eval(h, t));
        return s;
    }

    static void example(String title, String[][] edges, String... members) {
        var h = new SetDefinitionCompressor.InMemoryHierarchy();
        for (String[] e : edges) h.addEdge(e[0], e[1]);
        Set<String> s = new HashSet<>(Arrays.asList(members));
        var terms = new SetDefinitionCompressor(h, s, 2).compress();
        boolean ok = eval(h, terms).equals(s);
        System.out.println("== " + title + "  (exact: " + ok + ")");
        System.out.println(SetDefinitionCompressor.toEcl(terms, x -> x) + "\n");
    }

    public static void main(String[] args) {
        // M has 10 children; 9 (with their subtrees) are in the set, c10 is not
        List<String[]> e1 = new ArrayList<>();
        List<String> m1 = new ArrayList<>(List.of("M"));
        for (int i = 1; i <= 10; i++) {
            e1.add(new String[]{"c" + i, "M"});
            e1.add(new String[]{"g" + i, "c" + i});
            if (i < 10) { m1.add("c" + i); m1.add("g" + i); }
        }
        example("Nearly all of M (user's case)", e1.toArray(new String[0][]), m1.toArray(new String[0]));

        // Same but M itself is not in the set -> "< M MINUS << c10"
        m1.remove("M");
        example("All but one child of non-member M", e1.toArray(new String[0][]), m1.toArray(new String[0]));

        // Nested exclusion / re-inclusion
        example("Nested", new String[][]{
                {"a", "T"}, {"b", "T"}, {"n", "T"}, {"y", "n"}, {"z", "n"}, {"x", "y"}, {"w", "y"}},
                "T", "a", "b", "y", "w");

        // Fuzz: random DAGs with clustered sets; check exactness and compare to no-exclusion baseline
        Random r = new Random(42);
        int trials = 3000, worse = 0;
        long totalNew = 0, totalBaseline = 0;
        for (int trial = 0; trial < trials; trial++) {
            int n = 20 + r.nextInt(60);
            var h = new SetDefinitionCompressor.InMemoryHierarchy();
            for (int i = 1; i < n; i++) {
                int k = 1 + (r.nextInt(4) == 0 ? 1 + r.nextInt(2) : 0);
                Set<Integer> ps = new HashSet<>();
                for (int j = 0; j < k; j++) ps.add(r.nextInt(i));
                for (int p : ps) h.addEdge("n" + i, "n" + p);
            }
            // clustered set: take some subtrees, punch some holes, add some singles
            Set<String> s = new HashSet<>();
            for (int j = 0; j < 1 + r.nextInt(3); j++) {
                String root = "n" + r.nextInt(n);
                s.add(root); s.addAll(desc(h, root));
            }
            for (int j = 0; j < r.nextInt(4); j++) { String x = "n" + r.nextInt(n); s.remove(x); if (r.nextBoolean()) s.removeAll(desc(h, x)); }
            for (int j = 0; j < r.nextInt(3); j++) s.add("n" + r.nextInt(n));
            if (s.isEmpty()) continue;

            var terms = new SetDefinitionCompressor(h, s, 2).compress();
            if (!eval(h, terms).equals(s)) {
                System.out.println("MISMATCH in trial " + trial);
                return;
            }
            int cost = 0; for (var t : terms) cost += t.cost();
            int baseline = baseline(h, s);
            totalNew += cost; totalBaseline += baseline;
            if (cost > baseline) worse++;
        }
        System.out.printf("Fuzz: %d trials all exact. Avg terms %.2f vs %.2f without exclusions; worse in %d trials%n",
                trials, totalNew / (double) trials, totalBaseline / (double) trials, worse);
    }

    /** Previous approach: maximal fully-contained members as <<, the rest as self. */
    static int baseline(SetDefinitionCompressor.Hierarchy h, Set<String> s) {
        Set<String> cand = new HashSet<>();
        for (String m : s) if (s.containsAll(desc(h, m))) cand.add(m);
        int cost = s.size() - cand.size();
        for (String c : cand) {
            boolean hasCandParent = false;
            for (String p : h.parents(c)) if (cand.contains(p)) hasCandParent = true;
            if (!hasCandParent) cost++;
        }
        return cost;
    }
}
