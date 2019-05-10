package top.fols.aapp.eternalprocessxposed.activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import top.fols.aapp.eternalprocessxposed.Config;
import top.fols.aapp.eternalprocessxposed.Config.lockMessage;
import top.fols.aapp.eternalprocessxposed.R;
import top.fols.aapp.eternalprocessxposed.activity.MainActivity;
import top.fols.aapp.util.XUIHandler;
import top.fols.aapp.utils.WrapList;
import top.fols.aapp.view.alertdialog.SingleSelectAlertDialog;
import top.fols.aapp.view.alertdialog.SingleSelectAlertDialogCallback;
import top.fols.aapp.view.simplelistview.EntryAdapter;
import top.fols.box.util.XMapKeyCheck;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


	public boolean onKeyDown(int i, KeyEvent keyEvent) {
        switch (i) {
            case keyEvent.KEYCODE_BACK:
				android.os.Process.killProcess(android.os.Process.myPid());
				break;
        }
        return false;
    }

	@SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
		int id = item.getItemId();
		switch (id) {
			case R.id.nav_about:
				{
					Intent intent = new Intent();
					intent.setClassName(getApplicationContext(), AboutActivity.class.getCanonicalName());
					startActivity(intent);
				}break;
			case R.id.nav_donation:
				{
					AboutActivity.jz(this);
				}break;
			case R.id.nav_setting:
				{
					Intent intent = new Intent();
					intent.setClassName(getApplicationContext(), SettingActivity.class.getCanonicalName());
					startActivity(intent);
				}break;
			case R.id.nav_help:
				{
					AboutActivity.openGithubPage(this);
				}break;

		}

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
	}






    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
		final MenuItem item = menu.findItem(R.id.search_contact);
		final SearchView mSearchView = (SearchView) MenuItemCompat.getActionView(item);
		mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
			{
				@Override
				public boolean onQueryTextSubmit(String p1) {
					return false;
				}
				@Override
				public boolean onQueryTextChange(String p1) {
					search(p1);
					return true;
				}
			});
        return true;
    }
	public void search(String text) {
		final String txt = text.toLowerCase();
		if (!"".equals(text)) {
			ListFilter<EntryAdapter.Entry> filter = new ListFilter<EntryAdapter.Entry>() {
				@Override
				public boolean filt(EntryAdapter.Entry object) {
					boolean p = object.packageName.toLowerCase().contains(txt);
					boolean n = object.appName.toLowerCase().contains(txt);
					return p || n;
				}
			};
			searchData = filter(data, filter);
			wrapData.setList(searchData);
			sortList();
		} else {
			wrapData.setList(data);
			sortList();
		}
	}
	public <T> List<T> filter(List<T> list, ListFilter<T> filter) {
       	List<T> filtered = new ArrayList<>();
        int len = list.size();
        for (int i = 0; i < len; i++) {
            T obj = list.get(i);
            if (filter.filt(obj)) {
                filtered.add(obj);
            }
        }
        return filtered;
    }
	public interface ListFilter<T> {
		boolean filt(T object);
	}





    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
		if (id == R.id.action_selectAll) {
			for (EntryAdapter.Entry b:adapter.getData())
				b.checked = true;
			adapter.notifyDataSetChanged();
            return true;
        } else if (id == R.id.action_select_invert) {
			for (EntryAdapter.Entry b:adapter.getData())
				b.checked = !b.checked;
			adapter.notifyDataSetChanged();
            return true;
		} else if (id == R.id.action_deselect) {
			for (EntryAdapter.Entry b:adapter.getData())
				b.checked = false;
			adapter.notifyDataSetChanged();
            return true;
		} else if (id == R.id.action_select_systemapp) {
			for (EntryAdapter.Entry b:adapter.getData())
				if (b.infox.isSystemApp)
					b.checked = true;
				else continue;
			adapter.notifyDataSetChanged();
            return true;
		} else if (id == R.id.action_not_select_systemapp) {
			for (EntryAdapter.Entry b:adapter.getData())
				if (b.infox.isSystemApp)
					b.checked = false;
				else continue;
			adapter.notifyDataSetChanged();
            return true;
		} else if (id == R.id.action_select_userapp) {
			for (EntryAdapter.Entry b:adapter.getData())
				if (!b.infox.isSystemApp)
					b.checked = true;
				else continue;
			adapter.notifyDataSetChanged();
            return true;
		} else if (id == R.id.action_not_select_userapp) {
			for (EntryAdapter.Entry b:adapter.getData())
				if (!b.infox.isSystemApp)
					b.checked = false;
				else continue;
			adapter.notifyDataSetChanged();
            return true;
		}  else if (id == R.id.action_select_locked) {
			for (EntryAdapter.Entry b:adapter.getData())
				if (b.infox.isLock())
					b.checked = true;
				else continue;
			adapter.notifyDataSetChanged();
            return true;
		} else if (id == R.id.action_not_select_locked) {
			for (EntryAdapter.Entry b:adapter.getData())
				if (b.infox.isLock())
					b.checked = false;
				else continue;
			adapter.notifyDataSetChanged();
            return true;
		} else if (id == R.id.action_select_ordinary_lock) {
			for (EntryAdapter.Entry b:adapter.getData())
				if (b.infox.isLock() && !b.infox.isEternalLock())
					b.checked = true;
				else continue;
			adapter.notifyDataSetChanged();
            return true;
		} else if (id == R.id.action_not_select_ordinary_lock) {
			for (EntryAdapter.Entry b:adapter.getData())
				if (b.infox.isLock() && !b.infox.isEternalLock())
					b.checked = false;
				else continue;
			adapter.notifyDataSetChanged();
            return true;
		} else if (id == R.id.action_select_eternal_lock) {
			for (EntryAdapter.Entry b:adapter.getData())
				if (b.infox.isLock() && b.infox.isEternalLock())
					b.checked = true;
				else continue;
			adapter.notifyDataSetChanged();
            return true;
		} else if (id == R.id.action_not_select_eternal_lock) {
			for (EntryAdapter.Entry b:adapter.getData())
				if (b.infox.isLock() && b.infox.isEternalLock())
					b.checked = false;
				else continue;
			adapter.notifyDataSetChanged();
            return true;
		} else if (id == R.id.action_select_unknownapp) {
			for (EntryAdapter.Entry b:adapter.getData())
				if (b.infox.isUnknownApp)
					b.checked = true;
				else continue;
			adapter.notifyDataSetChanged();
            return true;
		} else if (id == R.id.action_not_select_unknownapp) {
			for (EntryAdapter.Entry b:adapter.getData())
				if (b.infox.isUnknownApp)
					b.checked = false;
				else continue;
			adapter.notifyDataSetChanged();
            return true;
		}
        return super.onOptionsItemSelected(item);
    }

	@Override
	protected void onResume() {
		// TODO: Implement this method
		super.onResume();
		this.setWorldReadable();
	}


	private ListView mRecyclerView;

	private List<EntryAdapter.Entry> data;
	private List<EntryAdapter.Entry> searchData = new ArrayList<>();

	private WrapList<EntryAdapter.Entry> wrapData;
	private EntryAdapter adapter;

	private Map<String,lockMessage> lockHash = Collections.synchronizedMap(new HashMap<>());

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		SharedPreferences sharedPreferences;
		sharedPreferences = getApplicationContext().getSharedPreferences("a", Context.MODE_WORLD_READABLE + Context.MODE_WORLD_WRITEABLE);
		sharedPreferences.edit()
			.putString("v", "x")
			.commit();
		setWorldReadable();

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
			MainActivity.this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
		drawer.setDrawerListener(toggle);
		toggle.syncState();

		NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
		navigationView.setNavigationItemSelectedListener(MainActivity.this);

		FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
		fab.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#66CCFF")));
		fab.setAlpha(0.7f);
		fab.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View view) {
					String[] items;
					if (isShowEternalLockOptions(MainActivity.this)) {
						items = new String[]{getString(R.string.remove_lock),getString(R.string.add_lock),getString(R.string.add_eternal_lock)};
					}	else {
						items = new String[]{getString(R.string.remove_lock),getString(R.string.add_lock)};
					}
					SingleSelectAlertDialog option;
					option = new SingleSelectAlertDialog();
					option.setContext(MainActivity.this);
					option.setItems(items);
					option.setDirectSelectMode(true);
					option.setCancelable(true);
					option.setCallback(new SingleSelectAlertDialogCallback(){
							@Override
							public void selectComplete(SingleSelectAlertDialog obj, String select, int index, boolean isSelected, boolean isNegativeButton) {
								// TODO: Implement this method
								if (isNegativeButton)
									return;
								if (!isSelected)
									return;
								switch (index) {
									case 0:{
											boolean existEternalLock = false;
											List<EntryAdapter.Entry> bs = adapter.getSelect();
											for (EntryAdapter.Entry b :bs) {
												lockMessage lock = lockHash.get(b.packageName);
												if (lock == null)
													continue;
												if (lock.isEternal())
													existEternalLock = true;
												lock.islock = false;
												lock.iseternal = false;
											}
											clearAndSavePackageList(getApplicationContext(), lockHash.values().toArray(new lockMessage[lockHash.size()]));
											reLoadPackageList();

											Toast.makeText(getApplicationContext(), R.string.restart_app_effective, Toast.LENGTH_LONG).show();
											if (existEternalLock)
												Toast.makeText(getApplicationContext(), R.string.eternallock_need_restart_device_effective, Toast.LENGTH_LONG).show();
										}
										break;
									case 1:{
											List<EntryAdapter.Entry> bs = adapter.getSelect();
											for (EntryAdapter.Entry b :bs) {
												lockMessage lock = lockHash.get(b.infox.packageName);
												if (lock == null)
													lockHash.put(b.infox.packageName, lock = new lockMessage(b.infox.packageName));
												lock.islock = true;
												lock.iseternal = false;
											}
											clearAndSavePackageList(getApplicationContext(), lockHash.values().toArray(new lockMessage[lockHash.size()]));
											reLoadPackageList();

											Toast.makeText(getApplicationContext(), R.string.restart_app_effective, Toast.LENGTH_LONG).show();	
										}
										break;
									case 2:{
											List<EntryAdapter.Entry> bs = adapter.getSelect();
											for (EntryAdapter.Entry b :bs) {
												lockMessage lock = lockHash.get(b.infox.packageName);
												if (lock == null)
													lockHash.put(b.infox.packageName, lock = new lockMessage(b.infox.packageName));
												lock.islock = true;
												lock.iseternal = true;
											}
											clearAndSavePackageList(getApplicationContext(), lockHash.values().toArray(new lockMessage[lockHash.size()]));
											reLoadPackageList();

											Toast.makeText(getApplicationContext(), R.string.restart_app_effective, Toast.LENGTH_LONG).show();	
										}
										break;
								}
							}
						});
					option.show();
				}
			});
		if (Config.getVersion() != -1) {
			if (Config.getVersion() != Config.version)
				((TextView)findViewById(R.id.activitymaincontentTextView1)).setText(R.string.xposed_modules_no_update);
			else 
				((TextView)findViewById(R.id.activitymaincontentTextView1)).setVisibility(View.GONE);
		} 
		final SwipeRefreshLayout swipeRefreshView =((SwipeRefreshLayout)findViewById(R.id.activitymaincontentSwipeRefreshLayout1));
		swipeRefreshView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
				@Override
				public void onRefresh() {
					//swipeRefreshView.setRefreshing(true);
					new Handler().postDelayed(new Runnable()
						{
							@Override
							public void run() {
								reLoadPackageList();
								// 加载完数据设置为不刷新状态，将下拉进度收起来
								swipeRefreshView.setRefreshing(false);
								adapter.notifyDataSetChanged();
							}
						}, 500);
				}
			});
		data = new ArrayList<EntryAdapter.Entry>();
		wrapData = new WrapList<EntryAdapter.Entry>();
		wrapData.setList(data);

		mRecyclerView = (ListView) findViewById(R.id.activitymaincontentListView1);
		adapter = new EntryAdapter(MainActivity.this, wrapData);
		mRecyclerView.setAdapter(adapter);

		final ProgressDialog prodialog;
		prodialog = new ProgressDialog(MainActivity.this);
		prodialog.setMessage(getString(R.string.apploading));
		prodialog.setCancelable(false);
		prodialog.show();

		new Thread(){
			public void run() {
				try {
					sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				runOnUiThread(new Runnable(){
						@Override
						public void run() {
							// TODO: Implement this method
							reLoadPackageList();
							Config.getPackageNameListSharedPreferencesDirFileAndUpdatePermissions(MainActivity.this);
							prodialog.cancel();
						}
					});
			}
		}.start();
	}



	public Drawable getLocksssDrawable() {
		if (locksssDrawable == null)
			locksssDrawable = getResources().getDrawable(R.drawable.lock2);
		return locksssDrawable;
	}
	public Drawable getLockDrawable() {
		if (lockDrawable == null)
			lockDrawable = getResources().getDrawable(R.drawable.lock);
		return lockDrawable;
	}
	public Drawable getUnLockDrawable() {
		if (unlockDrawable == null)
			unlockDrawable = getResources().getDrawable(R.drawable.unlock);
		return unlockDrawable;
	}
	private static Drawable unlockDrawable;
	private static Drawable locksssDrawable;
	private static Drawable lockDrawable;

	public void sortList() {
		List<EntryAdapter.Entry> beans = new ArrayList<>();
		beans.addAll(adapter.getData());
		Collections.sort(beans, new Comparator<EntryAdapter.Entry>()
			{  
				public int compare(EntryAdapter.Entry oldBean, EntryAdapter.Entry newBean) {  
					int oldInt = 0;
					int newInt = 0;

					oldInt += oldBean.infox.isLock() ?1: 0;
					newInt += newBean.infox.isLock() ?1: 0;

					oldInt += oldBean.infox.isEternalLock() ?2: 0;
					newInt += newBean.infox.isEternalLock() ?2: 0;

					if (oldInt < newInt)
						return 1;//newBean大于oldBean
					else if (oldInt == newInt)
						return 0;//oldBean等于newBean
					else
						return -1;//大于
				}  
			});
		for (int i = 0;i < beans.size();i++)
			adapter.setData(i, beans.get(i));
		adapter.notifyDataSetChanged();
	}
	public void reLoadPackageList() {
		lockHash.clear();
		lockHash.putAll(lockMessage.toMap(Config.getPackageList(Config.getPackageNameListSharedPreferencesDirFileAndUpdatePermissions(this))));

		data.clear();
		List<EntryAdapter.Entry> beans = new ArrayList<>();
		List<AppInfo> resolveInfos = getResolveInfos(this,
													 isLoadSystemApp(this), 
													 lockHash.values().toArray(new lockMessage[lockHash.size()]));  
		for (final AppInfo pak: resolveInfos) {
			boolean pt = pak.isLock();
			boolean yh = pak.isEternalLock();
			boolean lock = pt || yh;

			final EntryAdapter.Entry entry = new EntryAdapter.Entry();
			entry.infox = pak;
			entry.appICon = pak.getICon(MainActivity.this);
			entry.stateICon = lock == false ?getUnLockDrawable(): yh ?getLocksssDrawable(): getLockDrawable();
			entry.appName = pak.appName;
			entry.packageName = pak.packageName;
			entry.checked = false;

			entry.onChange = new CompoundButton.OnCheckedChangeListener(){
				@Override
				public void onCheckedChanged(CompoundButton p1, boolean p2) {
					// TODO: Implement this method
					entry.checked = p2;
				}
			};
			beans.add(entry);
		}
		data.addAll(beans);
		adapter.notifyDataSetChanged();

		sortList();
	}






	public class AppInfo {
		public PackageInfo packageInfo;
		public Drawable getICon(Context content) {
			if (packageInfo == null) return null;
			PackageManager pm = content.getPackageManager();
			Drawable drawable = null;
			try { drawable = pm.getApplicationIcon(packageInfo.applicationInfo); } catch (Exception e) { e = null; }
			return drawable;
		}
		public boolean isLock() {
			lockMessage lockmessage = lockHash.get(packageName);
			if (lockmessage == null)
				lockHash.put(packageName, lockmessage = new lockMessage(packageName));
			return lockmessage.isLock();
		}
		public boolean isEternalLock() {
			lockMessage lockmessage = lockHash.get(packageName);
			if (lockmessage == null)
				lockHash.put(packageName, lockmessage = new lockMessage(packageName));
			return lockmessage.isEternal();
		}


		/*应用包名*/
		public String packageName;
		/*应用名*/
		public String appName;

		/*是否为系统应用*/
		public boolean isSystemApp;
		/*不允许操作或者已经卸载掉了*/
		public boolean isUnknownApp;
	}

	public List<AppInfo> getResolveInfos(Context context, boolean loadSystemApp, lockMessage... baseApp) {
		PackageManager pm = context.getPackageManager();
		List<PackageInfo> originPackageList = pm.getInstalledPackages(0);

		Map<String,AppInfo> newList = new HashMap<>();
		// 已经添加锁的应用包名
		XMapKeyCheck<String> basePackageName = new XMapKeyCheck<>();
		if (baseApp != null)
			for (lockMessage lock:baseApp)
				basePackageName.put(lock.packageName);
		for (String packageName: basePackageName.map().keySet()) {
			AppInfo infos = new AppInfo();
			infos.packageInfo = null;
			infos.packageName = packageName;
			infos.appName = getString(R.string.unknown);
			infos.isUnknownApp = true;
			newList.put(packageName, infos);
		}


		for (PackageInfo pak: originPackageList) {
			String appName = pm.getApplicationLabel(pak.applicationInfo).toString();
			String packageName = pak.packageName;
			boolean isSystemApp = Config.isSystemApp(pak.applicationInfo);
			if (isSystemApp) {
				if (!loadSystemApp)
					continue;
				if (Config.isPersistentApp(pak.applicationInfo)) {
					continue;
				}
			}
			AppInfo infos = new AppInfo();
			infos.packageInfo = pak;
			infos.packageName = packageName;
			infos.appName = appName;
			infos.isSystemApp = isSystemApp;
			newList.put(packageName, infos);
		}
		return new ArrayList<AppInfo>(newList.values());
	}












 	private final static Object sync = new Object();
	private final static  lockMessage[] nullLockMessage = new lockMessage[0];
	public void clearPackageList() {
		synchronized (sync) {
			Config.clearPackageList(Config.getPackageNameListSharedPreferencesDirFileAndUpdatePermissions(this));
		}
	}
	public void clearAndSavePackageList(List<lockMessage> strArray) {
		synchronized (sync) {
			clearAndSavePackageList(this, strArray == null ?nullLockMessage: strArray.toArray(new lockMessage[strArray.size()]));
		}
	}
	public static void clearAndSavePackageList(Context context, lockMessage... strArray) {
		synchronized (sync) {
			if (strArray == null || strArray.length == 0) {
				Config.clearPackageList(Config.getPackageNameListSharedPreferencesDirFileAndUpdatePermissions(context));
				return;
			}
			Config.clearPackageList(Config.getPackageNameListSharedPreferencesDirFileAndUpdatePermissions(context));
			Config.addPackageName(Config.getPackageNameListSharedPreferencesDirFileAndUpdatePermissions(context), strArray);
		}
	}
	public void addPackageName(List<lockMessage> strArray) {
		synchronized (sync) {
			Config.addPackageName(Config.getPackageNameListSharedPreferencesDirFileAndUpdatePermissions(this), strArray == null ?nullLockMessage: strArray.toArray(new lockMessage[strArray.size()]));
		}
	}










	public static SharedPreferences getConfig(Context context) {
		return context.getSharedPreferences("info.config", MODE_PRIVATE);
	}

	public static void setNewInstallAutoAddLock(Context context, boolean b) {
		getConfig(context).edit().putBoolean("newInstallAutoAddLock", b).commit();
	}
	public static boolean isNewInstallAutoAddLock(Context context) {
		return getConfig(context).getBoolean("newInstallAutoAddLock", false);
	}

	public static void setLoadSystemApp(Context context, boolean b) {
		getConfig(context).edit().putBoolean("loadSystemApp", b).commit();
	}
	public static boolean isLoadSystemApp(Context context) {
		return getConfig(context).getBoolean("loadSystemApp", false);
	}

	public static void setShowEternalLockOptions(Context context, boolean b) {
		getConfig(context).edit().putBoolean("isShowEternalLockOptions", b).commit();
	}
	public static boolean isShowEternalLockOptions(Context context) {
		return getConfig(context).getBoolean("isShowEternalLockOptions", false);
	}

	public static String formatTime(long nowTime) {
		SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		return time.format(nowTime);
	}









	private void setWorldReadable() {
		File dataDir = new File(getApplicationInfo().dataDir);
		File prefsDir = new File(dataDir, "shared_prefs");
		File prefsFile = new File(prefsDir, "a.xml");
		if (prefsFile.exists()) {
			for (File file : new File[]{dataDir, prefsDir, prefsFile}) {
				file.setReadable(true, false);
				file.setExecutable(true, false);
			}
		}
	}

}
