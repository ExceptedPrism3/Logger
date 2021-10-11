# Logger

![Updated Badge](https://badges.pufler.dev/updated/ExceptedPrism3/Logger)
![Version](https://img.shields.io/github/v/release/ExceptedPrism3/Logger)
![Downloads](https://img.shields.io/github/downloads/ExceptedPrism3/Logger/total)
![Issues](https://img.shields.io/github/issues/ExceptedPrism3/Logger)
![Discord](https://img.shields.io/discord/850407951629287424)

A spigot plugin that logs all Activities from Commands, Chat, Player Join, Player Leave, Player Death and even Server's RAM, TPS and Much More!

This plugin can be found on two locations at the moment [Spigot](https://www.spigotmc.org/resources/logger.94236) and [MC-Market](https://www.mc-market.org/resources/20657/).

If support is needed, join my [Discord](https://discord.gg/MfR5mcpVfX).

# About the Plugin
Someone Destroyed your favorite painting, dropped your 5 hours worth of mining diamond chestplate into the Lava or turned off your server through the console?

With more than **20 Features** to choose from, nothing is left without monitoring.

**This is what The Logger Plugin can do:**
* Player Chat
* Player Commands
* Console Commands
* Player Sign Text
* Player Join
* Player Leave
* Player Kick
* Player Death
* Player Teleport
* Player Level
* Block Place
* Block Break
* Portal Creation
* Bucket Place
* Anvil
* TPS
* RAM
* Server Start
* Server Stop
* Item Pickup
* Item Drop
* AFK **[ Requires Essentials Plugin ]**
* Book Editing
* Enchanting
* Furnace

## Info
If you use MySQL, the IP that gets stored in the Database when a Player joins
isn't storing a clear IP, but a number representation of the IP.
To convert it back, just execute the following SQL Command:

```mysql
SELECT INET_NTOA("value");
```
