package top.fols.aapp.utils.donate;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class WeiXinDonate
{
	private static final String TAG = WeiXinDonate.class.getSimpleName();
	// 微信包名
	private static final String TENCENT_PACKAGE_NAME = "com.tencent.mm";
	// 微信二维码扫描页面地址
	private static final String TENCENT_ACTIVITY_BIZSHORTCUT = "com.tencent.mm.action.BIZSHORTCUT";
	// Extra data
	private static final String TENCENT_EXTRA_ACTIVITY_BIZSHORTCUT = "LauncherUI.From.Scaner.Shortcut";

	/**
	 * 启动微信二维码扫描页
	 * ps： 需要你引导用户从文件中扫描二维码
	 * @param activity activity
	 */
	private static void gotoWeChatQrScan(@NonNull Activity activity) throws ActivityNotFoundException
	{
		Intent intent = new Intent(TENCENT_ACTIVITY_BIZSHORTCUT);
		intent.setPackage(TENCENT_PACKAGE_NAME);
		intent.putExtra(TENCENT_EXTRA_ACTIVITY_BIZSHORTCUT, true);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
						Intent.FLAG_ACTIVITY_CLEAR_TOP |
						Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
		activity.startActivity(intent);
	}

	private static void sendPictureStoredBroadcast(Context context, String qrSavePath)
	{
		Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
		Uri uri = Uri.fromFile(new File(qrSavePath));
		intent.setData(uri);
		context.sendBroadcast(intent);
	}

	/**
	 * 保存图片到本地，需要权限
	 * @param qrBitmap 二维码图片
	 */
	public static void saveDonateQrImage2SDCard(@NonNull String qrSavePath, @NonNull Bitmap qrBitmap)
	{
		File qrFile = new File(qrSavePath);
		File parentFile = qrFile.getParentFile();
		if (!parentFile.exists())
		{
			parentFile.mkdirs();
		}
		try
		{
			FileOutputStream fos = new FileOutputStream(qrFile);
			qrBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
			fos.flush();
			fos.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * 微信捐赠
	 * @param activity activity
	 * @param qrSavePath 个人收款二维码，可以通过微信生成
	 */
	public static void donateViaWeiXin(Activity activity, String qrSavePath)
	{
		if (activity == null || TextUtils.isEmpty(qrSavePath))
		{
			//参数错误
			Log.d(TAG, "param for null");
			return;
		}
		sendPictureStoredBroadcast(activity, qrSavePath);
		gotoWeChatQrScan(activity);
	}

	/**
	 * 判断支付宝客户端是否已安装，建议调用转账前检查
	 * @param context Context
	 * @return 支付宝客户端是否已安装
	 */
	public static boolean hasInstalledWeiXinClient(Context context)
	{
		PackageManager pm = context.getPackageManager();
		try
		{
			PackageInfo info = pm.getPackageInfo(TENCENT_PACKAGE_NAME, 0);
			return info != null;
		}
		catch (PackageManager.NameNotFoundException e)
		{
			e.printStackTrace();
			return false;
		}
	}

	public static void donateWeixin(Activity context,int ResID,String qrPath) throws ActivityNotFoundException
	{
		InputStream weixinQrIs = context.getResources().openRawResource(ResID);
		WeiXinDonate.saveDonateQrImage2SDCard(qrPath, BitmapFactory.decodeStream(weixinQrIs));
		WeiXinDonate.donateViaWeiXin(context, qrPath);
	}

}
