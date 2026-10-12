package texthack.metrics;

import org.springframework.stereotype.Component;

/**
 * <h1>Parallel Computing Performance & Scalability Metrics Engine</h1>
 * <p>
 * Evaluates execution performance based on classical parallel computing metrics:
 * </p>
 * <ul>
 *   <li><b>Throughput:</b> documents processed / second</li>
 *   <li><b>Speedup (S_p):</b> T_1 / T_p</li>
 *   <li><b>Parallel Efficiency (E_p):</b> (S_p / p) * 100%</li>
 *   <li><b>Brent's Theorem Bound:</b> T_p &lt;= (T_1 / p) + T_infinity</li>
 * </ul>
 */
@Component
public class PerformanceMetricsEngine {

    /**
     * Calculates throughput in documents per second.
     */
    public double calculateThroughput(int documentCount, double executionTimeMs) {
        if (executionTimeMs <= 0.0 || documentCount <= 0) {
            return 0.0;
        }
        double seconds = executionTimeMs / 1000.0;
        double throughput = documentCount / seconds;
        return Math.round(throughput * 10.0) / 10.0;
    }

    /**
     * Calculates speedup S_p = T_1 / T_p.
     */
    public double calculateSpeedup(double sequentialTimeMs, double parallelTimeMs) {
        if (parallelTimeMs <= 0.0 || sequentialTimeMs <= 0.0) {
            return 1.00;
        }
        double speedup = sequentialTimeMs / parallelTimeMs;
        return Math.round(speedup * 100.0) / 100.0;
    }

    /**
     * Calculates parallel efficiency E_p = (S_p / p) * 100%.
     */
    public double calculateEfficiency(double speedup, int threadCount) {
        if (threadCount <= 0) {
            return 100.0;
        }
        double efficiency = (speedup / threadCount) * 100.0;
        return Math.min(100.0, Math.round(efficiency * 10.0) / 10.0);
    }
}
