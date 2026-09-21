<-!------------------------------------------ v1.8.5 ------------------------------------------!->

Fixes & Improvements
    [+] Language Configuration & Missing Key Fallback Resolution:
        [+] Resolved "Message not found: File.Player.<event>" errors when upgrading from legacy versions (e.g. v1.8.0.x with legacy `en_en`): enhanced `MessageManager` to normalize legacy language codes (`en_en` -> `en_US`) before filesystem lookup, preventing outdated unmigrated files from hijacking message formatting.
        [+] Universal Fallback Defaults: Attached bundled `en_US.yml` defaults directly to `YamlConfiguration` in `MessageManager`, guaranteeing that any custom, outdated, or incomplete language translation seamlessly falls back to standard templates instead of throwing missing-message errors.
        [+] Active Synchronization: Added automatic missing-key synchronization via `YamlMigrator` for the actively selected language file on server boot.
    [+] Discord Webhook Rate Limiting & Paper Nag Fix:
        [+] Webhook Rate Limiting & Auto-Retry Backoff: Implemented a dedicated single-threaded queue worker (`Logger-Discord-Webhook`) with automatic HTTP 429 (`retry_after`) rate-limit backoff and retry handling (up to 3 retries) in `DiscordManager`. Webhooks are now paced sequentially, respecting proactive Discord rate-limit headers (`X-RateLimit-Remaining: 0` / `X-RateLimit-Reset-After`) and eliminating HTTP 429 console spam and message loss during event bursts.
        [+] Paper Nag Warning Fix: Replaced all legacy `System.out.println` and `System.err.println` occurrences in `DiscordManager` with proper plugin `Log` calls (`JavaPlugin#getLogger()`), permanently resolving Paper's nag warning about direct stdout/stderr usage.
        [+] Discord %player% Placeholder Support: Updated default Discord message templates for `Item-Pickup`, `Item-Pickup-Staff`, `Item-Drop`, and `Item-Drop-Staff` across all 11 supported languages in `messages/*.yml` to include the `**%player%**` placeholder, ensuring player identities are clearly visible in both embed and normal text message formats.
    [+] Pastebin API Key & Environment Configuration (.env):
        [+] Restored and bundled `.env` containing `PASTEBIN_API` key for reliable `/logger dump` debug log uploads.
        [+] Implemented multi-tier key resolution in `Dump`: system environment variables -> local plugin data folder `.env` (automatically extracting bundled `.env` if missing) -> server root `.env` -> bundled jar resource stream -> internal fallback.
    [+] Discord Multi-Channel & Webhook Routing:
        [+] Fixed multi-channel Discord webhook and bot routing (`resolveWebhookForEvent` / `resolveChannelForEvent`) to properly route specific log events to dedicated webhooks while maintaining staff and default fallback delivery.
        [+] Added alias normalization for `rcon` (`rconcommand`, `serverrcon`, `serverrconcommand`) and `reload` (`serverreload`, `reloadconsole`) events.
        [+] Extended webhook URL validation to support Discord Canary, PTB, and legacy `discordapp.com` endpoints.
        [+] Corrected `isOnline()` status detection in webhook mode to verify default webhook URLs alongside mapped route endpoints.
        [+] Resource Cleanup: Added explicit `conn.disconnect()` handling in `DiscordManager` webhook delivery to prevent socket and connection leaks under high event volume.
    [+] BungeeCord Asynchronous Logging & Netty Thread Resilience:
        [+] Resolved `RejectedExecutionException` in BungeeCord `FileManager`: implemented self-healing executor with dedicated daemon worker threads (`LoggerBungee-File-Worker`) and guarded task submission to prevent terminated pool exceptions from bubbling into Netty IO workers (`UpstreamBridge`).
        [+] Clean lifecycle handling in `LoggerBungee.onDisable()` and `reload()`: automatically unregisters all proxy listeners, cancels scheduler tasks, unregisters commands, detaches console log filters, and restores proxy command maps.
        [+] Exception isolation in `BaseListener` and `LogManager`: ensures logging errors (file, Discord, database) are safely trapped as warnings and never disconnect players or disrupt proxy packet pipelines.
    [+] Velocity Proxy Robustness & Synchronization:
        [+] Added automatic offline status reporting (`markServerOffline`) upon proxy shutdown in Velocity `Logger.java`, keeping Web Panel server status cards in immediate sync when Velocity proxy instances stop.
        [+] Added automatic 60-second periodic status heartbeat in Velocity, ensuring running Velocity proxies maintain active `last_seen` timestamps on the Web Panel without timing out.
        [+] Added dynamic `Table-Prefix` support in `velocity-config.yml` (defaulting to `logger_`), unifying table prefix behavior across Spigot, BungeeCord, and Velocity.
        [+] Daily Log Rollover: Refactored Velocity `FileHandler` to dynamically resolve daily log files (`dd-MM-yyyy.log`), fixing an issue where proxy logs would remain locked to the initial startup date indefinitely.
        [+] Cross-Platform Retention & Encoding: Replaced filesystem `creationTime` checks with standard `lastModified()` for reliable log file retention deletion on Linux ext4 filesystems, and enforced explicit UTF-8 encoding on disk writes.
    [+] Proxy Dump Subcommand (/loggerproxy dump):
        [+] Added `/loggerproxy dump` on both BungeeCord and Velocity: asynchronously generates an online Pastebin dump of proxy configuration files, Discord configs, active language bundles, and proxy server logs (`proxy.log` / `velocity.log`).
        [+] Centralized `DumpHelper` and `PasteBin` in `logger-core`: shared across all platforms with multi-tier API key resolution and safe tail-truncation to prevent exceeding Pastebin payload limits.
        [+] Unified Spigot's `/logger dump` to utilize the centralized `DumpHelper`, eliminating duplicate code and adding tail-truncation to protect against out-of-memory errors on massive `latest.log` files.
        [+] Added tab-completion suggestions and usage information for `dump` on both BungeeCord and Velocity.
    [+] Database Concurrency & Shutdown Resilience:
        [+] SQLite Concurrency: Configured HikariCP pool size to 1 (`config.setMaximumPoolSize(1)`) and enabled Write-Ahead Logging (`journal_mode=WAL`), `busy_timeout=5000`ms, and `synchronous=NORMAL` to eliminate `SQLITE_BUSY` database file lock exceptions.
        [+] Instant Web Panel Offline Status: Enhanced `markServerOffline()` to update `last_seen` to a past timestamp immediately on shutdown, eliminating the 3-minute delay where stopped servers appeared "Online".
        [+] Shutdown Task Flushing: Added graceful queue draining in `DatabaseManager` and Spigot's `LoggerManager` upon shutdown, ensuring all pending log events (including `SERVER_STOP`) are committed to disk and database before worker threads terminate.
        [+] Schema Cleanup: Removed unused zombie table declarations `player_quit` and `player_login` from schema creation.
        [+] Removed unused legacy `caffeine` dependency and shade relocation, shaving ~800KB from the universal binary.

    [+] Player Horse & Mount Interaction Logging (Anti-Theft System):
        [+] Comprehensive Mount Tracking: Added dedicated `PLAYER_HORSE_INTERACTION` event tracking player interactions with all `AbstractHorse` variants (Horse, Donkey, Mule, Skeleton Horse, Zombie Horse, Llama, Trader Llama, and Camel).
        [+] Anti-Theft Ownership Resolution: Captures the legal owner via `horse.getOwner()` (`AnimalTamer` name and UUID), enabling server admins to instantly trace unauthorized players mounting and riding off with another player's tamed mounts.
        [+] Action & State Auditing: Hooks `VehicleEnterEvent` (Mount), `VehicleExitEvent` (Dismount), and `InventoryOpenEvent` (Mount inventory/chest access), logging coordinates, horse UUID, custom nametag, saddle status, horse armor type, and chest status.
        [+] Full Multi-Platform & Discord Bridge Support: Added formatted file logging, dedicated Discord channel/webhook routing (`Discord.Horse-Interaction`), automated database table creation (`player_horse_interaction`), and translations across all 11 supported languages.

    [+] Web Panel Performance Overhaul (Logger Web Panel v1.0.1):
        [+] Instant First-Paint & CDN Preconnecting: Added `preconnect` and `dns-prefetch` resource hints in `index.html` for Tailwind and external CDNs, and marked `chart.umd.min.js` with `defer`, preventing render-blocking network halts and ensuring the License Verification modal paints instantly even on slower network connections.
        [+] Eliminated Redundant DDL Table Creation: Replaced unconditional `CREATE TABLE IF NOT EXISTS` execution in `Database.php` with an initialization lock check (`.installed`), eliminating 6 DDL queries and table locks that were previously running on every single HTTP request (reducing API latency by 500ms–2,500ms on remote/cloud databases).
        [+] High-Speed Indexed License Verification: Pre-computed license key variations in PHP memory and migrated `login.php` to use `WHERE license_key IN (...)`, allowing MySQL to hit the `UNIQUE (license_key)` index in O(1) time without full table scans or runtime function overhead.
        [+] Non-Blocking PHP Session Locks: Added `Session::close()` (`session_write_close()`) across `login.php`, `data.php`, `notifications.php`, `logs.php`, `suggestions.php`, `admin.php`, and `test_db_connection.php`, immediately releasing PHP's exclusive file session lock so concurrent background requests (dashboard statistics, notifications, live logs) never stall or deadlock the login interface.
        [+] Dashboard Statistics Caching & Query Optimization: Optimized `data.php` with single-pass table inspections, reduced external release lookup timeout to 1s with persistent cache fallback, and added a 60-second transient server cache for compiled dashboard statistics.

<-!------------------------------------------ v1.8.4.1 ------------------------------------------!->

Fixes & Improvements
    [+] Spear LUNGE Enchantment & Korean Localization (#73):
        [+] Added vanilla `LUNGE` ("Lunge") enchantment support to `FriendlyEnchants` for spear weapons (Mounts of Mayhem).
        [+] Updated `ko_KR.yml` to align Korean terminology with official Minecraft localization (instant movement, enchanting spacing, spawn eggs, portals, and crafter).
    [+] Web Panel & Database Connection Documentation:
        [+] Documented MariaDB/MySQL Error 1130 fix ("Host ... is not allowed to connect") in Web Panel setup guide and FAQ.
    [+] Automatic Database Table Creation & Prefix Resolution:
        [+] Expanded `DatabaseManager` to auto-create all 40+ log tables on server startup, eliminating the need to manually sync or repair schema via the Web Panel.
        [+] Added dynamic table prefix resolution in `AbstractLogger` so all database loggers seamlessly respect `Table-Prefix` (e.g. `logger_` or custom prefixes).
        [+] Updated Web Panel dashboard (`data.php`) and log viewer (`logs.php`) to dynamically discover and count prefixed tables and refresh empty caches automatically.

<-!------------------------------------------ v1.8.4 ------------------------------------------!->

Fixes & Improvements
    [+] Server Whitelist Logging:
        [+] Added dedicated Server Whitelist logging (`SERVER_WHITELIST`, `Log-Server.Whitelist`) capturing `/whitelist add <player>`, `/whitelist remove <player>`, `/whitelist on`, `/whitelist off`, and `/whitelist reload` (including `/minecraft:whitelist ...` and console commands).
        [+] Multi-Channel Delivery: Logged directly to files (`logs/Server Whitelist/<date>.log`), Discord Addon (`Discord.Server-Side.Whitelist` channel/webhook with executor avatar and name in embeds), and database (`server_whitelist` table with auto-schema migration and index acceleration).
        [+] In-Game Inspector: Fully integrated into `/logger view SERVER_WHITELIST`.
        [+] Web Panel Integration: Added `server_whitelist` definition in `logs.php`.
        [+] Full Localization: Translated across all 11 bundled language files (`en_US`, `de_DE`, `fr_fr`, `es_ES`, `it_IT`, `pt_BR`, `ru_RU`, `zh_cn`, `ja_JP`, `ko_KR`, `ar`).
    [+] On-Demand File & Folder Generation:
        [+] Directories and `.log` files are now created strictly on-demand when an event actually occurs, eliminating empty folders and 0-byte log files on boot across Spigot, Paper, BungeeCord, and Velocity.
        [+] Completely halts file/folder generation when `Log-to-Files: false` (or `Files.Enabled: false` on Velocity) is configured.
        [+] Added null-safety checks in the retention cleaner (`deleteFiles()`) to prevent `NullPointerException` on uncreated folders.
    [+] Database Security & Driver Hardening:
        [+] Upgraded `org.postgresql:postgresql` to `42.7.13` (resolves SCRAM auth CPU exhaustion DoS - CVE-2026-42198 / Dependabot #23).
        [+] Upgraded `org.mariadb.jdbc:mariadb-java-client` to `3.3.5` (resolves CVE-2026-55856, CVE-2026-55857, CVE-2026-55858 / PR #72).
        [+] Migrated `mysql:mysql-connector-java` to modern `com.mysql:mysql-connector-j` `8.2.0` (resolves CVE-2023-22102).
    [+] Proxy Placeholder & Event Fixes (Velocity & BungeeCord):
        [+] Fixed placeholder evaluation order so `%server%` accurately reflects the connected backend server (e.g. lobby, survival) instead of falling back to the proxy name (#47).
        [+] Added `%proxy%` across file logs and Discord messages to explicitly reference the proxy network name.
        [+] Safe optional server extraction (`player.getCurrentServer().map(...)`) on Velocity `OnLogin`.
    [+] Discord Addon & Staff Segregation:
        [+] Implemented `logger.exempt.discord` (`loggerproxy.exempt.discord`) to exempt designated players from Discord alerts while keeping local/database logs intact.
        [+] Centralized `logger.exempt` at the entry point of `LoggerManager` and proxy managers.
        [+] Prevented fallback from `STAFF` notifications to public player channels when a dedicated staff channel is omitted.
        [+] Canonical `discord.yml` is now situated inside `plugins/Logger/` (`plugins/LoggerBungee/` on BungeeCord) with automatic migration from `plugins/LoggerDiscordAddon/`.
        [+] Added optional custom `Title` configuration per Discord channel/webhook route.
    [+] Performance & Stability:
        [+] Guarded against null enchantments, item stacks, and tables in `ItemEnchantListener` (#68). Added support for 1.21+ vanilla enchantments (Density, Breach, Wind Burst) and custom enchantments.
        [+] Removed all 15 unused and duplicate import statements across all 189 source files.
        [+] Added universal `server_status` schema with live status detection cards for Logger Core and Discord Addon on the Web Panel.
        [+] Added 26-page documentation overhaul in `docs/` with `.gitbook.yaml` for instant GitBook Git Sync.

<-!------------------------------------------ v1.8.3 ------------------------------------------!->

Fixes & Improvements
    [+] Full Folia Native Support (PaperMC Folia Engine):
        [+] Added `folia-supported: true` to plugin descriptors for both Logger and LoggerDiscordAddon.
        [+] Implemented universal `SchedulerAdapter` with runtime Folia environment detection (RegionizedServer).
        [+] Rerouted all async tasks to Folia native AsyncScheduler (runNow, runAtFixedRate, cancelTasks).
        [+] Rerouted all server-side timed tasks (Server Start delay, RAM/TPS/Player Count monitors, File retention cleanup, SpawnEgg pending cleaner) to Folia GlobalRegionScheduler.
        [+] Completely eliminated UnsupportedOperationException (Bukkit scheduler disabled) on Folia while maintaining 100% backward compatibility with Spigot/Paper/Purpur.
        [+] Replaced BukkitRunnable in server performance monitors with thread-safe Runnable implementations.
    [+] Discord Addon Routing Engine Overhaul:
        [+] Completely eliminated the fallback bug (getFirstChannel / getFirstWebhook) that dumped unassigned or unconfigured events (such as Command Blocks with 'CHANNEL_ID') into the first active channel (e.g. PlayerChat).
        [+] Added a canonical, case-insensitive channel resolution engine supporting 100% of event names across Spigot, Paper, BungeeCord, and Velocity.
        [+] Fixed DiscordChannels.java enum paths to accurately reflect discord.yml keys (e.g. Block-Place, Chest-Interaction, etc.).
        [+] Unconfigured events with placeholder channel IDs ('CHANNEL_ID', '0', empty) are now strictly and silently ignored.
    [+] Clean Discord Visuals & Formatting:
        [+] Dynamic readable embed titles (e.g. 'Player Chat', 'Command Block', 'Server Start', 'Block Place') replacing the generic 'Server Notification'.
        [+] Automatic stripping of Minecraft color codes (§, &) and hex color markers from all messages dispatched to Discord.
        [+] Player avatar embeds with Minotar thumbnail and author integration.
    [+] Discord Webhook & Embed Parity:
        [+] Added full Embed support in Webhook mode with author avatars, customizable hex colors, footers, and timestamps.
        [+] Robust JSON escaping for Webhooks to prevent HTTP 400 Bad Request errors on control characters.
    [+] Discord Safety & Truncation:
        [+] Safe message truncation enforcing Discord's 2,000-character content limit and 4,096-character embed description limit to prevent JDA / Discord API exceptions on large commands or NBT payloads.
    [+] Multi-Platform Release Distribution:
        [+] All binaries bumped to v1.8.3 across Spigot, Paper, BungeeCord, Velocity, and Discord Addon.

<-!------------------------------------------ v1.8.2 ------------------------------------------!->

Additions
    [+] Universal 3-in-1 Multi-Platform Single JAR:
        [+] A single downloadable JAR (Logger-1.8.2.jar) that boots natively on Spigot, Paper, Purpur, BungeeCord, Waterfall, FlameCord, and Velocity 3.x+
        [+] Multi-descriptor bundle containing plugin.yml, bungee.yml, and velocity-plugin.json
        [+] Platform-isolated config extraction (bungee-config.yml, velocity-config.yml) to prevent template collision
    [+] 11 Pre-Packaged Official Languages across Spigot, BungeeCord, and Velocity:
        [+] English (en_US), Spanish (es_ES), German (de_DE), French (fr_FR), Italian (it_IT), Portuguese (pt_BR), Russian (ru_RU), Chinese (zh_CN), Japanese (ja_JP), Korean (ko_KR), Arabic (ar_SA)
        [+] Non-destructive YAML Auto-Sync that safely injects missing translation keys on boot/reload without overwriting customizations
    [+] Modern 1.21+ Event Trackers:
        [+] Crafter Auto-Crafting listener (Crafter-Craft) recording recipe input grid and output items
        [+] Mace Smash Attacks & falling impact damage calculations
        [+] Trial Chamber Vaults and Ominous Spawner interactions
        [+] Sculk Shrieker activations and darkness warning logs
        [+] Villager Trading & Piglin Bartering economy transactions
        [+] Totem of Undying triggers & Respawn Anchor charges/explosions
    [+] Universal Lossless Database Auto-Evolution & Retention (SchemaMigrator):
        [+] Automatic startup detection and migration of legacy v1.6/v1.7 PascalCase tables to standard snake_case with 0% data loss
        [+] Automated non-destructive column injection (is_staff, server_name, world_name, coordinates) on legacy databases
        [+] Automatic composite index builder on (date) and (player_name) across all tables for instant query response times
        [+] Cross-engine unified schemas for SQLite (AUTOINCREMENT) and MySQL/MariaDB (AUTO_INCREMENT)
        [+] Automatic retention cleanup (purgeOldLogs via Data-Deletion) executed on startup and reload
        [+] Native multi-DBMS support for MySQL, MariaDB, PostgreSQL, and SQLite
    [+] Unified Proxy Administrative Commands:
        [+] /loggerproxy reload (Alias: /lgp reload) - Hot-reloads configuration, Discord bot, and re-pools database connections
        [+] /loggerproxy manual <message...> (Alias: /lgp manual) - Logs custom administrative entries across Files, Discord, and Database
        [+] /loggerproxy discord - Displays official support server invite
    [+] Guaranteed Discord Stop Delivery & Hot Reload:
        [+] Synchronous JDA .complete() and Webhook dispatch buffer guaranteeing Server-Side.Stop events are delivered before server shutdown
        [+] Dynamic Discord hot-reloading on /logger reload and /loggerproxy reload without requiring proxy/server restarts
    [+] Real-Time Web Control Panel & Analytics Suite (Optional Addon):
        [+] Real-time Live Stream log feed with sub-second polling and session discovery caching
        [+] Multi-filter interactive search across 35+ event types, player UUIDs, dates, and staff tags
        [+] 1-Click Database Schema Alignment & Self-Repair tool
        [+] CSV and JSON log export utilities
        [+] Live server telemetry dashboard (TPS, RAM, Online Players)

Fixes
    [!] Fixed Server-Side.Stop Discord delivery by enforcing synchronous dispatch and graceful 3-second connection draining
    [!] Fixed proxy language synchronization console spam by isolating proxy fallback paths from Spigot templates
    [!] Fixed SnakeYAML 2.x SafeConstructor tag mismatch (ConstructorException) on Velocity 3.4.0+
    [!] Fixed Google Guice SLF4J injection failure on Velocity when loading LoggerDiscordAddon
    [!] Fixed ClassCastException when 1.21 Wind Charges or non-TNT entities explode on modern Paper servers (PrimeTNTListener)
    [!] Fixed NoClassDefFoundError: org/bukkit/plugin/java/JavaPlugin on BungeeCord proxies by providing native bungee.yml descriptor
    [!] Fixed SLF4J binding warnings and Paper nag alerts by bundling slf4j-jdk14 for clean Java Logging integration
    [!] Fixed ItemEnchantEvent null-pointer exceptions with custom or unmapped enchantments (#68)
    [!] Fixed Velocity player login placeholder resolution for %server% / %Server% (#47)
    [!] Fixed database schema incompatibility and crashes when upgrading from previous Logger versions
    [!] Fixed prepared statement SQL errors when searching non-existent tables in /logger view
    [!] Fixed HikariCP connection pool thread leaks on server stop and /logger reload
    [!] Fixed missing translation key crashes with automatic fallback to default English (en_US.yml)
    [!] Fixed SQLite concurrent write lockups with asynchronous worker batching
    [!] Fixed Java 21 JVM reflection & module encapsulation warnings on modern Paper/Purpur builds

Changes
    [*] Standardized permissions matrix across Spigot (logger.*), BungeeCord (loggerproxy.* / logger.*), and Velocity (loggerproxy.*)
    [*] Upgraded HikariCP connection pooling and asynchronous worker batching for zero TPS impact
    [*] Created composite database indexes on (date) and (player_name) across all tables for instant search speeds
    [*] Overhauled config and message auto-reloading routines
    [*] Deep codebase cleanup: removed 119 MB legacy monolith directory (old/), eliminated dead classes (DiscordConfig, LanguageManager, DiscordFile), and purged unused test directories

<-!------------------------------------------ v1.8.1 ------------------------------------------!->

Additions
    [+] Plugin Prefix
    [+] 1.7 server versions enums
    [+] 1.19.2 / 1.19.3 / 1.20.1/2...6 Support
    [+] Latest BungeeCord & Velocity APIs Support
    [+] Sign Change Checker
    [+] SuperiorSkyblock Chat Checker

Fixes
    [!] Plugin not starting up correctly
    [!] The null value of a player's IP on database ( even when enabled in config )
    [!] Vault Checker Issue which causes a high TPS loss
    [!] Entity Death not working correctly
    [!] Reload command not reloading from the config
    [!] Commands not working in console
    [!] LiteBans table not auto deleting on Velocity instance
    [!] Toggle Commands are no longer case sensitive
    [!] Chest Interaction errors when run on 1.8.8 servers & being run with other plugins
    [!] Discord not working properly
    [!] Corrected the exact location of sign placement
    [!] Discord warnings showing on console when used
    [!] Anvil logging when player doesn't have enough XP
    [!] Plugin logo not showing with colors on most of terminals
    [!] Player Skin not showing on discord message embed
    [!] POM files for development

Changes
    [*] Remade permissions which will log OP and NOT OP players until they're permitted with the exempt permission
    [*] Inventory Restore Menu Title names to eliminate conflicts between other plugins
    [*] Minor messages changes
    [*] Minor Velocity & Bungee Code improvements

<-!------------------------------------------ v1.8.0 ------------------------------------------!->

Additions
    [+] Spy Feature toggle command in-game
    [+] Player Inventory Backup on Player Death
    [+] LiteBans & AdvancedBans Support ( spigot only )
    [+] Chest Interaction Checker
    [+] TNT Explosion Checker
    [+] Config Auto-Updater
    [+] Discord Auto-Updater
    [+] PlaceHolderAPI Plugin Support
    [+] StripLog for 1.13+ Servers
    [+] Added 1.19 Support
    [+] Dump command ( spigot only )
    [+] Discord Command
    [+] Command Blocks Checker
    [+] Chinese Traditional & Dutch Languages (Thanks to our Translation Team)
    [+] Geyser & FloodGate Partial-Support ( spigot only )

Fixes
    [!] Separated Command Blocks from Console Commands as they count the same
    [!] Discord Status starting even if disabled in discord config
    [!] RAM not being logged on BungeeCord & Velocity
    [!] Some languages were not being logged into databases
    [!] An issue that prevented the shutdown of the databases correctly on Server Stop
    [!] Error Spam on a rare occasion when a set of String contains the symbol '$'
    [!] Some typos in translated files

Changes
    [*] Databases Structure ( No user interaction is required, the plugin will take care of it )
    [*] OP now gets logged by default
    [*] Vault Checker has been increased to 6000 in the config by default
    [*] Vault Checker will no longer Log in if no one is online
    [*] Commands handling improvements
    [*] Huge improvement to the external database logging

<-!------------------------------------------ v1.7.5 [ DEV ] ------------------------------------------!->

Additions
    [+] Player Registration
    [+] Arabic, French and Chinese Simplified Languages

Fixes
    [!] Databases Logs not logging actions in the same second
    [!] Console Blocking being disabled when Console Logging is disabled
    [!] MariaDB not Connecting
    [!] TPS going down to 0.2165432 (Hopefully it's fixed)
    [!] Velocity Litebans table
    [!] loggerproxy.discord.exempt for Proxies and Velocity

Changes
    [*] Databases Structure
    [*] Messages & Discord Files structure for the future upcoming update

<-!------------------------------------------ v1.7.4 [ DEV ] ------------------------------------------!->

Additions
    [+] 1.18.2 Support
    [+] Player levels on Player Death Checker
    [+] Messages Folder added to Bukkit Instance for messages files to be added in the upcoming releases

Fixes
    [!] Craft Checker & Book Editing Permission checking whilst logging to External Databases
    [!] Enchanting logger.exempt not working properly
    [!] Block Break logging to file
    [!] Block Place logging to file
    [!] AuthMe-Wrong-Password Checker not logging to SQLite
    [!] AuthMe-Wrong-Password Checker not logging to Discord
    [!] AFK Checker not logging to Discord
    [!] Server-Reload Checker discord exempt feature not working
    [!] External Databases Tables Deletion not working properly
    [!] External not working correctly on Proxy
    [!] External Databases not working on Velocity
    [!] loggerproxy.reload not working on Velocity

Recoded most of the plugin and improved Databases queries and it's performance in general!

<-!------------------------------------------ v1.7.3 [ DEV ] ------------------------------------------!->

Additions
    [+] MariaDB to Proxy ( BungeeCord, FlameCord, WaterFall, etc... ) & Velocity Instances
    [+] Vault Checker - Checks for online player's balance changes
Fixes
    [!] Player-Chat / Commands / Sign any checker that requires player's input and ends with back-slash \ causes errors and not being logged
    [!] LiteBans logging only from the console
Changes
    [*] Added enchantment level %enchlevel% to the Enchant Checker and updated its Databases Tables

<-!------------------------------------------ v1.7.2 [ DEV ] ------------------------------------------!->

Additions
    [+] Player Crafting Checker
    [+] Update Checker Disabler
    [+] LiteBans Integration for only Proxies
    [+] New permission logger.spy.bypass - Allows seeing logger.staff commands
Fixes
    [!] Update Notification appears even if logger.update permission is revoked
    [!] MySQL Connection error spam on Proxies
    [!] Player Sign Text not being logged in SQLite
    [!] Error Spam when Discord Bot Token is left empty or Invalid & Discord Channel ID when left empty or invalid
Changes
    [*] Discord Status Activity Syntax Checker on Server Start
    [*] Item Pickup Checker will now check for everything
    [*] %time% placeholder will now display [ yyyy-MM-dd HH:mm:ss ] instead just [ HH:mm:ss ] Files and Discord Logging Features
    [*] Granting logger.staff.log will auto revoke the logger.exempt for them
Removed
    [-] Player Death Item Used by the Killer - Due to not being compatible across all versions

                                                    Extra
                                        Any Contributions are Welcomed!

<-!------------------------------------------ v1.7.1 ------------------------------------------!->

Additions
    [+] Plugin Wiki
    [+] Velocity Support that includes
        [+] Player Side
        [+] Player Chat
        [+] Player Commands
        [+] Player Login
        [+] Player Leave
        [+] Server Side
        [+] Server Start
        [+] Server Stop
        [+] Console Commands
        [+] RAM
        [+] Discord Integration
    [+] SQLite for BungeeCord
    [+] Console Blacklist Commands for Bukkit
    [+] Discord Activity Status for all instances
    [+] Time Stamp in discord logs
    [+] AuthMe Wrong Password Checker
    [+] Game Mode Checker
    [+] Bukkit Fill Checker
    [+] Anvil Spy
    [+] Text Sign Spy
    [+] Book Editing Spy
    [+] New Update Checker

Fixes
    [!] Player Leave console error [ Proxy only ] - This occurs when the targeted server is offline or unreachable
    [!] Startup Errors when messages field is empty
    [!] Messages and Discord files comment glitching out
    [!] MySQL Errors on server startup when credentials are wrong

Changes
    [*] Remade the BungeeCord Database table names
    [*] Removed emojis from messages.yml as it causes servers > 1.12 to crash / plugin not working
    [*] Players IP has been turned off by default in the config
    [*] Completely changed Config, Messages, and Discord files syntax
    [*] Enchantments names are more detailed
    [*] Players can no longer execute the plugin's command without the correct permission
