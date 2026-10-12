import React, { useState } from 'react';
import { api } from '../services/api';
import { Search, Zap, Clock, TrendingUp, Layers, CheckCircle2 } from 'lucide-react';

export default function SearchPage() {
  const [query, setQuery] = useState('algorithm');
  const [searchType, setSearchType] = useState('EXACT');
  const [mode, setMode] = useState('PARALLEL');
  const [threads, setThreads] = useState(4);
  const [algorithm, setAlgorithm] = useState('KMP');
  const [maxEditDistance, setMaxEditDistance] = useState(2);
  const [loading, setLoading] = useState(false);
  const [response, setResponse] = useState(null);
  const [error, setError] = useState(null);

  const handleSearch = async (e) => {
    e?.preventDefault();
    if (!query.trim()) return;

    setLoading(true);
    setError(null);

    try {
      const payload = {
        query: query.trim(),
        searchType,
        mode,
        threads: parseInt(threads),
        maxEditDistance: parseInt(maxEditDistance),
        algorithm
      };
      const res = await api.search(payload);
      setResponse(res);
    } catch (err) {
      setError(err.response?.data?.error || err.message || 'Search execution failed');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div>
      <div className="page-header">
        <h1 className="page-title">Search Console</h1>
        <p className="page-description">
          Execute keyword, phrase, multi-pattern, or fuzzy queries using sequential or parallel tree-reduced search.
        </p>
      </div>

      {/* Search Query Control Form */}
      <div className="card">
        <form onSubmit={handleSearch}>
          <div style={{ display: 'grid', gridTemplateColumns: '2fr 1fr 1fr 1fr', gap: 16 }}>
            <div className="form-group">
              <label className="form-label">Search Query (Max 100 chars)</label>
              <input
                type="text"
                className="input-control"
                placeholder="Enter search term or phrase..."
                value={query}
                maxLength={100}
                onChange={(e) => setQuery(e.target.value)}
                required
              />
            </div>

            <div className="form-group">
              <label className="form-label">Search Type</label>
              <select className="select-control" value={searchType} onChange={(e) => setSearchType(e.target.value)}>
                <option value="EXACT">Exact Search (KMP)</option>
                <option value="PHRASE">Phrase Search</option>
                <option value="MULTI_KEYWORD">Multi-Keyword (Aho-Corasick)</option>
                <option value="FUZZY">Fuzzy Search (Edit Dist)</option>
              </select>
            </div>

            <div className="form-group">
              <label className="form-label">Execution Mode</label>
              <select className="select-control" value={mode} onChange={(e) => setMode(e.target.value)}>
                <option value="PARALLEL">Parallel Reduction</option>
                <option value="SEQUENTIAL">Sequential (Baseline T1)</option>
              </select>
            </div>

            <div className="form-group">
              <label className="form-label">Worker Threads</label>
              <select
                className="select-control"
                value={threads}
                disabled={mode === 'SEQUENTIAL'}
                onChange={(e) => setThreads(e.target.value)}
              >
                <option value="1">1 Thread</option>
                <option value="2">2 Threads</option>
                <option value="4">4 Threads</option>
                <option value="8">8 Threads</option>
                <option value="16">16 Threads</option>
              </select>
            </div>
          </div>

          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr auto', gap: 16, marginTop: 8, alignItems: 'flex-end' }}>
            <div className="form-group" style={{ marginBottom: 0 }}>
              <label className="form-label">Algorithm Selection</label>
              <select className="select-control" value={algorithm} onChange={(e) => setAlgorithm(e.target.value)}>
                <option value="KMP">Knuth-Morris-Pratt (KMP)</option>
                <option value="Z_ALGORITHM">Z-Algorithm</option>
                <option value="RABIN_KARP">Rabin-Karp Rolling Hash</option>
                <option value="NAIVE">Naive Brute-Force O(nm)</option>
              </select>
            </div>

            {searchType === 'FUZZY' && (
              <div className="form-group" style={{ marginBottom: 0 }}>
                <label className="form-label">Max Edit Distance (Damerau-Levenshtein)</label>
                <select className="select-control" value={maxEditDistance} onChange={(e) => setMaxEditDistance(e.target.value)}>
                  <option value="1">1 Edit (Strict)</option>
                  <option value="2">2 Edits (Standard)</option>
                  <option value="3">3 Edits (Permissive)</option>
                </select>
              </div>
            )}

            <button type="submit" className="btn-primary" disabled={loading} style={{ height: 42 }}>
              <Search size={18} /> {loading ? 'Searching...' : 'Search Corpus'}
            </button>
          </div>
        </form>
      </div>

      {error && (
        <div style={{ background: 'rgba(244, 63, 94, 0.1)', border: '1px solid var(--accent-rose)', color: 'var(--accent-rose)', padding: 16, borderRadius: 8, marginBottom: 24 }}>
          {error}
        </div>
      )}

      {/* Performance Metrics Bar */}
      {response && (
        <>
          <div className="grid-cols-4">
            <div className="stat-card">
              <div className="stat-label" style={{ display: 'flex', alignItems: 'center', gap: 6 }}>
                <Clock size={16} color="#38bdf8" /> Execution Time
              </div>
              <div className="stat-value">{response.executionTimeMs} ms</div>
              <div className="stat-subtext">{response.mode} ({response.threads} Threads)</div>
            </div>

            <div className="stat-card">
              <div className="stat-label" style={{ display: 'flex', alignItems: 'center', gap: 6 }}>
                <Zap size={16} color="#34d399" /> Search Throughput
              </div>
              <div className="stat-value">{response.throughput.toLocaleString()}</div>
              <div className="stat-subtext">Documents / Second</div>
            </div>

            <div className="stat-card">
              <div className="stat-label" style={{ display: 'flex', alignItems: 'center', gap: 6 }}>
                <TrendingUp size={16} color="#818cf8" /> Parallel Speedup (Sp)
              </div>
              <div className="stat-value">{response.speedup.toFixed(2)}x</div>
              <div className="stat-subtext">Efficiency: {response.efficiency}%</div>
            </div>

            <div className="stat-card">
              <div className="stat-label" style={{ display: 'flex', alignItems: 'center', gap: 6 }}>
                <CheckCircle2 size={16} color="#fbbf24" /> Matching Documents
              </div>
              <div className="stat-value">{response.matchingDocumentsCount} / {response.totalDocumentsSearched}</div>
              <div className="stat-subtext">Total Matches: {response.totalOccurrences}</div>
            </div>
          </div>

          {/* Results List */}
          <div className="card">
            <div className="card-title">
              <span>Ranked Search Results ({response.matchingDocumentsCount} found)</span>
              <span className="badge badge-blue">ALGO: {response.algorithmUsed}</span>
            </div>

            {response.results.length === 0 ? (
              <div style={{ textAlign: 'center', padding: '40px 0', color: 'var(--text-muted)' }}>
                No occurrences found in the document collection for query: <b>"{response.query}"</b>
              </div>
            ) : (
              <div style={{ display: 'flex', flexDirection: 'column', gap: 14 }}>
                {response.results.map((res, idx) => (
                  <div key={res.documentId || idx} style={{ background: 'rgba(0,0,0,0.2)', padding: 18, borderRadius: 10, border: '1px solid var(--border-color)' }}>
                    <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
                      <div style={{ display: 'flex', alignItems: 'center', gap: 10 }}>
                        <span style={{ fontWeight: 700, fontSize: 16 }}>#{idx + 1} {res.documentName}</span>
                        <span className="badge badge-purple">{res.fileType}</span>
                        {res.editDistance > 0 && (
                          <span className="badge badge-amber">Edit Dist: {res.editDistance}</span>
                        )}
                      </div>
                      <div style={{ display: 'flex', alignItems: 'center', gap: 12 }}>
                        <span style={{ fontSize: 13, color: 'var(--text-muted)' }}>
                          Occurrences: <b style={{ color: 'var(--text-main)' }}>{res.occurrenceCount}</b>
                        </span>
                        <span className="badge badge-green" style={{ fontSize: 13 }}>
                          Score: {res.score.toFixed(3)}
                        </span>
                      </div>
                    </div>

                    {res.snippet && (
                      <div className="snippet-box">
                        {res.snippet}
                      </div>
                    )}
                  </div>
                ))}
              </div>
            )}
          </div>
        </>
      )}
    </div>
  );
}
