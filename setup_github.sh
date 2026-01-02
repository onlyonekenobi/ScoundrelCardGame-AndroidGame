#!/bin/bash
# Bash script to set up Git and push to GitHub

echo "Setting up Git repository and pushing to GitHub..."

# Navigate to script directory
cd "$(dirname "$0")"

# Initialize Git if not already done
if [ ! -d .git ]; then
    echo "Initializing Git repository..."
    git init
fi

# Add remote if not exists
if ! git remote | grep -q "origin"; then
    echo "Adding remote repository..."
    git remote add origin https://github.com/onlyonekenobi/ScoundrelCardGame-AndroidGame.git
else
    echo "Remote already exists, updating URL..."
    git remote set-url origin https://github.com/onlyonekenobi/ScoundrelCardGame-AndroidGame.git
fi

# Add all files
echo "Adding files to Git..."
git add .

# Check if there are changes to commit
if [ -n "$(git status --porcelain)" ]; then
    echo "Committing changes..."
    git commit -m "Initial Android app - Scoundrel Card Game"
    
    # Set main branch
    git branch -M main
    
    # Push to GitHub
    echo "Pushing to GitHub..."
    echo "You may be prompted for GitHub credentials."
    git push -u origin main
    
    echo "Successfully pushed to GitHub!"
else
    echo "No changes to commit."
fi

echo ""
echo "Next steps:"
echo "1. Open the Android folder in Android Studio"
echo "2. Build the APK: Build > Build Bundle(s) / APK(s) > Build APK(s)"
echo "3. Find the APK in: app/build/outputs/apk/debug/app-debug.apk"
echo "4. Transfer to your Android device and install"

