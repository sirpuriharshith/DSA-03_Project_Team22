-- Parallel TextHack Search Engine - PostgreSQL DDL Schema
-- Database: texthack_db

DROP TABLE IF EXISTS search_results CASCADE;
DROP TABLE IF EXISTS benchmark_results CASCADE;
DROP TABLE IF EXISTS algorithm_runs CASCADE;
DROP TABLE IF EXISTS search_queries CASCADE;
DROP TABLE IF EXISTS documents CASCADE;

-- 1. Document Corpus Table
CREATE TABLE documents (
    id BIGSERIAL PRIMARY KEY,
    file_name VARCHAR(255) NOT NULL,
    file_path VARCHAR(1024) NOT NULL,
    file_type VARCHAR(32) NOT NULL, -- TXT, PDF, DOCX, CSV
    content TEXT NOT NULL,
    file_size BIGINT NOT NULL,
    word_count INT NOT NULL DEFAULT 0,
    character_count INT NOT NULL DEFAULT 0,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    indexed_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_documents_file_name ON documents(file_name);
CREATE INDEX idx_documents_file_type ON documents(file_type);

-- 2. Search Query Audit Trail
CREATE TABLE search_queries (
    id BIGSERIAL PRIMARY KEY,
    query_text VARCHAR(255) NOT NULL,
    search_type VARCHAR(64) NOT NULL, -- EXACT, PHRASE, MULTI_KEYWORD, FUZZY
    execution_mode VARCHAR(32) NOT NULL, -- SEQUENTIAL, PARALLEL
    thread_count INT NOT NULL DEFAULT 1,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_queries_created_at ON search_queries(created_at);

-- 3. Search Results Linked to Query & Document
CREATE TABLE search_results (
    id BIGSERIAL PRIMARY KEY,
    query_id BIGINT NOT NULL REFERENCES search_queries(id) ON DELETE CASCADE,
    document_id BIGINT NOT NULL REFERENCES documents(id) ON DELETE CASCADE,
    occurrence_count INT NOT NULL DEFAULT 0,
    score DOUBLE PRECISION NOT NULL DEFAULT 0.0,
    edit_distance INT DEFAULT NULL
);

CREATE INDEX idx_results_query_id ON search_results(query_id);
CREATE INDEX idx_results_document_id ON search_results(document_id);

-- 4. Benchmark Scalability Records
CREATE TABLE benchmark_results (
    id BIGSERIAL PRIMARY KEY,
    query_text VARCHAR(255) NOT NULL,
    search_type VARCHAR(64) NOT NULL,
    document_count INT NOT NULL,
    thread_count INT NOT NULL,
    execution_time_ms DOUBLE PRECISION NOT NULL,
    throughput DOUBLE PRECISION NOT NULL, -- docs / sec
    speedup DOUBLE PRECISION NOT NULL DEFAULT 1.0,
    efficiency DOUBLE PRECISION NOT NULL DEFAULT 100.0, -- percentage
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_benchmarks_thread_count ON benchmark_results(thread_count);

-- 5. Educational Algorithm Micro-benchmarks
CREATE TABLE algorithm_runs (
    id BIGSERIAL PRIMARY KEY,
    algorithm_name VARCHAR(128) NOT NULL, -- KMP, Z, RABIN_KARP, AHO_CORASICK, DINIC, etc.
    input_size INT NOT NULL,
    execution_time_ns BIGINT NOT NULL,
    memory_used_bytes BIGINT NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_algo_runs_name ON algorithm_runs(algorithm_name);
