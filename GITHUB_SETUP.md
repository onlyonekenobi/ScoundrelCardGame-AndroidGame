# GitHub Setup Instructions

## Initial Setup

1. **Initialize Git repository** (if not already done):
   ```bash
   cd Android
   git init
   ```

2. **Add remote repository**:
   ```bash
   git remote add origin https://github.com/onlyonekenobi/ScoundrelCardGame-AndroidGame.git
   ```

3. **Add all files**:
   ```bash
   git add .
   ```

4. **Commit**:
   ```bash
   git commit -m "Initial Android app commit"
   ```

5. **Push to GitHub**:
   ```bash
   git branch -M main
   git push -u origin main
   ```

## Building the APK

1. **Open in Android Studio**:
   - Open Android Studio
   - Select "Open an Existing Project"
   - Navigate to the `Android` folder

2. **Build APK**:
   - Go to `Build > Build Bundle(s) / APK(s) > Build APK(s)`
   - Wait for build to complete
   - APK will be in `app/build/outputs/apk/debug/app-debug.apk`

3. **Install on Android device**:
   - Transfer the APK to your Android device
   - Enable "Install from Unknown Sources" in Android settings
   - Open the APK file and install

## Requirements

- Android Studio Hedgehog or later
- JDK 17 or later
- Android SDK 24+ (Android 7.0+)
- Gradle 8.2+

