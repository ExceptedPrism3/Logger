#!/bin/bash
set -e

# Set Java 21 Home
export JAVA_HOME="/Users/pixo/Library/Java/JavaVirtualMachines/corretto-21.0.4/Contents/Home"

echo "🔨 Building Logger multi-module project..."
mvn clean package -DskipTests -o

echo "📦 Copying artifacts to releases/..."
mkdir -p releases
cp logger-paper/target/logger-paper-1.8.3.jar releases/Logger-1.8.3.jar
cp logger-discord-addon/target/logger-discord-addon-1.8.3.jar releases/LoggerDiscordAddon-1.8.3.jar

echo "✅ Build Complete! JARs ready in releases/:"
ls -lh releases/
