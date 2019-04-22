package top.fols.aapp.eternalprocessxposed;

import android.content.pm.ApplicationInfo;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;
import top.fols.aapp.eternalprocessxposed.Config;
import top.fols.box.lang.XObject;
import top.fols.box.util.XExceptionTool;

public class Hook implements IXposedHookLoadPackage {

//	private static packageCache pc = new packageCache();
	private static class packageCache {
//		private static enum type {
//			eternalLock,
//			lock,
//			noExist,
//		}
//		private Map<String,type> hash = new HashMapUtils9<>();
//		public type getType(String packageName) {
//			type t =  hash.get(packageName);
//			if (t == null) {
//				boolean lock = checkLock(packageName);
//				boolean lockEternal = checkEternal(packageName);
//				if (lockEternal)
//					t = type.eternalLock;
//				else if (lock)
//					t = type.lock;
//				else
//					t = type.noExist;
//				hash.put(packageName, t);
//			}
//			return t;
//		}
//		public void clearCache() {
//			hash.clear();
//		}
//		public boolean isLock(String packageName) {
//			return getType(packageName) == type.lock || getType(packageName) == type.eternalLock;
//		}
//		public boolean isEternalLock(String packageName) {
//			return getType(packageName) == type.eternalLock;
//		}
//		public boolean isNoLock(String packageName) {
//			return getType(packageName) == type.noExist;
//		}
//
//
//


		private static boolean checkLock(String packageName) {
			return new File(Config.getPackageNameListXSharedPreferencesDirFile(), packageName).exists();
		}
		private static boolean checkEternal(String packageName) {
			return new File(Config.getPackageNameListXSharedPreferencesDirFile(), packageName + '+').exists();
		}

//		
//		
//		@Override
//		public String toString() {
//			// TODO: Implement this method
//			return hash.toString();
//		}
//		public String superToString() {
//			return super.toString();
//		}
	}

	private static final XObject<Integer> ProcessList_PERSISTENT_PROC_ADJ = new XObject<>(0);;
	private static final XObject<Integer> ProcessList_DEFAULT_ADJ = new XObject<>(0);
	private static boolean defaultSet(
		XC_MethodHook.MethodHookParam hookMethodParam,
		ApplicationInfo applicationInfoInstance,
		Object processRecordInstance
	) {
		boolean lock = packageCache.checkLock(applicationInfoInstance.packageName);
		boolean lockEternal = packageCache.checkEternal(applicationInfoInstance.packageName);

		log("newProc:" + applicationInfoInstance.packageName + 
			", issystemapp=" + Config.isSystemApp(applicationInfoInstance));

		boolean existLock = lock || lockEternal;
		if (!existLock) {
			return false;
		}

		int adj;
		if (lockEternal) {
			adj = ProcessList_PERSISTENT_PROC_ADJ.get().intValue();
			setFieldValue(processRecordInstance, "persistent", true);
		} else {
			adj = ProcessList_DEFAULT_ADJ.get().intValue();
		}

		try {
			setFieldValue(processRecordInstance, "reportLowMemory", false);
		} catch (Exception e) {
			log(e);
		}

		try {
			setFieldValue(processRecordInstance, "curRawAdj", adj);
		} catch (Exception e) {
			log(e);
		}
		try {
			setFieldValue(processRecordInstance, "setRawAdj", adj);
		} catch (Exception e) {
			log(e);
		}

		try {
			setFieldValue(processRecordInstance, "setAdj", adj);
		} catch (Exception e) {
			log(e);
		}
		try {
			setFieldValue(processRecordInstance, "curAdj", adj);
		} catch (Exception e) {
			log(e);
		}
		try {
			setFieldValue(processRecordInstance, "maxAdj", adj);
		} catch (Exception e) {
			log(e);
		}

		String setMessage = "defaultSet";
		log("addLocked:" + applicationInfoInstance.packageName + 
			", issystemapp=" + Config.isSystemApp(applicationInfoInstance) + 
			", eternallock=" + lockEternal + 
			", appname=" + applicationInfoInstance.name + 
			", adj=" + adj + 
			", setmessage=" + setMessage);
		return existLock;

	}






	public static boolean arrayContentsEquals(Object[] a1, Object[] a2) {
        if (a1 == null)
            return a2 == null || a2.length == 0;
        if (a2 == null)
            return a1.length == 0;
        if (a1.length != a2.length)
            return false;
        for (int i = 0; i < a1.length; i++)
            if (a1[i] != a2[i])
                return false;
        return true;
    }
	public static Set<XC_MethodHook.Unhook> getMethods(Class cls, String name) {
		Set<XC_MethodHook.Unhook> hookAllMethods = XposedBridge.hookAllMethods(cls, name, new XC_MethodHook(){});
		return hookAllMethods;
	}
	public static Method getMethod(Set<XC_MethodHook.Unhook> hookAllMethods, Class[] clss) {
		if (hookAllMethods == null || clss == null)
			return null;
		Method method0 = null;
		for (XC_MethodHook.Unhook un:hookAllMethods) {
			Method method = (Method) un.getHookedMethod();
			if (arrayContentsEquals(method.getParameterTypes(), clss)) {
				method0 = method;
				method0.setAccessible(true);
				break;
			}
		}
		return method0;
	}
	public static Method getMethodCheck(Class cls, String MethodName, Set<XC_MethodHook.Unhook> hookAllMethods, Class[] clss) {
		if (hookAllMethods == null || clss == null)
			return null;
		Method method0 = null;
		StringBuilder buildr = new StringBuilder();
		for (XC_MethodHook.Unhook un:hookAllMethods) {
			Method method = (Method) un.getHookedMethod();
			if (arrayContentsEquals(method.getParameterTypes(), clss)) {
				method0 = method;
				method0.setAccessible(true);
				break;
			} else {
				buildr.append(method.toString()).append('\n');
			}
		}
		if (method0 == null) {
			String error = buildr.toString();
			buildr.delete(0, buildr.length());
			throw new RuntimeException("not found " + (cls == null ?null: cls.getCanonicalName()) + '.' + MethodName + Arrays.toString(clss) + "\nall method:\n" + error);
		}
		return method0;
	}
	public static Method getMethod(Class cls, String MethodName, Class[] clss) {
		Method method0 = getMethodCheck(cls, MethodName, getMethods(cls, MethodName), clss);
		return method0;
	}











	public static void initAdjField(XC_LoadPackage.LoadPackageParam lpparam) {
		try {
			if (Config.android_PackageName.equals(lpparam.packageName)) {
				Class<?> ProcessListClass = XposedHelpers.findClass("com.android.server.am.ProcessList", lpparam.classLoader);

				ProcessList_DEFAULT_ADJ.set(((Integer)getStaticFieldValue(ProcessListClass, "FOREGROUND_APP_ADJ", 0)).intValue());
				ProcessList_PERSISTENT_PROC_ADJ.set(((Integer)getStaticFieldValue(ProcessListClass, "PERSISTENT_PROC_ADJ", ProcessList_DEFAULT_ADJ.get())).intValue());
			}
		} catch (Throwable e) {
			log(e);
		}
	}

	@Override
	public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
		// TODO: Implement this method
		try {
			if (Config.ThisPackageName.equals(lpparam.packageName)) {
				/*
				 * module state hook
				 */
				// don't use YourActivity.class here
				XposedHelpers.findAndHookMethod(Config.ThisPackageName + "." + Config.thisClassSimpleName, lpparam.classLoader, "getVersion",
					new XC_MethodHook() {
						@Override
						protected void afterHookedMethod(MethodHookParam param) throws Throwable {
							param.setResult(Config.version);
						}
					});
			}
		} catch (Throwable e) {
			log(e);
		}

		/*
		 * hook ProcessRecord !
		 */
		try {
			if (Config.android_PackageName.equals(lpparam.packageName)) {
				initAdjField(lpparam);

				Class<?> ActivityManagerServiceClass = XposedHelpers.findClass("com.android.server.am.ActivityManagerService", lpparam.classLoader);
				final Class<?> ProcessRecordClass = XposedHelpers.findClass("com.android.server.am.ProcessRecord", lpparam.classLoader);
				
				/*
				 * @return is ProcessRecord class Instance
				 */
				XposedBridge.hookAllMethods(ActivityManagerServiceClass, "updateLruProcessLocked",
					new XC_MethodHook()
					{
						@Override
						protected void beforeHookedMethod(MethodHookParam param) throws Throwable {

							try {
								
								Object ProcessRecord = findParamObject(param.args,ProcessRecordClass);
								if (ProcessRecord == null)
									return;
//								XSharedPreferences xsp;
//								xsp = new XSharedPreferences(StrringConfig.ThisPackageName, "a");
//								xsp.makeWorldReadable();
//								xsp.reload();
//								log(xsp.getString("v", "nul"));

								ApplicationInfo applicationInfo = (ApplicationInfo) getFieldValue(ProcessRecord, "info");
								String packageName = applicationInfo.packageName;
								if (Config.android_PackageName.equals(packageName))
									return;
								defaultSet(param, applicationInfo, ProcessRecord);
							} catch (Throwable e) {
								log(e);
							}

						}
					});
				log("hook init complete.");	
			}
		} catch (Throwable e) {
			log(e);
		}
	}

	private static void setFieldValue(Object obj, String name, Object value) {
		try {
			Field field = obj.getClass().getDeclaredField(name);
			field.setAccessible(true);
			field.set(obj, value);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}
	private static Object getFieldValue(Object obj, String name) {
		try {
			Field field = obj.getClass().getDeclaredField(name);
			field.setAccessible(true);
			return field.get(obj);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}
	private static Object getStaticFieldValue(Class cls, String name, Object defValue) {
		Field field;
		try {
			field = XposedHelpers.findField(cls, name);
		} catch (Throwable e) {
			return defValue;
		}
		try {
			return field.get(null);
		} catch (IllegalAccessException e) {
			return defValue;
		} catch (IllegalArgumentException e) {
			return defValue;
		}
	}
	private static Object findParamObject(Object[]args, Class objType) {
		if (args != null)
			for (Object obj:args)
				if (obj != null)
					if (obj.getClass() == objType)
						return obj;
		throw new RuntimeException("not found processRecord object");
	}



	public static void log(Object obj) {
		String Tag = Config.ThisPackageName;

		if (obj instanceof Throwable) {
			obj = XExceptionTool.StackTraceToString((Throwable)obj);
		}
		String content = new StringBuilder()
			.append('[').append(Tag).append(']').append(' ')
			.append(obj)
			.toString();
		XposedBridge.log(content);
		content = null;
	}
}
