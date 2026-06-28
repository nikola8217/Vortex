# Vortex

A distributed job scheduler built to demonstrate production-grade backend architecture patterns. Vortex handles task scheduling, execution, and monitoring — with a focus on fault tolerance, leader election, and horizontal scalability.

---

## Architecture Overview

Vortex is a modular monolith deployed as three independent instances behind an Nginx load balancer. All instances share a single PostgreSQL database and communicate asynchronously through Apache Kafka.

```
                    ┌─────────────┐
                    │    Nginx    │  ← Load Balancer
                    └──────┬──────┘
                           │
          ┌────────────────┼────────────────┐
          │                │                │
   ┌──────▼──────┐  ┌──────▼──────┐  ┌──────▼──────┐
   │  Vortex-1   │  │  Vortex-2   │  │  Vortex-3   │
   │  (Leader?)  │  │  (Follower) │  │  (Follower) │
   └──────┬──────┘  └──────┬──────┘  └──────┬──────┘
          │                │                │
          └────────────────┼────────────────┘
                           │
              ┌────────────┼────────────┐
              │                         │
       ┌──────▼──────┐         ┌────────▼────────┐
       │  PostgreSQL  │         │      Kafka      │
       └─────────────┘         └─────────────────┘
```

### Modules

| Module | Responsibility |
|---|---|
| **shared** | Domain entities, Kafka producer, events, exceptions |
| **scheduler** | Leader election, Quartz cron engine, priority task queue |
| **worker** | Task execution, heartbeat, retry logic, strategy handlers |
| **task** | REST API for task management (submit, get, cancel, retry) |
| **observability** | Health checks, Prometheus metrics |
| **app** | Single entry point — bootstraps all modules into one JAR |

---

## Key Architecture Patterns

### Modular Monolith
Vortex is structured as a modular monolith — a single deployable JAR composed of five well-defined modules with enforced boundaries. Each module follows hexagonal architecture internally. This approach was chosen deliberately over microservices to demonstrate that modularity does not require distribution, and to keep operational complexity low while preserving architectural discipline.

### Leader Election (PostgreSQL Advisory Locks)
Only one instance should schedule jobs at any given time. Vortex uses PostgreSQL Advisory Locks for distributed leader election — each instance attempts to acquire a shared lock on startup and on a fixed interval. The instance that holds the lock acts as the leader and drives the Quartz scheduler. If the leader goes down, the lock is released and another instance takes over automatically.

**Why Advisory Locks over Zookeeper or Redis?**

Vortex already depends on PostgreSQL, so introducing an additional coordination service (Zookeeper, Redis) would add operational overhead without meaningful benefit at this scale. Advisory Locks are transactional, automatically released on connection drop, and sufficient for this use case.

### Priority Queue
Submitted tasks are placed into an in-memory `PriorityBlockingQueue` ranked by `TaskPriority` (HIGH → MEDIUM → LOW). The leader instance drains the queue and dispatches tasks to Kafka for execution. This ensures higher-priority work is always processed first.

### Strategy Pattern (Task Handlers)
Task execution is dispatched through a strategy pattern. Each `TaskType` maps to a dedicated handler:

| Handler | TaskType |
|---|---|
| `DataGeneratorHandler` | DATA_GENERATOR |
| `DataProcessingHandler` | DATA_PROCESSING |
| `QualityCheckHandler` | QUALITY_CHECK |
| `CleanupHandler` | CLEANUP |
| `ReportGenerationHandler` | REPORT_GENERATION |

The `TaskConsumer` receives events from Kafka and delegates to the appropriate handler without any conditional logic in the consumer itself.

### Retry with Exponential Backoff + Dead Letter Queue
Failed tasks are retried up to `maxRetries` times with exponential backoff. Tasks that exhaust all retry attempts are moved to a Dead Letter Queue (DLQ) topic in Kafka and marked as `DEAD` in the database. This prevents poison messages from blocking the main queue indefinitely.

### Worker Heartbeat
Each instance registers itself as a worker on startup and emits a heartbeat event to Kafka every 5 seconds. This allows the system to track which workers are alive and detect dead workers. Worker status transitions: `IDLE` → `BUSY` → `DEAD`.

### Hexagonal Architecture (Ports & Adapters)
Each module follows hexagonal architecture — the domain core is isolated from infrastructure concerns (Kafka, PostgreSQL, HTTP). Ports define the interfaces the domain depends on; adapters implement them. This keeps domain logic independently testable and infrastructure-replaceable.

---

## Task Lifecycle

```
POST /api/tasks
       │
       ▼
  Task saved (PENDING)
       │
       ▼
  TaskSubmittedEvent → Kafka [task-submitted]
       │
       ▼
  Scheduler picks up from PriorityQueue
       │
       ▼
  TaskScheduledEvent → Kafka [task-scheduled]
       │
       ▼
  Worker consumes → Handler executes
       │
       ├── Success → Task marked COMPLETED
       │
       └── Failure → Retry with backoff
                        │
                        └── maxRetries exceeded → Task marked DEAD → DLQ
```

---

## Tech Stack

- **Java 21** + **Spring Boot 4.1.0**
- **Apache Kafka** — async messaging, task dispatch, heartbeat, DLQ
- **Quartz Scheduler** — cron-based job scheduling
- **PostgreSQL 16** — persistent storage, advisory locks for leader election
- **Micrometer + Prometheus** — metrics and observability
- **Nginx** — load balancing across 3 instances
- **Docker** + **Docker Compose** — local environment

---

## Running Locally

**Prerequisites:** Docker and Docker Compose

```bash
git clone https://github.com/nikola8217/Vortex.git
cd Vortex
docker-compose up --build
```

All services and infrastructure (Kafka, Zookeeper, PostgreSQL, Nginx, Prometheus) will start automatically. The application is available at `http://localhost`.

---

## API Reference

### Submit a task
```bash
POST /api/tasks
Content-Type: application/json

{
  "name": "Daily Report",
  "type": "REPORT_GENERATION",
  "priority": "HIGH",
  "maxRetries": 3,
  "timeoutSeconds": 60,
  "cronExpression": "0 0 * * *",
  "payload": {}
}
```

### Get tasks
```bash
GET /api/tasks?status=PENDING&priority=HIGH
```

### Get task by ID
```bash
GET /api/tasks/{id}
```

### Cancel a task
```bash
DELETE /api/tasks/{id}
```

### Retry a failed task
```bash
POST /api/tasks/{id}/retry
```

### Health check
```bash
GET /actuator/health
```

### Prometheus metrics
```bash
GET /actuator/prometheus
```

---

## Design Decisions & Trade-offs

**Why a modular monolith instead of microservices?**
Microservices introduce distributed systems complexity — network latency, partial failures, distributed tracing, independent deployments. For a job scheduler at this scale, a modular monolith provides the same architectural clarity (enforced module boundaries, hexagonal architecture per module) without the operational overhead. Three instances behind Nginx provide horizontal scalability without service mesh complexity.

**Why PostgreSQL Advisory Locks for leader election?**
Vortex already depends on PostgreSQL. Advisory Locks are transactional, automatically released on connection drop, and require zero additional infrastructure. At this scale, they are strictly simpler than Zookeeper or Redis-based solutions with no meaningful trade-off.

**Why Quartz with in-memory store?**
Quartz runs in-memory (`RAMJobStore`) on each instance, but only the leader instance actively schedules jobs. This avoids the complexity of a clustered Quartz JDBC store while still providing reliable cron scheduling — the leader election layer handles coordination instead.

**Why Strategy Pattern for task handlers?**
Adding a new task type requires implementing one new handler and registering it in the dispatcher — no changes to the consumer, router, or existing handlers. The pattern keeps the execution layer open for extension and closed for modification.