# Discord Activity

Customize how your Discord bot appears in server member lists.

In `plugins/Logger/discord.yml`:

```yaml
Activity:
  Enable: true
  Type: PLAYING # Options: PLAYING, STREAMING, LISTENING, WATCHING, COMPETING
  Status: ONLINE # Options: ONLINE, IDLE, DND, INVISIBLE
  Message: "Minecraft with %online%/%max% players!"
```

### Supported Placeholders:
* `%online%` - Current online player count.
* `%max%` - Maximum server player slots.
* `%server%` - Server name.
