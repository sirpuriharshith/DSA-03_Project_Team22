# Algorithmic Foundations & Complexity Reference

This document provides mathematical formulations, recurrences, pseudo-code structures, and complexity analysis for all handcrafted algorithms in the **Parallel TextHack Search Engine**.

---

## 1. String Matching Algorithms

### Knuth-Morris-Pratt (KMP)
- **Problem:** Locate pattern $P[0..m-1]$ within text $T[0..n-1]$.
- **LPS Array Construction:**
  `lps[i]` is the length of the longest proper prefix of $P[0..i]$ that is also a suffix of $P[0..i]$.
  $$\text{lps}[i] = \begin{cases} 0 & \text{if } i = 0 \\ \text{lps}[k-1] + 1 & \text{if } P[i] = P[k] \\ 0 & \text{otherwise} \end{cases}$$
- **Time Complexity:** $O(m)$ preprocessing + $O(n)$ search = $O(n + m)$.
- **Space Complexity:** $O(m)$ for LPS array.
- **Java Class:** [`texthack.strings.KmpMatcher`](file:///c:/Users/harshith/OneDrive/Desktop/DSA_3/DSA3_Project/backend/src/main/java/texthack/strings/KmpMatcher.java)

### Z-Algorithm
- **Concept:** Computes $Z[i]$ = length of longest common prefix between $S$ and $S[i..|S|-1]$.
- **Concatenated String:** $S = P + \text{'\u0000'} + T$.
- **Z-box Maintenance:** Maintains interval $[L, R]$ where $R$ is the rightmost matched boundary.
- **Time Complexity:** $O(n + m)$.
- **Space Complexity:** $O(n + m)$.
- **Java Class:** [`texthack.strings.ZAlgorithmMatcher`](file:///c:/Users/harshith/OneDrive/Desktop/DSA_3/DSA3_Project/backend/src/main/java/texthack/strings/ZAlgorithmMatcher.java)

### Rabin-Karp Algorithm (Double Polynomial Hash)
- **Hash Formulation:**
  $$H(S) = \left( \sum_{i=0}^{m-1} S[i] \cdot B^{m-1-i} \right) \pmod M$$
- **Primes Used:** $M_1 = 1,000,000,007$, $M_2 = 1,000,000,009$, Base $B = 257$.
- **Rolling Hash Update:**
  $$H_{new} = \left( (H_{old} - S_{out} \cdot B^{m-1}) \cdot B + S_{in} \right) \pmod M$$
- **Collision Verification:** If $H_1(T) = H_1(P) \land H_2(T) = H_2(P)$, characters are verified explicitly.
- **Time Complexity:** Average $O(n + m)$, Worst-case $O(nm)$.
- **Java Class:** [`texthack.strings.RabinKarpMatcher`](file:///c:/Users/harshith/OneDrive/Desktop/DSA_3/DSA3_Project/backend/src/main/java/texthack/strings/RabinKarpMatcher.java)

### Aho-Corasick Automaton
- **Multi-Pattern Matching:** Searches an arbitrary dictionary of keywords simultaneously in one pass.
- **Components:**
  1. Prefix Trie
  2. Failure Links (constructed via BFS)
  3. Dictionary Suffix Links (fast-forward pointers to output terminals)
- **Time Complexity:** $O(n + \sum |P_i| + z)$ where $z$ is the number of occurrences.
- **Java Class:** [`texthack.strings.AhoCorasickMatcher`](file:///c:/Users/harshith/OneDrive/Desktop/DSA_3/DSA3_Project/backend/src/main/java/texthack/strings/AhoCorasickMatcher.java)

---

## 2. Dynamic Programming Formulations

### Wagner-Fischer Levenshtein Distance
- **Recurrence:**
  $$D(i, j) = \min \begin{cases} D(i-1, j) + 1 & \text{(Deletion)} \\ D(i, j-1) + 1 & \text{(Insertion)} \\ D(i-1, j-1) + [S_1[i-1] \neq S_2[j-1]] & \text{(Substitution)} \end{cases}$$
- **Time:** $O(nm)$ | **Space:** $O(\min(n, m))$ using two rolling rows.
- **Java Class:** [`texthack.dp.LevenshteinDistance`](file:///c:/Users/harshith/OneDrive/Desktop/DSA_3/DSA3_Project/backend/src/main/java/texthack/dp/LevenshteinDistance.java)

### Damerau-Levenshtein Distance
- **Transposition Term:**
  $$\text{If } i > 1 \land j > 1 \land S_1[i-1] = S_2[j-2] \land S_1[i-2] = S_2[j-1]:$$
  $$D(i, j) = \min(D(i, j), D(i-2, j-2) + 1)$$
- **Application:** Powering fuzzy search to handle adjacent letter inversions.
- **Java Class:** [`texthack.dp.DamerauLevenshteinDistance`](file:///c:/Users/harshith/OneDrive/Desktop/DSA_3/DSA3_Project/backend/src/main/java/texthack/dp/DamerauLevenshteinDistance.java)

---

## 3. Network Flow & Min-Cut Theorem
- **Max-Flow Min-Cut Theorem:** In any flow network, the maximum amount of flow passing from source $s$ to sink $t$ is equal to the total capacity of the minimum capacity cut separating $s$ and $t$.
- **Dinic's Algorithm:**
  - Phase 1: Construct Level Graph via BFS ($O(E)$).
  - Phase 2: Find Blocking Flow via DFS with pointer retreat pruning ($O(VE)$).
  - Total Time: $O(V^2 E)$.
- **Java Class:** [`texthack.flow.NetworkFlowModule`](file:///c:/Users/harshith/OneDrive/Desktop/DSA_3/DSA3_Project/backend/src/main/java/texthack/flow/NetworkFlowModule.java)

---

## 4. NP-Completeness & Approximation
- **Vertex Cover 2-Approximation:**
  1. Find a maximal matching $M \subseteq E$ greedily.
  2. Let $C = \bigcup_{(u, v) \in M} \{u, v\}$.
  3. Since any valid vertex cover must include at least one endpoint from each disjoint edge in $M$:
     $$|\text{OPT}| \ge |M| \implies |C| = 2|M| \le 2|\text{OPT}|$$
- **Complexity:** $O(V + E)$ linear time.
- **Java Class:** [`texthack.np.NpCompletenessModule`](file:///c:/Users/harshith/OneDrive/Desktop/DSA_3/DSA3_Project/backend/src/main/java/texthack/np/NpCompletenessModule.java)

---

## 5. Randomized Algorithms
- **Randomized QuickSort:** Selects random pivot $r \in [\text{low}, \text{high}]$. Guarantees expected $O(n \log n)$ time.
- **Miller-Rabin Primality Test:**
  Decomposes $n - 1 = 2^s \cdot d$. For base $a$, tests whether $a^d \equiv 1 \pmod n$ or $a^{2^r d} \equiv -1 \pmod n$.
  For 64-bit integers, deterministic witness set $\{2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37\}$ gives 100% certainty.
- **Reservoir Sampling:** Selects $k$ samples from unknown size $N$ with uniform probability $k / N$.
- **Java Class:** [`texthack.randomized.RandomizedAlgorithmsModule`](file:///c:/Users/harshith/OneDrive/Desktop/DSA_3/DSA3_Project/backend/src/main/java/texthack/randomized/RandomizedAlgorithmsModule.java)
