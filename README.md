
# SpaceCore虚拟引擎内核  
  
SpaceCore是一个虚拟引擎内核，提供在Android上克隆和运行虚拟应用程序，并且已支持设备模拟、模拟定位以及其他更多的功能。本内核重新实现了现有框架的痛点，更贴合系统真实运行环境。

### [English](README_EN.md)

## 兼容
Android 6.0 ～ 13.0 及 鸿蒙

## 说明
开源部分为演示产品，不包含核心代码，成品Demo前往右手边 [Releases](https://github.com/FSpaceCore/SpaceCore/releases) 中下载。
其Demo中包含所有功能在SDK中均有提供，包含未展示部分。

如需SpaceCore完整商业版SDK（不提供源码），请联系 **Email: support@spacecore.dev**

功能 | 是否支持
---|---
NativeHook | 支持
JavaHook | 支持
IO | 支持
XPosed | 支持
免安装运行 | 支持
多开 | 支持
机型模拟 | 支持
虚拟定位 | 支持
语言模拟 | 支持
地区模拟 | 支持
时区模拟 | 支持
Root隐藏 | 支持
Sim卡隐藏 | 支持
禁用网络 | 支持

架构
架构 | 是否支持
---|---
arm64-v8a | 支持
armeabi-v7a | 支持

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
        // 加入规则
        FRuleCore.get().addRule(build);
        
        // 可云下发配置内容
        String json = new Gson().toJson(build);
```

## 最新更新
------
17、修复ActivityStack的一些问题<br/>
16、兼容targetSdkVersion至32<br/>
15、修复微信某些情况下的崩溃<br/>
14、修复低版本手机启动应用慢的问题、常见 微信、抖音等。<br/>
13、修复微信首次登陆成功后无法进入主页<br/>
12、初步支持规则配置系统<br/>
11、修复某团无法使用微信登陆问题<br/>
10、修复Messenger崩溃问题<br/>
9、修复某宝崩溃问题<br/>
8、支持 launchIntent API<br/>
7、兼容更多含有插件化功能的应用<br/>
6、修复免安装运行模式下apache问题<br/>
5、兼容Android 13<br/>
4、修复微信某些情况下崩溃问题<br/>
3、支持禁用网络<br/>
2、补全运行时权限<br/>
1、开放免安装运行模式<br/>
