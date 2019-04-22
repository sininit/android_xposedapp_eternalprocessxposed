```
切换分支可以看到各个版本的源码

Switch branches can see all versions of the source code

普通锁: 在系统内存低的时候自动清理应用 可以不被清理 你可以手动强制停止这个应用
永恒锁: 无论在什么情况下系统都无法清理你的应用

太极magisk说明：
在9.0可能出现各种问题能不用尽量不要用
支持太极 5.1.4及以上版本 需要刷入太极Magisk模块， 5.1.4以下版本不要用 不然开不了机
太极5.1.8下载 https://taichi.ctfile.com/fs/19604958-365278317
太极magisk模块4.7.0 下载 https://www.lanzous.com/i37r5vi
在太极 启动模块 并 添加应用 “Android 系统” 重启设备 才能生效
请注意 太极 magisk 版只支持 8.0 以上的系统  
由于太极magisk并不对所有设备支持 如果你要使用太极magisk 建议你安装mm管理以防翻车

我在Aex6.4（Android 9）和MIUI10（Android 9）上运行并没有问题

xposed说明：
如果Xposed版的翻车了请删除/data/user_de/0/de.robv.android.xposed.installer/conf/目录或者/data/data/de.robv.android.xposed.installer/conf/

★软件需要Xposed框架的支持
★主要用于经常被干掉后台的手机


● 此Xposed模块通过修改进程的OOM_adj参数,
达到在低运存时最大程度防止应用被系统清理, 当然你还是可以手动去停止应用

说明:
Xposed修改有风险 安装本应用代表你愿意承担风险.请谨慎操作

更多说明:
新应用安装时自动添加锁 (miui如果无效请在 安全管理-应用管理-权限-自启动管理 允许本应用自动启动(接收广播))
```

内个谁233(WhichWho) https://github.com/WhichWho
