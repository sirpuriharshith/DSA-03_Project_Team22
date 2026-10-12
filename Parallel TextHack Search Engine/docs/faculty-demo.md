# Faculty Viva & Live Demonstration Script

Use this step-by-step guide to present the **Parallel TextHack Search Engine** to faculty, examiners, and evaluators during demonstration and viva voce.

---

## 📋 25-Point Step-by-Step Presentation Script

### Part A: System Initialization & Architecture
1. **Start PostgreSQL**:
   Show that the `postgresql-x64-18` service is running and `texthack_db` holds the schema.
2. **Start Backend**:
   Run `.\mvnw.cmd spring-boot:run` in `backend/`. Point out the startup banner printing available hardware CPU cores and target database connection.
3. **Start Frontend**:
   Run `npm run dev` in `frontend/`. Open `http://localhost:5173`.
4. **Show Dashboard**:
   Highlight the real-time corpus telemetry cards (Indexed Documents, Total Words, CPU Cores, Database).
5. **Explain Architecture**:
   Show the four-tier separation: React UI -> Spring Boot REST Controller -> Handcrafted Algorithm & Concurrency Layer -> PostgreSQL 18.

### Part B: Document Ingestion & Parsing Subsystem
6. **Upload Documents**:
   Navigate to **Corpus Document Management**. Drag and drop sample files (`.txt`, `.pdf`, `.docx`, `.csv`).
   Explain how **Apache PDFBox** extracts PDF streams and **Apache POI** parses `.docx` XML trees with **Unicode NFC normalization**.
7. **Inspect Documents**:
   Click **View** on any document to show the raw parsed UTF-8 text and word count calculation.

### Part C: Search Modes & Handcrafted String Algorithms
8. **Demonstrate Exact Search (KMP)**:
   Navigate to **Search Console**. Query: `algorithm`.
   Explain the **Longest Prefix Suffix (LPS) array** in `KmpMatcher.java`. Show that the text pointer never backtracks, achieving $O(n + m)$ linear time.
9. **Demonstrate Phrase Search**:
   Query: `"parallel algorithm"`. Show exact phrase matching across document boundaries.
10. **Demonstrate Multi-Keyword Search (Aho-Corasick)**:
    Query: `parallel algorithm search`.
    Explain the **Aho-Corasick Automaton**: Trie prefix tree, BFS failure links, and dictionary output links matching multiple patterns simultaneously in a single linear pass.
11. **Demonstrate Fuzzy Search (Damerau-Levenshtein)**:
    Query: `algoritm` with max edit distance = 2.
    Show that it correctly retrieves documents containing `algorithm` (edit distance = 1).
    Explain the dynamic programming recurrence and how **transpositions** ($ab \leftrightarrow ba$) are detected in $O(nm)$ time.

### Part D: Sequential vs. Parallel Performance Analysis
12. **Run Sequential Search**:
    Select mode `Sequential` (1 Thread). Record execution time $T_1$ and baseline throughput.
13. **Run Parallel Search**:
    Select mode `Parallel Reduction` with 4 or 8 threads. Observe execution time drop and throughput surge.
14. **Demonstrate Tree-Style Parallel Reduction**:
    Explain to faculty that we **do NOT use a simple thread pool accumulating into a synchronized list**.
    Explain `TreeStyleParallelReduction.java`:
    - Disjoint corpus partitioning into $P$ slices
    - Worker threads produce independent immutable `PartialSearchResult` objects
    - Reduction tree executes pairwise merges in $\lceil \log_2 P \rceil$ stages, completely eliminating thread lock contention.
15. **Show Performance Metrics**:
    Point to the live telemetry bar:
    - **Throughput:** $N / T_{\text{sec}}$ (docs/sec)
    - **Speedup:** $S_P = T_1 / T_P$
    - **Efficiency:** $E_P = (S_P / P) \times 100\%$

### Part E: Scalability Benchmarking & Graphical Comparison
16. **Execute Scalability Benchmark**:
    Navigate to **Scalability Benchmark**. Click **Run Scalability Benchmark**.
17. **Explain Benchmark Table**:
    Examine the table showing real measurements for 1, 2, 4, and 8 threads.
18. **Explain Speedup Curve**:
    Show the **Empirical Speedup vs. Ideal Linear Speedup** graph. Relate it to **Amdahl's Law** and **Brent's Theorem**:
    $$T_P \le \frac{T_1}{P} + T_\infty$$
19. **Explain Throughput & Efficiency Graphs**:
    Show that throughput scales near-linearly as document slices are parallelized across physical CPU cores.

### Part F: Advanced Algorithm Curriculum Modules
20. **Demonstrate Algorithms Lab**:
    Navigate to **Algorithms Lab** to show that advanced modules run live from backend Java code.
21. **Explain Network Flow (Dinic & Min-Cut)**:
    Show Dinic's algorithm computing Maximum Flow (15 units) and partitioning vertices into Source Set (S) and Sink Set (T) satisfying the **Max-Flow Min-Cut Theorem**.
22. **Explain NP-Completeness & Vertex Cover 2-Approximation**:
    Show the polynomial-time 2-approximation using Maximal Matching. Explain why $|C| \le 2 \times |\text{OPT}|$.
23. **Demonstrate Randomized Algorithms**:
    Show **Randomized QuickSort** sorting in expected $O(n \log n)$ time.
    Show **Miller-Rabin Primality Test** verifying $1,000,000,007$ (prime) and $1,000,000,011$ (composite) using deterministic 64-bit witness bases.
    Show **Reservoir Sampling** picking uniform random items in a single stream pass.

### Part G: Database & Code Quality Viva Defense
24. **Explain PostgreSQL Database Design**:
    Show `texthack_db` tables (`documents`, `search_queries`, `search_results`, `benchmark_results`, `algorithm_runs`) and B-tree indexes.
25. **Highlight Strict Academic Compliance**:
    Affirm that **zero built-in search methods** (`indexOf`, `contains`, regex, or external search libraries) were used. All core string matching, DP matrices, reduction trees, and flow networks were constructed manually from fundamental computer science principles.
