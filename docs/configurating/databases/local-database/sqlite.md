# Local Database (SQLite)

SQLite is the default local database engine for Logger. It requires zero external installation, network configuration, or credentials.

```yaml
SQLite:
  Enable: true
  Data-Deletion: 30
```

Local SQLite files are stored inside `plugins/Logger/logs.db` (or proxy data folder).

---

## ⚡ Concurrency & Performance (v1.8.4.2+)

In **v1.8.4.2**, SQLite has been optimized for high-throughput server workloads:

* **WAL Mode (Write-Ahead Logging)**: Automatically enabled (`journal_mode=WAL`), allowing concurrent non-blocking reads (e.g. `/logger view` or Web Panel queries) while background logging tasks write to disk.
* **Busy Timeout (5000ms)**: Eliminates `SQLITE_BUSY` ("database is locked") exceptions by automatically awaiting locks during brief disk flushes.
* **Synchronous NORMAL**: Delivers fast disk I/O without sacrificing data integrity.
* **Single-Connection Dedicated Pool**: Serializes database writes cleanly through HikariCP to prevent file lock contention.
