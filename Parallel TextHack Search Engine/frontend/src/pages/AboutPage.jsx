import React from 'react';
import { BookOpen, Award, CheckCircle, Cpu, GitMerge } from 'lucide-react';

export default function AboutPage() {
  return (
    <div>
      <div className="page-header">
        <h1 className="page-title">About Parallel TextHack Search Engine</h1>
        <p className="page-description">
          Advanced Algorithms & Parallel Computing Capstone Project Documentation.
        </p>
      </div>

      <div className="card">
        <div className="card-title">
          <span style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
            <Award size={18} color="#38bdf8" /> Academic Problem Statement
          </span>
        </div>
        <p style={{ color: 'var(--text-muted)', lineHeight: 1.8, fontSize: 14 }}>
          "Develop a search engine that performs keyword searching over large document collections using parallel search techniques.
          The system partitions datasets across multiple processing threads and combines partial search results using parallel reduction.
          Users compare sequential and parallel search performance using execution time, throughput, speedup, and efficiency metrics."
        </p>
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 20 }}>
        <div className="card">
          <div className="card-title">
            <span style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
              <Cpu size={18} color="#34d399" /> Brent's Scheduling Theorem
            </span>
          </div>
          <p style={{ color: 'var(--text-muted)', fontSize: 13, lineHeight: 1.7 }}>
            Brent's theorem establishes a fundamental upper bound on parallel computation time. For an algorithm with total work <b>T₁</b> and critical path span <b>T_∞</b> executed on <b>P</b> processors:
          </p>
          <div style={{ background: 'rgba(0,0,0,0.3)', padding: 12, borderRadius: 6, margin: '12px 0', fontFamily: 'var(--font-mono)', fontSize: 14, color: 'var(--primary)' }}>
            T_p ≤ (T₁ / P) + T_∞
          </div>
          <p style={{ color: 'var(--text-muted)', fontSize: 13, lineHeight: 1.7 }}>
            In embarrassingly parallel document partitioning, work T₁ scales with corpus size while span T_∞ corresponds to single-document scanning plus O(log P) reduction tree depth.
          </p>
        </div>

        <div className="card">
          <div className="card-title">
            <span style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
              <GitMerge size={18} color="#818cf8" /> Tree-Style Parallel Reduction
            </span>
          </div>
          <p style={{ color: 'var(--text-muted)', fontSize: 13, lineHeight: 1.7 }}>
            Rather than accumulating partial results sequentially in a shared lock-guarded structure, our engine executes a binary reduction tree:
          </p>
          <div style={{ background: 'rgba(0,0,0,0.3)', padding: 12, borderRadius: 6, margin: '12px 0', fontFamily: 'var(--font-mono)', fontSize: 13, color: 'var(--accent-purple)' }}>
            Stage 1: (R₁ ⊕ R₂), (R₃ ⊕ R₄), ...<br/>
            Stage 2: (R₁₂ ⊕ R₃₄), ...<br/>
            Depth: ⌈log₂ P⌉ stages (Zero contention)
          </div>
          <p style={{ color: 'var(--text-muted)', fontSize: 13, lineHeight: 1.7 }}>
            Guarantees total freedom from race conditions and linear scalability up to available hardware CPU cores.
          </p>
        </div>
      </div>

      {/* Algorithmic Asymptotics Table */}
      <div className="card">
        <div className="card-title">Handcrafted Algorithm Asymptotic Complexities</div>
        <table className="data-table">
          <thead>
            <tr>
              <th>Algorithm</th>
              <th>Category</th>
              <th>Time Complexity</th>
              <th>Space Complexity</th>
              <th>Practical Application in TextHack</th>
            </tr>
          </thead>
          <tbody>
            <tr>
              <td><b>Knuth-Morris-Pratt (KMP)</b></td>
              <td>String Matching</td>
              <td>O(n + m)</td>
              <td>O(m)</td>
              <td>Default exact keyword and phrase search</td>
            </tr>
            <tr>
              <td><b>Z-Algorithm</b></td>
              <td>String Matching</td>
              <td>O(n + m)</td>
              <td>O(n + m)</td>
              <td>Linear substring search via Z-box maintenance</td>
            </tr>
            <tr>
              <td><b>Rabin-Karp</b></td>
              <td>Rolling Hash</td>
              <td>O(n + m) avg</td>
              <td>O(1)</td>
              <td>Double polynomial hashing with collision verification</td>
            </tr>
            <tr>
              <td><b>Aho-Corasick</b></td>
              <td>Automaton</td>
              <td>O(n + Σm + z)</td>
              <td>O(Σm)</td>
              <td>Simultaneous multi-keyword search with dictionary links</td>
            </tr>
            <tr>
              <td><b>Suffix Array & Kasai LCP</b></td>
              <td>Suffix Structures</td>
              <td>O(n log² n) / O(n)</td>
              <td>O(n)</td>
              <td>Sub-string indexing and binary search retrieval</td>
            </tr>
            <tr>
              <td><b>Damerau-Levenshtein</b></td>
              <td>Dynamic Prog.</td>
              <td>O(nm)</td>
              <td>O(nm)</td>
              <td>Fuzzy search with adjacent transposition support</td>
            </tr>
            <tr>
              <td><b>Dinic Max Flow</b></td>
              <td>Network Flow</td>
              <td>O(V² E)</td>
              <td>O(V + E)</td>
              <td>Level graph BFS + Blocking flow DFS demonstration</td>
            </tr>
            <tr>
              <td><b>Vertex Cover 2-Approx</b></td>
              <td>NP-Completeness</td>
              <td>O(V + E)</td>
              <td>O(V + E)</td>
              <td>Maximal matching based polynomial approximation</td>
            </tr>
            <tr>
              <td><b>Miller-Rabin Test</b></td>
              <td>Randomized</td>
              <td>O(k log³ n)</td>
              <td>O(1)</td>
              <td>Monte Carlo primality testing with 64-bit witnesses</td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  );
}
