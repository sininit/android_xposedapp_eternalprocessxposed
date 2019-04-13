package top.fols.aapp.utils;
import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.Toast;
import java.util.List;
import top.fols.box.util.ArrayListUtils;
import top.fols.box.util.XExceptionTool;
import top.fols.box.util.XObjects;

public class XStackUtils {
	private static XStackUtils def;
	public static XStackUtils defStack() {
		return def == null ?def = new XStackUtils(): def;
	}
	private static XUIHandler xui;
	public static XUIHandler defHander() {
		return xui == null ?xui = new XUIHandler(): xui;
	}
	public static abstract class DefStackInterface extends Interface {
		public DefStackInterface() {
			super.setXStackUthis(defStack());
			super.setXUIHander(defHander());
		}
		
		@Override
		public XStackUtils getXStackUthis() {
			// TODO: Implement this method
			XStackUtils xsu = super.getXStackUthis();
			if (xsu == null)
				super.setXStackUthis(xsu = defStack());
			return xsu;
		}
		@Override
		public XUIHandler getXUIHander() {
			// TODO: Implement this method
			XUIHandler xui = super.getXUIHander();
			if (xui == null)
				super.setXUIHander(xui = defHander());
			return xui;
		}
	}




	public static abstract class Interface extends Activity {
		private XStackUtils xstackutils;
		public void setXStackUthis(XStackUtils a) {
			this.xstackutils = XObjects.requireNonNull(a);
		}
		public XStackUtils getXStackUthis() {
			return this.xstackutils;
		}

		private XUIHandler xuihander;
		public XUIHandler getXUIHander() {
			return this.xuihander;
		}
		public void setXUIHander(XUIHandler hander) {
			this.xuihander = XObjects.requireNonNull(hander);
		}
		@Override
		public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
			// TODO: Implement this method
			if (this.getXStackUthis() != null)
				this.getXStackUthis().add(this);
			if (this.getXUIHander() == null)
				this.setXUIHander(new XUIHandler());
			super.onCreate(savedInstanceState, persistentState);
		}
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			// TODO: Implement this method
			if (this.getXStackUthis() != null)
				this.getXStackUthis().add(this);
			if (this.getXUIHander() == null)
				this.setXUIHander(new XUIHandler());
			super.onCreate(savedInstanceState);
		}
		@Override
		protected void onResume() {
			// TODO: Implement this method
			if (this.getXStackUthis() != null)
				this.getXStackUthis().add(this);
			if (this.getXUIHander() == null)
				this.setXUIHander(new XUIHandler());
			super.onResume();
		}
	}



	private final Object sync = new Object();
	private final ArrayListUtils<Activity> list = new ArrayListUtils<>();
	public boolean equalsTop(Activity activity) {
		Activity top;
		if ((top = getTop()) == null) 
			return activity == null;
		else 
			return top.equals(activity);
	}
	public boolean isEmpty() {
		synchronized (sync) {
			return list.isEmpty();
		}
	}
	public int size() {
		synchronized (sync) {
			return list.size();
		}
	}
	public boolean remove(Activity p1) {
		synchronized (sync) {
			return list.remove(p1);
		}
	}
	public void clear() {
		synchronized (sync) {
			list.clear();
		}
	}
	public List<Activity> list() {
		synchronized (sync) {
			return list;
		}
	}
	public void add(Activity activity) {
		synchronized (sync) {
			int index = list.indexOf(activity);
			if (index != -1)
				list.remove(index);
			list.add(activity);
		}
	}
	public void finishAllAndClear() {
		synchronized (sync) {
			for (Activity activity:list)
				if (activity == null)
					continue;
				else
					activity.finish();
			list.clear();
		}
	}
	public Activity getTop() {
		synchronized (sync) {
			if (list.size() == 0)
				return null;
			return list.get(list.size() - 1);
		}
	}
	public void finishAndRemove(Activity activity) {
		synchronized (sync) {
			if (activity == null)
				return;
			activity.finish();
			list.remove(activity);
		}
	}
	public void finishTopAndRemove() {
		synchronized (sync) {
			Activity top = getTop();
			if (top == null)
				return;
			top.finish();
			list.remove(top);
		}
	}
	public void exit() {
		finishAllAndClear();
		android.os.Process.killProcess(android.os.Process.myPid());
		System.exit(0);
	}




	private void checkStackNull() {
		synchronized (sync) {
			if (list.isEmpty())
				throw new NullPointerException("null ntack");
		}
	}



	public Context getTopContext() {
		checkStackNull();
		return getTop().getApplicationContext();
	}

	public void toast(Throwable text) {
		checkStackNull();
		Toast.makeText(getTopContext(), "" + XExceptionTool.StackTraceToString(text), Toast.LENGTH_LONG).show();
	}
	public void toast(Object text) {
		checkStackNull();
		Toast.makeText(getTopContext(), "" + text, Toast.LENGTH_LONG).show();
	}
	public void copy(CharSequence content) {  
		checkStackNull();
		Context context = getTop();
		ClipboardManager cmb = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);  
		cmb.setText(content == null ?"": content);  
	}
	public String getString(int id) {
		return getString(null, id);
	}
	public String getString(Context context, int id) {
		if (context != null)
			return context. getString(id);
		checkStackNull();
		Context top = getTop();
		return top.getString(id);
	}

}

