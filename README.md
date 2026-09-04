# Logger
### High-Performance Asynchronous Minecraft Server & Proxy Auditing Suite

![Java](https://img.shields.io/badge/Java-8%20|%2017%20|%2021-orange)
![Minecraft](https://img.shields.io/badge/Minecraft-1.8%20--%201.21+-green)
![Velocity](https://img.shields.io/badge/Velocity-3.x+-blue)
![BungeeCord](https://img.shields.io/badge/BungeeCord-Supported-yellow)
![Build](https://img.shields.io/badge/Build-Maven-purple)

**Logger** is a modular, high-throughput logging and server auditing suite designed for Spigot, Paper, Purpur, BungeeCord, Waterfall, FlameCord, and Velocity networks.

Packaged as a **Universal 3-in-1 Multi-Platform JAR**, a single file (`Logger-1.8.3.jar`) runs natively on any backend Minecraft server or proxy network without separate builds or dependencies.

All logging operations are executed asynchronously off the main server thread using HikariCP connection pooling, ensuring 0.00 TPS impact even on high-capacity servers.

---

## ⚡ Features

### 🌐 Universal 3-in-1 Single JAR
* **Universal Multi-Platform**: Drop `Logger-1.8.3.jar` into Spigot, Paper, Purpur, BungeeCord, Waterfall, FlameCord, or Velocity 3.x+ — each platform automatically detects its native plugin descriptor (`plugin.yml`, `bungee.yml`, `velocity-plugin.json`).
* **Isolated Configurations**: Dedicated configuration and message files are extracted per platform without namespace collisions or config pollution.

### 🎮 Player Auditing (35+ Event Types)
* **Chat & Commands**: Public chat, private whispers, command blocks, and executed commands with whitelist/blacklist filtering.
* **Signs & Books**: Sign placements, live sign text edits, and Book & Quill contents.
* **Inventories & Containers**: Chests, Barrels, Hoppers, Furnaces, and Anvil item renaming.
* **Combat & Deaths**: PvP kills, death locations, killer weapons, and full inventory backups.
* **Proxy Events**: Player connect/disconnect, server switching, kicks with reasons, and command interception.
* **1.21+ Mechanics**: Crafter auto-crafting (`Crafter-Craft`), Mace smash attacks, Trial Chamber Vaults, Sculk Shriekers, Villager Trades, and Piglin Bartering.
* **Staff Tracking**: Separate logs and permission tags for staff activity (`logger.staff.log` / `loggerproxy.staff.log`).

### 🖥️ Server Performance & Telemetry
* **Hardware & Tick Monitoring**: Real-time TPS drops and high RAM usage threshold alerts.
* **Server Lifecycle**: Server start, stop, reload, console commands, and RCON executions.
* **Manual Auditing**: On-the-fly custom admin logs via `/logger manual <msg>` or `/loggerproxy manual <msg>`.
* **Player Peaks**: Online player count milestone tracking.

### 🗄️ Database Auto-Evolution
* **Supported Storage**: **MySQL**, **MariaDB**, **PostgreSQL**, and **SQLite**.
* **Automatic Migration**: Upgrading from older Logger versions automatically converts legacy table names to modern snake_case and appends missing columns with **0% data loss**.
* **Automatic Retention Purging**: Configurable `Data-Deletion` automatically purges old logs beyond the retention period upon startup and reload.
* **Performance Indexes**: Automatically creates optimized composite indexes on `(date)` and `(player_name)` across all tables.

### 💬 Multilingual Messages & PlaceholderAPI
* **11 Pre-Packaged Languages**: English (`en_US`), Spanish (`es_ES`), German (`de_DE`), French (`fr_FR`), Italian (`it_IT`), Portuguese (`pt_BR`), Russian (`ru_RU`), Chinese (`zh_CN`), Japanese (`ja_JP`), Korean (`ko_KR`), and Arabic (`ar_SA`).
* **Auto-Extraction & Safe Sync**: Missing translation keys are automatically synchronized on startup and reload without overwriting existing customizations.
* **PlaceholderAPI Support**: Full expansion placeholder support across all in-game messages, chat prefixes, and Discord alerts.

### 🤖 Discord Integration (Addon)
* **Dual Operating Modes**: Support for custom JDA Discord Bot tokens (with player skin avatars and rich embeds) or standalone Discord Webhooks.
* **Dynamic Reload**: `/logger reload` and `/loggerproxy reload` immediately reconnect and update Discord channel mappings on the fly without server restarts.
* **Guaranteed Stop Logging**: Synchronous stop dispatching and graceful connection draining guarantee `Server-Side.Stop` events are delivered before shutdown.

### 🌐 Web Control Panel (Optional Addon)
* **Live Streaming Feed**: Real-time browser stream for server logs with sub-second polling.
* **Search & Filter**: Multi-criteria search across event categories, player UUIDs, dates, and staff tags.
* **Database Maintenance**: 1-Click database schema sync and self-repair tool.
* **Export**: Direct export of log tables to CSV and JSON formats.

---

## 🧩 Project Modules

| Module | Description |
|---|---|
| **`logger-core`** | Central HikariCP database engine, async queue workers, schema migrator, and shared models. |
| **`logger-spigot`** | Bukkit & Spigot event listeners, config managers, and command executors. |
| **`logger-v1_21`** | Dynamic NMS/Bukkit adapter for modern 1.21+ mechanics (Crafter, Mace, Vaults, Sculk). |
| **`logger-paper`** | Universal multi-platform shaded release artifact for Spigot, Paper, Purpur, Bungee, and Velocity. |
| **`logger-velocity`** | Velocity 3.x+ proxy implementation and event hooks. |
| **`logger-bungee`** | BungeeCord, Waterfall, and FlameCord proxy implementation. |
| **`logger-discord-addon`** | Standalone multi-platform Discord integration bridge (Spigot, BungeeCord, Velocity). |
| **`logger-web-panel`** | Dedicated web dashboard and real-time log analytics interface. |

---

## ⌨️ Commands & Permissions

### Server Commands (Spigot / Paper / Purpur)
```
/logger view <type|player> [<page>]  - In-game interactive log browser with clickable pagination
/logger reload                       - Reloads configurations, messages, and database pools
/logger manual <message...>          - Logs custom server/admin messages
/logger dump                         - Generates a secure online diagnostic dump for support
/logger support                      - Quick links to documentation and Discord
```

### Proxy Commands (Velocity / BungeeCord / Waterfall)
```
/loggerproxy reload          - Reloads proxy configuration, Discord bot, and database pools
/loggerproxy manual <msg...> - Records a custom administrative log across files, Discord, and DB
/loggerproxy discord         - Displays Discord support server link
(Alias: /lgp)
```

### Permissions Matrix

| Permission | Platform | Description | Default |
|:---|:---|:---|:---|
| `logger.admin` | Spigot / Bungee | Full administrative access to commands and views | OP |
| `logger.view` | Spigot | Permission to use `/logger view` | OP |
| `logger.reload` | Spigot | Permission to reload configurations | OP |
| `logger.staff` | Spigot | Marks player as staff member | OP |
| `logger.staff.log` | Spigot | Logs player events into dedicated Staff records | OP |
| `logger.exempt` | Spigot / Bungee | Excludes player from being logged | False |
| `logger.exempt.discord` | Spigot | Excludes player from Discord broadcasts | False |
| `loggerproxy.admin` | Bungee / Velocity | Full proxy administrative access | OP / Superuser |
| `loggerproxy.reload` | Bungee / Velocity | Allows `/loggerproxy reload` and `/loggerproxy manual` | OP / Superuser |
| `loggerproxy.staff.log` | Bungee / Velocity | Logs player into proxy Staff records | OP / Superuser |
| `loggerproxy.exempt` | Bungee / Velocity | Excludes player from proxy logs | False |

---

## 🏗️ Building from Source

### Requirements
* **Java 21 JDK**
* **Apache Maven 3.8+**

```bash
# Clone the repository
git clone https://github.com/ExceptedPrism3/Logger.git
cd Logger

# Build all modules and Universal JARs
mvn clean package -DskipTests
```

The compiled release artifacts will be placed in `releases/`:
* `releases/Logger-1.8.3.jar` *(Universal JAR for Spigot, Paper, Purpur, BungeeCord, and Velocity)*
* `releases/LoggerDiscordAddon-1.8.3.jar` *(Universal Discord Addon for Spigot, BungeeCord, and Velocity)*

---

## 💬 Community & Support

* **SpigotMC Resource**: [spigotmc.org/resources/logger.82729](https://www.spigotmc.org/resources/logger.82729/)
* **Official Documentation**: [prism3.gitbook.io/logger](https://prism3.gitbook.io/logger/)
* **Discord Community**: [discord.gg/MfR5mcpVfX](https://discord.gg/MfR5mcpVfX)

---

Developed with ❤️ by **Prism3**
