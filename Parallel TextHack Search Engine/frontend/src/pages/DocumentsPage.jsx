import React, { useEffect, useState, useRef } from 'react';
import { api } from '../services/api';
import { UploadCloud, Trash2, FileText, Check, Eye } from 'lucide-react';

export default function DocumentsPage() {
  const [documents, setDocuments] = useState([]);
  const [loading, setLoading] = useState(true);
  const [uploading, setUploading] = useState(false);
  const [uploadMessage, setUploadMessage] = useState(null);
  const [selectedDoc, setSelectedDoc] = useState(null);
  const fileInputRef = useRef(null);

  const fetchDocuments = () => {
    setLoading(true);
    api.getDocuments()
      .then(docs => setDocuments(docs))
      .catch(err => console.error("Error fetching docs:", err))
      .finally(() => setLoading(false));
  };

  useEffect(() => {
    fetchDocuments();
  }, []);

  const handleFileUpload = async (files) => {
    if (!files || files.length === 0) return;
    const file = files[0];

    const formData = new FormData();
    formData.append('file', file);

    setUploading(true);
    setUploadMessage(null);

    try {
      await api.uploadDocument(formData);
      setUploadMessage({ type: 'success', text: `Successfully indexed ${file.name}` });
      fetchDocuments();
    } catch (err) {
      setUploadMessage({ type: 'error', text: err.response?.data?.error || `Failed to parse ${file.name}` });
    } finally {
      setUploading(false);
    }
  };

  const handleDelete = async (id, name) => {
    if (!window.confirm(`Delete document "${name}"?`)) return;
    try {
      await api.deleteDocument(id);
      fetchDocuments();
    } catch (err) {
      alert("Error deleting document: " + err.message);
    }
  };

  return (
    <div>
      <div className="page-header">
        <h1 className="page-title">Corpus Document Management</h1>
        <p className="page-description">
          Upload, inspect, and index multi-format documents (TXT, PDF, DOCX, CSV) into the PostgreSQL repository.
        </p>
      </div>

      {/* Upload Zone */}
      <div className="card">
        <div
          className="upload-zone"
          onClick={() => fileInputRef.current?.click()}
          onDragOver={(e) => e.preventDefault()}
          onDrop={(e) => {
            e.preventDefault();
            handleFileUpload(e.dataTransfer.files);
          }}
        >
          <input
            type="file"
            ref={fileInputRef}
            style={{ display: 'none' }}
            accept=".txt,.pdf,.docx,.csv"
            onChange={(e) => handleFileUpload(e.target.files)}
          />
          <UploadCloud size={40} color="#38bdf8" style={{ margin: '0 auto 12px auto' }} />
          <div style={{ fontSize: 16, fontWeight: 600 }}>
            {uploading ? 'Parsing and Extracting Content...' : 'Drag & drop document files here or click to browse'}
          </div>
          <div style={{ fontSize: 13, color: 'var(--text-muted)', marginTop: 4 }}>
            Supported formats: <b>.TXT</b> (UTF-8), <b>.PDF</b> (PDFBox), <b>.DOCX</b> (POI), <b>.CSV</b> (Commons CSV)
          </div>
        </div>

        {uploadMessage && (
          <div style={{
            marginTop: 16,
            padding: 12,
            borderRadius: 8,
            fontSize: 14,
            background: uploadMessage.type === 'success' ? 'rgba(52, 211, 153, 0.15)' : 'rgba(244, 63, 94, 0.15)',
            color: uploadMessage.type === 'success' ? 'var(--accent-emerald)' : 'var(--accent-rose)',
            border: `1px solid ${uploadMessage.type === 'success' ? 'var(--accent-emerald)' : 'var(--accent-rose)'}`
          }}>
            {uploadMessage.text}
          </div>
        )}
      </div>

      {/* Document Table */}
      <div className="card">
        <div className="card-title">
          <span>Indexed Documents Repository ({documents.length})</span>
          <button className="btn-secondary" onClick={fetchDocuments}>Refresh</button>
        </div>

        {loading ? (
          <div style={{ textAlign: 'center', padding: 40, color: 'var(--text-muted)' }}>Loading documents...</div>
        ) : documents.length === 0 ? (
          <div style={{ textAlign: 'center', padding: 40, color: 'var(--text-muted)' }}>No documents currently indexed.</div>
        ) : (
          <div style={{ overflowX: 'auto' }}>
            <table className="data-table">
              <thead>
                <tr>
                  <th>#</th>
                  <th>File Name</th>
                  <th>Format</th>
                  <th>Size</th>
                  <th>Words</th>
                  <th>Chars</th>
                  <th>Indexed Date</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                {documents.map((doc, idx) => (
                  <tr key={doc.id}>
                    <td>{idx + 1}</td>
                    <td style={{ fontWeight: 600 }}>{doc.fileName}</td>
                    <td><span className="badge badge-purple">{doc.fileType}</span></td>
                    <td>{(doc.fileSize / 1024).toFixed(1)} KB</td>
                    <td>{doc.wordCount?.toLocaleString()}</td>
                    <td>{doc.characterCount?.toLocaleString()}</td>
                    <td style={{ fontSize: 12, color: 'var(--text-muted)' }}>
                      {doc.indexedAt ? new Date(doc.indexedAt).toLocaleDateString() : 'N/A'}
                    </td>
                    <td>
                      <div style={{ display: 'flex', gap: 8 }}>
                        <button className="btn-secondary" style={{ padding: '4px 8px' }} onClick={() => setSelectedDoc(doc)}>
                          <Eye size={14} /> View
                        </button>
                        <button className="btn-danger" onClick={() => handleDelete(doc.id, doc.fileName)}>
                          <Trash2 size={14} />
                        </button>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>

      {/* Document View Modal */}
      {selectedDoc && (
        <div style={{
          position: 'fixed', top: 0, left: 0, right: 0, bottom: 0,
          backgroundColor: 'rgba(0,0,0,0.7)', display: 'flex', alignItems: 'center', justifyContent: 'center', zIndex: 100
        }}>
          <div style={{
            background: 'var(--bg-secondary)', border: '1px solid var(--border-color)', borderRadius: 12,
            width: '90%', maxWidth: 750, maxHeight: '80vh', display: 'flex', flexDirection: 'column', padding: 24
          }}>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: 16 }}>
              <div style={{ fontWeight: 700, fontSize: 18 }}>{selectedDoc.fileName}</div>
              <button className="btn-secondary" onClick={() => setSelectedDoc(null)}>Close</button>
            </div>
            <div style={{ flex: 1, overflowY: 'auto', background: 'rgba(0,0,0,0.3)', padding: 16, borderRadius: 8, whiteSpace: 'pre-wrap', fontSize: 13, lineHeight: 1.6 }}>
              {selectedDoc.content}
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
