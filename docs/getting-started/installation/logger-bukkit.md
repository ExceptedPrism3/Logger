# Logger (Bukkit / Paper / Purpur / Folia)

## Requirements

1. **Java Runtime**: **Java 17 or higher** installed on the server machine (**Java 21 is strongly recommended** for Minecraft 1.20.5+ and 1.21+).
2. **Server Platform**: Bukkit, Spigot, Paper, Purpur, or Folia server running **Minecraft 1.8.8 through 1.21+**.

{% hint style="tip" %}
**Running a legacy 1.8.8/1.8.9 server?** 
Logger fully supports Minecraft 1.8.8/1.8.9 gameplay and logging! Simply ensure your host environment or Pterodactyl container runs with **Java 17** (standard for modern 1.8 networks).
{% endhint %}

## Installation Steps

1. Download [`Logger-1.8.4.jar`](https://github.com/ExceptedPrism3/Logger/releases).
2. Drop `Logger-1.8.4.jar` directly into your server's `plugins/` directory.
3. *(Optional)* If you want Discord notifications, also place [`LoggerDiscordAddon-1.8.4.jar`](../discord-integration/discord.md) into the `plugins/` folder.
4. Start or restart your server.
5. Configuration files will be generated under `plugins/Logger/`.
