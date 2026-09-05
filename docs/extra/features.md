# Features List

Logger provides comprehensive auditing capabilities across all platforms.

---

## Bukkit / Paper / Folia
* **Player Activity**: Chat, Commands, Logins, Leaves, Kicks, Teleports, Deaths, Level Changes, GameMode changes, Registrations.
* **Block & World Actions**: Block Place, Block Break, Bucket Fill, Bucket Empty, Primed TNT, Entity Deaths.
* **Inventory & Items**: Chest / Container Interactions, Item Pickups, Item Drops, Crafting, Anvil usage, Book edits, Enchanting (including 1.21+ Mace & Wind Burst enchantments), Furnace smelting.
* **Modern 1.21+ Mechanics**: Auto-Crafter interactions, Mace combat, Trial Vault unlocks, Sculk Shrieker activations.
* **Staff Tools**: In-game live Spy monitors (`/logger toggle spy`), interactive Inventory Rollback GUI (`/logger playerinventory`), sanitized online diagnostic dump (`/logger dump`).

---

## Proxy (Velocity & BungeeCord)
* Network-wide player chat and command monitoring.
* Server switching and proxy connect/disconnect logs.
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
