import axios from 'axios';

const API_BASE_URL = '/api';

const apiClient = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json'
  }
});

export const api = {
  // Health & Metrics
  getHealth: () => apiClient.get('/health').then(res => res.data),
  getMetrics: () => apiClient.get('/metrics').then(res => res.data),

  // Search Engine
  search: (searchRequest) => apiClient.post('/search', searchRequest).then(res => res.data),

  // Document Management
  getDocuments: () => apiClient.get('/documents').then(res => res.data),
  getDocumentById: (id) => apiClient.get(`/documents/${id}`).then(res => res.data),
  uploadDocument: (formData) => apiClient.post('/documents/upload', formData, {
    headers: { 'Content-Type': 'multipart/form-data' }
  }).then(res => res.data),
  deleteDocument: (id) => apiClient.delete(`/documents/${id}`).then(res => res.data),

  // Benchmarking
  getBenchmarks: () => apiClient.get('/benchmarks').then(res => res.data),
  runBenchmark: (query, searchType) => apiClient.post(`/benchmarks/run?query=${encodeURIComponent(query)}&searchType=${encodeURIComponent(searchType)}`).then(res => res.data),

  // Algorithm Modules
  getAlgorithmModules: () => apiClient.get('/algorithms/modules').then(res => res.data)
};
