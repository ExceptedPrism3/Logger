# Permissions

## Proxy (BungeeCord & Velocity)

| Permission | Description |
| :--- | :--- |
| `loggerproxy.staff` | Grants access to the `/loggerproxy` command suite. |
| `loggerproxy.reload` | Allows `/loggerproxy reload`, `/loggerproxy manual`, and `/loggerproxy dump`. |
| `loggerproxy.staff.log` | Enables dedicated proxy logging for players with staff permissions. |
| `loggerproxy.exempt` | Fully excludes the player from all proxy-level logging (chat, commands, switches). |
| `loggerbungee.staff.log` | Legacy alias for proxy staff logging on BungeeCord. |
| `loggerbungee.exempt` | Legacy alias for proxy logging exemption on BungeeCord. |

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
