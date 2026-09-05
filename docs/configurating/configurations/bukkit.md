# Bukkit / Paper Configuration

Located at `plugins/Logger/config.yml`.

```yaml
# ==============================================================================
#                              Logger Main Config
# ==============================================================================

General:
  Language: en_en
  Prefix: "&8[&bLogger&8] &7"
  Check-Updates: true

Database:
  Enable: false
  Type: SQLite # Options: SQLite, MySQL, MariaDB, PostGreSQL
  Host: localhost
  Port: 3306
  Username: root
  Password: password
  Database: logger
  Data-Deletion: 30 # Days before old logs are automatically purged
```
