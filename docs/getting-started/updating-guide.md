# Updating & Upgrading Guide

This guide covers everything you need to know when upgrading Logger from an older version (v1.7.x or v1.8.0–v1.8.3) to **v1.8.4**.

---

## 1. Java Runtime Environment

{% hint style="warning" %}
**Minimum Java Version**: Logger v1.8.4 requires **Java 17 or higher** (Java 21 is strongly recommended).
{% endhint %}

* **Legacy Minecraft Servers (1.8.8 – 1.16.5)**: 
  * Logger's core logging and listeners are 100% compatible with Minecraft 1.8.8 through 1.21+.
  * However, because the jar bundles modern cross-platform libraries (Caffeine cache, modern JDBC connectors, Velocity proxy classes), your host container/JVM must be running **Java 17+**.
  * Modern server hosts and panels (like Pterodactyl, Apex, Bisect) allow selecting Java 17 with a single click in their panel settings.

---

## 2. Upgrading the Main Plugin JAR

1. Stop your server or proxy safely (`stop` / `end`).
2. Delete the old `Logger-*.jar` from your `plugins/` folder.
3. Place [`Logger-1.8.4.jar`](https://github.com/ExceptedPrism3/Logger/releases) into `plugins/`.
4. Start your server.

---

## 3. Discord Addon Migration

In earlier versions, Discord was integrated into the main plugin jar. In v1.8+, Discord functionality is provided via the standalone companion addon:

1. Download [`LoggerDiscordAddon-1.8.4.jar`](https://github.com/ExceptedPrism3/Logger/releases).
2. Place `LoggerDiscordAddon-1.8.4.jar` directly into your `plugins/` folder.
3. **Configuration Path**:
   * The Discord config is located at `plugins/Logger/discord.yml`.
   * **Automatic Migration**: If you previously had configuration inside `plugins/LoggerDiscordAddon/discord.yml`, Logger v1.8.4 will automatically detect, migrate, and cleanly remove the legacy directory on startup!

---

## 4. Database Schema Auto-Migration

Logger features a zero-downtime, safe schema migration engine:

* **MySQL / MariaDB / PostgreSQL / SQLite**:
  * When starting v1.8.4, Logger automatically detects existing database tables and applies any schema updates (such as adding index optimizations and the new `server_status` heartbeat table for Web Panel integration).
  * **No manual SQL scripts or database drops are required.** Your existing log history is completely preserved.

---

## 5. New Permission Exemption

* **`logger.exempt.discord`**:
  * In v1.8.4, you can now exempt specific staff members or VIPs from sending Discord alerts while still recording their actions in local logs/database!
  * Simply assign `logger.exempt.discord` to players or groups you wish to mute on Discord.
