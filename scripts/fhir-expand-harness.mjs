#!/usr/bin/env node
// Test harness for GET /api/fhir/r4/ValueSet/$expand?url=<iri>.
//
// Two modes:
//
//   gold (default)  "Gold standard" regression check. Lists every set in the database, expands them one by one and,
//                   per set: if no output file exists yet, saves the expansion; otherwise compares the expansion with
//                   the file on disk and reports any differences. Run once before a change to capture the baseline,
//                   then again after to prove the set logic is unchanged.
//
//   perf            Repeatedly expands a single IRI and reports latency stats.
//
// Usage:
//   node scripts/fhir-expand-harness.mjs [options]
//
// Common options (or env vars):
//   --mode       MODE          gold | perf                 (default gold)
//   --base-url   IMAPI_URL     Base URL of IMAPI           (default http://localhost:8080)
//   --timeout    TIMEOUT_MS    Per-request timeout, ms     (default 120000)
//   --token      IMAPI_TOKEN   Bearer token; only needed if IMAPI isn't in "public" mode (local dev runs don't need one)
//
// Gold mode options:
//   --out        GOLD_DIR      Directory for expansion files (default scripts/fhir-expand-gold)
//   --update     (flag value true) Overwrite files on disk instead of comparing
//   --filter     FILTER        Only sets whose IRI contains this text
//   --limit      LIMIT         Only the first N sets (after filtering)
//   --concurrency CONCURRENCY  Parallel requests           (default 1)
//   --types      SET_TYPES     Comma separated set type IRIs
//                              (default http://endhealth.info/im#ConceptSet,http://endhealth.info/im#ValueSet)
//
// Perf mode options:
//   --iri        IRI           ValueSet IRI                (default http://apiqcodes.org/qcodes#QPredict_334)
//   --requests   REQUESTS      Total number of requests    (default 50)
//   --concurrency CONCURRENCY  Parallel requests           (default 1)
//   --delay      DELAY_MS      Delay between requests per worker, ms (default 0)
//
// Requires Node 18+ (built-in fetch). No dependencies.

import { existsSync } from "node:fs";
import { mkdir, readFile, writeFile, unlink } from "node:fs/promises";
import path from "node:path";
import { fileURLToPath } from "node:url";

const argv = process.argv.slice(2);
const args = Object.fromEntries(
  argv.reduce((acc, a, i, all) => {
    if (a.startsWith("--")) {
      const next = all[i + 1];
      acc.push([a.slice(2), next === undefined || next.startsWith("--") ? "true" : next]);
    }
    return acc;
  }, [])
);
const opt = (name, env, def) => args[name] ?? process.env[env] ?? def;

const mode = opt("mode", "MODE", "gold");
const baseUrl = opt("base-url", "IMAPI_URL", "http://localhost:8080").replace(/\/+$/, "");
const concurrency = Math.max(1, Number(opt("concurrency", "CONCURRENCY", 1)));
const timeoutMs = Number(opt("timeout", "TIMEOUT_MS", 120000));
const token = opt("token", "IMAPI_TOKEN", undefined);

const headers = { Accept: "application/json" };
if (token) headers.Authorization = `Bearer ${token}`;

const expandUrl = (iri) => `${baseUrl}/api/fhir/r4/ValueSet/$expand?url=${encodeURIComponent(iri)}`;

// Runs fn(item, index) over items with at most `concurrency` in flight.
async function forEachParallel(items, fn) {
  let next = 0;
  const worker = async () => {
    while (true) {
      const i = next++;
      if (i >= items.length) return;
      await fn(items[i], i);
    }
  };
  await Promise.all(Array.from({ length: Math.min(concurrency, items.length) || 1 }, worker));
}

// ---------------------------------------------------------------------------------------------------------------
// gold mode
// ---------------------------------------------------------------------------------------------------------------

async function listSetIris() {
  const types = opt("types", "SET_TYPES", "http://endhealth.info/im#ConceptSet,http://endhealth.info/im#ValueSet")
    .split(",")
    .map((t) => t.trim())
    .filter(Boolean);
  const iris = new Set();
  for (const type of types) {
    const res = await fetch(`${baseUrl}/api/entity/protected/type/entities?iri=${encodeURIComponent(type)}`, {
      headers,
      signal: AbortSignal.timeout(timeoutMs),
    });
    if (!res.ok) {
      throw new Error(`Listing sets of type ${type} failed: HTTP ${res.status} ${(await res.text()).slice(0, 300)}`);
    }
    const entities = await res.json();
    for (const e of entities) if (e?.iri) iris.add(e.iri);
    console.log(`Found ${entities.length} ${type}`);
  }
  return [...iris].sort();
}

// Order-insensitive, run-insensitive canonical form: object keys sorted, every "contains" array sorted by
// system+code, and volatile fields (expansion timestamp) dropped.
function normalise(node, key) {
  if (Array.isArray(node)) {
    const items = node.map((n) => normalise(n));
    if (key === "contains") {
      const k = (c) => `${c?.system ?? ""}|${c?.code ?? ""}|${c?.display ?? ""}`;
      items.sort((a, b) => k(a).localeCompare(k(b)));
    }
    return items;
  }
  if (node && typeof node === "object") {
    const out = {};
    for (const k of Object.keys(node).sort()) {
      if (k === "timestamp") continue;
      out[k] = normalise(node[k], k);
    }
    return out;
  }
  return node;
}

function flattenContains(node, acc = new Map()) {
  for (const c of node?.expansion?.contains ?? []) {
    acc.set(`${c.system ?? ""}|${c.code ?? ""}`, c.display ?? "");
    if (c.contains) flattenContains({ expansion: { contains: c.contains } }, acc);
  }
  return acc;
}

function describeDiff(expected, actual) {
  const e = flattenContains(expected);
  const a = flattenContains(actual);
  const missing = [...e.keys()].filter((k) => !a.has(k));
  const extra = [...a.keys()].filter((k) => !e.has(k));
  const changed = [...e.keys()].filter((k) => a.has(k) && a.get(k) !== e.get(k));
  const sample = (list) => list.slice(0, 5).join(", ") + (list.length > 5 ? ", ..." : "");
  const lines = [`members expected ${e.size}, actual ${a.size}`];
  if (missing.length) lines.push(`missing ${missing.length}: ${sample(missing)}`);
  if (extra.length) lines.push(`extra ${extra.length}: ${sample(extra)}`);
  if (changed.length) lines.push(`display changed ${changed.length}: ${sample(changed)}`);
  if (!missing.length && !extra.length && !changed.length) lines.push("members identical; other fields differ");
  return lines.join("; ");
}

const fileNameFor = (iri) => encodeURIComponent(iri).replace(/\*/g, "%2A") + ".json";

async function fetchExpansion(iri) {
  const res = await fetch(expandUrl(iri), { headers, signal: AbortSignal.timeout(timeoutMs) });
  const body = await res.text();
  if (!res.ok) return { ok: false, error: `HTTP ${res.status} ${body.slice(0, 200)}` };
  try {
    return { ok: true, json: normalise(JSON.parse(body)) };
  } catch {
    return { ok: false, error: "response was not valid JSON" };
  }
}

async function runGold() {
  const here = path.dirname(fileURLToPath(import.meta.url));
  const outDir = path.resolve(opt("out", "GOLD_DIR", path.join(here, "fhir-expand-gold")));
  const update = opt("update", "UPDATE", "false") === "true";
  const filter = opt("filter", "FILTER", undefined);
  const limit = Number(opt("limit", "LIMIT", 0));

  await mkdir(outDir, { recursive: true });
  let iris = await listSetIris();
  if (filter) iris = iris.filter((i) => i.includes(filter));
  if (limit > 0) iris = iris.slice(0, limit);
  console.log(`\nExpanding ${iris.length} sets -> ${outDir}${update ? " (update mode)" : ""}\n`);

  const counts = { saved: 0, updated: 0, match: 0, mismatch: 0, error: 0 };
  const problems = [];
  const note = (i, iri, status, detail = "") =>
    console.log(`[${i + 1}/${iris.length}] ${status.padEnd(8)} ${iri}${detail ? "  " + detail : ""}`);

  await forEachParallel(iris, async (iri, i) => {
    const file = path.join(outDir, fileNameFor(iri));
    const actualFile = file.replace(/\.json$/, ".actual.json");
    const start = performance.now();
    let r;
    try {
      r = await fetchExpansion(iri);
    } catch (e) {
      r = { ok: false, error: e.message };
    }
    const ms = `${(performance.now() - start).toFixed(0)}ms`;

    if (!r.ok) {
      counts.error++;
      problems.push(`${iri}: ${r.error}`);
      return note(i, iri, "ERROR", `${r.error} (${ms})`);
    }
    const text = JSON.stringify(r.json, null, 2) + "\n";
    const members = flattenContains(r.json).size;

    if (!existsSync(file)) {
      await writeFile(file, text);
      counts.saved++;
      return note(i, iri, "SAVED", `${members} members (${ms})`);
    }
    if (update) {
      await writeFile(file, text);
      counts.updated++;
      return note(i, iri, "UPDATED", `${members} members (${ms})`);
    }
    const expectedText = await readFile(file, "utf8");
    // Re-normalise the stored file so hand edits or older formats still compare fairly.
    const expected = normalise(JSON.parse(expectedText));
    if (JSON.stringify(expected) === JSON.stringify(r.json)) {
      counts.match++;
      if (existsSync(actualFile)) await unlink(actualFile);
      return note(i, iri, "MATCH", `${members} members (${ms})`);
    }
    counts.mismatch++;
    await writeFile(actualFile, text);
    const diff = describeDiff(expected, r.json);
    problems.push(`${iri}: ${diff}`);
    note(i, iri, "MISMATCH", `${diff} (${ms}) -> ${path.basename(actualFile)}`);
  });

  console.log("\n--- Summary ---");
  console.log(
    `Sets: ${iris.length}  saved ${counts.saved}  updated ${counts.updated}  match ${counts.match}  ` +
      `mismatch ${counts.mismatch}  error ${counts.error}`
  );
  if (problems.length) {
    console.log("\nProblems:");
    for (const p of problems) console.log(`  ${p}`);
  }
  process.exit(counts.mismatch || counts.error ? 1 : 0);
}

// ---------------------------------------------------------------------------------------------------------------
// perf mode
// ---------------------------------------------------------------------------------------------------------------

async function runPerf() {
  const iri = opt("iri", "IRI", "http://apiqcodes.org/qcodes#QPredict_334");
  const total = Number(opt("requests", "REQUESTS", 50));
  const delayMs = Number(opt("delay", "DELAY_MS", 0));
  const url = expandUrl(iri);

  const results = [];
  let next = 0;

  async function once(n) {
    const start = performance.now();
    try {
      const res = await fetch(url, { headers, signal: AbortSignal.timeout(timeoutMs) });
      const body = await res.text();
      const ms = performance.now() - start;
      let count;
      try {
        count = JSON.parse(body)?.expansion?.contains?.length;
      } catch {
        /* non-JSON body */
      }
      results.push({ n, ok: res.ok, status: res.status, ms, bytes: body.length, count });
      console.log(
        `#${n} ${res.status} ${ms.toFixed(0)}ms ${body.length}B` + (count !== undefined ? ` contains=${count}` : "")
      );
    } catch (e) {
      const ms = performance.now() - start;
      results.push({ n, ok: false, status: 0, ms, bytes: 0, error: e.message });
      console.log(`#${n} ERROR ${ms.toFixed(0)}ms ${e.message}`);
    }
  }

  async function worker() {
    while (true) {
      const n = ++next;
      if (n > total) return;
      await once(n);
      if (delayMs > 0) await new Promise((r) => setTimeout(r, delayMs));
    }
  }

  const pct = (sorted, p) => sorted[Math.min(sorted.length - 1, Math.ceil((p / 100) * sorted.length) - 1)];

  console.log(`GET ${url}\n${total} requests, concurrency ${concurrency}\n`);
  const wallStart = performance.now();
  await Promise.all(Array.from({ length: concurrency }, worker));
  const wall = (performance.now() - wallStart) / 1000;

  const times = results.map((r) => r.ms).sort((a, b) => a - b);
  const failures = results.filter((r) => !r.ok);
  const sum = times.reduce((a, b) => a + b, 0);
  console.log("\n--- Summary ---");
  console.log(`Requests : ${results.length} (${failures.length} failed)`);
  console.log(`Wall time: ${wall.toFixed(1)}s  (${(results.length / wall).toFixed(2)} req/s)`);
  console.log(
    `Latency  : min ${times[0]?.toFixed(0)}ms  avg ${(sum / times.length).toFixed(0)}ms  ` +
      `p50 ${pct(times, 50).toFixed(0)}ms  p95 ${pct(times, 95).toFixed(0)}ms  ` +
      `p99 ${pct(times, 99).toFixed(0)}ms  max ${times.at(-1).toFixed(0)}ms`
  );
  const statuses = results.reduce((m, r) => ((m[r.status] = (m[r.status] ?? 0) + 1), m), {});
  console.log(`Statuses : ${JSON.stringify(statuses)}`);
  process.exit(failures.length ? 1 : 0);
}

if (mode === "perf") await runPerf();
else if (mode === "gold") await runGold();
else {
  console.error(`Unknown --mode "${mode}" (expected gold or perf)`);
  process.exit(2);
}
