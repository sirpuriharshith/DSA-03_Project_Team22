# REST API Specification

The backend server runs on `http://localhost:8080`. All payloads are formatted in UTF-8 JSON.

---

## 1. Search Endpoints

### `POST /api/search`
Executes sequential or parallel keyword search across the document collection.

**Request Body:**
```json
{
  "query": "algorithm",
  "searchType": "EXACT",       // EXACT, PHRASE, MULTI_KEYWORD, FUZZY
  "mode": "PARALLEL",          // SEQUENTIAL, PARALLEL
  "threads": 4,                // 1 to 64
  "maxEditDistance": 2,        // 0 to 5 (used when searchType = FUZZY)
  "algorithm": "KMP"           // KMP, Z_ALGORITHM, RABIN_KARP, NAIVE
}
```

**Response (200 OK):**
```json
{
  "query": "algorithm",
  "searchType": "EXACT",
  "mode": "PARALLEL",
  "threads": 4,
  "algorithmUsed": "KMP",
  "totalDocumentsSearched": 20,
  "matchingDocumentsCount": 8,
  "totalOccurrences": 14,
  "executionTimeMs": 1.45,
  "throughput": 13793.1,
  "speedup": 2.85,
  "efficiency": 71.3,
  "results": [
    {
      "documentId": 1,
      "documentName": "kmp_algorithm.txt",
      "fileType": "TXT",
      "occurrenceCount": 3,
      "score": 1.85,
      "editDistance": 0,
      "snippet": "...The Knuth-Morris-Pratt KMP string searching algorithm searches...",
      "matchPositions": [4, 49, 120]
    }
  ]
}
```

---

## 2. Document Endpoints

### `GET /api/documents`
Retrieves all indexed documents in the repository.

### `GET /api/documents/{id}`
Retrieves full text and metadata for a specific document.

### `POST /api/documents/upload`
Uploads a new document file.
- **Form Param:** `file` (Multipart file: `.txt`, `.pdf`, `.docx`, `.csv`)
- **Response (201 Created):** `DocumentDto`

### `DELETE /api/documents/{id}`
Deletes a document from the database and local storage.

---

## 3. Benchmark Endpoints

### `GET /api/benchmarks`
Retrieves recent benchmark execution records from PostgreSQL.

### `POST /api/benchmarks/run`
Executes scalability benchmark over 1, 2, 4, 8, and maximum CPU threads.
- **Query Params:** `query` (string), `searchType` (string)
- **Response:** `BenchmarkReportDto` with performance metrics and row table.

---

## 4. Educational & Health Endpoints

### `GET /api/algorithms/modules`
Returns live calculation outputs for String Matching, Dynamic Programming, Network Flow, NP-Completeness, and Randomized Algorithms.

### `GET /api/health`
Returns system status, JVM memory stats, and Java version.

### `GET /api/metrics`
Returns corpus totals (documents, word count, file size) and processor thread capacity.
