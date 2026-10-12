package texthack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;

/**
 * Parallel TextHack Search Engine - Application Bootstrap
 * Academic Advanced Algorithms & High-Performance Search Platform
 */
@SpringBootApplication
public class TextHackApplication {

    private static final Logger log = LoggerFactory.getLogger(TextHackApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(TextHackApplication.class, args);
    }

    @Bean
    public CommandLineRunner systemDiagnosticsRunner() {
        return args -> {
            int cores = Runtime.getRuntime().availableProcessors();
            MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
            long maxHeapMb = memoryBean.getHeapMemoryUsage().getMax() / (1024 * 1024);
            long totalMemMb = Runtime.getRuntime().totalMemory() / (1024 * 1024);
            String javaVersion = System.getProperty("java.version");
            String osName = System.getProperty("os.name");

            log.info("===============================================================================");
            log.info("      PARALLEL TEXTHACK SEARCH ENGINE — INITIALIZED SUCCESSFULLY               ");
            log.info("===============================================================================");
            log.info(" Operating System     : {}", osName);
            log.info(" Java Runtime         : Java {}", javaVersion);
            log.info(" Available CPU Cores  : {} hardware threads", cores);
            log.info(" JVM Max Heap Memory  : {} MB (Allocated: {} MB)", maxHeapMb, totalMemMb);
            log.info(" Target Database      : PostgreSQL (texthack_db)");
            log.info(" Algorithm Engine     : 100% Handcrafted (KMP, Z, Rabin-Karp, Aho-Corasick)");
            log.info(" Dynamic Programming  : Levenshtein, Damerau-Levenshtein, Needleman-Wunsch");
            log.info(" Concurrency Strategy : Dynamic Partitioning + Tree-Style Parallel Reduction");
            log.info(" Web Server Port      : 8080 (REST API ready at http://localhost:8080)");
            log.info("===============================================================================");
        };
    }
}
