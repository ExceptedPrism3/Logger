<-!------------------------------------------ v1.8.4 ------------------------------------------!->

Fixes & Improvements
    [+] Velocity & BungeeCord Server Placeholder Priority (#47):
        [+] Fixed placeholder evaluation order in LogManager and MessageManager so `%server%` accurately displays the player connected backend server (e.g. lobby, hub, survival) instead of defaulting to the proxy name.
        [+] Added `%proxy%` placeholder support across files and Discord messages for explicitly referencing the Velocity/BungeeCord proxy network name.
        [+] Hardened Velocity OnLogin event with safe optional server extraction (player.getCurrentServer().map(...)).
    [+] EnchantItemEvent Null-Safety & 1.21+ Enchantments (#68):
        [+] Added comprehensive null-safety checks in ItemEnchantListener for enchanting tables, item stacks, and added enchants to prevent listener exceptions.
        [+] Hardened FriendlyEnchants lookup with robust fallback handling and support for 1.21+ vanilla enchantments (including Mace enchantments: Density, Breach, Wind Burst) and custom/modded enchantments.

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
