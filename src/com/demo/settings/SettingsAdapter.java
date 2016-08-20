package com.demo.settings;

import com.example.socketdemo.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SettingsAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private String[] Item;

	public SettingsAdapter(Context cxt) {
		// TODO Auto-generated constructor stub
		this.inflater = LayoutInflater.from(cxt);
		Item = new String[] { "Item1", "Item2", "Item3" };
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return Item.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.settings_content, parent,
					false);
			viewHolder = new ViewHolder();
			viewHolder.tv_item = (TextView) convertView
					.findViewById(R.id.tv_item);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.tv_item.setText(Item[position]);
		return convertView;
	}

	private class ViewHolder {
		public TextView tv_item;
	}
}
