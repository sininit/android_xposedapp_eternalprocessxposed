package top.fols.aapp.utils.xposed;

import android.content.Context;
import de.robv.android.xposed.XSharedPreferences;
import java.io.File;
import java.io.IOException;
public class SharedPreferencesUtils {
	public static final String shared_prefsDirFileName = "shared_prefs";
	public static String getFilePathAndUpdatePermissions(Context context, String filename) {
		return newFile(context, filename).getAbsolutePath();
	}
	public static String getDirFilePathAndUpdatePermissions(Context context, String filename) {
		return newDirFile(context, filename).getAbsolutePath();
	}


	public static File getFilePath(Context context, String filename) {
		File file = new File(context.getApplicationInfo().dataDir);
		File file2 = new File(file, shared_prefsDirFileName);
		if (!file.exists())
			file.mkdirs();
		if (!file2.exists())
			file2.mkdirs();
		file.setReadable(true, false);
		file.setExecutable(true, false);
		file2.setReadable(true, false);
		file2.setExecutable(true, false);
		return new File(file2, filename);
	}
	public static File newFile(Context context, String filename) {
		return newFile(getFilePath(context, filename));
	}
	public static File newFile(File file) {
		try {
			if (!file.exists())
				file.createNewFile();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		file.setReadable(true, false);
		file.setExecutable(true, false);
		return file;
	}

	public static File newDirFile(Context context, String filename) {
		return newDirFile(getFilePath(context, filename));
	}
	public static File newDirFile(File file) {
		if (!file.exists())
			file.mkdirs();
		file.setReadable(true, false);
		file.setExecutable(true, false);
		return file;
	}





	public static String getXSharedPreferencesFilePath(String packageName, String filename) {
		if (packageName == null || filename == null)
			return null;
		File file;
		file = new XSharedPreferences(packageName, filename).getFile();
		file = file.getParentFile();
		if (file == null)
			return null;
		return new File(file, filename).getAbsolutePath();
	}
}
