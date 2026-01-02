# Building APK Without Android Studio

You have several options to build the APK without installing Android Studio:

## Option 1: GitHub Actions (Recommended - Easiest)

This automatically builds the APK when you push to GitHub.

### Steps:

1. **Push the code to GitHub** (if not already done):
   ```powershell
   cd Android
   .\setup_github.ps1
   ```

2. **The APK will be built automatically** on GitHub Actions

3. **Download the APK**:
   - Go to your GitHub repository: https://github.com/onlyonekenobi/ScoundrelCardGame-AndroidGame
   - Click on "Actions" tab
   - Click on the latest workflow run
   - Download the APK from "Artifacts" section

**Note**: The GitHub Actions workflow is already set up in `.github/workflows/build.yml`

## Option 2: Command Line with Android SDK

If you have Android SDK installed (without Android Studio):

### Prerequisites:
- JDK 17 or later
- Android SDK Command Line Tools
- Set ANDROID_HOME environment variable

### Steps:

1. **Install Android SDK Command Line Tools**:
   - Download from: https://developer.android.com/studio#command-tools
   - Extract to a folder (e.g., `C:\Android\Sdk`)
   - Set environment variable: `ANDROID_HOME=C:\Android\Sdk`

2. **Install required SDK components**:
   ```powershell
   # Using sdkmanager (in Android SDK tools/bin)
   sdkmanager "platform-tools" "platforms;android-34" "build-tools;34.0.0"
   ```

3. **Build the APK**:
   ```powershell
   cd Android
   .\build_apk.ps1
   ```

   Or manually:
   ```powershell
   cd Android
   .\gradlew.bat assembleDebug
   ```

4. **Find the APK**:
   - Location: `app\build\outputs\apk\debug\app-debug.apk`

## Option 3: Online Build Services

### GitHub Actions (Already Configured)
- Push code to GitHub
- APK builds automatically
- Download from Actions tab

### Alternative Services:
- **AppCircle** (free tier available)
- **Bitrise** (free for open source)
- **CircleCI** (free tier available)

## Option 4: Docker (Advanced)

If you have Docker installed, you can use an Android build container:

```bash
docker run --rm -v "$PWD/Android:/project" -w /project \
  android-build-tools:latest ./gradlew assembleDebug
```

## Quick Start (GitHub Actions)

The easiest way is to use GitHub Actions:

1. **Push to GitHub**:
   ```powershell
   cd Android
   git init
   git remote add origin https://github.com/onlyonekenobi/ScoundrelCardGame-AndroidGame.git
   git add .
   git commit -m "Initial commit"
   git branch -M main
   git push -u origin main
   ```

2. **Wait for build** (usually 2-5 minutes)

3. **Download APK**:
   - Go to: https://github.com/onlyonekenobi/ScoundrelCardGame-AndroidGame/actions
   - Click latest workflow run
   - Download from Artifacts

## Troubleshooting

**"Gradle not found"**:
- Make sure you're in the Android directory
- The gradlew.bat file should be present

**"Android SDK not found"**:
- Use GitHub Actions instead (no SDK needed)
- Or install Android SDK Command Line Tools

**"Java not found"**:
- Install JDK 17+ from https://adoptium.net/
- Set JAVA_HOME environment variable

## Recommendation

**Use GitHub Actions** - it's the easiest option:
- No local setup required
- Automatic builds on every push
- APK available for download
- Works on any platform

