import React, { useState, useEffect } from 'react';
import { api } from '../services/api';
import { Play, TrendingUp, BarChart3, Clock, Zap, ShieldCheck } from 'lucide-react';
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  BarElement,
  Title,
  Tooltip,
  Legend
} from 'chart.js';
import { Line, Bar } from 'react-chartjs-2';

ChartJS.register(
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  BarElement,
  Title,
  Tooltip,
  Legend
);

export default function BenchmarkPage() {
  const [query, setQuery] = useState('algorithm');
  const [searchType, setSearchType] = useState('EXACT');
  const [loading, setLoading] = useState(false);
  const [report, setReport] = useState(null);
  const [history, setHistory] = useState([]);

  const fetchHistory = () => {
    api.getBenchmarks()
      .then(data => setHistory(data))
      .catch(err => console.error("Error fetching benchmark history:", err));
  };

  useEffect(() => {
    fetchHistory();
  }, []);

  const handleRunBenchmark = async (e) => {
    e?.preventDefault();
    setLoading(true);

    try {
      const res = await api.runBenchmark(query, searchType);
      setReport(res);
      fetchHistory();
    } catch (err) {
      alert("Benchmark failed: " + (err.response?.data?.error || err.message));
    } finally {
      setLoading(false);
    }
  };

  // Speedup Chart Data
  const speedupChartData = report ? {
    labels: report.rows.map(r => `${r.threads} Threads`),
    datasets: [
      {
        label: 'Empirical Speedup (Sp = T1 / Tp)',
        data: report.rows.map(r => r.speedup),
        borderColor: '#38bdf8',
        backgroundColor: 'rgba(56, 189, 248, 0.2)',
        tension: 0.3,
        pointRadius: 6,
      },
      {
        label: 'Ideal Linear Speedup (Sp = p)',
        data: report.rows.map(r => r.threads),
        borderColor: 'rgba(255, 255, 255, 0.3)',
        borderDash: [5, 5],
        pointRadius: 0,
      }
    ]
  } : null;

  // Execution Time vs Threads Data
  const timeChartData = report ? {
    labels: report.rows.map(r => `${r.threads} Threads`),
    datasets: [
      {
        label: 'Execution Time (ms)',
        data: report.rows.map(r => r.executionTimeMs),
        borderColor: '#f43f5e',
        backgroundColor: 'rgba(244, 63, 94, 0.2)',
        tension: 0.3,
        pointRadius: 6,
      }
    ]
  } : null;

  // Throughput Data
  const throughputChartData = report ? {
    labels: report.rows.map(r => `${r.threads} Threads`),
    datasets: [
      {
        label: 'Throughput (docs / sec)',
        data: report.rows.map(r => r.throughput),
        backgroundColor: '#34d399',
        borderRadius: 6,
      }
    ]
  } : null;

  // Efficiency Data
  const efficiencyChartData = report ? {
    labels: report.rows.map(r => `${r.threads} Threads`),
    datasets: [
      {
        label: 'Parallel Efficiency (Ep = Sp / p * 100%)',
        data: report.rows.map(r => r.efficiency),
        borderColor: '#fbbf24',
        backgroundColor: 'rgba(251, 191, 36, 0.2)',
        tension: 0.3,
        pointRadius: 6,
      }
    ]
  } : null;

  const chartOptions = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: { labels: { color: '#94a3b8' } }
    },
    scales: {
      x: { grid: { color: '#334155' }, ticks: { color: '#94a3b8' } },
      y: { grid: { color: '#334155' }, ticks: { color: '#94a3b8' } }
    }
  };

  return (
    <div>
      <div className="page-header">
        <h1 className="page-title">Scalability & Performance Benchmarking</h1>
        <p className="page-description">
          Evaluate parallel execution times, empirical speedup, and efficiency metrics across 1, 2, 4, 8, and maximum CPU hardware threads.
        </p>
      </div>

      {/* Control Card */}
      <div className="card">
        <form onSubmit={handleRunBenchmark} style={{ display: 'flex', gap: 16, alignItems: 'flex-end' }}>
          <div className="form-group" style={{ flex: 2, marginBottom: 0 }}>
            <label className="form-label">Benchmark Search Query</label>
            <input
              type="text"
              className="input-control"
              value={query}
              onChange={(e) => setQuery(e.target.value)}
              required
            />
          </div>

          <div className="form-group" style={{ flex: 1, marginBottom: 0 }}>
            <label className="form-label">Search Type</label>
            <select className="select-control" value={searchType} onChange={(e) => setSearchType(e.target.value)}>
              <option value="EXACT">EXACT (KMP)</option>
              <option value="PHRASE">PHRASE</option>
              <option value="MULTI_KEYWORD">MULTI_KEYWORD</option>
              <option value="FUZZY">FUZZY</option>
            </select>
          </div>

          <button type="submit" className="btn-primary" disabled={loading} style={{ height: 42 }}>
            <Play size={18} /> {loading ? 'Running Multi-Thread Benchmarks...' : 'Run Scalability Benchmark'}
          </button>
        </form>
      </div>

      {/* Benchmark Results Table */}
      {report && (
        <div className="card">
          <div className="card-title">
            <span>Benchmark Metrics Table ({report.documentCount} Documents Tested)</span>
            <span className="badge badge-green">QUERY: "{report.query}"</span>
          </div>

          <table className="data-table">
            <thead>
              <tr>
                <th>Thread Count (p)</th>
                <th>Execution Time (Tp)</th>
                <th>Throughput (docs/sec)</th>
                <th>Speedup (Sp = T1 / Tp)</th>
                <th>Efficiency (Ep)</th>
              </tr>
            </thead>
            <tbody>
              {report.rows.map((row) => (
                <tr key={row.threads}>
                  <td style={{ fontWeight: 700 }}>{row.threads} Thread{row.threads > 1 ? 's' : ''}</td>
                  <td>{row.executionTimeMs} ms</td>
                  <td style={{ fontFamily: 'var(--font-mono)' }}>{row.throughput.toLocaleString()}</td>
                  <td>
                    <span className="badge badge-blue">{row.speedup.toFixed(2)}x</span>
                  </td>
                  <td>
                    <span className={`badge ${row.efficiency > 70 ? 'badge-green' : 'badge-amber'}`}>
                      {row.efficiency}%
                    </span>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}

      {/* Interactive Charts Grid */}
      {report && (
        <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 20, marginBottom: 24 }}>
          <div className="card" style={{ height: 350 }}>
            <div className="card-title">Speedup vs Thread Count (Amadahl & Brent's Bound)</div>
            <div style={{ height: 260 }}>
              <Line data={speedupChartData} options={chartOptions} />
            </div>
          </div>

          <div className="card" style={{ height: 350 }}>
            <div className="card-title">Execution Time (ms) vs Thread Count</div>
            <div style={{ height: 260 }}>
              <Line data={timeChartData} options={chartOptions} />
            </div>
          </div>

          <div className="card" style={{ height: 350 }}>
            <div className="card-title">Throughput (Documents / Second)</div>
            <div style={{ height: 260 }}>
              <Bar data={throughputChartData} options={chartOptions} />
            </div>
          </div>

          <div className="card" style={{ height: 350 }}>
            <div className="card-title">Parallel Efficiency (%) vs Threads</div>
            <div style={{ height: 260 }}>
              <Line data={efficiencyChartData} options={chartOptions} />
            </div>
          </div>
        </div>
      )}

      {/* Historical Benchmarks */}
      <div className="card">
        <div className="card-title">
          <span>Recent Benchmark Runs from PostgreSQL</span>
          <button className="btn-secondary" onClick={fetchHistory}>Refresh</button>
        </div>
        {history.length === 0 ? (
          <div style={{ color: 'var(--text-muted)' }}>No historical benchmark runs recorded yet.</div>
        ) : (
          <table className="data-table">
            <thead>
              <tr>
                <th>Query</th>
                <th>Search Type</th>
                <th>Docs</th>
                <th>Threads</th>
                <th>Time (ms)</th>
                <th>Throughput</th>
                <th>Speedup</th>
                <th>Efficiency</th>
              </tr>
            </thead>
            <tbody>
              {history.slice(0, 8).map(h => (
                <tr key={h.id}>
                  <td><b>{h.queryText}</b></td>
                  <td>{h.searchType}</td>
                  <td>{h.documentCount}</td>
                  <td>{h.threadCount}</td>
                  <td>{h.executionTimeMs} ms</td>
                  <td>{h.throughput}</td>
                  <td>{h.speedup}x</td>
                  <td>{h.efficiency}%</td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </div>
    </div>
  );
}
