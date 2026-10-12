package texthack.randomized;

import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.*;

/**
 * <h1>Randomized Algorithms Educational Module</h1>
 * <p>
 * Demonstrates:
 * </p>
 * <ul>
 *   <li><b>Randomized QuickSort:</b> Expected O(n log n) by uniform random pivot selection.</li>
 *   <li><b>Miller-Rabin Primality Test:</b> Monte Carlo randomized test with deterministic 64-bit witnesses.</li>
 *   <li><b>Universal Hashing:</b> Hash function family H_{a,b}(x) = ((ax + b) mod p) mod m.</li>
 *   <li><b>Reservoir Sampling:</b> Uniform k-item sampling from a streaming source in O(n) single pass.</li>
 * </ul>
 */
@Component
public class RandomizedAlgorithmsModule {

    private final Random random = new Random(42); // Seeded for reproducibility

    /**
     * Randomized QuickSort: sorts an array in-place.
     */
    public void randomizedQuickSort(int[] arr) {
        if (arr == null || arr.length <= 1) return;
        quickSort(arr, 0, arr.length - 1);
    }

    private void quickSort(int[] arr, int low, int high) {
        if (low < high) {
            int pivotIndex = partitionRandom(arr, low, high);
            quickSort(arr, low, pivotIndex - 1);
            quickSort(arr, pivotIndex + 1, high);
        }
    }

    private int partitionRandom(int[] arr, int low, int high) {
        int r = low + random.nextInt(high - low + 1);
        swap(arr, r, high);
        int pivot = arr[high];
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (arr[j] <= pivot) {
                i++;
                swap(arr, i, j);
            }
        }
        swap(arr, i + 1, high);
        return i + 1;
    }

    private void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    /**
     * Miller-Rabin Primality Test with deterministic witnesses for 64-bit integer range.
     * Guaranteed 100% deterministic accuracy for any long value n < 3.3 * 10^18.
     */
    public boolean isPrime(long n) {
        if (n <= 1) return false;
        if (n <= 3) return true;
        if (n % 2 == 0) return false;

        // Write n - 1 as 2^s * d
        long d = n - 1;
        int s = 0;
        while (d % 2 == 0) {
            d /= 2;
            s++;
        }

        // Deterministic witness set sufficient for all 64-bit integers
        long[] witnesses = {2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37};

        for (long a : witnesses) {
            if (n <= a) break;
            if (!millerRabinWitness(a, d, s, n)) {
                return false; // Composite
            }
        }
        return true;
    }

    private boolean millerRabinWitness(long a, long d, int s, long n) {
        BigInteger bigA = BigInteger.valueOf(a);
        BigInteger bigD = BigInteger.valueOf(d);
        BigInteger bigN = BigInteger.valueOf(n);

        BigInteger x = bigA.modPow(bigD, bigN);

        if (x.equals(BigInteger.ONE) || x.equals(bigN.subtract(BigInteger.ONE))) {
            return true;
        }

        for (int r = 1; r < s; r++) {
            x = x.modPow(BigInteger.valueOf(2), bigN);
            if (x.equals(bigN.subtract(BigInteger.ONE))) {
                return true;
            }
        }
        return false;
    }

    /**
     * Reservoir Sampling:
     * Selects k items uniformly at random from a stream of unknown size n.
     * Each item in the stream has identical probability k / n of being selected.
     */
    public <T> List<T> reservoirSample(Iterator<T> stream, int k) {
        List<T> reservoir = new ArrayList<>(k);
        int count = 0;

        while (stream.hasNext()) {
            T item = stream.next();
            count++;

            if (reservoir.size() < k) {
                reservoir.add(item);
            } else {
                int j = random.nextInt(count);
                if (j < k) {
                    reservoir.set(j, item);
                }
            }
        }

        return reservoir;
    }

    /**
     * Universal Hashing: H_{a,b}(x) = ((a * x + b) mod p) mod m
     * Demonstrates collision rate bounded by 1/m.
     */
    public int universalHash(int key, int a, int b, int primeP, int tableSizeM) {
        long val = ((long) a * key + b) % primeP;
        if (val < 0) val += primeP;
        return (int) (val % tableSizeM);
    }
}
