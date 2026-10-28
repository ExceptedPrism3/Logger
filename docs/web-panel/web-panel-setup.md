# Logger Web Panel Setup

The **Logger Web Panel (v1.0.6)** is the official web application for Logger, allowing server administrators and staff teams to search, filter, and inspect logs in real time from any browser.

---

## Features
* 📊 **Real-Time Analytics**: Live charts showing daily log activity, commands, chat, and player actions.
* 🖥️ **Server Heartbeat Monitor**: Live status cards for every connected Minecraft server and proxy backed by the database `server_status` table.
* 🤖 **Discord Addon Health Check**: Live indicator showing whether the companion Discord Bot is online and syncing.
* 🔍 **Advanced Filtering**: Search by player UUID/username, date range, action type, server instance, or keyword across all Spigot, 1.21+/26.x, and Proxy tables.
* 🔧 **1-Click Schema Repair**: Built-in Database Maintenance tool to automatically align and index all 53+ Logger tables.
* 🛡️ **Role-Based Access**: Multi-account license management with interactive Dark Mode and mobile-responsive layout.

---

## Requirements
* Web server: **Apache** or **Nginx**
* PHP: **PHP 8.0 or higher** with `pdo_mysql` extension enabled
* Database: MySQL or MariaDB (for internal Web Panel credentials and your Minecraft server logs)

---

## Installation Walkthrough (3-Step Setup Wizard)

1. Unzip `LoggerWebPanel-1.0.6.zip` into your web server root (e.g. `/var/www/html/logger` or `/var/www/logger-web-client`).
2. Ensure correct file permissions so the web server can write `api/config/db.php` and `api/config/.installed`:
   ```bash
   chown -R www-data:www-data /var/www/logger-web-client
   chmod -R 755 /var/www/logger-web-client
   ```
3. Open your browser at `https://your-domain.com/logger/install.php` and follow the **3-Step Setup Wizard**:
   * **Step 1**: Enter your Web Panel MySQL/MariaDB database credentials and click **Test & Continue**.
   * **Step 2**: Choose your Administrator username and generate your Admin License Key (`XXXX-XXXX-XXXX-XXXX`).
   * **Step 3**: Copy your generated license key and click **Launch Web Panel** (`index.php`).
4. Once logged in, navigate to **Settings -> Database Configuration** to connect your Minecraft server's Logger database (and optional Table Prefix).

---

## 🌟 Test the Live Demo
Experience the Web Panel in action before you buy!
* **🔗 Live Demo:** [https://prism3.me/logger/](https://prism3.me/logger/)
* **🔑 Demo License Key:** `ABCDEFGHIJKLMNOP`

---

## 💼 Acquiring the Web Panel
The Web Panel is a private companion addon. To obtain a license or source access, join our [**Discord Server**](https://discord.gg/MfR5mcpVfX) and open a ticket!

---

## Troubleshooting & Common Errors

### "Host 'xxx.xxx.xxx.xxx' is not allowed to connect to this MariaDB/MySQL server" (Error 1130)

#### Why this happens:
The IP address shown in the error message is **the outgoing IP address of your Web Panel server**, not the target database host. By default, MySQL and MariaDB user accounts only permit connections originating from `localhost`. When the Web Panel server attempts to query your remote database, MariaDB/MySQL refuses the connection.

#### How to fix:
Grant your database user remote connection privileges for the Web Panel's IP address:

**Option 1: Allow the Web Panel's specific IP (Recommended)**
Run this query in phpMyAdmin, MySQL Workbench, or your database shell:
```sql
GRANT ALL PRIVILEGES ON logger.* TO 'your_user'@'WEB_PANEL_IP' IDENTIFIED BY 'your_password';
FLUSH PRIVILEGES;
```
*(Replace `WEB_PANEL_IP` with the IP address shown in the error, and adjust `logger`, `your_user`, and `your_password` to match your setup).*

**Option 2: Allow remote connections from any host (`%`)**
If your Web Panel uses a dynamic IP, Docker bridge, or shared hosting:
```sql
GRANT ALL PRIVILEGES ON logger.* TO 'your_user'@'%' IDENTIFIED BY 'your_password';
FLUSH PRIVILEGES;
```

**Option 3: Web Panel and Database on the same machine**
If the Web Panel and MariaDB/MySQL are hosted on the exact same server/VPS, set the **Host** field in the Web Panel settings to `127.0.0.1` or `localhost`.
