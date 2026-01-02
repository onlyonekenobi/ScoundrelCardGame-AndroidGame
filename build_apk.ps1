# PowerShell script to build APK without Android Studio
# Requires: JDK 17+ and Android SDK

Write-Host "Building Scoundrel Android APK..." -ForegroundColor Green
Write-Host ""

# Check for Java
Write-Host "Checking for Java..." -ForegroundColor Yellow
$javaVersion = java -version 2>&1 | Select-String -Pattern "version"
if (-not $javaVersion) {
    Write-Host "ERROR: Java not found!" -ForegroundColor Red
    Write-Host "Please install JDK 17 or later from:" -ForegroundColor Yellow
    Write-Host "https://adoptium.net/ or https://www.oracle.com/java/technologies/downloads/" -ForegroundColor Cyan
    exit 1
}
Write-Host "Java found: $javaVersion" -ForegroundColor Green

# Check for Android SDK
Write-Host ""
Write-Host "Checking for Android SDK..." -ForegroundColor Yellow
$sdkPath = $env:ANDROID_HOME
if (-not $sdkPath) {
    $sdkPath = $env:ANDROID_SDK_ROOT
}
if (-not $sdkPath) {
    Write-Host "WARNING: ANDROID_HOME not set. Attempting to find SDK..." -ForegroundColor Yellow
    # Common locations
    $commonPaths = @(
        "$env:LOCALAPPDATA\Android\Sdk",
        "$env:USERPROFILE\AppData\Local\Android\Sdk",
        "C:\Android\Sdk"
    )
    foreach ($path in $commonPaths) {
        if (Test-Path $path) {
            $sdkPath = $path
            Write-Host "Found SDK at: $sdkPath" -ForegroundColor Green
            break
        }
    }
}

if (-not $sdkPath -or -not (Test-Path $sdkPath)) {
    Write-Host ""
    Write-Host "Android SDK not found!" -ForegroundColor Red
    Write-Host ""
    Write-Host "Options:" -ForegroundColor Yellow
    Write-Host "1. Install Android SDK Command Line Tools:" -ForegroundColor Cyan
    Write-Host "   https://developer.android.com/studio#command-tools" -ForegroundColor White
    Write-Host ""
    Write-Host "2. Use GitHub Actions (automatic build):" -ForegroundColor Cyan
    Write-Host "   Push to GitHub and the APK will be built automatically" -ForegroundColor White
    Write-Host ""
    Write-Host "3. Use online build service:" -ForegroundColor Cyan
    Write-Host "   - GitHub Actions (recommended)" -ForegroundColor White
    Write-Host "   - Or use the .github/workflows/build.yml workflow" -ForegroundColor White
    exit 1
}

Write-Host "Android SDK found at: $sdkPath" -ForegroundColor Green

# Set environment variables
$env:ANDROID_HOME = $sdkPath
$env:ANDROID_SDK_ROOT = $sdkPath

# Check for Gradle wrapper
if (-not (Test-Path "gradlew.bat")) {
    Write-Host "ERROR: gradlew.bat not found!" -ForegroundColor Red
    Write-Host "Please run this script from the Android directory" -ForegroundColor Yellow
    exit 1
}

# Build APK
Write-Host ""
Write-Host "Building APK (this may take a few minutes)..." -ForegroundColor Yellow
Write-Host ""

& .\gradlew.bat assembleDebug

if ($LASTEXITCODE -eq 0) {
    Write-Host ""
    Write-Host "Build successful!" -ForegroundColor Green
    Write-Host ""
    $apkPath = "app\build\outputs\apk\debug\app-debug.apk"
    if (Test-Path $apkPath) {
        Write-Host "APK location: $apkPath" -ForegroundColor Cyan
        Write-Host ""
        Write-Host "Next steps:" -ForegroundColor Yellow
        Write-Host "1. Transfer the APK to your Android device" -ForegroundColor White
        Write-Host "2. Enable 'Install Unknown Apps' in Android settings" -ForegroundColor White
        Write-Host "3. Open the APK file and install" -ForegroundColor White
    } else {
        Write-Host "WARNING: APK file not found at expected location" -ForegroundColor Yellow
    }
} else {
    Write-Host ""
    Write-Host "Build failed!" -ForegroundColor Red
    Write-Host "Consider using GitHub Actions for automatic building" -ForegroundColor Yellow
}

