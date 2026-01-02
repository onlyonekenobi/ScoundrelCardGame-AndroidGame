# Building the Scoundrel Android App

## Prerequisites

1. **Android Studio** (Hedgehog or later)
   - Download from: https://developer.android.com/studio

2. **JDK 17 or later**
   - Usually included with Android Studio

3. **Android SDK**
   - Install via Android Studio SDK Manager
   - Minimum SDK: 24 (Android 7.0)
   - Target SDK: 34 (Android 14)

## Building the APK

### Method 1: Using Android Studio (Recommended)

1. **Open the project**:
   - Launch Android Studio
   - Select "Open an Existing Project"
   - Navigate to the `Android` folder and select it

2. **Wait for Gradle sync**:
   - Android Studio will automatically sync Gradle dependencies
   - This may take a few minutes on first open

3. **Build the APK**:
   - Go to `Build > Build Bundle(s) / APK(s) > Build APK(s)`
   - Wait for the build to complete (usually 1-2 minutes)

4. **Locate the APK**:
   - Navigate to: `app/build/outputs/apk/debug/app-debug.apk`
   - Or click "locate" in the build notification

### Method 2: Using Command Line

```bash
cd Android
./gradlew assembleDebug
```

The APK will be in: `app/build/outputs/apk/debug/app-debug.apk`

## Installing on Your Android Device

1. **Enable Developer Options**:
   - Go to Settings > About Phone
   - Tap "Build Number" 7 times
   - Go back to Settings > Developer Options
   - Enable "USB Debugging" (optional, for direct install)
   - Enable "Install via USB" or "Install Unknown Apps" (depending on Android version)

2. **Transfer APK to device**:
   - Method A: USB transfer
     - Connect phone via USB
     - Copy APK to phone storage
   - Method B: Cloud/Email
     - Upload APK to Google Drive/Dropbox
     - Download on phone
   - Method C: Direct install via ADB
     ```bash
     adb install app-debug.apk
     ```

3. **Install**:
   - Open the APK file on your phone
   - Tap "Install"
   - If prompted about "Unknown Sources", allow it

## Features

✅ Full Scoundrel game implementation
✅ Modern Material Design 3 UI
✅ Undo functionality
✅ Card preview system
✅ Visual deck representation
✅ End game screen with rankings
✅ Touch-optimized for mobile

## Troubleshooting

**Build fails with "SDK not found"**:
- Open SDK Manager in Android Studio
- Install Android SDK Platform 34

**Gradle sync fails**:
- Check internet connection
- Try: File > Invalidate Caches / Restart

**APK won't install**:
- Make sure "Install Unknown Apps" is enabled for your file manager
- Check that your device meets minimum SDK 24 requirement

