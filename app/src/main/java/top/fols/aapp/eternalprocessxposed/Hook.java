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
import top.fols.aapp.eternalprocessxposed.StrringConfig;
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
			return new File(StrringConfig.getPackageNameListXSharedPreferencesDirFile(), packageName).exists();
		}
		private static boolean checkEternal(String packageName) {
			return new File(StrringConfig.getPackageNameListXSharedPreferencesDirFile(), packageName + '+').exists();
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
	private static final XObject<Integer> DEFAULT_ADJ = new XObject<>(0);
	private static final XObject<Boolean> init = new XObject<>(false);
	private static boolean defaultSet(ApplicationInfo info,
									  Object processRecord) {

		boolean lock = packageCache.checkLock(info.packageName);
		boolean lockEternal = packageCache.checkEternal(info.packageName);
		int Adj;

		if (lockEternal)
			Adj = ProcessList_PERSISTENT_PROC_ADJ.get().intValue();
		else
			Adj = DEFAULT_ADJ.get().intValue();
		return directSet(info,
						 processRecord,
						 lock,
						 lockEternal,
						 Adj,

						 "defaultSet"
						 );
	}





	private static boolean directSet(ApplicationInfo info, 
									 Object processRecord,

									 boolean lock, 
									 boolean eternalLock,

									 int setMaxAdj,

									 String setMessage
									 ) {
		log("newProc:" + info.packageName + ", issystemapp=" + StrringConfig.isSystemApp(info));

		boolean existLock = lock || eternalLock;
		if (!existLock) {
			return false;
		}

		setMaxAdj(processRecord, setMaxAdj);
		if (eternalLock) {
			setPersistent(processRecord, true);
		}
		log("addLocked:" + info.packageName + ", issystemapp=" + StrringConfig.isSystemApp(info) + ", eternallock=" + eternalLock + ", appname=" + info.name + ", adj=" + setMaxAdj + ", setmessage=" + setMessage);
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


	private static void setPersistent(Object processRecord, boolean isPersistent) {
		/*为真时 App被强制结束了 会立刻重新启动*/
		XposedHelpers.setBooleanField(processRecord, "persistent", isPersistent);
	}


	private static void setMaxAdj(Object processRecord, int maxAdj) {
		/*越低越不容易被杀*/
		/*此过程的最大OOM调整*/
		XposedHelpers.setIntField(processRecord, "maxAdj", maxAdj);
	}







	/*
	 * updateOomAdjLocked (ProcessRecord)
	 */
	private static final XObject<Method> updateOomAdjLocked = new XObject<>(null);
	/*
	 * Android 8.0 or above
	 * updateOomAdjLocked (ProcessRecord,boolean)
	 */
	private static final XObject<Method> updateOomAdjLocked2 = new XObject<>(null);
	public static void updateOomAdjLocked(Object asmObj,Object ProcessRecord) throws IllegalArgumentException {
		try {
			Method updateOomAdjLockedMethod = updateOomAdjLocked.get();
			Method updateOomAdjLocked2Method = updateOomAdjLocked2.get();
			
			if (updateOomAdjLockedMethod != null) {
				updateOomAdjLockedMethod.invoke(asmObj,ProcessRecord);
			} else {
				updateOomAdjLocked2Method.invoke(asmObj,ProcessRecord, true);
			}
		} catch (Throwable e) {
			log(e);
		}
	}




	public static void initUpdateOomAdjLocked(XC_LoadPackage.LoadPackageParam lpparam) {
		try {
			if (StrringConfig.android_PackageName.equals(lpparam.packageName)) {
				if (updateOomAdjLocked.get() == null && updateOomAdjLocked2.get() == null) {
					final Class ActivityManagerServiceClass = XposedHelpers.findClass("com.android.server.am.ActivityManagerService", lpparam.classLoader);
					final Class<?> ProcessRecordClass = XposedHelpers.findClass("com.android.server.am.ProcessRecord", lpparam.classLoader);
					
					/*
					 * Update OomAdj for a specific process.
					 * @param app The process to update
					 * @param oomAdjAll If it's ok to call updateOomAdjLocked() for all running apps
					 *                  if necessary, or skip.
					 * @return whether updateOomAdjLocked(app) was successful.
					 * final boolean updateOomAdjLocked(ProcessRecord app, boolean oomAdjAll) {
					 *	 
					 * }
					 */
					try {
						updateOomAdjLocked .set(getMethod(ActivityManagerServiceClass, "updateOomAdjLocked", new Class[]{ProcessRecordClass}));
					} catch (Throwable e) {
						updateOomAdjLocked2.set(getMethod(ActivityManagerServiceClass, "updateOomAdjLocked", new Class[]{ProcessRecordClass,boolean.class}));
					}

				}
			}
		} catch (Throwable e) {
			log(e);
		}
	}
	public static void initAdjField(XC_LoadPackage.LoadPackageParam lpparam) {
		try {
			if (StrringConfig.android_PackageName.equals(lpparam.packageName)) {
				Class<?> ProcessListClass = XposedHelpers.findClass("com.android.server.am.ProcessList", lpparam.classLoader);
				
				DEFAULT_ADJ.set(((Integer)getStaticFieldValue(ProcessListClass, "FOREGROUND_APP_ADJ", 0)).intValue());
				ProcessList_PERSISTENT_PROC_ADJ.set(((Integer)getStaticFieldValue(ProcessListClass, "PERSISTENT_PROC_ADJ", DEFAULT_ADJ.get())).intValue());
			}
		} catch (Throwable e) {
			log(e);
		}
	}





	private static final Class ApplicationInfoCls = ApplicationInfo.class;
	@Override
	public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
		// TODO: Implement this method


		try {
			if (StrringConfig.ThisPackageName.equals(lpparam.packageName)) {
				/*
				 * module state hook
				 */
				// don't use YourActivity.class here
				XposedHelpers.findAndHookMethod(StrringConfig.ThisPackageName + ".StrringConfig", lpparam.classLoader, "getVersion",
					new XC_MethodHook() {
						@Override
						protected void afterHookedMethod(MethodHookParam param) throws Throwable {
							param.setResult(StrringConfig.version);
						}
					});
			}
		} catch (Throwable e) {
			log(e);
		}




		/*
		 * hook ActivityManagerService !
		 */
		try {
			if (StrringConfig.android_PackageName.equals(lpparam.packageName)) {
				if (!init.get().booleanValue()) {
					init.set(true);
					
					initAdjField(lpparam);
					initUpdateOomAdjLocked(lpparam);
					
					Class<?> ActivityManagerServiceClass = XposedHelpers.findClass("com.android.server.am.ActivityManagerService", lpparam.classLoader);
					/*
					 * @return is ProcessRecord class Instance
					 */
					XposedBridge.hookAllMethods(ActivityManagerServiceClass, "newProcessRecordLocked",
						new XC_MethodHook()
						{
							@Override
							protected void afterHookedMethod(MethodHookParam param) throws Throwable {

//								XSharedPreferences xsp;
//								xsp = new XSharedPreferences(StrringConfig.ThisPackageName, "a");
//								xsp.makeWorldReadable();
//								xsp.reload();
//								log(xsp.getString("v", "nul"));

								ApplicationInfo applicationInfo = (ApplicationInfo) findObject(ApplicationInfoCls, param.args);
								Object ProcessRecord = param.getResult();
								String packageName = applicationInfo.packageName;
								if (StrringConfig.android_PackageName.equals(packageName))
									return;

								if (defaultSet(applicationInfo, ProcessRecord)) {
									updateOomAdjLocked(param.thisObject,ProcessRecord);
								}
							}
						});

					log("hook activity manager service complete.");	
				}
			}


		} catch (Throwable e) {
			log(e);
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
	private static Object findObject(Class objType, Object[]args) {
		if (args != null)
			for (Object obj:args)
				if (obj != null)
					if (obj.getClass() == objType)
						return obj;
		throw new RuntimeException("not found processRecord object");
	}



	public static void log(Object obj) {
		String Tag = StrringConfig.ThisPackageName;
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
