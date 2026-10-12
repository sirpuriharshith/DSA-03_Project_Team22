-- Parallel TextHack Search Engine - DDL Schema
CREATE TABLE IF NOT EXISTS documents (
    id BIGSERIAL PRIMARY KEY,
    file_name VARCHAR(255) NOT NULL,
    file_path VARCHAR(1024) NOT NULL,
    file_type VARCHAR(32) NOT NULL,
    content TEXT NOT NULL,
    file_size BIGINT NOT NULL,
    word_count INT NOT NULL DEFAULT 0,
    character_count INT NOT NULL DEFAULT 0,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    indexed_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS search_queries (
    id BIGSERIAL PRIMARY KEY,
    query_text VARCHAR(255) NOT NULL,
    search_type VARCHAR(64) NOT NULL,
    execution_mode VARCHAR(32) NOT NULL,
    thread_count INT NOT NULL DEFAULT 1,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS search_results (
    id BIGSERIAL PRIMARY KEY,
    query_id BIGINT NOT NULL REFERENCES search_queries(id) ON DELETE CASCADE,
    document_id BIGINT NOT NULL REFERENCES documents(id) ON DELETE CASCADE,
    occurrence_count INT NOT NULL DEFAULT 0,
    score DOUBLE PRECISION NOT NULL DEFAULT 0.0,
    edit_distance INT DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS benchmark_results (
    id BIGSERIAL PRIMARY KEY,
    query_text VARCHAR(255) NOT NULL,
    search_type VARCHAR(64) NOT NULL,
    document_count INT NOT NULL,
    thread_count INT NOT NULL,
    execution_time_ms DOUBLE PRECISION NOT NULL,
    throughput DOUBLE PRECISION NOT NULL,
    speedup DOUBLE PRECISION NOT NULL DEFAULT 1.0,
    efficiency DOUBLE PRECISION NOT NULL DEFAULT 100.0,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS algorithm_runs (
    id BIGSERIAL PRIMARY KEY,
    algorithm_name VARCHAR(128) NOT NULL,
    input_size INT NOT NULL,
    execution_time_ns BIGINT NOT NULL,
    memory_used_bytes BIGINT NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
