import React, { useState } from 'react';
import DashboardPage from './pages/DashboardPage';
import SearchPage from './pages/SearchPage';
import DocumentsPage from './pages/DocumentsPage';
import BenchmarkPage from './pages/BenchmarkPage';
import AlgorithmsPage from './pages/AlgorithmsPage';
import AboutPage from './pages/AboutPage';
import {
  LayoutDashboard,
  Search,
  FileText,
  BarChart3,
  Cpu,
  Info,
  Server
} from 'lucide-react';

export default function App() {
  const [activeTab, setActiveTab] = useState('dashboard');

  const renderContent = () => {
    switch (activeTab) {
      case 'dashboard':
        return <DashboardPage setActiveTab={setActiveTab} />;
      case 'search':
        return <SearchPage />;
      case 'documents':
        return <DocumentsPage />;
      case 'benchmark':
        return <BenchmarkPage />;
      case 'algorithms':
        return <AlgorithmsPage />;
      case 'about':
        return <AboutPage />;
      default:
        return <DashboardPage setActiveTab={setActiveTab} />;
    }
  };

  return (
    <div className="app-container">
      {/* Sidebar Navigation */}
      <aside className="sidebar">
        <div className="sidebar-header">
          <div className="logo-badge">Parallel Engine</div>
          <div className="sidebar-title">TextHack Search</div>
          <div className="sidebar-subtitle">Advanced Algorithms Capstone</div>
        </div>

        <nav className="sidebar-menu">
          <div
            className={`nav-item ${activeTab === 'dashboard' ? 'active' : ''}`}
            onClick={() => setActiveTab('dashboard')}
          >
            <LayoutDashboard size={18} />
            <span>Dashboard</span>
          </div>

          <div
            className={`nav-item ${activeTab === 'search' ? 'active' : ''}`}
            onClick={() => setActiveTab('search')}
          >
            <Search size={18} />
            <span>Search Console</span>
          </div>

          <div
            className={`nav-item ${activeTab === 'documents' ? 'active' : ''}`}
            onClick={() => setActiveTab('documents')}
          >
            <FileText size={18} />
            <span>Documents Corpus</span>
          </div>

          <div
            className={`nav-item ${activeTab === 'benchmark' ? 'active' : ''}`}
            onClick={() => setActiveTab('benchmark')}
          >
            <BarChart3 size={18} />
            <span>Scalability Benchmark</span>
          </div>

          <div
            className={`nav-item ${activeTab === 'algorithms' ? 'active' : ''}`}
            onClick={() => setActiveTab('algorithms')}
          >
            <Cpu size={18} />
            <span>Algorithms Lab</span>
          </div>

          <div
            className={`nav-item ${activeTab === 'about' ? 'active' : ''}`}
            onClick={() => setActiveTab('about')}
          >
            <Info size={18} />
            <span>About & Theory</span>
          </div>
        </nav>

        <div className="sidebar-footer">
          <div style={{ display: 'flex', alignItems: 'center', gap: 6 }}>
            <span style={{ width: 8, height: 8, borderRadius: '50%', background: '#34d399', display: 'inline-block' }} />
            <span>Spring Boot 3.4 + Java 25</span>
          </div>
          <div style={{ marginTop: 4, color: 'var(--text-muted)' }}>PostgreSQL: texthack_db</div>
        </div>
      </aside>

      {/* Main Execution View */}
      <div className="main-wrapper">
        <header className="top-bar">
          <div style={{ fontSize: 14, color: 'var(--text-muted)' }}>
            Academic Platform / <span style={{ color: 'var(--text-main)', fontWeight: 600, textTransform: 'capitalize' }}>{activeTab}</span>
          </div>
          <div style={{ display: 'flex', alignItems: 'center', gap: 12 }}>
            <span className="badge badge-blue">Java 25 LTS</span>
            <span className="badge badge-green">PostgreSQL 18</span>
          </div>
        </header>

        <main className="content-body">
          {renderContent()}
        </main>
      </div>
    </div>
  );
}
