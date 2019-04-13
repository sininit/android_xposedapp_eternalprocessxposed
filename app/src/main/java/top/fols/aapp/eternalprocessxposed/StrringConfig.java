package top.fols.aapp.eternalprocessxposed;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.util.Log;
import java.io.File;
import java.util.Map;
import top.fols.aapp.utils.xposed.SharedPreferencesUtils;
import top.fols.box.util.HashMapUtils9;

public class StrringConfig {
	public static int getVersion() {
		Log.i("fake", "getVersion");
		return -1;
	}

	final static public int 
	version = 21;

	final static public Object 
	sync = new Object();

	final static public lockMessage[] 
	nullLockMessage = new lockMessage[0];

	public static lockMessage[] getPackageList(File file) {
		synchronized (sync) {
			if (file == null)
				return nullLockMessage;
			Map<String,lockMessage> list = new HashMapUtils9<>();
			try {
				String[] names = file.list();
				if (names != null)
					for (String name:names) {
						if (name == null)
							continue;
						lockMessage lock = new lockMessage();
						if (name.endsWith("+")) {
							name = name.substring(0, name.length() - 1);
							lock.packageName = name;
							lock.islock = true;
							lock.iseternal = true;
						} else {
							lock.packageName = name;
							lock.islock = true;
							lock.iseternal = false;
						}
						if (!lock.isEternal() && list.containsKey(name) && list.get(name).isEternal())
							continue;
						list.put(name, lock);
					}
				lockMessage[] packages = list.values().toArray(new lockMessage[list.size()]);
				list.clear();
				return packages;
			} catch (Throwable e) {
				return nullLockMessage;
			}
		}
	}


	public static void addPackageName(File file, lockMessage... strArray) {
		synchronized (sync) {
			if (strArray == null || strArray.length == 0)
				return;

			try {
				for (lockMessage lock:strArray) {

					if (lock.packageName == null || !lock.isLock())
						continue;

					File f;
					if (lock.isEternal())
						(f = new File(file, lock.packageName + "+")).createNewFile();
					else
						(f = new File(file, lock.packageName)).createNewFile();
				}
			} catch (Throwable e) {
				updatePackageList();
				return;
			}
			updatePackageList();
		}
	}
	public static void removePackageList(File file, lockMessage... strArray) {
		synchronized (sync) {
			try {
				for (lockMessage lock:strArray) {
					if (lock.packageName == null)
						continue;
					if (lock.isEternal())
						new File(file, lock.packageName + "+").delete();
					else
						new File(file, lock.packageName).delete();
				}
			} catch (Throwable e) {
				updatePackageList();
				return;
			}
			updatePackageList();
		}
	}
	public static void clearPackageList(File file) {
		synchronized (sync) {
			File[] fs = file.listFiles();
			if (fs != null)
				for (File f:fs)
					f.delete();
			updatePackageList();
		}
	}
	public static boolean updatePackageList() {
		/*
		 * hook this method
		 */
		Log.i("fake", "updatePackageList");
		return true;
	}


	public static final String
	android_PackageName = "android";

	public static final String
	ThisPackageName = "top.fols.aapp.eternalprocessxposed";

	public static final String 
	packageListKey = "packagename.list";

	public static final String 
	semiEternalPackageListFileName = "locked_semi_eternal";






	public static File 
	getPackageNameListXSharedPreferencesDirFile = null;
	public static File getXSharedPreferencesFile(String packageName, String fileName) {
		return new File(SharedPreferencesUtils.getXSharedPreferencesFilePath(packageName, fileName));
	}
	public static File getPackageNameListXSharedPreferencesDirFile() {
		if (getPackageNameListXSharedPreferencesDirFile == null)
			getPackageNameListXSharedPreferencesDirFile = getXSharedPreferencesFile(ThisPackageName, semiEternalPackageListFileName);
		return getPackageNameListXSharedPreferencesDirFile;
	}
	public static File getSharedPreferencesFile(Context context, String fileName) {
		File f = SharedPreferencesUtils.getFilePath(context, fileName);
		return f;
	}
	public static File getSharedPreferencesFileAndUpdatePermissions(Context context, String fileName) {
		File f = new File(SharedPreferencesUtils.getFilePathAndUpdatePermissions(context, fileName));
		return f;
	}
	public static File getPackageNameListSharedPreferencesDirFileAndUpdatePermissions(Context context) {
		File f = new File(SharedPreferencesUtils.getDirFilePathAndUpdatePermissions(context, semiEternalPackageListFileName));
		return f;
	}






	static public class lockMessage {
		public static final Map<String,lockMessage> toMap(lockMessage...arr) {
			Map<String,lockMessage> hash = new HashMapUtils9<>();
			if (arr == null || arr.length == 0)
				return hash;
			for (lockMessage m:arr)
				if (m == null)
					continue;
				else
					hash.put(m.packageName, m);
			return hash;
		}
		public String packageName;
		public boolean islock = false;//0为永生锁 1为半永生 -1无锁
		public boolean iseternal = false;

		public boolean isLock() {
			return islock || iseternal;
		}
		public boolean isEternal() {
			return iseternal;
		}
		public lockMessage(String packageName) {
			this.packageName = packageName;
		}
		public lockMessage() {
			super();
		}
	}
	public static boolean isPersistentApp(ApplicationInfo applicationInfo) {
		return applicationInfo != null && ((applicationInfo.flags & ApplicationInfo.FLAG_PERSISTENT) != 0);
	}
	public static boolean isSystemApp(ApplicationInfo applicationInfo) {
		return applicationInfo != null && ((applicationInfo.flags & applicationInfo.FLAG_SYSTEM) > 0);
	}
}
