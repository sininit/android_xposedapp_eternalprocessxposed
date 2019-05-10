package top.fols.aapp.view.simplelistview;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import top.fols.aapp.eternalprocessxposed.R;
import top.fols.aapp.eternalprocessxposed.activity.MainActivity;  

public class EntryAdapter extends ArrayAdapter<EntryAdapter.Entry> {  

    private static final int resourceId = R.layout.element_item;

	private static final int _appname = R.id.item_app_name;
	private static final int _checkid = R.id.item_check;
	private static final int _appiconid = R.id.item_app_icon;
	private static final int _lockstateid = R.id.item_state_icon;

	private List<Entry> objects;
    public EntryAdapter(Context context, List<Entry> objects) {  
        super(context, resourceId, objects);  
		this.objects = objects;
    }  
    @Override  
    public View getView(int position, View convertView, ViewGroup parent) {  
		ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(resourceId, null); //这里确定你listview里面每一个item的layout

			holder.appIcon = convertView.findViewById(_appiconid);
			holder.stateICon = convertView.findViewById(_lockstateid);
			holder.title = convertView.findViewById(_appname);//注意：此处的findVIewById前要加convertView.
            holder.checked = convertView.findViewById(_checkid);

			convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag(); //这里是为了提高listview的运行效率
        }
		Entry entry = objects.get(position);
		holder.appIcon.setImageDrawable(entry.appICon);
		holder.stateICon.setImageDrawable(entry.stateICon);
		holder.title.setText(entry.appName + "\n" + entry.packageName);
		
		holder.checked.setOnCheckedChangeListener(null);
		holder.checked.setChecked(entry.checked);
		holder.checked.setOnCheckedChangeListener(entry.onChange);

        return convertView;
	}
	public List<Entry> getSelect() {
		List<Entry> list = new ArrayList<>();
		Entry[] entrys = objects.toArray(new Entry[objects.size()]);
		for (Entry ei:entrys) {
			if (ei == null) continue;
			if (ei.checked) list.add(ei);
		}
		return list;
	}
	public List<Entry> getData() {
		return objects;
	}
	public void setData(List<Entry> list) {
		List<Entry> newList = new ArrayList<>(list);
		objects.clear();
		objects.addAll(newList);
		notifyDataSetChanged();
	}
	public void setData(int i, Entry e) {
		objects.set(i, e);
	}
	
	
	public static class Entry {
		public Drawable appICon; // 应用图标
		public Drawable stateICon; //状态图标
		
		public MainActivity.AppInfo infox;
		public String appName;//appName
		public String packageName;
		public boolean checked = false;//Select
		public CompoundButton.OnCheckedChangeListener onChange = null;//
	}  
	public static class ViewHolder {
		public ImageView appIcon;
		public ImageView stateICon;

        public TextView title;
        public CheckBox checked;
    }



	private static void setViewVisible(View View, boolean b) {
		int visibility = b ? View.VISIBLE: View.INVISIBLE;
		View.setVisibility(visibility);
	}
	public static final int FILL_PARENT = ViewGroup.LayoutParams.FILL_PARENT;
	public static final int MATCH_PARENT = ViewGroup.LayoutParams.MATCH_PARENT;
	public static final int WRAP_CONTENT = ViewGroup.LayoutParams.WRAP_CONTENT;
}       


