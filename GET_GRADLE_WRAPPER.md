# Getting Gradle Wrapper JAR

The `gradle-wrapper.jar` file is a binary file that needs to be downloaded.

## Option 1: Download Directly

Download from:
https://raw.githubusercontent.com/gradle/gradle/v8.2.0/gradle/wrapper/gradle-wrapper.jar

Save it to: `Android/gradle/wrapper/gradle-wrapper.jar`

## Option 2: Use Gradle (if installed)

If you have Gradle installed:
```bash
cd Android
gradle wrapper --gradle-version=8.2
```

## Option 3: GitHub Actions (Recommended)

**You don't need the wrapper jar if using GitHub Actions!**

Just push to GitHub and the workflow will handle everything automatically.

## Note

The gradlew.bat and gradlew scripts are included, but they need the wrapper jar to function.
For GitHub Actions builds, this is handled automatically.

