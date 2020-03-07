package com.health.hl.adapter;

import java.util.List;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.health.hl.R;

public class AdapterBlueDevice extends BaseAdapter{
	private Context context;
	private List<BluetoothDevice> list_device;
	private LayoutInflater mInflater;
	public AdapterBlueDevice(Context context,List<BluetoothDevice> list_device){
		this.context=context;
		this.list_device=list_device;
		this.mInflater = LayoutInflater.from(context);
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list_device.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return list_device.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
        if (convertView == null) {
            holder=new ViewHolder();  
            convertView = mInflater.inflate(R.layout.item_lv_device, null);
            holder.name = (TextView)convertView.findViewById(R.id.text_name);
            holder.address = (TextView)convertView.findViewById(R.id.text_address);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder)convertView.getTag();
        }
        holder.name.setText(list_device.get(arg0).getName());
        holder.address.setText(list_device.get(arg0).getAddress());
		return convertView;
	}
	public final class ViewHolder{
        public TextView name;
        public TextView address;
    }
}
