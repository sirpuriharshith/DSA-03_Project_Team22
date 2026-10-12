package texthack.strings;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * <h1>Suffix Automaton (DAWG - Directed Acyclic Word Graph)</h1>
 * <p>
 * Minimal deterministic finite automaton that recognizes all suffixes of a string.
 * Constructed online character-by-character in strictly <b>O(n) time</b> and <b>O(n) space</b>.
 * </p>
 */
@Component
public class SuffixAutomaton {

    public static class State {
        public int len;
        public int link;
        public final Map<Character, Integer> next = new HashMap<>();

        public State(int len, int link) {
            this.len = len;
            this.link = link;
        }
    }

    private State[] states;
    private int size;
    private int last;

    public SuffixAutomaton() {
        init(1);
    }

    public void init(int maxLen) {
        int maxStates = 2 * maxLen + 2;
        states = new State[maxStates];
        states[0] = new State(0, -1);
        size = 1;
        last = 0;
    }

    /**
     * Builds automaton for given string in linear time O(n).
     */
    public void build(String s) {
        if (s == null) return;
        init(s.length());
        for (int i = 0; i < s.length(); i++) {
            extend(s.charAt(i));
        }
    }

    /**
     * Extends the automaton by one character in amortized O(1) time.
     */
    public void extend(char c) {
        int cur = size++;
        states[cur] = new State(states[last].len + 1, 0);

        int p = last;
        while (p != -1 && !states[p].next.containsKey(c)) {
            states[p].next.put(c, cur);
            p = states[p].link;
        }

        if (p == -1) {
            states[cur].link = 0;
        } else {
            int q = states[p].next.get(c);
            if (states[p].len + 1 == states[q].len) {
                states[cur].link = q;
            } else {
                int clone = size++;
                states[clone] = new State(states[p].len + 1, states[q].link);
                states[clone].next.putAll(states[q].next);

                while (p != -1 && states[p].next.get(c) == q) {
                    states[p].next.put(c, clone);
                    p = states[p].link;
                }

                states[q].link = states[cur].link = clone;
            }
        }
        last = cur;
    }

    /**
     * Tests whether pattern appears as a substring in the indexed text.
     * Complexity: O(m)
     */
    public boolean contains(String pattern) {
        if (pattern == null || states == null) return false;
        int cur = 0;
        for (int i = 0; i < pattern.length(); i++) {
            char c = pattern.charAt(i);
            if (!states[cur].next.containsKey(c)) {
                return false;
            }
            cur = states[cur].next.get(c);
        }
        return true;
    }

    public int getStateCount() {
        return size;
    }
}
