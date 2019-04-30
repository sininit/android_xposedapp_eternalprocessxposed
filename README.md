Xda link: https://forum.xda-developers.com/xposed/modules/mod-eternal-process-lock-v1-0-t3797243

```
* 切换分支可以看到各个版本的源码
* Switch branches can see all versions of the source code

# 更新后需要取消勾选模块再勾选模块才重启系统
# Updated to uncheck the module to check the module to restart the system

* 每个分支已经有编译好的app安装包 /app/build/bin/app.apk 可自行下载
* Each branch already has a compiled app installation package/app/build/bin/app.apk to download on its own

* 模块需要Xposed框架的支持
* Module needs the support of Xposed framework

* 主要用于经常被干掉后台的手机
* Mainly used for mobile phones that are often killed backstage

* 此Xposed模块通过修改进程的OOM_adj参数,
达到在低运存时最大程度防止应用被系统清理, 当然你还是可以手动去停止应用
* This Xposed module minimizes the application's system cleanup by modifying the OOM_adj parameters of the process to minimize the application at low storage, although you can still manually stop the app.

* Xposed修改有风险 安装本应用代表你愿意承担风险.请谨慎操作
Xposed Modify risky installation This app means you're willing to take risks. Please operate with caution

普通锁: 在系统内存低的时候自动清理应用 可以不被清理 但是你可以手动强制停止这个应用
Normal Lock(black lock): Automatic cleanup app can not be cleaned when system memory is low but you can manually force the app to stop

永恒锁: 无论在什么情况下系统都无法清理你的应用后台
Eternal Lock(RED lock): No matter under any circumstances the system can clean up your app background

太极magisk说明：
在9.0可能出现各种问题能不用尽量不要用
支持太极Magisk 5.1.4及以上版本
太极5.1.8下载 https://taichi.ctfile.com/fs/19604958-365278317
太极magisk模块4.7.0 下载 https://www.lanzous.com/i37r5vi
在太极 启动模块 并 添加应用 “Android 系统” 重启设备 才能生效
太极 magisk 版只支持 Android 8.0 以上的系统  
**** 由于太极magisk并不对所有设备支持 如果你要使用太极magisk 建议你安装 "mm管理器" 以防系统出现异常

xposed说明：
如果Xposed版出现异常无法启动系统，请进入recovery删除目录/data/user_de/0/de.robv.android.xposed.installer/conf/或目录/data/data/de.robv.android.xposed.installer/conf/然后重启手机

更多说明:
新应用安装时自动添加锁 (miui如果无效请在 安全管理-应用管理-权限-自启动管理 允许本应用自动启动(接收广播))
```

内个谁233(WhichWho) https://github.com/WhichWho
