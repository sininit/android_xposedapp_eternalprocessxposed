package top.fols.aapp.eternalprocessxposed.activity;


import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.recyclerview.R;
import android.widget.CompoundButton;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.List;
import top.fols.aapp.view.simplelistview.SettingAdapter;

public class SettingActivity extends AppCompatActivity {
	@Override
	protected void onResume() {
		// TODO: Implement this method
		super.onResume();
	}

	private List<SettingAdapter.Entry> data;
	private ListView mRecyclerView;
	private SettingAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		setTitle(R.string.setting);

		data = new ArrayList<>();
		mRecyclerView = (ListView) findViewById(R.id.activitysettingListView1);
		adapter = new SettingAdapter(SettingActivity.this, data);
		mRecyclerView.setAdapter(adapter);


		final SettingAdapter.Entry entry;
		entry = new SettingAdapter.Entry();
		entry.title = getString(R.string.showsystemapp);
		entry.title2 = "" + (MainActivity.isLoadSystemApp(this) ?getText(R.string.on): getText(R.string.off));
		entry.title2show = true;
		entry.checkbox = MainActivity.isLoadSystemApp(this);
		entry.checkboxShow = true;
		entry.onChange = new CompoundButton.OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(CompoundButton p1, boolean p2) {
				// TODO: Implement this method
				MainActivity.setLoadSystemApp(SettingActivity.this, p2);
				entry.checkbox = p2;
				entry.title2 = "" + (MainActivity.isLoadSystemApp(SettingActivity.this) ?getText(R.string.on): getText(R.string.off));
				adapter.notifyDataSetChanged();


			}
		};
		data.add(entry);


		entry = new SettingAdapter.Entry();
		entry.title = getString(R.string.showeternallockoptions);
		entry.title2 = "" + (MainActivity.isShowEternalLockOptions(this) ?getText(R.string.on): getText(R.string.off));
		entry.title2show = true;
		entry.checkbox = MainActivity.isShowEternalLockOptions(this);
		entry.checkboxShow = true;
		entry.onChange = new CompoundButton.OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(CompoundButton p1, boolean p2) {
				// TODO: Implement this method
				MainActivity.setShowEternalLockOptions(SettingActivity.this, p2);
				entry.checkbox = p2;
				entry.title2 = "" + (MainActivity.isShowEternalLockOptions(SettingActivity.this) ?getText(R.string.on): getText(R.string.off));
				adapter.notifyDataSetChanged();
			}
		};
		data.add(entry);


		entry = new SettingAdapter.Entry();
		entry.title = getString(R.string.newappinstallautoaddlock);
		entry.title2 = "" + (MainActivity.isNewInstallAutoAddLock(this) ?getText(R.string.on): getText(R.string.off));
		entry.title2show = true;
		entry.checkbox = MainActivity.isNewInstallAutoAddLock(this);
		entry.checkboxShow = true;
		entry.onChange = new CompoundButton.OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(CompoundButton p1, boolean p2) {
				// TODO: Implement this method
				MainActivity.setNewInstallAutoAddLock(SettingActivity.this, p2);
				entry.checkbox = p2;
				entry.title2 = "" + (MainActivity.isNewInstallAutoAddLock(SettingActivity.this) ?getText(R.string.on): getText(R.string.off));
				adapter.notifyDataSetChanged();
			}
		};
		data.add(entry);

		entry = new SettingAdapter.Entry();
		entry.title = getString(R.string.showhomeicon);
		entry.title2 = "" + ((getPackageManager().getComponentEnabledSetting(c()) == PackageManager.COMPONENT_ENABLED_STATE_DEFAULT) ?getText(R.string.on): getText(R.string.off));
		entry.title2show = true;
		entry.checkbox = getPackageManager().getComponentEnabledSetting(c()) == PackageManager.COMPONENT_ENABLED_STATE_DEFAULT;
		entry.checkboxShow = true;
		entry.onChange = new CompoundButton.OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(CompoundButton p1, boolean p2) {
				// TODO: Implement this method
				if (getPackageManager().getComponentEnabledSetting(c()) == PackageManager.COMPONENT_ENABLED_STATE_DEFAULT) 
                    getPackageManager().setComponentEnabledSetting(c(), PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
				else 
                    getPackageManager().setComponentEnabledSetting(c(), PackageManager.COMPONENT_ENABLED_STATE_DEFAULT, PackageManager.DONT_KILL_APP);
				entry.checkbox = p2;
				entry.title2 = "" + ((getPackageManager().getComponentEnabledSetting(c()) == PackageManager.COMPONENT_ENABLED_STATE_DEFAULT) ?getText(R.string.on): getText(R.string.off));
				adapter.notifyDataSetChanged();
			}
		};
		data.add(entry);

		adapter.notifyDataSetChanged();
	}
	private ComponentName c() {
		return new ComponentName(this, "top.fols.aapp.eternalprocessxposed.Hook");
	}
}
