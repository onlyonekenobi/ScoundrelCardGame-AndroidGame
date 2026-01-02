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

## Building

See [BUILD_INSTRUCTIONS.md](BUILD_INSTRUCTIONS.md) for detailed build instructions.

### Quick Start

1. Open the `Android` folder in Android Studio
2. Wait for Gradle sync to complete
3. Build APK: `Build > Build Bundle(s) / APK(s) > Build APK(s)`
4. Find APK in: `app/build/outputs/apk/debug/app-debug.apk`

## Installing on Android Device

1. Transfer the APK to your Android device
2. Enable "Install from Unknown Sources" in Android settings
3. Open the APK file and install

## GitHub Setup

To push this project to GitHub:

**Windows:**
```powershell
cd Android
.\setup_github.ps1
```

**Linux/Mac:**
```bash
cd Android
chmod +x setup_github.sh
./setup_github.sh
```

Or manually:
```bash
cd Android
git init
git remote add origin https://github.com/onlyonekenobi/ScoundrelCardGame-AndroidGame.git
git add .
git commit -m "Initial Android app"
git branch -M main
git push -u origin main
```

## Requirements

- Android Studio Hedgehog or later
- JDK 17+
- Android SDK 24+ (Android 7.0+)
- Gradle 8.2+

## Game Rules

See the main [README.md](../scoundrel_game/README.md) for complete game rules.

## Project Structure

```
Android/
├── app/
│   ├── src/main/
│   │   ├── java/com/scoundrel/cardgame/
│   │   │   ├── MainActivity.kt      # UI with Jetpack Compose
│   │   │   ├── ScoundrelGame.kt     # Game logic
│   │   │   ├── Card.kt              # Card data class
│   │   │   ├── Suit.kt              # Suit enum
│   │   │   └── GameState.kt         # State serialization
│   │   └── res/                     # Android resources
│   └── build.gradle.kts             # App-level build config
├── build.gradle.kts                 # Project-level build config
└── settings.gradle.kts              # Gradle settings
```
