#!/bin/bash
set -e

# Set Java 21 Home
export JAVA_HOME="$(/usr/libexec/java_home -v 21 2>/dev/null || echo "/Users/pixo/Library/Java/JavaVirtualMachines/corretto-21.0.4/Contents/Home")"
export PATH="$JAVA_HOME/bin:/opt/homebrew/bin:/usr/local/bin:$PATH"

# Extract version dynamically from root pom.xml
VERSION=$(grep -m 1 "<version>" pom.xml | sed -E "s/.*<version>([^<]+)<\/version>.*/\1/" | tr -d "[:space:]")

echo "🔨 Building Logger (v${VERSION}) multi-module project with Java 21..."
mvn clean install -DskipTests

echo "📦 Copying artifacts to releases/..."
mkdir -p releases
cp "logger-paper/target/logger-paper-${VERSION}.jar" "releases/Logger-${VERSION}.jar"
cp "logger-discord-addon/target/logger-discord-addon-${VERSION}.jar" "releases/LoggerDiscordAddon-${VERSION}.jar"

# Delete the old zip file to ensure removed files don't linger in the archive
rm -f releases/LoggerWebPanel-1.0.5.zip
(cd logger-web-client && zip -q -r ../releases/LoggerWebPanel-1.0.5.zip index.php install.php INSTALL.md CHANGELOG.md LICENSE.md api assets -x "*.DS_Store*")

echo "✅ Build Complete! Release artifacts ready in releases/:"
ls -lh releases/
