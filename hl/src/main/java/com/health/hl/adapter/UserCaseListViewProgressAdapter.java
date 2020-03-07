package com.health.hl.adapter;

import java.util.List;  
  
import android.content.Context;  
import android.graphics.Color;
import android.view.LayoutInflater;  
import android.view.View;  
import android.view.ViewGroup;  
import android.widget.BaseAdapter;  
import android.view.View.OnClickListener;
import android.widget.TextView;  
import com.health.hl.R;
  
  
public class UserCaseListViewProgressAdapter extends BaseAdapter implements
OnClickListener {  
      
    private List<GoodsProgress> list;  
    private LayoutInflater inflater;  
    private ViewHolder viewHolder; 
    private InnerItemOnclickListener mListener; 
    private int  selectItem=-1;
    public int row=0;
      
    public UserCaseListViewProgressAdapter(Context context, List<GoodsProgress> list){  
        this.list = list;  
        inflater = LayoutInflater.from(context);  
    }  
      
    @Override  
    public int getCount() {  
        int ret = 0;  
        if(list!=null){  
            ret = list.size();  
        }  
        return ret;  
    }  
  
    @Override  
    public Object getItem(int position) {  
        return list.get(position);  
    }  
  
    @Override  
    public long getItemId(int position) {  
        return position;  
    }  
  
    @Override  
    public View getView(int position, View convertView, ViewGroup parent) {  
          
        GoodsProgress goods = (GoodsProgress) this.getItem(position);  
          
        if(convertView == null){  
              
            viewHolder = new ViewHolder();  
              
            convertView = inflater.inflate(R.layout.listitem_usercase_listview_progress, null);  
            viewHolder.goodRank = (TextView) convertView.findViewById(R.id.lv_recover_usercase_progress_rank);  
            viewHolder.goodUserName = (TextView) convertView.findViewById(R.id.lv_recover_usercase_progress_username);  
            viewHolder.goodProgressNum = (TextView) convertView.findViewById(R.id.lv_recover_usercase_progress_progressnum);  
            viewHolder.goodMaxPowerVary = (TextView) convertView.findViewById(R.id.lv_recover_usercase_progress_maxpowervary);
//            viewHolder.goodPhone = (TextView) convertView.findViewById(R.id.lv_recover_usercase_phonenum); 
//            viewHolder.goodMuscleI = (TextView) convertView.findViewById(R.id.lv_recover_usercase_muscleI);
//            viewHolder.goodMuscleII = (TextView) convertView.findViewById(R.id.lv_recover_usercase_muscleII);
//            viewHolder.goodTrainPlan = (TextView) convertView.findViewById(R.id.lv_recover_usercase_trainplan);
//            viewHolder.goodOperation = (TextView) convertView.findViewById(R.id.lv_recover_usercase_operation);
        }else{  
            viewHolder = (ViewHolder) convertView.getTag();  
        }  
          
        if (position == selectItem) {  
        	row = position;
            convertView.setBackgroundColor(Color.rgb(251, 191, 189));  
        }   
        else {  
            convertView.setBackgroundColor(Color.TRANSPARENT);  
        }  
        viewHolder.goodRank.setText(goods.getRank());  
        viewHolder.goodRank.setTextSize(13);  
        viewHolder.goodUserName.setText(goods.getUserName());  
        viewHolder.goodUserName.setTextSize(13);  
        viewHolder.goodProgressNum.setText(goods.getProgressNum());  
        viewHolder.goodProgressNum.setTextSize(13);  
        viewHolder.goodMaxPowerVary.setText(goods.getMaxPowerVary());  
        viewHolder.goodMaxPowerVary.setTextSize(13);  
//        viewHolder.goodPhone.setText(goods.getPhone()+"");  
//        viewHolder.goodPhone.setTextSize(13);  
//        viewHolder.goodMuscleI.setText(goods.getMuscleI()+"");  
//        viewHolder.goodMuscleI.setTextSize(13);  
//        viewHolder.goodMuscleII.setText(goods.getMuscleII()+"");  
//        viewHolder.goodMuscleII.setTextSize(13);  
//        viewHolder.goodTrainPlan.setText(goods.getTrainPlan()+"");  
//        viewHolder.goodTrainPlan.setTextSize(13);  
//        viewHolder.goodOperation.setText(goods.getOperation()+"");  
//        viewHolder.goodOperation.setTextSize(13);  
//        viewHolder.goodOperation.setClickable(true);
//        viewHolder.goodOperation.setOnClickListener(this);
//        viewHolder.goodOperation.setTag(position);
        convertView.setTag(viewHolder);  
        return convertView;  
    }  
      
    public static class ViewHolder{  
        public TextView goodRank;  
        public TextView goodUserName;  
        public TextView goodProgressNum;  
        public TextView goodMaxPowerVary;  
//        public TextView goodPhone;  
//        public TextView goodMuscleI; 
//        public TextView goodMuscleII; 
//        public TextView goodTrainPlan; 
//        public TextView goodOperation; 
    }  
      
    public interface InnerItemOnclickListener {  
        void itemClick(View v);  
    }  
  
    public void setOnInnerItemOnClickListener(InnerItemOnclickListener listener){  
        this.mListener=listener;  
    }  
  
    @Override  
    public void onClick(View v) {  
        mListener.itemClick(v);  
    } 
    
    public  void setSelectItem(int selectItem) {  
        this.selectItem = selectItem;  
   }  

    
}  

