# Database Management — PostgreSQL

## Database Name
`texthack_db`

## Schemas & Scripts
- `schema.sql`: Contains DDL table definitions, primary keys, foreign keys, and indexes.
- `sample_data.sql`: Contains baseline seed records for testing queries and benchmark records.

## Connection Defaults
- **Host**: `localhost`
- **Port**: `5432`
- **Username**: `postgres`
- **Database**: `texthack_db`

## Initialization Commands (Windows PowerShell)
```powershell
# Connect to PostgreSQL and create database
psql -U postgres -c "CREATE DATABASE texthack_db;"

# Execute schema
psql -U postgres -d texthack_db -f database/schema.sql

# Execute sample seed data
psql -U postgres -d texthack_db -f database/sample_data.sql
```
