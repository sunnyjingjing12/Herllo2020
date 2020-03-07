package com.health.hl.util;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.health.hl.models.PersonalInfo;

/**
 * Json帮助
 * 
 * @author dell
 * 
 */
public class PersonalJsonUtil {
	private static final String USERNAME = "userName";
	private static final String SEX = "sex";
	private static final String AGE = "age";
	private static final String HEIGHT = "height";
	private static final String LEVEL = "level";
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
	public static JSONObject writeJson(List<PersonalInfo> list) {
		JSONObject jo = new JSONObject();
		JSONArray ja = new JSONArray();
		try {
			for (int i = 0; i < list.size(); i++) {
				JSONObject jo_weight = new JSONObject();
				jo_weight.put(USERNAME, list.get(i).getUserName());
				jo_weight.put(SEX, list.get(i).getSex());
				jo_weight.put(HEIGHT, list.get(i).getHeight());
				jo_weight.put(AGE, list.get(i).getAge());
				jo_weight.put(LEVEL, list.get(i).getLevel());
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
	public static ArrayList<PersonalInfo> readerJson(String jsonUrl) {
		ArrayList<PersonalInfo> list = new ArrayList<PersonalInfo>();
		PersonalInfo personal = null;
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
				personal = new PersonalInfo();
				personal.setUserName(item.getString(USERNAME));
				personal.setSex(item.getString(SEX));
				personal.setHeight(item.getString(HEIGHT));
				personal.setAge(item.getString(AGE));
				personal.setLevel(item.getString(LEVEL));
				list.add(personal);
			}
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return list;
	}
}
