package com.health.hl.util;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.health.hl.entity.FoodInfo;

/**
 * Json帮助
 * 
 * @author dell
 * 
 */
public class CalorieJsonUtil {
	private static final String DATE = "date";
	private static final String BELONG = "belong";
	private static final String NAME = "name";
	private static final String IMG_ID = "img_id";
	private static final String CALORIE = "calorie";
	private static final String UNIT = "unit";
	private static final String NUM = "num";
	// public static String writeJsonString(){
	// JSONObject object=new JSONObject();
	// JSONArray array=new JSONArray();
	// try {
	// object.put(TIME, ParseUtil.get().getTime());
	// object.put(BMI, ParseUtil.get().getBmi());
	// object.put(TIZHONG, ParseUtil.get().getTizhong());
	// object.put(GUGE, ParseUtil.get().getGuge());
	// object.put(ZHIFANG, ParseUtil.get().getZhifang());
	// object.put(JIROU, ParseUtil.get().getJirou());
	// object.put(SHUIFEN, ParseUtil.get().getShuifen());
	// object.put(NEIZANG, ParseUtil.get().getNeizang());
	// object.put(BMR, ParseUtil.get().getBMR());
	// array.put(object);
	// } catch (JSONException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// return array.toString();
	// }

	/**
	 * 写入 JSON
	 * 
	 * @param list
	 * @return
	 */
	public static JSONObject writeJson(List<FoodInfo> list) {
		JSONObject jo = new JSONObject();
		JSONArray ja = new JSONArray();
		try {
			for (int i = 0; i < list.size(); i++) {
				JSONObject jo_weight = new JSONObject();
				jo_weight.put(DATE, list.get(i).getDate());
				jo_weight.put(BELONG, list.get(i).getBelong());
				jo_weight.put(NAME, list.get(i).getName());
				jo_weight.put(IMG_ID, list.get(i).getImg_id());
				jo_weight.put(CALORIE, list.get(i).getCalorie());
				jo_weight.put(UNIT, list.get(i).getUnit());
				jo_weight.put(NUM, list.get(i).getNum());
				ja.put(jo_weight);
			}
			jo.put("result", ja);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jo;
	}

	/**
	 * 读取 JSON 信息
	 * 
	 * @param jsonData
	 * @return
	 */
	public static ArrayList<FoodInfo> readerJson(String jsonUrl) {
		ArrayList<FoodInfo> list = new ArrayList<FoodInfo>();
		FoodInfo foodInfo = null;
		if (jsonUrl.trim().length() == 0) {
			return list;
		}
		try {
			JSONObject jo = null;
			JSONArray ja = null;
			jo = new JSONObject(jsonUrl);
			ja = jo.getJSONArray("result");
			int length = ja.length();
			// int id_song=1;
			for (int i = 0; i < length; i++) {
				JSONObject item = ja.getJSONObject(i);
				foodInfo = new FoodInfo();
				foodInfo.setDate(item.getString(DATE));
				foodInfo.setBelong(item.getInt(BELONG));
				foodInfo.setName(item.getString(NAME));
				foodInfo.setImg_id(item.getInt(IMG_ID));
				foodInfo.setCalorie(item.getInt(CALORIE));
				foodInfo.setUnit(item.getString(UNIT));
				foodInfo.setNum((float)(item.getDouble(NUM)));
				list.add(foodInfo);
			}
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return list;
	}
}
