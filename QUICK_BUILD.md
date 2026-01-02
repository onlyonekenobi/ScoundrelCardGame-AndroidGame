# Quick Build Guide (No Android Studio Required)

## Easiest Method: GitHub Actions ⭐

**This is the recommended method - no local setup needed!**

1. **Push code to GitHub**:
   ```powershell
   cd Android
   git init
   git remote add origin https://github.com/onlyonekenobi/ScoundrelCardGame-AndroidGame.git
   git add .
   git commit -m "Initial commit"
   git branch -M main
   git push -u origin main
   ```

2. **Wait 2-5 minutes** for GitHub Actions to build

3. **Download APK**:
   - Go to: https://github.com/onlyonekenobi/ScoundrelCardGame-AndroidGame/actions
   - Click the latest workflow run (green checkmark)
   - Scroll down to "Artifacts"
   - Click "app-debug" to download the APK

4. **Install on your phone**:
   - Transfer APK to Android device
   - Enable "Install Unknown Apps"
   - Open and install

**That's it!** No Android Studio, no SDK setup needed.

## Alternative: Command Line (If you have JDK)

If you have Java installed:

1. **Download Gradle Wrapper** (one-time setup):
   - The gradlew.bat file is included, but you may need the wrapper jar
   - Or use: `gradle wrapper --gradle-version=8.2`

2. **Build**:
   ```powershell
   cd Android
   .\gradlew.bat assembleDebug
   ```

3. **Find APK**: `app\build\outputs\apk\debug\app-debug.apk`

**Note**: This still requires Android SDK. GitHub Actions is easier!

