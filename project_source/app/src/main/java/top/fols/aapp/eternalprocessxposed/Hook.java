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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import top.fols.aapp.eternalprocessxposed.Config;
import top.fols.box.lang.XObject;
import top.fols.box.lang.reflect.optdeclared.XReflectAccessible;
import top.fols.box.lang.reflect.optdeclared.XReflectMatcher;
import top.fols.box.lang.reflect.optdeclared.XReflectQuick;
import top.fols.box.util.XArrays;
import top.fols.box.util.XExceptionTool;
import top.fols.aapp.utils.XTool;
import top.fols.aapp.eternalprocessxposed.activity.MainActivity;

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

	private static final XObject<Integer> ProcessList_PERSISTENT_SERVICE_ADJ = new XObject<>(0);;
	private static final XObject<Integer> ProcessList_DEFAULT_ADJ = new XObject<>(0);


	public static Method[] getMethods(Class cls, String name) {
		Set<XC_MethodHook.Unhook> hookAllMethods = XposedBridge.hookAllMethods(cls, name, new XC_MethodHook(){});
		List<Method> ms = new ArrayList<>();
		for (XC_MethodHook.Unhook un:hookAllMethods) {
			Method method = (Method) un.getHookedMethod();
			if (!method.getName().equals(name)) {
				un.unhook();
				continue;
			}
			ms.add(method);
			un.unhook();
		}
		return ms.toArray(XReflectAccessible.setAccessibleTrue(new Method[ms.size()]));
	}
	public static Method getMethodCheck(Class cls, String MethodName, Method[] allMethods, Class[] clss) {
		if (allMethods == null || clss == null)
			return null;
		Method method0 = null;
		StringBuilder buildr = new StringBuilder();
		for (Method un:getMethods(cls, MethodName)) {
			Method method = un;
			if (XArrays.arrayContentsEquals(method.getParameterTypes(), clss)) {
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




	public static void setOomAdj(Object ProcessRecordObj, int newAdj) {
		try {
			int pid = getFieldValue(ProcessRecordObj, "pid");
			int adj = newAdj;
			try {
				XReflectMatcher.defaultInstance.execMethod(ProcessListClass.get(),
														   "setOomAdj",
														   pid, adj);
			} catch (Throwable e) {
				try {
					int uid  = ((ApplicationInfo) getFieldValue(ProcessRecordObj, "info")).uid;
					XReflectMatcher.defaultInstance.execMethod(ProcessListClass.get(),
															   "setOomAdj", 
															   pid, uid, adj);
				} catch (Throwable e2) {
					log(e2);
				}
			}
		} catch (Throwable e) {
			log(e);
		}

	}










	private static final XObject<Class<?>> ProcessListClass = new XObject<>();;
	public static void initHook(XC_LoadPackage.LoadPackageParam lpparam) {
		try {
			if (Config.android_PackageName.equals(lpparam.packageName)) {
				Hook.ProcessListClass.set(XposedHelpers.findClass("com.android.server.am.ProcessList", lpparam.classLoader));
				// This is the process running the current foreground app.  We'd really
				// rather not kill it!
				ProcessList_DEFAULT_ADJ.set(((Integer) getStaticFieldValue(Hook.ProcessListClass.get(), "FOREGROUND_APP_ADJ", 0)).intValue());

				// This is a process that the system or a persistent process has bound to,
				// and indicated it is important.
				ProcessList_PERSISTENT_SERVICE_ADJ.set(((Integer) getStaticFieldValue(Hook.ProcessListClass.get(), "FOREGROUND_APP_ADJ", ProcessList_DEFAULT_ADJ.get())).intValue());
			}
		} catch (Throwable e) {
			log(e);
		}
	}



	/*
	 * AMS
	 * updateOomAdjLocked：更新adj，当目标进程为空，或者被杀则返回false；否则返回true;
	 * computeOomAdjLocked：计算adj，返回计算后RawAdj值;
	 * applyOomAdjLocked：应用adj，当需要杀掉目标进程则返回false；否则返回true。
	 * 
	 * 要想提高进程优先级，尽量避免自己被杀,那就得提高进程的oom_score_adj ；
	 * 在activity的创建与启动,结束; 
	 * service的创建与启动,结束等场景下，调用
	 * AMS.applyOomAdjLocked ==> Process.setOomAdj ==> 修改/proc/pid/oom_adj 。
	 */
	@Override
	public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
		// TODO: Implement this method

		try {
			if (Config.ThisPackageName.equals(lpparam.packageName)) {
				/*
				 * module state hook
				 */
				// don't use YourActivity.class here
				XposedHelpers.findAndHookMethod(
					Config.ThisPackageName + "." + Config.thisClassSimpleName, lpparam.classLoader,
					"getVersion",

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
		


//			XSharedPreferences xsp;
//			xsp = new XSharedPreferences(StrringConfig.ThisPackageName, "a");
//			xsp.makeWorldReadable();
//			xsp.reload();
//			log(xsp.getString("v", "nul"));

		/*
		 * hook ProcessRecord !
		 */
		try {
			if (Config.android_PackageName.equals(lpparam.packageName)) {
				initHook(lpparam);

				/*
				 * @return is ProcessRecord class Instance
				 */
				final Class<?> ActivityManagerServiceClass = XposedHelpers.findClass("com.android.server.am.ActivityManagerService", lpparam.classLoader);
				XposedBridge.hookAllMethods(ActivityManagerServiceClass, "newProcessRecordLocked",
					new XC_MethodHook() {
						@Override
						protected void afterHookedMethod(MethodHookParam param) throws Throwable {
							Object ProcessRecord = param.getResult();
							if (ProcessRecord == null) return;
							ApplicationInfo applicationInfo = (ApplicationInfo) getFieldValue(ProcessRecord, "info");
							if (Config.android_PackageName.equals(applicationInfo.packageName)) return;
							
							boolean persistent = getFieldValue(ProcessRecord, "persistent");
							if(persistent) return;
							
							boolean lock = packageCache.checkLock(applicationInfo.packageName);
							boolean lockEternal = packageCache.checkEternal(applicationInfo.packageName);
							if (lock || lockEternal) {
								log("newProc:" + applicationInfo.packageName + 
									", issystemapp=" + Config.isSystemApp(applicationInfo));
								
								int adj;
								if (lockEternal) {
									adj = ProcessList_PERSISTENT_SERVICE_ADJ.get().intValue();
									setFieldValue(ProcessRecord, "persistent", true);
								} else {
									adj = ProcessList_DEFAULT_ADJ.get().intValue();
								}
								Hook.setAdj(ProcessRecord,adj);
								String setMessage = "defaultSet";
								log("addLocked:" + applicationInfo.packageName + 
									", issystemapp=" + Config.isSystemApp(applicationInfo) + 
									", eternallock=" + lockEternal + 
									", appname=" + applicationInfo.name + 
									", adj=" + adj + 
									", setmessage=" + setMessage);
							}
						}
					});

				final Class<?> ProcessRecordClass = XposedHelpers.findClass("com.android.server.am.ProcessRecord", lpparam.classLoader);
				XposedBridge.hookAllMethods(ActivityManagerServiceClass, "updateLruProcessLocked",
					new XC_MethodHook() {
						@Override
						protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
							try {
								Object ProcessRecord = findParamObject(param.args, ProcessRecordClass);
								if (ProcessRecord == null) return;
								ApplicationInfo applicationInfo = (ApplicationInfo) getFieldValue(ProcessRecord, "info");
								if (Config.android_PackageName.equals(applicationInfo.packageName)) return;
								
								boolean persistent = getFieldValue(ProcessRecord, "persistent");
								if(persistent) return;
								
								boolean lock = packageCache.checkLock(applicationInfo.packageName);
								boolean lockEternal = packageCache.checkEternal(applicationInfo.packageName);
								if (lock || lockEternal) {
									int adj;
									if (lockEternal) {
										adj = ProcessList_PERSISTENT_SERVICE_ADJ.get().intValue();
									} else {
										adj = ProcessList_DEFAULT_ADJ.get().intValue();
									}
									Hook.setAdj(ProcessRecord,adj);
									try { Hook.setOomAdj(ProcessRecord, adj); } catch (Exception e) { log(e); }
								}
							} catch (Throwable e) {
								log(e);
							}

						}
					});
				log("hook activity manager service complete.");	
			}
		} catch (Throwable e) {
			log(e);
		}
	}
	public static void setAdj(Object ProcessRecord,int adj){
		try { setFieldValue(ProcessRecord, "setAdj", adj); } catch (Exception e) { log(e); }
		try { setFieldValue(ProcessRecord, "maxAdj", adj); } catch (Exception e) { log(e); }
		try { setFieldValue(ProcessRecord, "curAdj", adj); } catch (Exception e) { log(e); }
		
	}





	private static void setFieldValue(Object obj, String name, Object value) throws IllegalAccessException, IllegalArgumentException {
		XReflectQuick.defaultInstance.getField(obj.getClass(), name).set(obj, value);
	}
	private static Object getFieldValue(Object obj, String name) throws IllegalAccessException, IllegalArgumentException {
		return XReflectQuick.defaultInstance.getField(obj.getClass(), name).get(obj);
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
