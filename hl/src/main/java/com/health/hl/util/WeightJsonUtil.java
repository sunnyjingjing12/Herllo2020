package com.health.hl.util;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.health.hl.models.WeightInfo;

/**
 * Json帮助
 * 
 * @author dell
 * 
 */
public class WeightJsonUtil {
	private static final String USERNAME = "username";
	private static final String TIME = "time";
	private static final String BMI = "bmi";
	private static final String TIZHONG = "tizhong";
	private static final String GUGE = "guge";
	private static final String ZHIFANG = "zhifang";
	private static final String JIROU = "jirou";
	private static final String SHUIFEN = "shuifen";
	private static final String NEIZANG = "neizang";
	private static final String BMR = "bmr";

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
	public static JSONObject writeJson(List<WeightInfo> list) {
		JSONObject jo = new JSONObject();
		JSONArray ja = new JSONArray();
		try {
			for (int i = 0; i < list.size(); i++) {
				JSONObject jo_weight = new JSONObject();
				jo_weight.put(USERNAME, list.get(i).getUserName());
				jo_weight.put(TIME, list.get(i).getTime());
				jo_weight.put(BMI, list.get(i).getBmi());
				jo_weight.put(TIZHONG, list.get(i).getWeight());
				jo_weight.put(GUGE, list.get(i).getGuge());
				jo_weight.put(ZHIFANG, list.get(i).getZhifang());
				jo_weight.put(JIROU, list.get(i).getJirou());
				jo_weight.put(SHUIFEN, list.get(i).getShuifen());
				jo_weight.put(NEIZANG, list.get(i).getNeizang());
				jo_weight.put(BMR, list.get(i).getBMR());
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
	public static List<WeightInfo> readerJson(String jsonUrl) {
		List<WeightInfo> list = new ArrayList<WeightInfo>();
		WeightInfo weight = null;
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
				weight = new WeightInfo();
				weight.setUserName(item.getString(USERNAME));
				weight.setTime(item.getString(TIME));
				weight.setBmi(Float.parseFloat(item.getString(BMI)));
				weight.setWeight(item.getString(TIZHONG));
				weight.setGuge(item.getString(GUGE));
				weight.setZhifang(item.getString(ZHIFANG));
				weight.setJirou(item.getString(JIROU));
				weight.setShuifen(item.getString(SHUIFEN));
				weight.setNeizang(item.getString(NEIZANG));
				weight.setBMR(item.getString(BMR));
				list.add(weight);
			}
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return list;
	}
}
