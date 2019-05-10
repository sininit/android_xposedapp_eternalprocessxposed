package top.fols.aapp.view.simplelistview;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import java.util.List;
import top.fols.aapp.eternalprocessxposed.R;
import android.widget.ArrayAdapter;

public class SettingAdapter extends ArrayAdapter<SettingAdapter.Entry> {
	private static final int resourceId = R.layout.settingadapter_item;
	private List<Entry> objects;
    public SettingAdapter(Context context, List<Entry> objects) {  
        super(context, resourceId, objects);  
		this.objects = objects;
    }  

    @Override  
    public View getView(int position, View convertView, ViewGroup parent) {  
		ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(resourceId, null); //这里确定你listview里面每一个item的layout

			holder.title = convertView.findViewById(R.id.fruit_name);//注意：此处的findVIewById前要加convertView.
            holder.title2 = convertView.findViewById(R.id.fruit_name2);
			holder.checkbox = convertView.findViewById(R.id.fruit_checkbox);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag(); //这里是为了提高listview的运行效率
        }
		holder.title.setText(objects.get(position).title);
		setViewVisible(holder.title , objects.get(position).titleshow);

        holder.title2.setText(objects.get(position).title2);
		setViewVisible(holder.title2 , objects.get(position).title2show);

		convertView.setOnClickListener(null);
		convertView.setOnClickListener(objects.get(position).onClick);

		holder.checkbox.setOnCheckedChangeListener(null);
		holder.checkbox.setChecked(objects.get(position).checkbox);
		holder.checkbox.setOnCheckedChangeListener(objects.get(position).onChange);
		setViewVisible(holder.checkbox , objects.get(position).checkboxShow);

		if (!objects.get(position).title2show) {
			holder.title.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
			holder.title.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
			holder.title.setGravity(Gravity.CENTER | Gravity.LEFT);
		}
        return convertView;
	}

	public static class Entry {
		public String title;
		public boolean titleshow = true;

		public String title2;
		public boolean title2show = false;
		public boolean checkbox = false;
		public boolean checkboxShow = false;

		public View.OnClickListener onClick = null;
		public CompoundButton.OnCheckedChangeListener onChange = null;
	}  



	public static class ViewHolder {
        public TextView title;
        public TextView title2;
		public CheckBox checkbox;
    }
	private static void setViewVisible(View View, boolean b) {
		int visibility = b ? View.VISIBLE: View.INVISIBLE;
		View.setVisibility(visibility);
	}
	public static final int FILL_PARENT = ViewGroup.LayoutParams.FILL_PARENT;
	public static final int MATCH_PARENT = ViewGroup.LayoutParams.MATCH_PARENT;
	public static final int WRAP_CONTENT = ViewGroup.LayoutParams.WRAP_CONTENT;

}
