package top.fols.aapp.eternalprocessxposed.activity;


import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.recyclerview.R;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import java.util.List;
import top.fols.aapp.utils.OSHelper;
import top.fols.aapp.utils.XStackUtils;
import top.fols.box.util.ArrayListUtils;

public class SettingActivity extends AppCompatActivity {
	@Override
	protected void onResume() {
		// TODO: Implement this method
		super.onResume();
		XStackUtils.defStack().add(this);
	}

	List<Bean> data;
	RecyclerView mRecyclerView;
	AnimationAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		XStackUtils.defStack().add(this);

		setContentView(R.layout.activity_setting);
		setTitle(R.string.setting);

		data = new ArrayListUtils<>();
		mRecyclerView = (RecyclerView) findViewById(R.id.activitysettingListView1);
		mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
		adapter = new AnimationAdapter(data);
		mRecyclerView.setAdapter(adapter);

		Bean bean;

		bean = new Bean();
		bean.checked = MainActivity.isLoadSystemApp(this);
		bean.showCheckBox = true;
		bean.title = getString(R.string.showsystemapp);
		bean.onclick = new Bean.OnClick()
		{
			@Override
			public void onClick(BaseViewHolder helper, Bean bean) {
				// TODO: Implement this method
				MainActivity.setLoadSystemApp(SettingActivity.this, bean.checked);
			}
		};
		adapter.addData(bean);

		bean = new Bean();
		bean.checked = MainActivity.isShowEternalLockOptions(this);
		bean.showCheckBox = true;
		bean.title = getString(R.string.showeternallockoptions);
		bean.onclick = new Bean.OnClick()
		{
			@Override
			public void onClick(BaseViewHolder helper, Bean bean) {
				// TODO: Implement this method
				MainActivity.setShowEternalLockOptions(SettingActivity.this, bean.checked);
			}
		};
		adapter.addData(bean);

		bean = new Bean();
		bean.checked = MainActivity.isNewInstallAutoAddLock(this);
		bean.showCheckBox = true;
		bean.title = getString(R.string.newappinstallautoaddlock);
		bean.onclick = new Bean.OnClick()
		{
			@Override
			public void onClick(BaseViewHolder helper, Bean bean) {
				// TODO: Implement this method
				MainActivity.setNewInstallAutoAddLock(SettingActivity.this, bean.checked);
			}
		};
		adapter.addData(bean);

		bean = new Bean();
		bean.checked = getPackageManager().getComponentEnabledSetting(c()) == PackageManager.COMPONENT_ENABLED_STATE_DEFAULT;
		bean.showCheckBox = true;
		bean.title = getString(R.string.showhomeicon);
		bean.onclick = new Bean.OnClick()
		{
			@Override
			public void onClick(BaseViewHolder helper, Bean bean) {
				// TODO: Implement this method
				if (getPackageManager().getComponentEnabledSetting(c()) == PackageManager.COMPONENT_ENABLED_STATE_DEFAULT) 
                    getPackageManager().setComponentEnabledSetting(c(), PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
				else 
                    getPackageManager().setComponentEnabledSetting(c(), PackageManager.COMPONENT_ENABLED_STATE_DEFAULT, PackageManager.DONT_KILL_APP);
			}
		};
		adapter.addData(bean);
	}
	private ComponentName c() {
		return new ComponentName(this, "top.fols.aapp.eternalprocessxposed.Hook");
	}




	public static class Bean {
		public static abstract class OnClick {
			public abstract void onClick(BaseViewHolder helper, Bean bean);
		}
		public OnClick onclick = null;
		public boolean checked;
		public boolean showCheckBox;
		public String title;
	}
	public class AnimationAdapter extends BaseQuickAdapter<Bean, BaseViewHolder> {
		public AnimationAdapter(List<? extends Bean> data) {
			super(R.layout.layout_animation_setting, data);
		}
		@Override
		protected void convert(final BaseViewHolder helper, final Bean item) {
			String title = item.title;
			helper.setVisible(R.id.aaCheckBox1_Setting, item.showCheckBox);

			final ImageView imageView = helper.getView(R.id.aaImageView1_Setting);
			imageView.getLayoutParams().width = 0;imageView.getLayoutParams().height = 0;

			helper.setText(R.id.aaTextView1_Setting, title);
			final CheckBox checkBox = helper.getView(R.id.aaCheckBox1_Setting);
			if (item.checked) 
				checkBox.setChecked(true);
			else 
				checkBox.setChecked(false);
			helper.getConvertView().setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View v) {
						checkBox.setChecked(item.checked = !item.checked);
						if (item.onclick != null)
							item.onclick.onClick(helper, item);
					}
				});
		}
	}

}
