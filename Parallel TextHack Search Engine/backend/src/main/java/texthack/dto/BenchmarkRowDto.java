package texthack.dto;

public class BenchmarkRowDto {

    private int threads;
    private double executionTimeMs;
    private double throughput; // docs/sec
    private double speedup;
    private double efficiency; // percentage

    public BenchmarkRowDto() {
    }

    public BenchmarkRowDto(int threads, double executionTimeMs, double throughput, double speedup, double efficiency) {
        this.threads = threads;
        this.executionTimeMs = executionTimeMs;
        this.throughput = throughput;
        this.speedup = speedup;
        this.efficiency = efficiency;
    }

    public int getThreads() {
        return threads;
    }

    public void setThreads(int threads) {
        this.threads = threads;
    }

    public double getExecutionTimeMs() {
        return executionTimeMs;
    }

    public void setExecutionTimeMs(double executionTimeMs) {
        this.executionTimeMs = executionTimeMs;
    }

    public double getThroughput() {
        return throughput;
    }

    public void setThroughput(double throughput) {
        this.throughput = throughput;
    }

    public double getSpeedup() {
        return speedup;
    }

    public void setSpeedup(double speedup) {
        this.speedup = speedup;
    }

    public double getEfficiency() {
        return efficiency;
    }

    public void setEfficiency(double efficiency) {
        this.efficiency = efficiency;
    }
}
