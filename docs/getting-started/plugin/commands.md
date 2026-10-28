# Commands

## Proxy Commands

| Command | Permission | Description |
| :--- | :--- | :--- |
| `/loggerproxy` | `loggerproxy.staff` | Displays the proxy plugin help menu. |
| `/loggerproxy reload` | `loggerproxy.staff` \| `loggerproxy.reload` | Hot-reloads proxy configuration files, Discord bot, and database pools. |
| `/loggerproxy manual <msg>` | `loggerproxy.staff` \| `loggerproxy.reload` | Records a custom administrative log across proxy files, Discord, and database. |
| `/loggerproxy dump` | `loggerproxy.staff` \| `loggerproxy.reload` | Generates a safe online diagnostic Pastebin dump of proxy configs and logs. |
| `/loggerproxy discord` | `loggerproxy.staff` | Displays the official Discord support link. |

---

## Bukkit / Paper / Purpur / Folia Commands

| Command | Permission | Description |
| :--- | :--- | :--- |
| `/logger` | `logger.use` \| `logger.admin` | Displays the plugin command help menu. |
| `/logger view <type\|player> [<page>]` | `logger.view` \| `logger.admin` | In-game interactive log browser with clickable pagination. |
| `/logger reload` | `logger.reload` \| `logger.admin` | Hot-reloads configs without requiring a server reboot. |
| `/logger manual <msg>` | `logger.use` \| `logger.admin` | Records a custom administrative log across files, Discord, and database. |
| `/logger toggle spy [commands \| book \| anvil \| sign]` | `logger.staff` | Toggles real-time in-game staff spy features on or off. |
| `/logger discord` | `logger.use` | Displays the official Discord support link. |
| `/logger dump` | `logger.admin` | Generates a sanitized online pastebin of system information and configs (database passwords and tokens are automatically scrubbed). |
| `/logger playerinventory <player>` | `logger.staff` | Opens an interactive GUI of online players and their historic inventory backups. |
