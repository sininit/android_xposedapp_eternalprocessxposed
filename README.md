[Xda link](https://forum.xda-developers.com/xposed/modules/mod-eternal-process-lock-v1-0-t3797243)

[Help](https://github.com/xiaoxinwangluo/android_xposedapp_eternalprocessxposed/tree/master/help)

[Download](http://repo.xposed.info/module/top.fols.aapp.eternalprocessxposed)

```


* 切换分支可以看到各个版本的源码
* Switch branches can see all versions of the source code

# 更新后需要取消勾选模块再勾选模块
# After updating the module, uncheck the module and then tick the module

* 模块需要Xposed框架的支持
* Module needs the support of Xposed framework

* 此Xposed模块通过修改进程的OOM_adj参数,
达到在低运存时最大程度防止应用被系统清理, 当然你还是可以手动去停止应用
* This Xposed module minimizes the application's system cleanup by modifying the OOM_adj parameters of the process to minimize the application at low storage, although you can still manually stop the app.

* Xposed修改有风险 安装本应用代表你愿意承担风险.请谨慎操作
Xposed Modify risky installation This app means you're willing to take risks. Please operate with caution

* 永恒锁校验方法: 使用用永恒锁锁住网易云音乐 ，打开网易云音乐随便播放一首歌曲，然后强制停止应用，看看它是否还在播放
Eternal lock verification method: Use the Eternal Lock to lock Netease Cloud Music, open Netease Cloud Music and play a song casually, then force the application to stop and see if it is still playing.
```
