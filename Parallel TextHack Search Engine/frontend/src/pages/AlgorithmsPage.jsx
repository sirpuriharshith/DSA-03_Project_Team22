import React, { useState, useEffect } from 'react';
import { api } from '../services/api';
import { Code2, GitFork, Network, ShieldAlert, Dices } from 'lucide-react';

export default function AlgorithmsPage() {
  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    api.getAlgorithmModules()
      .then(res => setData(res))
      .catch(err => console.error("Error fetching algorithms data:", err))
      .finally(() => setLoading(false));
  }, []);

  return (
    <div>
      <div className="page-header">
        <h1 className="page-title">Advanced Algorithms Laboratory</h1>
        <p className="page-description">
          Live educational modules and verifiable execution demonstrations for String Algorithms, Dynamic Programming, Network Flow, NP-Completeness, and Randomized Algorithms.
        </p>
      </div>

      {loading ? (
        <div style={{ padding: 40, color: 'var(--text-muted)' }}>Executing and computing algorithmic modules...</div>
      ) : (
        <div style={{ display: 'flex', flexDirection: 'column', gap: 24 }}>
          {/* Module 1: String Algorithms */}
          <div className="card">
            <div className="card-title">
              <span style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
                <Code2 size={18} color="#38bdf8" /> 1. String Processing Algorithms
              </span>
              <span className="badge badge-blue">O(n + m) & O(n log² n)</span>
            </div>
            <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 16 }}>
              <div style={{ background: 'rgba(0,0,0,0.2)', padding: 14, borderRadius: 8 }}>
                <div style={{ fontWeight: 600, color: 'var(--primary)', marginBottom: 6 }}>Knuth-Morris-Pratt (KMP)</div>
                <div style={{ fontSize: 13, color: 'var(--text-muted)' }}>
                  Pattern: <b>"{data?.stringAlgorithms?.samplePattern}"</b> | Occurrences: <b>{data?.stringAlgorithms?.kmp?.count}</b>
                </div>
                <div style={{ fontSize: 13, color: 'var(--text-muted)', marginTop: 4 }}>
                  Match Indices: [{data?.stringAlgorithms?.kmp?.matchIndices?.join(', ')}]
                </div>
              </div>

              <div style={{ background: 'rgba(0,0,0,0.2)', padding: 14, borderRadius: 8 }}>
                <div style={{ fontWeight: 600, color: 'var(--primary)', marginBottom: 6 }}>Z-Algorithm [L, R] Box</div>
                <div style={{ fontSize: 13, color: 'var(--text-muted)' }}>
                  Occurrences: <b>{data?.stringAlgorithms?.zAlgorithm?.count}</b>
                </div>
                <div style={{ fontSize: 13, color: 'var(--text-muted)', marginTop: 4 }}>
                  Match Indices: [{data?.stringAlgorithms?.zAlgorithm?.matchIndices?.join(', ')}]
                </div>
              </div>

              <div style={{ background: 'rgba(0,0,0,0.2)', padding: 14, borderRadius: 8 }}>
                <div style={{ fontWeight: 600, color: 'var(--primary)', marginBottom: 6 }}>Rabin-Karp Double Rolling Hash</div>
                <div style={{ fontSize: 13, color: 'var(--text-muted)' }}>
                  Primes: 10⁹+7, 10⁹+9 | Verified Occurrences: <b>{data?.stringAlgorithms?.rabinKarp?.count}</b>
                </div>
                <div style={{ fontSize: 13, color: 'var(--text-muted)', marginTop: 4 }}>
                  Match Indices: [{data?.stringAlgorithms?.rabinKarp?.matchIndices?.join(', ')}]
                </div>
              </div>

              <div style={{ background: 'rgba(0,0,0,0.2)', padding: 14, borderRadius: 8 }}>
                <div style={{ fontWeight: 600, color: 'var(--primary)', marginBottom: 6 }}>Suffix Array & Kasai LCP</div>
                <div style={{ fontSize: 13, color: 'var(--text-muted)' }}>
                  Suffix Array: [{data?.stringAlgorithms?.suffixArray?.join(', ')}]
                </div>
                <div style={{ fontSize: 13, color: 'var(--text-muted)', marginTop: 4 }}>
                  Kasai LCP Array: [{data?.stringAlgorithms?.lcpArray?.join(', ')}]
                </div>
              </div>
            </div>
          </div>

          {/* Module 2: Dynamic Programming */}
          <div className="card">
            <div className="card-title">
              <span style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
                <GitFork size={18} color="#34d399" /> 2. Dynamic Programming Formulations
              </span>
              <span className="badge badge-green">Fuzzy & Alignment</span>
            </div>
            <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(260px, 1fr))', gap: 16 }}>
              <div style={{ background: 'rgba(0,0,0,0.2)', padding: 14, borderRadius: 8 }}>
                <div style={{ fontWeight: 600, color: 'var(--accent-emerald)', marginBottom: 4 }}>Levenshtein Distance</div>
                <div style={{ fontSize: 13 }}>"algorithm" ↔ "algoritm": <b>{data?.dynamicProgramming?.levenshtein_algorithm_algoritm}</b></div>
                <div style={{ fontSize: 12, color: 'var(--text-muted)', marginTop: 4 }}>Wagner-Fischer Matrix O(nm)</div>
              </div>

              <div style={{ background: 'rgba(0,0,0,0.2)', padding: 14, borderRadius: 8 }}>
                <div style={{ fontWeight: 600, color: 'var(--accent-emerald)', marginBottom: 4 }}>Damerau-Levenshtein</div>
                <div style={{ fontSize: 13 }}>"ca" ↔ "ac" (Transposition): <b>{data?.dynamicProgramming?.damerau_levenshtein_ca_ac}</b></div>
                <div style={{ fontSize: 12, color: 'var(--text-muted)', marginTop: 4 }}>Adjacent character swap support</div>
              </div>

              <div style={{ background: 'rgba(0,0,0,0.2)', padding: 14, borderRadius: 8 }}>
                <div style={{ fontWeight: 600, color: 'var(--accent-emerald)', marginBottom: 4 }}>Interval DP (Matrix Chain)</div>
                <div style={{ fontSize: 13 }}>Min Scalar Multiplications: <b>{data?.dynamicProgramming?.matrixChainOperations}</b></div>
                <div style={{ fontSize: 12, color: 'var(--text-muted)', marginTop: 4 }}>Chain dimensions: 10×20×30×40×30</div>
              </div>

              <div style={{ background: 'rgba(0,0,0,0.2)', padding: 14, borderRadius: 8 }}>
                <div style={{ fontWeight: 600, color: 'var(--accent-emerald)', marginBottom: 4 }}>Bitmask DP (Held-Karp TSP)</div>
                <div style={{ fontSize: 13 }}>Optimal Tour Cost: <b>{data?.dynamicProgramming?.tspMinTourCost}</b></div>
                <div style={{ fontSize: 12, color: 'var(--text-muted)', marginTop: 4 }}>Complexity: O(n² · 2ⁿ)</div>
              </div>
            </div>
          </div>

          {/* Module 3: Network Flow */}
          <div className="card">
            <div className="card-title">
              <span style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
                <Network size={18} color="#818cf8" /> 3. Network Flow & Min-Cut Theorem
              </span>
              <span className="badge badge-purple">{data?.networkFlow?.algorithm}</span>
            </div>
            <div style={{ background: 'rgba(0,0,0,0.2)', padding: 16, borderRadius: 8 }}>
              <div style={{ display: 'flex', gap: 24, flexWrap: 'wrap' }}>
                <div>
                  <div style={{ fontSize: 12, color: 'var(--text-muted)' }}>MAXIMUM FLOW VALUE</div>
                  <div style={{ fontSize: 24, fontWeight: 700, color: 'var(--accent-purple)' }}>{data?.networkFlow?.maxFlow} Units</div>
                </div>
                <div>
                  <div style={{ fontSize: 12, color: 'var(--text-muted)' }}>SOURCE CUT PARTITION (S)</div>
                  <div style={{ fontSize: 16, fontWeight: 600, marginTop: 6 }}>[{Array.from(data?.networkFlow?.sourcePartition || []).join(', ')}]</div>
                </div>
                <div>
                  <div style={{ fontSize: 12, color: 'var(--text-muted)' }}>SINK CUT PARTITION (T)</div>
                  <div style={{ fontSize: 16, fontWeight: 600, marginTop: 6 }}>[{Array.from(data?.networkFlow?.sinkPartition || []).join(', ')}]</div>
                </div>
              </div>
            </div>
          </div>

          {/* Module 4 & 5: NP-Completeness & Randomized */}
          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 20 }}>
            <div className="card">
              <div className="card-title">
                <span style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
                  <ShieldAlert size={18} color="#fbbf24" /> 4. NP-Completeness & Approximation
                </span>
                <span className="badge badge-amber">2-APPROX</span>
              </div>
              <div style={{ background: 'rgba(0,0,0,0.2)', padding: 16, borderRadius: 8 }}>
                <div style={{ fontWeight: 600, color: 'var(--accent-amber)' }}>{data?.npCompleteness?.problem}</div>
                <div style={{ fontSize: 13, marginTop: 6 }}>
                  Approximate Vertex Cover Set: <b>[{Array.from(data?.npCompleteness?.approximateCover || []).join(', ')}]</b>
                </div>
                <div style={{ fontSize: 12, color: 'var(--text-muted)', marginTop: 8 }}>
                  {data?.npCompleteness?.guarantee}
                </div>
              </div>
            </div>

            <div className="card">
              <div className="card-title">
                <span style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
                  <Dices size={18} color="#f43f5e" /> 5. Randomized Algorithms
                </span>
                <span className="badge badge-rose" style={{ background: 'rgba(244, 63, 94, 0.15)', color: '#f43f5e' }}>Monte Carlo</span>
              </div>
              <div style={{ background: 'rgba(0,0,0,0.2)', padding: 16, borderRadius: 8 }}>
                <div style={{ fontWeight: 600, color: '#f43f5e' }}>Miller-Rabin Primality Test</div>
                <div style={{ fontSize: 13, marginTop: 4 }}>
                  1,000,000,007 is Prime: <b>{String(data?.randomizedAlgorithms?.millerRabin_1000000007_isPrime)}</b>
                </div>
                <div style={{ fontSize: 13, marginTop: 2 }}>
                  1,000,000,011 is Prime: <b>{String(data?.randomizedAlgorithms?.millerRabin_1000000011_isPrime)}</b> (3 × 333,333,337)
                </div>
                <div style={{ fontWeight: 600, color: '#f43f5e', marginTop: 10 }}>Reservoir Sampling (k=3)</div>
                <div style={{ fontSize: 13, marginTop: 2 }}>
                  Sampled: [{data?.randomizedAlgorithms?.reservoirSample?.join(', ')}]
                </div>
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
