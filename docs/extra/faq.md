# Frequently Asked Questions (FAQ)

## What Minecraft versions does Logger support?
Logger v1.8.4 supports **Minecraft 1.8.8 all the way through 1.21+** (Paper, Purpur, Folia, Spigot, BungeeCord, Velocity).

## What Java version do I need?
Logger requires **Java 17 or higher** on the server machine (**Java 21 is recommended**). Even if your server runs Minecraft 1.8.8, running the server JVM on Java 17 will ensure complete compatibility with Logger's universal jar and modern database drivers.

## How does player logging work?
Every player activity (chat, commands, block breaks, chest opens, etc.) is logged by default. To exempt specific players or staff ranks from being logged, assign them the permission `logger.exempt`.

## How can I exempt staff from Discord alerts only?
In v1.8.4, assign the permission **`logger.exempt.discord`**. The player will still be logged to your database/files, but their actions will not spam your Discord channels!

## How do I set up the Discord Bot?
Download and place `LoggerDiscordAddon-1.8.4.jar` into your `plugins/` folder and configure your bot token in `plugins/Logger/discord.yml`. See our [**Discord Guide**](../getting-started/discord-integration/discord.md) for full instructions.

## What database engines are supported?
Logger natively supports **MySQL**, **MariaDB**, **PostgreSQL**, and **SQLite** with lag-free HikariCP connection pooling and automatic schema updates.

## I need help or found a bug!
Join our [**Discord Server**](https://discord.gg/MfR5mcpVfX), run `/logger dump` on your server to create a sanitized configuration paste, and open a ticket!
