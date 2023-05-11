  
# SpaceCore

SpaceCore是一个基于Android系统的应用程序虚拟化引擎，一个Android系统的沙盒环境，亦可看作是轻量级的Android虚拟机。可用于APP多开、数据隔离、脚本自动化、插件化开发、谷歌商店上架等。

官网：[https://spacecore.dev](https://spacecore.dev)

Telegram：[@android_spacecore](https://t.me/android_spacecore)


## 使用说明

##### SpaceCore SDK
 * SpaceCore SDK 为免费产品，**非开源**，您可以自由使用而无须通知本作者，亦可作为商业用途使用 [SpaceCore SDK 下载](https://github.com/FSpaceCore/SpaceCore/releases)
 * **如有定制需求，请联系**  [support@spacecore.dev](mailto:support@spacecore.dev)

##### SpaceCore Demo
 * SpaceCore Demo 用于演示SpaceCore SDK的使用方式，即本仓库中的代码  [SpaceCore Demo Release 下载](https://github.com/FSpaceCore/SpaceCore/releases)


## 兼容性


|                     | 兼容性             |
|---------------------|----------------------------------|
|  ABIs               | `armeabi-v7a / arm64-v8a`           |
| Android version     | `6.0 ~ 14.0 及后续版本持续兼容更新`       |


## 集成SDK

### Step 1.  依赖引用

版本根据主工程版本即可，如主工程未添加依赖则需要添加以下依赖
```
implementation "com.tencent:mmkv-static:1.2.10"
implementation "com.google.code.gson:gson:2.9.1"
```

如果主工程未使用kotlin，需要额外引入
```
implementation "org.jetbrains.kotlin:kotlin-stdlib:1.5.30"
```
<br>

### Step 2 初始化，在Application#attachBaseContext中加入以下代码初始化

```java
FCore.get().init(this);
```

请注意，在调用init之后，若FCore.get().isClient()条件成立，请尽量不要在Application中做其他事情的初始化，如遇到问题，请联系技术人员。
```kotlin
override fun attachBaseContext(base: Context) {  
    super.attachBaseContext(base)  
    FCore.get().init(this)
	// 允许与系统交互  
    FCore.get().setAllowSystemInteraction(true)  
	// 允许智能预加载  
    FCore.get().setAutoPreloadApplication(true)
	// .....
    if(FCore.isClient()) {
	    return
    }
    // do ....
}  
  
override fun onCreate() {  
    super.onCreate()
    if(FCore.isClient()) {
	    return
    }
    // do ....
}
```
<br>

### Step 3.  安装

##### 克隆APP
此方式会依赖系统已安装应用，如果系统上的应用卸载，那么空间内应用也会跟随消失
```java
FCore.get().installPackageAsUser("包名", USER_ID)
```

##### 免安装运行
此方式支持免安装，不会受系统安装卸载影响。
```java
FCore.get().installPackageAsUser(new File("/sdcard/wechat.apk"), USER_ID)
```

### Step 4.  启动沙盒应用
```java
FCore.get().launchApk("包名", USER_ID)
```
<br>

## 接口文档

方法 | 描述 | en 
---|--- | ---
init | 初始化内核，请在Application#attachBaseContext调用 | Initialize sandbox |
isInstalled | 沙盒内是否已安装 | Check if the app is installed in the sandbox |
installPackageAsUser | 提供包名克隆应用到沙盒 | Clone App into sandbox according to package name|
installPackageAsUser | 提供File安装应用到沙盒 | Clone App into sandbox via apk file|
uninstallPackage | 卸载沙盒中所有用户已安装的某个应用 | Uninstall an App installed in the sandbox globally |
uninstallPackageAsUser | 卸载某个用户的应用 | Uninstall an App installed in the sandbox by user|
getInstalledApplications | 获取已安装在沙盒內的应用 | Get all applications installed in the sandbox|
getApplicationInfo | 获取沙盒內某个应用的ApplicationInfo | Get application info of an App in the sandbox|
getPackageInfo | 获取沙盒內某个应用的PackageInfo | Get package info of an application in the sandbox|
getLaunchIntentForPackage | 获取应用Launcher Intent | Get LauncherIntent of an App |
launchApk | 启动沙盒应用 | Launch App in sandbox |
launchIntent | 通过Intent启动应用 | Launch App via Intent |
isRunning | 判断某个应用是否正在运行 | Check if an App is running |
clearPackage | 清除沙盒APK数据 | Clear App data |
stopPackage | 停止某个应用运行 | Stop an app from running|
stopAllPackages | 停止所有正在运行的应用 | Stop all running applications|
setApplicationCallback | 注册应用Application启动回调 | |
preloadApplication | launchApk时间较长，可以使用此方法阻塞加载，加载成功后再launchApk。期间可以自己做动画或者等待启动的事件
setAutoForeground | 设置自动启动/关闭通知栏，当没有进程活跃时自动关闭通知栏

### 内部进程：

方法 | 描述 | en 
---|--- | ---
findProcessRecord | 寻找进程信息
addProcessMonitor | 添加沙盒内部进程监听
removeProcessMonitor | 移除沙盒内部进程监听

### 内核用户：

方法 | 描述 | en 
---|--- | ---
getUsers | 获取沙盒中的用户
createUser | 创建沙盒中的用户
deleteUser | 删除沙盒中的用户（所有应用信息将被删除）

### 应用数据：

方法 | 描述 | en 
---|--- | ---
exportAppData | 导出某个应用的所有数据
importAppData | 导入某个应用的所有数据

### 应用规则：

方法 | 描述 | en 
---|--- | ---
addRule| 加入一份配置规则
setAllowSystemInteraction | 当沙盒查找不到广播、Activity等时，是否允与系统应用交互
setHideRoot | 隐藏Root
setHideSim | 隐藏SIM卡状态
setHideVPN | 隐藏VPN状态
setVisitExternalApp | 沙盒內应用可感知外部应用
setDisableKill | 防止应用闪退
setDisableNetwork | 禁用应用网络
setHidePath | 隐藏多开路径、存储路径
getSpaceLanguage | 获取某个空间的模拟语言
setSpaceLanguage | 设置某个空间的模拟语言（中国：zh）
getSpaceRegion | 获取某个空间的模拟地区
setSpaceRegion | 设置某个空间的模拟地区（中国：CN）
getSpaceTimeZone | 获取某个空间的模拟时区
setSpaceTimeZone | 设置某个空间的模拟时区（上海：Asia/Shanghai）

### 应用权限：

方法 | 描述 | en 
---|--- | ---
getPermission | 获取应用权限规则
updatePermission | 更新应用权限规则
revokePermission | 移除应用权限规则（应用将跟随宿主实际权限）


### 内核配置：

方法 | 描述 | en 
---|--- | ---
enableOptRule | 是否启用规则屏蔽推送、三方sdk、热更新、广告等，优化应用运行速度，如遇到应用异常，请关闭
setAutoPreloadApplication | 智能预加载应用，内核自动根据应用使用情况主动加载应用，加快启动速度，默认：开
preloadApplicationCount | 默认预加载应用数量，默认：2
setPreloadProcessCount | 设置预加载进程数量，加快应用启动速度，默认：3
setBackToHome | 当沙盒应用退出时是否返回宿主
setSpaceTaskDescriptionPrefix | 设置最近任务栏的应用前缀（默认：F{用户ID}）
setEnableLauncherView | 是否启用 应用启动图
restartCoreSystem | 重启内核（所有应用将会杀死）


## 规则配置系统
在面对各种应用时，支持配置不同的运行时参数，虚拟机参数来达到适配，SpaceCore支持强大的规则配置系统，能对每个应用定制专属的规则，可以通过云配置方式，动态更新规则库。规则支持的功能正在逐步开发。

```java
PackageRule.Builder builder = new PackageRule.Builder("com.tencent.mm",
                /*作用域进程，如果所有进程则留空*/
                "com.tencent.mm", "com.tencent.mm:tools", "com.tencent.mm:appbrand1", "com.tencent.mm:appbrand2")
                // 禁用某个Activity
                .addBlackActivity("com.tencent.mm.plugin.base.stub.WXEntryActivity")
                // 禁用某个广播
                .addBlackBroadcast("com.tencent.mm.plugin.appbrand.task.AppBrandTaskPreloadReceiver")
                // 禁用某个服务
                .addBlackService("com.tencent.mm.plugin.backup.backuppcmodel.BackupPcService")
                // 禁用某个ContentProvider
                .addBlackContentProvider("androidx.startup.InitializationProvider")
                // 预加载进程，可预先启动某个进程 加快运行时体验速度。
                .addPreloadProcessName("com.tencent.mm:appbrand1")
                // 禁止某个进程启动
                .addBlackProcessName("com.tencent.mm:appbrand2")
                // 禁止访问某文件
                .addBlackIO("/proc/self/maps")
                // 重定向某文件
                .addRedirectIO("/proc/self/cmdline", "/proc/self/fake-cmdline")
                // 隐藏Root
                .isHideRoot(true)
                // 隐藏Sim
                .isHideSim(true)
                // 隐藏VPN
                .isHideVpn(true)
                // 许多等等....
                // 设置环境语言
                .setLanguage("zh")
                // 设置当前所在区域
                .setRegion("CN")
                // 设置当前时区
                .setTimeZone("Asia/Shanghai");
        PackageRule build = builder.build();
        
        // 加入单条规则
        FCore.get().addRule(build);


		// 如果多条规则请放入FRule
	    FRule fRule = new FRule(builder.build(), builder.build(), builder.build());
        FCore.get().addFRule(fRule);


        // 可云下发配置内容
        String json = new Gson().toJson(fRule);
        // 客户端加载云下发json
        FCore.get().addFRuleContent(json);
```
