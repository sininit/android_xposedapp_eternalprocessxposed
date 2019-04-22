package top.fols.aapp.eternalprocessxposed;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import top.fols.aapp.eternalprocessxposed.R;
import top.fols.aapp.eternalprocessxposed.activity.MainActivity;
public class AppInstallReciver extends BroadcastReceiver {
	public static void addNotification(Context context, String app_name, String packageName) {
		Notification.Builder builder1 = new Notification.Builder(context);
		builder1.setContentTitle(context.getString(R.string.newappinstallautoaddlockreminder));
		builder1.setContentText(app_name + "[" + packageName + "]"); //消息内容

		builder1.setSmallIcon(R.drawable.ic_launcher) ;
		builder1.setAutoCancel(true);

		Notification notification = builder1.build();
		NotificationManager notificationManager = (NotificationManager)context. getSystemService(Activity.NOTIFICATION_SERVICE);
		notificationManager.notify((int)(Long.parseLong(Long.toString(System.currentTimeMillis()).substring(3))), notification); // 通过通知管理器发送通
	}
    private Context mContext;
    private PackageInfo packageInfo = null;
    @Override
    public void onReceive(Context context, Intent intent) {
        mContext = context;
        String action = intent.getAction();
        // 获取包名
        String packageName = intent.getData().getSchemeSpecificPart();
		CharSequence appName = null;
		if (TextUtils.equals(action, Intent.ACTION_PACKAGE_ADDED)) {
			try {
				//获取应用名
				packageInfo = mContext.getPackageManager().getPackageInfo(packageName, 0);
				if (MainActivity.isNewInstallAutoAddLock(context)) {
					Config.lockMessage lockMessage = new Config.lockMessage();
					lockMessage.islock = true;
					lockMessage.packageName = packageName;
					Config.addPackageName(Config.getPackageNameListSharedPreferencesDirFileAndUpdatePermissions(context), lockMessage);
				} else
					return;
			} catch (PackageManager.NameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			appName = packageInfo.applicationInfo.loadLabel(mContext.getPackageManager());
			addNotification(context, appName == null ?null: appName.toString(), packageName);
		} else if (TextUtils.equals(intent.getAction(), Intent.ACTION_PACKAGE_REMOVED)) {
			Config.lockMessage lockMessage = new Config.lockMessage();
			lockMessage.packageName = packageName;
			Config.removePackageList(Config.getPackageNameListSharedPreferencesDirFileAndUpdatePermissions(context), lockMessage);
		}
    }
}
