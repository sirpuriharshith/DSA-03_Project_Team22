-- Parallel TextHack Search Engine - Baseline Sample Seed Data
-- Database: texthack_db

INSERT INTO documents (file_name, file_path, file_type, content, file_size, word_count, character_count) VALUES
('kmp_algorithm.txt', 'data/documents/kmp_algorithm.txt', 'TXT', 
'The Knuth-Morris-Pratt KMP string searching algorithm searches for occurrences of a pattern word within a main text document by employing the observation that when a mismatch occurs, the pattern itself embodies sufficient information to determine where the next match could begin. It preprocesses the pattern to construct a Longest Prefix Suffix LPS array in linear time O(m). During search, the text pointer never backtracks, resulting in optimal linear time complexity O(n + m).', 
472, 70, 472),

('aho_corasick_automaton.txt', 'data/documents/aho_corasick_automaton.txt', 'TXT', 
'The Aho-Corasick algorithm is an advanced multi-pattern string matching algorithm that constructs a finite-state machine resembling a trie with fallback failure links and dictionary output links. It enables simultaneous matching of multiple keyword patterns in a single linear pass over the target text corpus. The preprocessing phase runs in time proportional to the sum of pattern lengths, making it the industry standard for intrusion detection and multi-keyword search engines.', 
493, 70, 493),

('rabin_karp_hashing.txt', 'data/documents/rabin_karp_hashing.txt', 'TXT', 
'The Rabin-Karp algorithm uses rolling hash techniques to find any one of a set of pattern strings in a text document. By utilizing polynomial rolling hash functions with large prime moduli such as 1000000007 and 1000000009, the algorithm calculates hash values of successive substrings in constant time O(1). To prevent false positives from hash collisions, exact character verification is performed whenever the pattern hash matches the window hash.', 
468, 69, 468),

('z_algorithm.txt', 'data/documents/z_algorithm.txt', 'TXT', 
'The Z algorithm is a linear time string matching method that finds all occurrences of a pattern in a text document in O(n + m) time. It constructs the Z array where Z[i] is the length of the longest substring starting at index i that matches a prefix of the concatenated string pattern plus text. The algorithm maintains an active interval called the Z-box [L, R] to achieve linear asymptotic complexity without redundant character comparisons.', 
463, 74, 463),

('suffix_array_kasai.txt', 'data/documents/suffix_array_kasai.txt', 'TXT', 
'A suffix array is a sorted array of all suffixes of a string, providing a memory-efficient alternative to suffix trees. Using prefix doubling, the suffix array can be constructed in O(n log^2 n) or O(n log n) time. Kasai algorithm computes the Longest Common Prefix LCP array from the suffix array in linear time O(n). Together, suffix arrays and LCP arrays enable fast substring queries, longest repeated substrings, and pattern matching.', 
459, 71, 459),

('levenshtein_distance.txt', 'data/documents/levenshtein_distance.txt', 'TXT', 
'The Levenshtein distance is a metric for measuring the difference between two sequences using dynamic programming. Defined as the minimum number of single-character edits including insertions, deletions, or substitutions required to change one word into another, it is calculated using the Wagner-Fischer algorithm in O(nm) time and O(min(n, m)) auxiliary space. It serves as the foundation for fuzzy search and spelling correction.', 
451, 65, 451),

('damerau_levenshtein.txt', 'data/documents/damerau_levenshtein.txt', 'TXT', 
'The Damerau-Levenshtein distance extends the classic Levenshtein metric by allowing adjacent character transpositions as an elementary edit operation alongside insertions, deletions, and substitutions. This accurately reflects human typing errors, where two adjacent characters are frequently swapped. The dynamic programming recurrence accounts for transpositions by referencing cells two rows and columns prior in the DP matrix.', 
445, 61, 445),

('parallel_reduction.txt', 'data/documents/parallel_reduction.txt', 'TXT', 
'Parallel reduction is a fundamental parallel computing primitive that combines a set of partial results into a single final aggregate value using an associative binary operator. In our parallel search engine, document slices are searched concurrently across worker threads, and their partial result maps are merged using a tree-style reduction tree in O(log P) stages, eliminating lock contention on shared memory structures.', 
443, 67, 443),

('amdahls_brents_laws.txt', 'data/documents/amdahls_brents_laws.txt', 'TXT', 
'Amdahls law predicts the theoretical maximum speedup of a program using multiple processors when a fraction of the work is strictly sequential. Complementing Amdahl, Brents theorem states that with P processors, a parallel computation with total work T1 and critical path span T_infinity can execute in time bounded by T1/P + T_infinity. In embarrassingly parallel document search workloads, parallel efficiency remains high when corpus size dominates.', 
462, 69, 462),

('network_flow_dinic.txt', 'data/documents/network_flow_dinic.txt', 'TXT', 
'Dinic algorithm is a strongly polynomial algorithm for computing the maximum flow in a flow network in O(V^2 E) time. It introduces the concept of level graphs constructed via Breadth-First Search BFS and finds blocking flows using Depth-First Search DFS with pointers to avoid retreating. According to the Max-Flow Min-Cut theorem, the maximum amount of flow passing from source to sink equals the minimum total capacity of edges in a cut.', 
449, 70, 449),

('vertex_cover_approximation.txt', 'data/documents/vertex_cover_approximation.txt', 'TXT', 
'The Minimum Vertex Cover problem is a classical NP-complete decision problem. While finding an exact minimum vertex cover is computationally intractable unless P equals NP, a 2-approximation algorithm exists using maximal matching. By repeatedly selecting an arbitrary edge, adding both endpoints to the cover, and deleting all incident edges, the algorithm guarantees a vertex cover no larger than twice the optimal size in linear time O(V + E).', 
453, 71, 453),

('randomized_quicksort.txt', 'data/documents/randomized_quicksort.txt', 'TXT', 
'Randomized QuickSort avoids worst-case O(n^2) quadratic degradation by choosing a random pivot element from the input partition. This guarantees an expected execution time of O(n log n) regardless of the initial ordering of the elements. Randomized algorithms such as QuickSort and Universal Hashing leverage randomness to defeat worst-case inputs crafted by an adversary, yielding robust average-case performance in practice.', 
442, 63, 442),

('miller_rabin_primality.txt', 'data/documents/miller_rabin_primality.txt', 'TXT', 
'The Miller-Rabin primality test is a probabilistic randomized Monte Carlo algorithm that determines whether a given integer is prime. By testing whether a candidate base acts as a non-trivial square root of 1 modulo n, it achieves an error probability strictly less than 4^(-k) for k random witness trials. For 64-bit integers, deterministic sets of bases guarantee absolute certainty with zero false positives.', 
428, 64, 428),

('operating_systems_concurrency.txt', 'data/documents/operating_systems_concurrency.txt', 'TXT', 
'Modern operating systems provide thread scheduling, synchronization primitives, and virtual memory management to enable multi-core parallelism. Concurrency models such as work-stealing thread pools and fork-join frameworks balance processor workloads while minimizing context switching overhead. Ensuring cache locality and avoiding false sharing between processor cores are critical for high-throughput parallel data processing.', 
448, 59, 448),

('database_indexing_btrees.txt', 'data/documents/database_indexing_btrees.txt', 'TXT', 
'Relational databases utilize B-tree and B+tree data structures to maintain sorted data and allow logarithmic time search, sequential access, insertions, and deletions. By storing multiple keys per node matching disk block sizes, B-trees minimize expensive disk I/O operations. In our PostgreSQL architecture, B-tree indexes on document names and query metadata provide instantaneous retrieval for search analytics.', 
431, 62, 431),

('distributed_consensus_raft.txt', 'data/documents/distributed_consensus_raft.txt', 'TXT', 
'Distributed systems rely on consensus algorithms like Raft and Paxos to agree on state transitions across unreliable network nodes. Raft decomposes consensus into leader election, log replication, and safety invariants, providing strong fault tolerance against network partitions and server crashes. High-performance search engines distribute sharded document indices across clustered nodes using consensus protocols.', 
436, 58, 436),

('information_retrieval_ranking.txt', 'data/documents/information_retrieval_ranking.txt', 'TXT', 
'Information retrieval systems score and rank candidate documents based on relevance signals. Key factors include normalized term frequency, inverse document frequency, exact phrase matches, and edit distance penalties for misspelled terms. Transparent scoring formulas allow users to inspect why a document matched and evaluate how different weighting coefficients affect the final ranked search result ordering.', 
429, 61, 429),

('computer_networks_tcp.txt', 'data/documents/computer_networks_tcp.txt', 'TXT', 
'The Transmission Control Protocol TCP provides reliable, ordered, and error-checked delivery of octets between applications running on hosts communicating over an IP network. Flow control using sliding window buffers and congestion control algorithms like TCP Cubic dynamically modulate transmission rates to prevent network congestion while maximizing bandwidth utilization across high-speed connections.', 
426, 57, 426),

('compiler_syntax_trees.txt', 'data/documents/compiler_syntax_trees.txt', 'TXT', 
'Compilers translate source code into target machine instructions through lexical analysis, parsing, semantic analysis, intermediate code generation, and optimization. Abstract Syntax Trees AST represent the hierarchical grammatical structure of the program. String matching algorithms like Aho-Corasick play an essential role in lexical scanners for recognizing reserved keywords and multi-character tokens in linear time.', 
437, 59, 437),

('multicore_cache_coherence.txt', 'data/documents/multicore_cache_coherence.txt', 'TXT', 
'In shared-memory symmetric multiprocessing SMP systems, multiple processor cores maintain private L1 and L2 caches while sharing a common main memory. Hardware cache coherence protocols such as MESI Modified Exclusive Shared Invalid ensure that updates to shared memory locations are propagated consistently. Parallel search engines must employ immutable data slices to avoid bus invalidation traffic.', 
427, 59, 427);
