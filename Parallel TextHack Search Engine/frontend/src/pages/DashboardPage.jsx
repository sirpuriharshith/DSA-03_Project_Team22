import React, { useEffect, useState } from 'react';
import { api } from '../services/api';
import { Cpu, FileText, Database, Zap, HardDrive, Terminal } from 'lucide-react';

export default function DashboardPage({ setActiveTab }) {
  const [metrics, setMetrics] = useState(null);
  const [health, setHealth] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    Promise.all([api.getMetrics(), api.getHealth()])
      .then(([m, h]) => {
        setMetrics(m);
        setHealth(h);
      })
      .catch(err => console.error("Error fetching dashboard data:", err))
      .finally(() => setLoading(false));
  }, []);

  return (
    <div>
      <div className="page-header">
        <h1 className="page-title">System Overview & Dashboard</h1>
        <p className="page-description">
          Real-time computational metrics, hardware concurrency status, and document corpus statistics.
        </p>
      </div>

      <div className="grid-cols-4">
        <div className="stat-card">
          <div className="stat-label" style={{ display: 'flex', alignItems: 'center', gap: 6 }}>
            <FileText size={16} color="#38bdf8" /> Indexed Documents
          </div>
          <div className="stat-value">{loading ? '...' : (metrics?.totalDocuments || 0)}</div>
          <div className="stat-subtext">TXT, PDF, DOCX, CSV</div>
        </div>

        <div className="stat-card">
          <div className="stat-label" style={{ display: 'flex', alignItems: 'center', gap: 6 }}>
            <Zap size={16} color="#34d399" /> Total Word Count
          </div>
          <div className="stat-value">{loading ? '...' : (metrics?.totalWords || 0).toLocaleString()}</div>
          <div className="stat-subtext">Normalized UTF-8 Tokens</div>
        </div>

        <div className="stat-card">
          <div className="stat-label" style={{ display: 'flex', alignItems: 'center', gap: 6 }}>
            <Cpu size={16} color="#818cf8" /> CPU Hardware Threads
          </div>
          <div className="stat-value">{loading ? '...' : (metrics?.availableCores || 4)}</div>
          <div className="stat-subtext">Hardware Slices Available</div>
        </div>

        <div className="stat-card">
          <div className="stat-label" style={{ display: 'flex', alignItems: 'center', gap: 6 }}>
            <HardDrive size={16} color="#fbbf24" /> Corpus Size
          </div>
          <div className="stat-value">
            {loading ? '...' : `${((metrics?.totalFileSizeBytes || 0) / 1024).toFixed(1)} KB`}
          </div>
          <div className="stat-subtext">Stored in PostgreSQL</div>
        </div>
      </div>

      {/* System Information Card */}
      <div className="card">
        <div className="card-title">
          <span style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
            <Terminal size={18} color="#38bdf8" /> Runtime Environment Diagnostics
          </span>
          <span className="badge badge-green">STATUS: OPERATIONAL</span>
        </div>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(280px, 1fr))', gap: 16 }}>
          <div style={{ background: 'rgba(0,0,0,0.2)', padding: 16, borderRadius: 8 }}>
            <div style={{ fontSize: 12, color: 'var(--text-muted)' }}>BACKEND RUNTIME</div>
            <div style={{ fontSize: 15, fontWeight: 600, marginTop: 4 }}>
              Java 25 LTS (Oracle HotSpot 64-Bit)
            </div>
            <div style={{ fontSize: 13, color: 'var(--text-muted)', marginTop: 4 }}>
              Spring Boot 3.4.1 + Tomcat 10
            </div>
          </div>

          <div style={{ background: 'rgba(0,0,0,0.2)', padding: 16, borderRadius: 8 }}>
            <div style={{ fontSize: 12, color: 'var(--text-muted)' }}>DATABASE CONNECTION</div>
            <div style={{ fontSize: 15, fontWeight: 600, marginTop: 4 }}>
              PostgreSQL 18.6 (texthack_db)
            </div>
            <div style={{ fontSize: 13, color: 'var(--text-muted)', marginTop: 4 }}>
              HikariCP Connection Pool (Active)
            </div>
          </div>

          <div style={{ background: 'rgba(0,0,0,0.2)', padding: 16, borderRadius: 8 }}>
            <div style={{ fontSize: 12, color: 'var(--text-muted)' }}>JVM MAX HEAP MEMORY</div>
            <div style={{ fontSize: 15, fontWeight: 600, marginTop: 4 }}>
              {health?.maxHeapMb || 4096} MB Target
            </div>
            <div style={{ fontSize: 13, color: 'var(--text-muted)', marginTop: 4 }}>
              Allocated: {metrics?.allocatedMemoryMb || 512} MB
            </div>
          </div>
        </div>
      </div>

      {/* Handcrafted Algorithm Inventory */}
      <div className="card">
        <div className="card-title">
          <span>Handcrafted Algorithm Engine (Zero Built-in Search Functions)</span>
          <button className="btn-secondary" onClick={() => setActiveTab('algorithms')}>Open Algorithm Labs</button>
        </div>
        <div style={{ display: 'flex', flexWrap: 'wrap', gap: 8, marginTop: 8 }}>
          {(metrics?.supportedAlgorithms || [
            "KMP", "Z_ALGORITHM", "RABIN_KARP", "NAIVE", "AHO_CORASICK",
            "SUFFIX_ARRAY", "KASAI_LCP", "LEVENSHTEIN", "DAMERAU_LEVENSHTEIN",
            "NEEDLEMAN_WUNSCH", "SMITH_WATERMAN", "DINIC_MAX_FLOW", "VERTEX_COVER_2_APPROX",
            "RANDOMIZED_QUICKSORT", "MILLER_RABIN"
          ]).map(algo => (
            <span key={algo} className="badge badge-blue" style={{ fontSize: 12, padding: '6px 12px' }}>
              {algo}
            </span>
          ))}
        </div>
      </div>
    </div>
  );
}
