# Environment Setup & VS Code Execution Guide

## 1. Prerequisites
- **Operating System:** Windows 10/11 (64-bit)
- **Java Development Kit:** Java 25 LTS (Oracle HotSpot)
- **Database:** PostgreSQL 18.x
- **Build Tool:** Apache Maven 3.9.9 (Included via Maven Wrapper)
- **Node.js:** Node.js v24.x + npm

---

## 2. Windows Environment Verification
Open a Windows PowerShell terminal in VS Code and verify installed tools:
```powershell
java --version
node --version
psql --version
```

---

## 3. Database Initialization
Ensure PostgreSQL service is running and execute the database setup commands:
```powershell
# Set PostgreSQL password for terminal session
$env:PGPASSWORD = "1234"

# Create database texthack_db
psql -U postgres -c "CREATE DATABASE texthack_db ENCODING 'UTF8';"

# Execute schema DDL
psql -U postgres -d texthack_db -f database/schema.sql

# Insert baseline sample documents
psql -U postgres -d texthack_db -f database/sample_data.sql
```

---

## 4. Backend Execution
From the root project directory:
```powershell
cd backend

# Option A: Using Maven Wrapper
.\mvnw.cmd spring-boot:run

# Option B: Using installed Maven
mvn spring-boot:run
```
The backend REST API will be live at `http://localhost:8080`.
Diagnostics and loaded documents will be logged to the console.

---

## 5. Frontend Execution
Open a second PowerShell terminal in VS Code:
```powershell
cd frontend

# Install dependencies (already executed)
npm install

# Start Vite hot-reload development server
npm run dev
```
Open your browser and navigate to `http://localhost:5173`.

---

## 6. Common Troubleshooting

| Issue | Cause | Solution |
| :--- | :--- | :--- |
| `mvn : The term 'mvn' is not recognized` | PATH environment variable not refreshed | Use `.\mvnw.cmd` from `backend/` or open a new terminal tab. |
| `psql: error: password authentication failed` | Incorrect password in `application.properties` | Verify postgres password is `1234` or update `spring.datasource.password` in `backend/src/main/resources/application.properties`. |
| `Port 8080 already in use` | Another process is holding port 8080 | Run `Stop-Process -Id (Get-NetTCPConnection -LocalPort 8080).OwningProcess -Force` |
| `Port 5173 already in use` | Vite dev server already running | Vite will automatically offer port 5174 or stop other Node instances via Task Manager. |
| `CORS Error in Browser` | Backend CORS not configured | `CorsConfig.java` is already configured for `http://localhost:5173`. Ensure backend is running. |
