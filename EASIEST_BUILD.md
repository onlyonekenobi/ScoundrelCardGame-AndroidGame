# Easiest Way to Build APK (No Android Studio!)

## 🎯 Recommended: GitHub Actions

**This is the absolute easiest method - no software installation needed!**

### Steps:

1. **Push code to GitHub**:
   ```powershell
   cd Android
   git init
   git remote add origin https://github.com/onlyonekenobi/ScoundrelCardGame-AndroidGame.git
   git add .
   git commit -m "Initial Android app"
   git branch -M main
   git push -u origin main
   ```

2. **Wait 2-5 minutes** - GitHub will automatically build the APK

3. **Download your APK**:
   - Go to: https://github.com/onlyonekenobi/ScoundrelCardGame-AndroidGame/actions
   - Click on the latest workflow run (should have a green checkmark)
   - Scroll down to "Artifacts" section
   - Click "app-debug" to download the APK file

4. **Install on your phone**:
   - Transfer the APK to your Android device (via USB, email, cloud storage, etc.)
   - On your phone: Settings > Security > Enable "Install Unknown Apps" (or similar)
   - Open the APK file and tap "Install"

**That's it!** No Android Studio, no SDK, no Java installation needed.

## How It Works

GitHub Actions automatically:
- Sets up Java and Android SDK
- Builds your APK
- Makes it available for download
- All happens in the cloud!

## Troubleshooting

**"Workflow not running"**:
- Make sure you pushed the code to GitHub
- Check that `.github/workflows/build.yml` exists in your repository
- Go to Actions tab and check for any errors

**"Can't find artifacts"**:
- Wait a few more minutes for the build to complete
- Make sure the workflow completed successfully (green checkmark)

**"APK won't install"**:
- Make sure "Install Unknown Apps" is enabled on your device
- Check that your device meets Android 7.0+ requirement

## Alternative: If You Have Java Installed

If you have JDK 17+ installed, you can try:

```powershell
cd Android
.\build_apk.ps1
```

But this still requires Android SDK setup. **GitHub Actions is much easier!**

