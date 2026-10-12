package texthack.strings;

import org.springframework.stereotype.Component;

import java.util.*;

/**
 * <h1>Aho-Corasick Multi-Pattern String Matching Automaton</h1>
 * <p>
 * Constructs a finite state automaton over a dictionary of patterns with:
 * </p>
 * <ol>
 *   <li><b>Trie Structure:</b> Stores pattern prefixes.</li>
 *   <li><b>Failure Links:</b> Fallback pointers computed via Breadth-First Search (BFS).</li>
 *   <li><b>Dictionary / Output Links:</b> Fast-forward links to avoid traversing non-terminal failure chains.</li>
 * </ol>
 * <ul>
 *   <li><b>Preprocessing Time:</b> O(sum of pattern lengths * alphabet)</li>
 *   <li><b>Search Time:</b> O(n + matches) linear in text length regardless of pattern count</li>
 *   <li><b>Space Complexity:</b> O(total characters across all patterns)</li>
 * </ul>
 */
@Component
public class AhoCorasickMatcher implements StringMatcher {

    /**
     * Trie Node representing an automaton state.
     */
    public static class Node {
        final Map<Character, Node> children = new HashMap<>();
        Node failureLink = null;
        Node dictLink = null; // Dictionary / compressed suffix link
        final List<String> outputPatterns = new ArrayList<>();
    }

    @Override
    public MatchResult search(String text, String pattern) {
        if (pattern == null || pattern.isEmpty()) {
            return new MatchResult(getAlgorithmName(), pattern, Collections.emptyList(), 0);
        }
        Map<String, List<Integer>> multiMap = searchMultiple(text, Collections.singletonList(pattern));
        List<Integer> positions = multiMap.getOrDefault(pattern, Collections.emptyList());
        return new MatchResult(getAlgorithmName(), pattern, positions, 0);
    }

    /**
     * Searches for multiple pattern keywords simultaneously across the document text.
     *
     * @param text     document text corpus
     * @param patterns list of search keywords
     * @return Map of pattern -> list of occurrence start indices
     */
    public Map<String, List<Integer>> searchMultiple(String text, Collection<String> patterns) {
        Map<String, List<Integer>> resultMap = new HashMap<>();
        if (text == null || patterns == null || patterns.isEmpty()) {
            return resultMap;
        }

        // Initialize empty lists for each pattern
        for (String p : patterns) {
            if (p != null && !p.isEmpty()) {
                resultMap.put(p, new ArrayList<>());
            }
        }

        if (resultMap.isEmpty() || text.isEmpty()) {
            return resultMap;
        }

        // 1. Build Trie
        Node root = new Node();
        for (String pat : resultMap.keySet()) {
            Node curr = root;
            for (int i = 0; i < pat.length(); i++) {
                char c = pat.charAt(i);
                curr = curr.children.computeIfAbsent(c, k -> new Node());
            }
            curr.outputPatterns.add(pat);
        }

        // 2. Build Failure and Output Links via BFS
        Queue<Node> queue = new ArrayDeque<>();
        for (Node child : root.children.values()) {
            child.failureLink = root;
            queue.add(child);
        }

        while (!queue.isEmpty()) {
            Node u = queue.poll();

            for (Map.Entry<Character, Node> entry : u.children.entrySet()) {
                char ch = entry.getKey();
                Node v = entry.getValue();
                queue.add(v);

                // Find failure state for child v
                Node f = u.failureLink;
                while (f != null && !f.children.containsKey(ch)) {
                    f = f.failureLink;
                }
                v.failureLink = (f != null) ? f.children.get(ch) : root;

                // Set dictionary link: points to nearest terminal node on failure path
                if (!v.failureLink.outputPatterns.isEmpty()) {
                    v.dictLink = v.failureLink;
                } else {
                    v.dictLink = v.failureLink.dictLink;
                }
            }
        }

        // 3. Scan Text Corpus
        Node curr = root;
        int n = text.length();

        for (int i = 0; i < n; i++) {
            char c = text.charAt(i);

            // Follow failure transitions if current state doesn't have transition on c
            while (curr != null && !curr.children.containsKey(c)) {
                curr = curr.failureLink;
            }

            if (curr == null) {
                curr = root;
                continue;
            }

            curr = curr.children.get(c);

            // Harvest matches directly from current node
            for (String matchedPattern : curr.outputPatterns) {
                int startIndex = i - matchedPattern.length() + 1;
                resultMap.get(matchedPattern).add(startIndex);
            }

            // Follow dictionary suffix links to collect overlapping matches
            Node dict = curr.dictLink;
            while (dict != null) {
                for (String matchedPattern : dict.outputPatterns) {
                    int startIndex = i - matchedPattern.length() + 1;
                    resultMap.get(matchedPattern).add(startIndex);
                }
                dict = dict.dictLink;
            }
        }

        return resultMap;
    }

    @Override
    public String getAlgorithmName() {
        return "AHO_CORASICK";
    }
}
