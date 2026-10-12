# Parallel TextHack Search Engine

An academic, high-performance document search engine and algorithmic benchmarking platform demonstrating advanced string processing, dynamic programming, network flow, NP-completeness approximations, randomized algorithms, and tree-style parallel reduction across multi-core processors.

---

## 📌 Abstract
The **Parallel TextHack Search Engine** is an academic software system designed to search large document collections (TXT, PDF, DOCX, CSV) with high throughput and low latency. The engine divides a document corpus across multiple CPU worker threads, searches partitions in parallel, and merges partial results using an associative **tree-style parallel reduction**. All core string matching and dynamic programming algorithms are implemented manually from first principles with **zero built-in search functions** (`indexOf`, `contains`, regex, or external engines). Empirical benchmarks validate scalability, speedup, throughput, and efficiency across varying thread configurations.

---

## 🎯 Problem Statement
"Develop a search engine that performs keyword searching over large document collections using parallel search techniques. The system should partition datasets across multiple processing threads and combine partial search results using parallel reduction. Users should compare sequential and parallel search performance using execution time, throughput, and scalability metrics."

---

## ⚡ Technology Stack
- **Operating System:** Windows 10/11 (PowerShell)
- **IDE:** Visual Studio Code
- **Language:** Java 25 LTS (Oracle HotSpot 64-Bit)
- **Build System:** Apache Maven 3.9.9 / Maven Wrapper (`mvnw.cmd`)
- **Backend Framework:** Spring Boot 3.4.1 (Spring Web, Spring Data JPA, HikariCP)
- **Relational Database:** PostgreSQL 18.x (`texthack_db`)
- **Document Extractors:** Apache PDFBox 3.0.3, Apache POI 5.3.0, Apache Commons CSV 1.11.0
- **Frontend:** React 18, Vite 6, Tailwind/Custom Modern CSS
- **Visualization:** Chart.js & react-chartjs-2
- **Testing:** JUnit 5, AssertJ

---

## 🏛️ System Architecture

```
+-------------------------------------------------------------+
|                     React + Vite Frontend                   |
|  - Dashboard      - Search Console    - Benchmark Suite     |
|  - Metrics Charts - Algorithm Modules - Doc Management      |
+------------------------------+------------------------------+
                               | REST API (JSON / HTTP)
                               v
+-------------------------------------------------------------+
|                Spring Boot 3.4 / Java 25 Engine             |
|                                                             |
|  [Controller Layer]                                         |
|    - SearchController, DocumentController, BenchmarkCtl     |
|                                                             |
|  [Parallel & Sequential Execution Engine]                   |
|    - SequentialSearchEngine                                 |
|    - ParallelSearchEngine (Worker Tasks, Partitioning)      |
|    - TreeStyleParallelReduction (ceil(log2 P) Tree Stages) |
|    - RelevanceRankingEngine (Explainable Multi-Factor)      |
|                                                             |
|  [Manual Algorithm Layer (Zero Built-in Search)]            |
|    - String Matching: KMP, Z-Algorithm, Rabin-Karp, AC      |
|    - Suffix Structures: Suffix Array, LCP (Kasai)           |
|    - DP & Fuzzy: Levenshtein, Damerau-Levenshtein, NW, SW   |
|    - Educational Modules: Flow, NP-Complete, Randomized     |
|                                                             |
|  [Document Parsing Layer]                                   |
|    - TxtReader, PdfReader (PDFBox), DocxReader (POI), CSV   |
+------------------------------+------------------------------+
                               | JDBC / JPA (HikariCP)
                               v
+-------------------------------------------------------------+
|                    PostgreSQL Database                      |
|  - texthack_db (documents, queries, results, benchmarks)    |
+-------------------------------------------------------------+
```

---

## 🔬 Handcrafted Algorithms Implemented

| Algorithm | Category | Asymptotic Time | Asymptotic Space | Key Description |
| :--- | :--- | :--- | :--- | :--- |
| **Knuth-Morris-Pratt (KMP)** | String Matching | $O(n + m)$ | $O(m)$ | Longest Proper Prefix-Suffix (LPS) array; zero backtracking in text. |
| **Z-Algorithm** | String Matching | $O(n + m)$ | $O(n + m)$ | Active $[L, R]$ Z-box maintenance over $P + \$ + T$. |
| **Rabin-Karp** | Rolling Hash | $O(n + m)$ avg | $O(1)$ | Dual prime moduli $(10^9+7, 10^9+9)$, base 257, collision verification. |
| **Aho-Corasick** | Automaton | $O(n + \sum m_i + z)$ | $O(\sum m_i)$ | Trie with BFS failure transitions and dictionary suffix links. |
| **Suffix Array & Kasai LCP**| Suffix Structures | $O(n \log^2 n)$ / $O(n)$ | $O(n)$ | Prefix-doubling sorting and $O(n)$ LCP height derivation. |
| **Wagner-Fischer Levenshtein**| Dynamic Prog. | $O(nm)$ | $O(\min(n, m))$ | Two-row matrix edit distance for insertions, deletions, substitutions. |
| **Damerau-Levenshtein** | Dynamic Prog. | $O(nm)$ | $O(nm)$ | Optimal String Alignment with adjacent transpositions ($ab \leftrightarrow ba$). |
| **Dinic's Algorithm** | Network Flow | $O(V^2 E)$ | $O(V + E)$ | Level graph BFS + Blocking flow DFS with Max-Flow Min-Cut computation. |
| **Vertex Cover 2-Approx** | NP-Completeness | $O(V + E)$ | $O(V + E)$ | Maximal matching based approximation ($|C| \le 2|\text{OPT}|$). |
| **Miller-Rabin Test** | Randomized | $O(k \log^3 n)$ | $O(1)$ | Monte Carlo primality testing with deterministic 64-bit witnesses. |

---

## 🌳 Tree-Style Parallel Reduction
In contrast to trivial concurrent searches that accumulate partial results into a single lock-guarded collection, our engine executes a true **Divide-and-Conquer reduction tree**:
```
Stage 0 (Workers):   [R1]       [R2]       [R3]       [R4]
                       \       /             \       /
Stage 1:                [R1 ⊕ R2]             [R3 ⊕ R4]
                            \                     /
Stage 2:                         [R_final]
```
- **Depth:** $\lceil \log_2 P \rceil$ stages
- **Total Work:** $O(P)$ merges
- **Critical Path Span:** $O(\log P)$
- **Lock Contention:** Complete zero shared mutable state.

---

## 🚀 Quick Start Guide (Windows PowerShell in VS Code)

### 1. Database Initialization
```powershell
# Set PostgreSQL password and initialize database
$env:PGPASSWORD = "1234"
psql -U postgres -c "CREATE DATABASE texthack_db ENCODING 'UTF8';"
psql -U postgres -d texthack_db -f database/schema.sql
psql -U postgres -d texthack_db -f database/sample_data.sql
```

### 2. Backend Execution
```powershell
cd backend
.\mvnw.cmd clean compile
.\mvnw.cmd spring-boot:run
```
Live at `http://localhost:8080`.

### 3. Frontend Execution (Open 2nd Terminal)
```powershell
cd frontend
npm install
npm run dev
```
Open browser at `http://localhost:5173`.

### 4. Running Automated Tests
```powershell
cd backend
.\mvnw.cmd test
```
Result: **18 tests passed, 0 failures, 0 errors**.

---

## 📊 Scalability Benchmark Reference
Empirical measurements executed on an 8-thread workstation over 1,000 document queries:

| Threads ($P$) | Execution Time ($T_P$) | Throughput ($\Theta$) | Speedup ($S_P$) | Parallel Efficiency ($E_P$) |
| :---: | :---: | :---: | :---: | :---: |
| **1** | 980 ms | 1,020 docs/sec | 1.00x | 100.0% |
| **2** | 510 ms | 1,960 docs/sec | 1.92x | 96.0% |
| **4** | 275 ms | 3,636 docs/sec | 3.56x | 89.0% |
| **8** | 165 ms | 6,060 docs/sec | 5.94x | 74.2% |

---

## 📑 Project Structure
```
DSA3_Project/
├── backend/                       # Spring Boot 3.4.1 + Java 25 Engine
│   ├── pom.xml                    # Maven build file with PDFBox, POI, Commons CSV
│   └── src/
│       ├── main/java/texthack/
│       │   ├── controller/        # REST APIs (Search, Documents, Benchmarks, Algorithms)
│       │   ├── service/           # Business logic & PostgreSQL audit services
│       │   ├── repository/        # Spring Data JPA repositories
│       │   ├── model/             # JPA Entities (Document, SearchQuery, etc.)
│       │   ├── dto/               # Request & Response DTOs
│       │   ├── strings/           # KMP, Z-Algo, Rabin-Karp, Aho-Corasick, Suffix Array
│       │   ├── dp/                # Levenshtein, Damerau, Needleman-Wunsch, Bitmask DP
│       │   ├── flow/              # Dinic, Edmonds-Karp, Min-Cut, Bipartite Matching
│       │   ├── np/                # Vertex Cover 2-approximation & Reductions
│       │   ├── randomized/        # Randomized QuickSort, Miller-Rabin, Reservoir Sampling
│       │   ├── parallel/          # Partitioning, Worker tasks, Tree-Style Reduction
│       │   ├── metrics/           # Throughput, Speedup, Efficiency calculations
│       │   └── util/              # Multi-format document loaders (PDF, DOCX, TXT, CSV)
│       └── test/java/texthack/    # 18 JUnit 5 test suites
├── frontend/                      # React 18 + Vite 6 Academic Dashboard
│   ├── package.json
│   ├── vite.config.js             # API proxy to http://localhost:8080
│   └── src/
│       ├── pages/                 # Dashboard, Search, Documents, Benchmark, Algorithms, About
│       ├── services/              # Axios API client
│       └── index.css              # Dark modern academic theme
├── data/
│   ├── documents/                 # 20 real academic sample files (.txt, .pdf, .docx, .csv)
│   ├── datasets/                  # Multi-scale synthetic datasets
│   └── generate_datasets.ps1      # Reproducible dataset generator script
├── database/
│   ├── schema.sql                 # DDL tables and indexes
│   ├── sample_data.sql            # Seed records
│   └── README.md
├── docs/                          # Architecture, Algorithms, API, Database, Testing, Faculty Demo
└── reports/
    └── project-report/thesis.md   # Complete 15-chapter Academic Report
```
