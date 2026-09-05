# Local Database (SQLite)

SQLite is the default database engine for Logger. It requires zero installation or configuration.

```yaml
Database:
  Enable: false # When false, Logger uses local file logs or local SQLite storage
  Type: SQLite
  Data-Deletion: 30
```

Local SQLite files are stored safely inside `plugins/Logger/`.
