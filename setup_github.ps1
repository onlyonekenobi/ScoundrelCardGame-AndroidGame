# PowerShell script to set up Git and push to GitHub

Write-Host "Setting up Git repository and pushing to GitHub..." -ForegroundColor Green

# Navigate to Android directory
Set-Location $PSScriptRoot

# Initialize Git if not already done
if (-not (Test-Path .git)) {
    Write-Host "Initializing Git repository..." -ForegroundColor Yellow
    git init
}

# Add remote if not exists
$remoteExists = git remote | Select-String -Pattern "origin"
if (-not $remoteExists) {
    Write-Host "Adding remote repository..." -ForegroundColor Yellow
    git remote add origin https://github.com/onlyonekenobi/ScoundrelCardGame-AndroidGame.git
} else {
    Write-Host "Remote already exists, updating URL..." -ForegroundColor Yellow
    git remote set-url origin https://github.com/onlyonekenobi/ScoundrelCardGame-AndroidGame.git
}

# Add all files
Write-Host "Adding files to Git..." -ForegroundColor Yellow
git add .

# Check if there are changes to commit
$status = git status --porcelain
if ($status) {
    Write-Host "Committing changes..." -ForegroundColor Yellow
    git commit -m "Initial Android app - Scoundrel Card Game"
    
    # Set main branch
    git branch -M main
    
    # Push to GitHub
    Write-Host "Pushing to GitHub..." -ForegroundColor Yellow
    Write-Host "You may be prompted for GitHub credentials." -ForegroundColor Cyan
    git push -u origin main
    
    Write-Host "Successfully pushed to GitHub!" -ForegroundColor Green
} else {
    Write-Host "No changes to commit." -ForegroundColor Yellow
}

Write-Host "`nNext steps:" -ForegroundColor Cyan
Write-Host "1. Open the Android folder in Android Studio" -ForegroundColor White
Write-Host "2. Build the APK: Build > Build Bundle(s) / APK(s) > Build APK(s)" -ForegroundColor White
Write-Host "3. Find the APK in: app/build/outputs/apk/debug/app-debug.apk" -ForegroundColor White
Write-Host "4. Transfer to your Android device and install" -ForegroundColor White

