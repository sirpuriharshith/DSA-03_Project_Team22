# Test Execution & Verification Manual

The project uses **JUnit 5** and **AssertJ** for automated testing.

---

## 1. Running Test Suite (Windows PowerShell)

From the project root:
```powershell
cd backend
mvn test
# Or using Maven Wrapper
.\mvnw.cmd test
```

---

## 2. Test Suites Overview

| Test Class | Scope & Key Test Scenarios | Status |
| :--- | :--- | :--- |
| `KmpMatcherTest` | Normal search, pattern not found, pattern equal to text, empty & null strings, multi-lingual Unicode, repetitive overlapping patterns | PASS |
| `ZAndRabinKarpTest` | Z-box maintenance, rolling polynomial double hashing, collision verification | PASS |
| `AhoCorasickTest` | Multi-pattern dictionary trie, BFS failure transitions, dictionary suffix links | PASS |
| `DynamicProgrammingTest`| Levenshtein edit distance, Damerau transpositions ($ab \leftrightarrow ba$), Needleman-Wunsch global alignment | PASS |
| `EducationalModulesTest`| Dinic vs Edmonds-Karp Max Flow, Vertex Cover 2-approximation ($|C| \le 2|\text{OPT}|$), Miller-Rabin primality, Randomized QuickSort | PASS |
| `ParallelSearchReductionTest`| Dynamic corpus partitioning, Tree-Style parallel reduction across $P$ workers without data loss | PASS |

---

## 3. Results Summary
- **Tests Executed:** 18
- **Failures:** 0
- **Errors:** 0
- **Skipped:** 0
- **Build Status:** SUCCESS
