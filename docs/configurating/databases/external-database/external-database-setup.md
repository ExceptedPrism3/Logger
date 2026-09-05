# External Database Setup (MySQL, MariaDB, PostgreSQL)

Logger v1.8.4 features native multi-DBMS support with built-in connection pooling (HikariCP) and automatic schema migration.

---

## Supported Database Engines

1. **MySQL** (using modern `com.mysql:mysql-connector-j 8.2.0`)
2. **MariaDB** (using modern `org.mariadb.jdbc 3.3.5`)
3. **PostgreSQL** (using modern `org.postgresql:postgresql 42.7.13`)

---

## Configuration

In `plugins/Logger/config.yml`:

```yaml
Database:
  Enable: true
  Type: MySQL # Options: MySQL, MariaDB, PostGreSQL, SQLite
  Host: 127.0.0.1
  Port: 3306 # Use 3306 for MySQL/MariaDB, 5432 for PostgreSQL
  Username: logger_user
  Password: "YourSecurePassword"
  Database: logger_db
  Data-Deletion: 30 # Number of days to retain logs before automatic cleanup
```

---

## How it Works
* **Automatic Table Creation**: On first launch, Logger automatically creates all tables (`players`, `logs`, `inventories`, `server_status`).
* **Connection Pooling**: Uses HikariCP for asynchronous, lag-free database queries.
* **Web Panel Ready**: Connecting multiple Minecraft servers to the same external database allows the [**Logger Web Panel**](../../web-panel/web-panel-setup.md) to display unified network logs and live server heartbeats!
