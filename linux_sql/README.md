# Jarvis Remote Host Monitoring Agent

## Overview

The **Jarvis Remote Host Monitoring Agent** is a professional-grade infrastructure monitoring system designed to automatically collect hardware specifications and real-time resource utilization from distributed Linux servers.

It follows a lightweight **agent-based architecture**, where each node runs simple Bash scripts that push system telemetry to a centralized **PostgreSQL** database running inside **Docker**. This solution enables System Administrators and DevOps Engineers to monitor server health, detect performance bottlenecks, and make data-driven scaling decisionswithout manual checks.

## Key Capabilities
- Automatic hardware inventory
- Continuous resource monitoring (CPU, memory, disk)
- Centralized PostgreSQL data store
- Cron-based automation
- SQL-based analytics for operational insights

---

## Technology Stack

| Component     | Purpose |
|--------------|---------|
| **Bash**     | System metric collection & parsing |
| **Docker**   | PostgreSQL container management |
| **PostgreSQL** | Telemetry data storage |
| **Git**      | Version control & collaboration |
| **Crontab**  | Automated metric scheduling |

---

## System Architecture

The system uses a **hub-and-spoke** model:

- **Agent Nodes**  
  Each server runs lightweight Bash scripts to collect system metrics.

- **Central Database Node**  
  A Dockerized PostgreSQL instance stores all telemetry data.

Each agent independently pushes data to the same database endpoint over the network.

---

## Quick Start
Follow these steps to initialize your monitoring environment:

```bash
# 1. Provision the PostgreSQL container
./scripts/psql_docker.sh create [db_username] [db_password]

# 2. Initialize the database schema
psql -h localhost -U postgres -d host_agent -f sql/ddl.sql

# 3. Register the host hardware specifications
./scripts/host_info.sh "localhost" 5432 "host_agent" "postgres" "password"

# 4. Execute a manual usage data collection
./scripts/host_usage.sh "localhost" 5432 "host_agent" "postgres" "password"

# 5. Automate collection via Crontab
crontab -e
# Add the following line to sync every minute:
# * * * * * bash /absolute/path/to/host_usage.sh "localhost" 5432 "host_agent" "postgres" "password" > /tmp/host_usage.log 2>&1
```

---

## Diagram
```mermaid
graph TD
    subgraph "Managed Linux Nodes (Agents)"
        Node1[Linux Host 1]
        Node2[Linux Host 2]
        Node3[Linux Host 3]
    end

    subgraph "Agent Scripts per Node"
        direction TB
        A1[host_info.sh<br/>Static Hardware Specs]
        A2[host_usage.sh<br/>Real-time Metrics]
        CRON[Crontab<br/>Schedules usage every 1m]
    end

    subgraph "Central Management Node"
        DOCKER[Docker Container]
        DB[(PostgreSQL Database)]
        DOCKER --> DB
    end

    %% Connections
    Node1 & Node2 & Node3 --> A1
    Node1 & Node2 & Node3 --> A2
    CRON --> A2
    
    A1 -- "Insert Specs (Once)" --> DB
    A2 -- "Insert Usage (Every minute)" --> DB

    style DB fill:#f9f,stroke:#333,stroke-width:2px
    style DOCKER fill:#2496ed,color:#fff
```
