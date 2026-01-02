# Scoundrel Card Game - Android

Android version of the Scoundrel card game, built with Kotlin and Jetpack Compose.

## Features

- ✅ Full Scoundrel game implementation
- ✅ Modern Material Design 3 UI with dark theme
- ✅ Undo functionality (enabled by default)
- ✅ Card preview system showing outcomes
- ✅ Visual deck representation
- ✅ End game screen with rankings
- ✅ Touch-optimized for mobile devices
- ✅ All game rules properly implemented

## Building the APK

### ⭐ Easiest Method: GitHub Actions (No Android Studio Needed!)

**See [EASIEST_BUILD.md](EASIEST_BUILD.md) for step-by-step instructions!**

1. Push code to GitHub
2. Wait 2-5 minutes for automatic build
3. Download APK from GitHub Actions artifacts

### Alternative Methods

- **Android Studio**: See [BUILD_INSTRUCTIONS.md](BUILD_INSTRUCTIONS.md)
- **Command Line**: See [BUILD_WITHOUT_STUDIO.md](BUILD_WITHOUT_STUDIO.md)

## Quick Start (GitHub Actions)

```powershell
cd Android
git init
git remote add origin https://github.com/onlyonekenobi/ScoundrelCardGame-AndroidGame.git
git add .
git commit -m "Initial Android app"
git branch -M main
git push -u origin main
```

Then:
1. Go to: https://github.com/onlyonekenobi/ScoundrelCardGame-AndroidGame/actions
2. Wait for build to complete
3. Download APK from "Artifacts"

## Installing on Android Device

1. Transfer the APK to your Android device
2. Enable "Install from Unknown Sources" in Android settings
3. Open the APK file and install

## Requirements

- **For GitHub Actions**: Just Git (no other software needed!)
- **For local build**: Android Studio or Android SDK + JDK 17+

## Game Rules

See the main [README.md](../scoundrel_game/README.md) for complete game rules.
