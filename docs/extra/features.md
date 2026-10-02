# Features List

Logger provides comprehensive auditing capabilities across all platforms.

---

## Bukkit / Paper / Folia
* **Player Activity**: Chat, Commands, Logins, Leaves, Kicks, Teleports, Deaths, Level Changes, GameMode changes, Registrations.
* **Block & World Actions**: Block Place, Block Break, Bucket Fill, Bucket Empty, Primed TNT, Entity Deaths.
* **Inventory & Items**: Chest / Container Interactions, Item Pickups, Item Drops, Crafting, Anvil usage, Book edits, Enchanting (including 1.21+ Mace & Wind Burst enchantments), Furnace smelting.
* **Creative & NBT**: Creative menu item spawns with full NBT, lore, and custom gear capture.
* **Mount Auditing**: Horse, Llama, and Camel interactions including ownership tracking and inventory access.
* **Modern 1.21+ Mechanics**: Auto-Crafter interactions, Mace combat, Trial Vault unlocks, Sculk Shrieker activations.
* **Server Administration**: Server Start/Stop, Console Commands, RCON Commands, Command Blocks, Server Whitelist (`/whitelist add`, `remove`, `on`, `off`, `reload`), RAM & TPS performance monitors.
* **Staff Tools**: In-game live Spy monitors (`/logger toggle spy`), interactive Inventory Rollback GUI (`/logger playerinventory`), sanitized online diagnostic dump (`/logger dump`).

---

## Proxy (Velocity & BungeeCord)
* Network-wide player chat and command monitoring.
* Server switching and proxy connect/disconnect logs.
* Diagnostic online dump: `/loggerproxy dump` with multi-tier Pastebin key resolution.
* Custom administrative logging: `/loggerproxy manual <msg...>`.
* Real-time Web Panel server status heartbeat synchronization (60s).
* Dynamic daily log rollover (`dd-MM-yyyy.log`) and UTF-8 encoding across all proxy files.
* Custom proxy placeholders: `%proxy%` and `%server%`.
* Centralized database logging across your entire network.

---

## Discord Companion Addon
* Real-time Discord embeds for every server event.
* Individual channel routing for chat, commands, joins, and deaths.
* Bot Presence & Member Activity status displaying live player counts.
* Granular webhook exemptions (`logger.exempt.discord`).

---

## Web Panel Dashboard
* Web-based real-time log search and filtering.
* Live server heartbeat monitoring (`server_status`).
* Discord bot status health checks.
* Visual analytics and interactive log timeline.
