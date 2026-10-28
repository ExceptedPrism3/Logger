# Discord Setup & Addon

The Discord integration runs through the companion **`LoggerDiscordAddon-1.8.6.jar`** alongside the main Logger plugin.

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

1. Download [`LoggerDiscordAddon-1.8.6.jar`](https://github.com/ExceptedPrism3/Logger/releases).
2. Place it inside your server's `plugins/` directory.
3. Start or restart your server to generate `plugins/Logger/discord.yml`.

---

## Step 3: Configure discord.yml

Open `plugins/Logger/discord.yml`, set `Mode: 'BOT'` (or `'WEBHOOK'`), and configure your Bot Token and per-event Channel IDs (or Webhook URLs):

```yaml
Discord:
  Enabled: true
  Mode: 'BOT'
  Bot-Token: "YOUR_BOT_TOKEN_HERE"
  Player-Chat:
    Channel-ID: 'YOUR_CHAT_CHANNEL_ID'
    Webhook: "https://discord.com/api/webhooks/XXXX/XXXX"
```

> **Note**: Any event left with the default `'CHANNEL_ID'` or `'https://discord.com/api/webhooks/XXXX/XXXX'` placeholder will stay silent and will not be sent to Discord. You can also disable individual events by adding `Enable: false` under that event's section.
