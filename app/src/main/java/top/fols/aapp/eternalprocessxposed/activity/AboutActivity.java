package top.fols.aapp.eternalprocessxposed.activity;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import top.fols.aapp.eternalprocessxposed.R;
import top.fols.aapp.utils.XStackUtils;
import top.fols.aapp.utils.XTool;
import top.fols.aapp.utils.donate.AlipayDonate;

public class AboutActivity extends AppCompatActivity implements View.OnClickListener {

	@Override
	protected void onResume() {
		// TODO: Implement this method
		super.onResume();
		XStackUtils.defStack().add(this);
	}
	
	public static void copy(Context context, CharSequence content) {  
		ClipboardManager cmb = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);  
		cmb.setText(content == null ?"": content);  
	}
	public static void jz(final Activity activity) {
		try {
			AlipayDonate.donateAlipay(activity, "FKX04955FJ0O85WYHT3R75");
		} catch (ActivityNotFoundException e) {
			Toast.makeText(activity.getApplicationContext(), R.string.failed_to_start_alipay, Toast.LENGTH_SHORT).show();
		}
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		XStackUtils.defStack().add(this);
		
		setContentView(R.layout.activity_about);
		setTitle(R.string.about);



		try {
            ((TextView) findViewById(R.id.tv_version)).setText(getApplication().getPackageManager().getPackageInfo(getPackageName(), 0).versionName + "(" + getApplication().getPackageManager().getPackageInfo(getPackageName(), 0).versionCode + ")");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
		findViewById(R.id.open_github).setOnClickListener(this);
        findViewById(R.id.tv_version).setOnClickListener(this);
        findViewById(R.id.tv_author_wawashiwo).setOnClickListener(this);
        findViewById(R.id.tv_technical_support_neigeshui233).setOnClickListener(this);
		findViewById(R.id.tv_link_qqGroup).setOnClickListener(this);
	}

	public static void openGithubPage(Activity activity){
		XTool.openLink(activity,"https://github.com/xiaoxinwangluo/android_xposedapp_eternalprocessxposed");
	}

	@Override
    public void onClick(View view) {
        switch (view.getId()) {
			case R.id.open_github: 
				openGithubPage(this);
				break;
			case R.id.tv_author_wawashiwo:
				break;
            case R.id.tv_technical_support_neigeshui233:
				break;
			case R.id.tv_link_qqGroup:
				XTool.openLink(this,"mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D" + "SvDLjxQJ7UAJVnZYdSYh14Qij2pb9p7c");
        }
    }












}

