
[中文文档](https://github.com/FSpaceCore/SpaceCore/blob/main/README_CN.md)

# SpaceCore 

SpaceCore is an "Application Virtualization Engine" on Android, also known as a lightweight Android virtual machine, which creates a sandbox environment for the Android system. It can be used for multiple app instances, data isolation, script automation, plug-in development, and Google Play Store deployment.

Official website: [https://spacecore.dev](https://spacecore.dev)

<br>

## Instructions

##### SpaceCore SDK
 * The SpaceCore SDK is a free product that is **NOT** open-source, and you are free to use it without notifying the author. It can also be used for commercial purposes. [SpaceCore SDK Download](https://github.com/FSpaceCore/SpaceCore/releases)
 * **For any customization**, please contact  [support@spacecore.dev](mailto:support@spacecore.dev)

##### SpaceCore Demo
 * The SpaceCore Demo is intended to demonstrate the usage of the SpaceCore SDK, which can be found in the code within this repository.  [SpaceCore Demo Release Download](https://github.com/FSpaceCore/SpaceCore/releases)

<br>

## Compatibility

|                     | Compatibility             |
|---------------------|----------------------------------|
|  ABIs               | `armeabi-v7a / arm64-v8a`           |
| Android version     | `6.0 ~ 14.0 and future versions`     |


## SDK Integration

### 0. Dependency
The version is based on the main project version, if the main project does not add dependencies, you need to add the following dependencies

```
implementation "com.tencent:mmkv-static:1.2.10"
implementation "com.google.code.gson:gson:2.9.1"
```

If the main project does not use kotlin, you need to additionally introduce
```
implementation "org.jetbrains.kotlin:kotlin-stdlib:1.5.30"
```

### 1. Initialization

Put this initialization code within "Application#attachBaseContext".

`FCore.get().init(this);`

Please note that after calling `init()`, if the `FCore.get().isClient()` condition is true, please try not to do other initialization in the Application. If you encounter any problems, please contact technical support.

```kotlin
override fun attachBaseContext(base: Context) {
    super.attachBaseContext(base)
    FCore.get().init(this)
    FCore.get().setAllowSystemInteraction(true)
    FCore.get().setAutoPreloadApplication(true)
    if(FCore.isClient()) {
        return
    }
    // do something...
}

override fun onCreate() {
    super.onCreate()
    if(FCore.isClient()) {
        return
    }
    // do something...
}
```

### 2. Examples

**Method 1: App Clone**

This method relies on the application that is already installed on the system. If the application is uninstalled from the system, the cloned app will also disappear.

```java
FCore.get().installPackageAsUser("package_name", USER_ID)
```

**Method 2: Running without installation**

This method supports running without installation and will not be affected by system installation or uninstallation.

```java
FCore.get().installPackageAsUser(new File("/sdcard/wechat.apk"), USER_ID)
```

### 3. Launch sandboxed application

```java
FCore.get().launchApk("package_name", USER_ID)
```

### API Documentation

METHOD | DESCRIPTION
---|--- 
init | Initialize sandbox |
isInstalled | Check if the app is installed in the sandbox |
installPackageAsUser | Clone App into sandbox according to package name|
installPackageAsUser | Clone App into sandbox via apk file|
uninstallPackage | Uninstall an App installed in the sandbox globally |
uninstallPackageAsUser | Uninstall an App installed in the sandbox by user|
getInstalledApplications | Get all applications installed in the sandbox|
getApplicationInfo | Get application info of an App in the sandbox|
getPackageInfo | Get package info of an application in the sandbox|
getLaunchIntentForPackage | Get LauncherIntent of an App |
launchApk | Launch App in sandbox |
launchIntent | Launch App via Intent |
isRunning | Check if an App is running |
clearPackage | Clear App data |
stopPackage | Stop an app from running|
stopAllPackages | Stop all running applications|
setAutoForeground | Set auto start/close notification bar to automatically close notification bar when no process is active

### Internal Process：

METHOD | DESCRIPTION
---|--- 
findProcessRecord | Finding process information
addProcessMonitor | Adding sandboxed internal process listeners
removeProcessMonitor | Removing sandboxed internal process listeners

### SandBox User：

METHOD | DESCRIPTION
---|--- 
getUsers | Getting users in the sandbox
createUser | Create users in the sandbox
deleteUser | Delete users in the sandbox (all application information will be deleted)

### APP Data：

METHOD | DESCRIPTION
---|--- 
exportAppData | Export all data of a certain application
importAppData | Import all data of a certain application

### APP Rules：

METHOD | DESCRIPTION
---|--- 
addRule| Add a rule
setAllowSystemInteraction | Whether to allow interaction with system applications when the sandbox cannot find broadcasts, activities, etc
setHideRoot | Hide root status
setHideSim | Hide SIM card status
setHideVPN | Hide VPN status
setVisitExternalApp | Allow sandboxed applications to perceive external applications
setDisableKill | Prevent application crashes
setDisableNetwork | Disable application network
setHidePath | Hide multi-open path and storage path
getSpaceLanguage | Get the simulated language of a certain space
setSpaceLanguage | Set the simulated language of a certain space (e.g. Chinese: zh)
getSpaceRegion | Get the simulated region of a certain space
setSpaceRegion | Set the simulated region of a certain space (e.g. China: CN)
getSpaceTimeZone | Get the simulated time zone of a certain space
setSpaceTimeZone | Set the simulated time zone of a certain space (e.g. Shanghai: Asia/Shanghai)

### App Permissions：

METHOD | DESCRIPTION
---|--- 
getPermission | Get app permission rules
updatePermission | Update app permission rules
revokePermission | Remove app permission rules (the app will follow the actual permissions of the host APP)


### SandBox Configuration：

METHOD | DESCRIPTION
---|--- 
enableOptRule | Whether to enable rule-based blocking of push notifications, third-party SDKs, hot updates, ads, etc. to optimize app running speed. If an application exception occurs, please turn off
setAutoPreloadApplication | Intelligent preloading of applications, where the kernel automatically loads applications based on usage to accelerate startup speed. Default: on
preloadApplicationCount | Default number of preloaded applications: 2
setPreloadProcessCount | Set the number of preloading processes to speed up application startup. Default: 3
setBackToHome | Whether to return to the host app when the sandbox app exits
setSpaceTaskDescriptionPrefix | Set the application prefix in the recent tasks list (default: F{user ID})
setEnableLauncherView | Whether to enable splash screen
restartCoreSystem | Restart the kernel (all applications will be killed)

### Virtual GPS Location：

METHOD | DESCRIPTION
---|--- 
disableFakeLocation | Disable virtual location for a certain user
enableFakeLocation | Enable virtual location for a certain user
setLocation | Set virtual location parameters for a certain user
getLocation | Get virtual location parameters for a certain user
setGlobalLocation | Set global virtual location parameters
getGlobalLocation | Get global virtual location parameters


## Rule Configuration System
When dealing with various applications, SpaceCore supports configuring different runtime parameters and virtual machine parameters to achieve adaptation. SpaceCore supports a powerful rule configuration system that can customize exclusive rules for each application. The rule library can be dynamically updated through cloud configuration. The supported rule functions are gradually under development.

```java
PackageRule.Builder builder = new PackageRule.Builder("com.tencent.mm",
    /* Scoped process. Leave blank if the scope is all processes */
    "com.tencent.mm", "com.tencent.mm:tools", "com.tencent.mm:appbrand1", "com.tencent.mm:appbrand2")
    // disable a Activity
    .addBlackActivity("com.tencent.mm.plugin.base.stub.WXEntryActivity")
    // disable a broadcast
    .addBlackBroadcast("com.tencent.mm.plugin.appbrand.task.AppBrandTaskPreloadReceiver")
    // disable a service
    .addBlackService("com.tencent.mm.plugin.backup.backuppcmodel.BackupPcService")
    // disable a ContentProvider
    .addBlackContentProvider("androidx.startup.InitializationProvider")
    // preloading process, can pre-start a certain process to speed up the runtime experience
    .addPreloadProcessName("com.tencent.mm:appbrand1")
    // disable a process from starting
    .addBlackProcessName("com.tencent.mm:appbrand2")
    // deny access to a file
    .addBlackIO("/proc/self/maps")
    // redirect a certain file
    .addRedirectIO("/proc/self/cmdline", "/proc/self/fake-cmdline")
    // hide root
    .isHideRoot(true)
    // hide SIM
    .isHideSim(true)
    // hide VPN
    .isHideVpn(true)
    // many more...
    // set language
    .setLanguage("zh")
    // set region
    .setRegion("CN")
    // set timezone
    .setTimeZone("Asia/Shanghai");

PackageRule build = builder.build();

// add a rule
FCore.get().addRule(build);

// if there are multiple rules, put them in FRule
FRule fRule = new FRule(builder.build(), builder.build(), builder.build());
FCore.get().addFRule(fRule);

// support fetching configuration content from remote cloud
String json = new Gson().toJson(fRule);
// load json rule
FCore.get().addFRuleContent(json);
```
