# Parallel TextHack Search Engine - Synthetic Dataset Generator
# Generates reproducible multi-scale document datasets using a fixed pseudo-random seed.

param (
    [int]$Count = 100,
    [int]$Seed = 42,
    [string]$OutputDir = "data/datasets"
)

$targetDir = "$OutputDir/$Count"
if (-not (Test-Path $targetDir)) {
    New-Item -ItemType Directory -Path $targetDir -Force | Out-Null
}

$rnd = New-Object System.Random($Seed)

$topics = @(
    "Knuth-Morris-Pratt KMP string matching and LPS prefix table preprocessing",
    "Aho-Corasick automaton with failure transitions and dictionary output links",
    "Rabin-Karp polynomial rolling double hashing with prime moduli verification",
    "Z-algorithm linear pattern search maintaining active interval Z-box bounds",
    "Suffix arrays prefix doubling construction and Kasai longest common prefix",
    "Wagner-Fischer Levenshtein distance dynamic programming matrix recurrence",
    "Damerau-Levenshtein edit distance accounting for adjacent transpositions",
    "Tree-style parallel reduction merging partial results in log P stages",
    "Amdahls law and Brents theorem bounding parallel speedup on multicore processors",
    "Dinic blocking flow algorithm with level graphs and max-flow min-cut theorem",
    "Minimum vertex cover maximal matching based polynomial two approximation",
    "Randomized QuickSort expected running time and Miller-Rabin primality test",
    "Operating system thread scheduling work-stealing pools and cache coherence",
    "PostgreSQL relational indexing B-trees and query execution plan optimization",
    "Distributed consensus protocols Raft and Paxos fault tolerance guarantees"
)

$adjectives = @("efficient", "asymptotic", "concurrent", "probabilistic", "deterministic", "parallel", "polynomial", "scalable", "robust", "distributed")
$verbs = @("analyzes", "evaluates", "implements", "optimizes", "processes", "traverses", "reduces", "computes", "transforms", "partitions")

Write-Output "Generating $Count synthetic documents in $targetDir (Seed: $Seed)..."

for ($i = 1; $i -le $Count; $i++) {
    $topicIndex = $rnd.Next($topics.Length)
    $topic = $topics[$topicIndex]
    
    $paragraphs = @()
    $pCount = $rnd.Next(2, 5)
    for ($p = 0; $p -lt $pCount; $p++) {
        $sentences = @()
        $sCount = $rnd.Next(3, 7)
        for ($s = 0; $s -lt $sCount; $s++) {
            $adj = $adjectives[$rnd.Next($adjectives.Length)]
            $verb = $verbs[$rnd.Next($verbs.Length)]
            $subTopic = $topics[$rnd.Next($topics.Length)]
            $sentences += "The $adj computational system $verb the domain of $subTopic."
        }
        $paragraphs += ($sentences -join " ")
    }
    
    $fullText = "Document ID: $i`nTopic: $topic`n`n" + ($paragraphs -join "`n`n")
    $filePath = "$targetDir/doc_$("{0:D6}" -f $i).txt"
    [System.IO.File]::WriteAllText($filePath, $fullText, [System.Text.Encoding]::UTF8)
}

Write-Output "Successfully generated $Count documents in $targetDir."
