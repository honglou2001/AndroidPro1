package com.example.tools;

import com.example.domain.CommunicationShareData;
import com.example.activitydemo.R;
import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


public class CommunicationShareAdapter extends BaseAdapter{
	private LayoutInflater layoutInflater;
	private Context context;
	private ArrayList<CommunicationShareData> list;
	
	public CommunicationShareAdapter(Context _context, ArrayList<CommunicationShareData> _list){
		context = _context;
		list = _list;
		layoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	public void setList(ArrayList<CommunicationShareData> _list){
		list = _list;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(list == null){
			return 0;
		} else {
			return list.size();
		}
		
	}

	@Override
	public Object getItem(int arg0) {
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

			convertView = layoutInflater.inflate(R.layout.activity_second_item, null);
			viewHolder = new ViewHolder();
			viewHolder.date = (TextView) convertView
					.findViewById(R.id.secDate);
			viewHolder.name = (TextView) convertView
					.findViewById(R.id.secName);

		viewHolder.date.setText(list.get(position).getDate());
		viewHolder.name.setText(list.get(position).getName() + ":" + " " + list.get(position).getContent());
		return convertView;
	}
	public static class ViewHolder {
		public TextView date, name;
	}

}