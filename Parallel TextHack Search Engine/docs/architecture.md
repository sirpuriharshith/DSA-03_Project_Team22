# Parallel TextHack Search Engine — Architecture Specification

## 1. System Overview
The **Parallel TextHack Search Engine** is a high-performance, academic text retrieval and algorithm analysis platform designed to process large corpus datasets (TXT, PDF, DOCX, CSV) using handcrafted string algorithms, dynamic programming, and parallel reduction paradigms.

## 2. Multi-Tier Architecture

```
+-------------------------------------------------------------+
|                     React + Vite Frontend                   |
|  - Dashboard      - Search Console    - Benchmark Suite     |
|  - Metrics Charts - Algorithm Modules - Doc Management      |
+------------------------------+------------------------------+
                               | REST API (JSON / HTTP)
                               v
+-------------------------------------------------------------+
|                Spring Boot 3.x / Java 25 Engine             |
|                                                             |
|  [Controller Layer]                                         |
|    - SearchController, DocumentController, BenchmarkCtl     |
|                                                             |
|  [Parallel & Sequential Execution Engine]                   |
|    - SequentialSearchEngine                                 |
|    - ParallelSearchEngine (Worker Tasks, Partitioning)      |
|    - TreeStyleParallelReduction (ForkJoin / Divide & Qnr)   |
|    - RelevanceRankingEngine                                 |
|                                                             |
|  [Manual Algorithm Layer (Zero Built-in Search)]            |
|    - String Matching: KMP, Z-Algorithm, Rabin-Karp, AC      |
|    - Suffix Structures: Suffix Array, LCP (Kasai), SA-IS    |
|    - DP & Fuzzy: Levenshtein, Damerau-Levenshtein, NW, SW   |
|    - Educational Modules: Flow, NP-Complete, Randomized     |
|                                                             |
|  [Document Parsing Layer]                                   |
|    - TxtReader, PdfReader (PDFBox), DocxReader (POI), CSV   |
+------------------------------+------------------------------+
                               | JDBC / JPA (Parameterized)
                               v
+-------------------------------------------------------------+
|                    PostgreSQL Database                      |
|  - texthack_db (documents, queries, results, benchmarks)    |
+-------------------------------------------------------------+
```

## 3. Parallel Reduction Model
1. **Partitioning**: A document set $D$ of size $N$ is divided uniformly across $P$ threads into blocks of size $\lceil N / P \rceil$.
2. **Worker Search**: Each thread processes its assigned slice independently, producing an immutable `PartialSearchResult`.
3. **Tree Reduction**: The partial results are reduced pairwise in $\lceil \log_2 P \rceil$ stages:
   $$\text{Result} = \bigoplus_{i=1}^P R_i$$
   This eliminates synchronization bottlenecks and contention on shared mutable data structures.
