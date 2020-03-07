package com.health.hl.adapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.health.hl.models.BalanceInfo.MyData;
import com.health.hl.R;

public class UserDataAdapter extends BaseAdapter {

	//private List<WeightInfo> list;
	private List<MyData> list;
	private Context context;

	/*public UserDataAdapter(List<WeightInfo> list, Context context) {
		this.list = list;
		this.context = context;
	}*/
	public UserDataAdapter(List<MyData> list, Context context) {
		this.list = list;
		this.context = context;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		View v = null;
		ViewHolder viewHolder;
		if (null == arg1) {
			viewHolder = new ViewHolder();
			v = LayoutInflater.from(context).inflate(R.layout.listitem_userdata,
					null, false);
			viewHolder.txt_date = (TextView) v.findViewById(R.id.txt_date);
			viewHolder.txt_weight = (TextView) v.findViewById(R.id.txt_weight);
			viewHolder.txt_bmi = (TextView) v.findViewById(R.id.txt_bmi);
			v.setTag(viewHolder);
		} else {
			v = arg1  ;
			viewHolder = (ViewHolder) v.getTag();
		}
//		viewHolder.txt_date.setText(list.get(arg0).getTime());
//		viewHolder.txt_weight.setText(list.get(arg0).getWeight());
//		viewHolder.txt_bmi.setText(list.get(arg0).getBmi()+"");
		MyData data1 = list.get(arg0);
		long time = data1.getDate().getTime();
		Date d = new Date(time);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy骞碝M鏈坉d鏃�");
		String date_item = sdf.format(d);
		viewHolder.txt_date.setText(date_item);
		viewHolder.txt_weight.setText(data1.weight+"");
		viewHolder.txt_bmi.setText(data1.bmi+"");
		return v;
	}

	private class ViewHolder {
		TextView txt_date;
		TextView txt_weight;
		TextView txt_bmi;
	}
}
