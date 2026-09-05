# Permissions

## Proxy (BungeeCord & Velocity)

| Permission | Description |
| :--- | :--- |
| `loggerproxy.staff` | Grants access to `/loggerproxy` command suite and excludes user from basic staff logging. |
| `loggerproxy.reload` | Ability to hot-reload proxy configurations via `/loggerproxy reload`. |
| `loggerbungee.staff.log` | Specifically enables logging for players with staff rank. |
| `loggerbungee.exempt` | Fully excludes the player from all proxy-level logging. |

---

## Bukkit / Paper / Purpur / Folia

| Permission | Description |
| :--- | :--- |
| `logger.staff` | Core staff permission; grants access to the `/logger` admin command suite. |
| `logger.reload` | Ability to reload plugin configurations via `/logger reload`. |
| `logger.staff.log` | Enables logging for staff members. |
| `logger.exempt` | Prevents the player from being logged into the database and log files. |
| `logger.exempt.discord` | **[NEW]** Prevents the player's actions from sending Discord webhook alerts while preserving local audit logs. |
| `logger.spy` | Allows viewing live in-game spy monitors (commands, signs, anvils, books) from players and staff. |
| `logger.spy.bypass` | Ability to view other players' actions in spy mode without having your own actions broadcast to other spies. |
| `logger.update` | Receives in-game notifications when a new version of Logger is released. |
