# Discord Setup & Addon

The Discord integration runs through the companion **`LoggerDiscordAddon-1.8.4.jar`** alongside the main Logger plugin.

---

## Step 1: Create Your Discord Bot

1. Navigate to the [**Discord Developer Portal**](https://discord.com/developers/applications).
2. Click **New Application** and enter your desired bot name.
3. In the left sidebar, navigate to the **Bot** tab.
4. Click **Reset Token** (or **Copy Token**) and save this bot token for later.
5. Scroll down to **Privileged Gateway Intents** and enable:
   * ✅ **Presence Intent**
   * ✅ **Server Members Intent**
   * ✅ **Message Content Intent**
6. In the left sidebar, go to **OAuth2 -> URL Generator**:
   * Under **Scopes**, select `bot`.
   * Under **Bot Permissions**, select `Administrator` (or `Send Messages`, `Embed Links`, `Attach Files`, `View Channels`).
   * Copy the generated invitation URL, open it in your browser, and authorize the bot to join your Discord server.

---

## Step 2: Install the Addon

1. Download [`LoggerDiscordAddon-1.8.4.jar`](https://github.com/ExceptedPrism3/Logger/releases).
2. Place it inside your server's `plugins/` directory.
3. Start or restart your server to generate `plugins/Logger/discord.yml`.

---

## Step 3: Configure discord.yml

Open `plugins/Logger/discord.yml` and paste your Bot Token and Channel IDs:

```yaml
Discord:
  Enable: true
  Token: "YOUR_BOT_TOKEN_HERE"
  Channel-ID: "YOUR_MAIN_LOGS_CHANNEL_ID"
```

You can customize individual channels for Chats, Commands, Joins, Leaves, Deaths, Server Start/Stop, and more!
