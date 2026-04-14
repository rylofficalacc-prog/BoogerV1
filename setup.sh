#!/bin/sh
# Run this once before building to download the Gradle wrapper jar
echo "Downloading Gradle wrapper jar..."
curl -L -o gradle/wrapper/gradle-wrapper.jar \
  "https://raw.githubusercontent.com/gradle/gradle/v8.11.1/gradle/wrapper/gradle-wrapper.jar"
echo "Done! Now run: ./gradlew build"
