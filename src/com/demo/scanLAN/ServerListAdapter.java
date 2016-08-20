package com.demo.scanLAN;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.socketdemo.R;

public class ServerListAdapter extends BaseAdapter {
	private LayoutInflater mInflater;

	public ServerListAdapter(Context context) {
		// TODO Auto-generated constructor stub
		this.mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return ServerList.getData().size();
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
		ViewHolder mHolder;
		Log.v("MyListViewBase", "getView " + position + " " + convertView);
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.scanlist_ipcontent,
					parent, false);

			mHolder = new ViewHolder();

			mHolder.iv_server = (ImageView) convertView
					.findViewById(R.id.iv_server);
			mHolder.tv_server_ip = (TextView) convertView
					.findViewById(R.id.tv_serverIp);
			mHolder.tv_ip_tag = (TextView) convertView
					.findViewById(R.id.tv_ip_tag);

			convertView.setTag(mHolder);// 绑定ViewHolder对象
		} else {
			mHolder = (ViewHolder) convertView.getTag();// 取出ViewHolder对象
		}
		mHolder.tv_server_ip.setText(ServerList.getData().get(position)
				.get("ServerIp").toString());
		mHolder.tv_ip_tag.setText(ServerList.getData().get(position)
				.get("IpTag").toString());
		return convertView;
	}

	public final class ViewHolder {
		public ImageView iv_server;
		public TextView tv_server_ip;
		public TextView tv_ip_tag;
	}

}
