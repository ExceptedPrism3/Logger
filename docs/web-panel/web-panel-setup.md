# Logger Web Panel Setup

The **Logger Web Panel (v1.0.1)** is the official web application for Logger, allowing server administrators and staff teams to search, filter, and inspect logs in real time from any browser.

---

## Features
* 📊 **Real-Time Analytics**: Live charts showing daily log activity, commands, chat, and player actions.
* 🖥️ **Server Heartbeat Monitor**: Live status cards for every connected Minecraft server and proxy backed by the database `server_status` table.
* 🤖 **Discord Addon Health Check**: Live indicator showing whether the companion Discord Bot is online and syncing.
* 🔍 **Advanced Filtering**: Search by player UUID/username, date range, action type, server instance, or keyword.
* 🛡️ **Role-Based Access**: Multi-account support with Admin and Staff permissions.

---

## Requirements
* Web server: **Apache** or **Nginx**
* PHP: **PHP 8.0 or higher** with `pdo_mysql`, `pdo_pgsql`, or `pdo_sqlite` extension enabled
* Database: MySQL, MariaDB, or PostgreSQL (same database as your Minecraft servers)

---

## Installation Walkthrough

1. Unzip `LoggerWebPanel-1.0.1.zip` into your web server root (e.g. `/var/www/html/logger` or `/var/www/logger-web-panel`).
2. Copy `api/v1/config.example.php` to `api/v1/config.php`:
   ```bash
   cp api/v1/config.example.php api/v1/config.php
   ```
3. Edit `api/v1/config.php` and set your database connection details to match your Minecraft server's `config.yml`.
4. Ensure correct file permissions:
   ```bash
   chown -R www-data:www-data /var/www/logger-web-panel
   chmod -R 755 /var/www/logger-web-panel
   ```
5. Open your browser at `https://your-domain.com/logger/` and log in!

---

## Acquiring the Web Panel
The Web Panel is a private companion addon. To obtain a license or source access, join our [**Discord Server**](https://discord.gg/MfR5mcpVfX) and open a ticket!
