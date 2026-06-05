# R.jar kilidi olursa: Cursor/Android Studio kapali iken calistirin.
$ErrorActionPreference = "Stop"
Set-Location $PSScriptRoot

Get-Process -Name java -ErrorAction SilentlyContinue |
    Where-Object { $_.Path -like "*gradle*" -or $_.CommandLine -like "*GradleServer*" } |
    Stop-Process -Force -ErrorAction SilentlyContinue

& .\gradlew.bat --stop | Out-Null
& .\gradlew.bat assembleDebug --no-daemon
