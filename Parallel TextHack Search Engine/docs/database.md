# PostgreSQL Database Architecture (`texthack_db`)

## 1. Database Connection Properties
- **Host:** `localhost`
- **Port:** `5432`
- **Database:** `texthack_db`
- **Default Username:** `postgres`
- **Configured Password:** `1234`
- **Driver:** `org.postgresql.Driver`
- **Pool:** HikariCP (Max 10, Min idle 2)

---

## 2. Table Schemas & Relationships

```mermaid
erDiagram
    documents ||--o{ search_results : contains
    search_queries ||--o{ search_results : generates

    documents {
        bigserial id PK
        varchar file_name
        varchar file_path
        varchar file_type
        text content
        bigint file_size
        integer word_count
        integer character_count
        timestamp created_at
        timestamp indexed_at
    }

    search_queries {
        bigserial id PK
        varchar query_text
        varchar search_type
        varchar execution_mode
        integer thread_count
        timestamp created_at
    }

    search_results {
        bigserial id PK
        bigint query_id FK
        bigint document_id FK
        integer occurrence_count
        double_precision score
        integer edit_distance
    }

    benchmark_results {
        bigserial id PK
        varchar query_text
        varchar search_type
        integer document_count
        integer thread_count
        double_precision execution_time_ms
        double_precision throughput
        double_precision speedup
        double_precision efficiency
        timestamp created_at
    }

    algorithm_runs {
        bigserial id PK
        varchar algorithm_name
        integer input_size
        bigint execution_time_ns
        bigint memory_used_bytes
        timestamp created_at
    }
```

---

## 3. Index Strategy
- `idx_documents_file_name` on `documents(file_name)`: Fast lookup when syncing disk documents.
- `idx_documents_file_type` on `documents(file_type)`: Format-based filtering.
- `idx_results_query_id` on `search_results(query_id)`: Instant retrieval of ranked query matches.
- `idx_benchmarks_thread_count` on `benchmark_results(thread_count)`: Fast historical trend analytics.
