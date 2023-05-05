
[中文文档](https://github.com/FSpaceCore/SpaceCore/blob/main/README_CN.md)

# SpaceCore 

SpaceCore is an "Application Virtualization Engine" on Android, also known as a lightweight Android virtual machine, which creates a sandbox environment for the Android system. It can be used for multiple app instances, data isolation, script automation, plug-in development, and Google Play Store deployment.

Official website: [https://spacecore.dev](https://spacecore.dev)

<br>

## Instructions

##### SpaceCore SDK
 * The SpaceCore SDK is a free product that is **NOT** open-source, and you are free to use it without notifying the author. It can also be used for commercial purposes. [SpaceCore SDK Download](https://github.com/FSpaceCore/SpaceCore/releases)
 * ** For any customization **, please contact  [support@spacecore.dev](mailto:support@spacecore.dev)

##### SpaceCore Demo
 * The SpaceCore Demo is intended to demonstrate the usage of the SpaceCore SDK, which can be found in the code within this repository.  [SpaceCore Demo Release Download](https://github.com/FSpaceCore/SpaceCore/releases)

<br>

## Compatibility

|                     | Compatibility             |
|---------------------|----------------------------------|
|  ABIs               | `armeabi-v7a / arm64-v8a`           |
| Android version     | `6.0 ~ 14.0 and future versions`     |


## Usages

### 0. Dependency

```
implementation "com.tencent:mmkv-static:1.2.10"
implementation "com.google.code.gson:gson:2.9.1"

implementation "org.jetbrains.kotlin:kotlin-stdlib:1.5.30"
```

### 1. Initialization

Put this initialization code within "Application#attachBaseContext".

`FCore.get().init(this);`

### 2. Examples

App clone

`FCore.get().installPackageAsUser("package_name", USER_ID)`

App running without installing

`FCore.get().installPackageAsUser(new File("/sdcard/some_app.apk"), USER_ID)`

Launch App in sandbox

`FCore.get().launchApk("package_name", USER_ID)`

### 3. Interfaces

|   METHOD      |  DESCRIPTION |
|-----|----|
| init | Initialize sandbox | 
| isInstalled | Check if the App is installed in the sandbox | 
| installPackageAsUser | Clone App into sandbox according to package name | 
| installPackageAsUser | Clone App into sandbox via apk file | 
| uninstallPackage | Uninstall an App installed in the sandbox globally | 
| uninstallPackageAsUser | Uninstall an App installed in the sandbox by user | 
| getInstalledApplications | Get all applications installed in the sandbox | 
| getApplicationInfo | Get application info of an App in the sandbox | 
| getPackageInfo | Get package info of an application in the sandbox | 
| getLaunchIntentForPackage | Get LauncherIntent of an App | 
| launchApk | Launch App in sandbox | 
| launchIntent | Launch App via Intent | 
| isRunning | Check if an App is running | 
| clearPackage | Clear App data | 
| stopPackage | Stop an app from running | 
| stopAllPackages | Stop all running applications | 
| setApplicationCallback |  | 
| disableFakeLocation |  | 
| enableFakeLocation |  | 
| setLocation |  | 
| getLocation |  | 
| setGlobalLocation |  | 
| getGlobalLocation |  | 
| getUsers |  | 
| createUser |  | 
| deleteUser |  | 
| exportAppData |  | 
| importAppData |  | 
| setPreloadProcessCount |  | 
| setHideRoot |  | 
| setHideSim |  | 
| setHideVPN |  | 
| setVisitExternalApp |  | 
| setDisableKill |  | 
| setDisableNetwork |  | 
| setHidePath |  | 
| getSpaceLanguage |  | 
| setSpaceLanguage |  | 
| getSpaceRegion |  | 
| setSpaceRegion |  | 
| getSpaceTimeZone |  | 
| setSpaceTimeZone |  | 
| getSpaceTaskDescriptionPrefix |  | 

