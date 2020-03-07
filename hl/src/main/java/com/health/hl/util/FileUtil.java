package com.health.hl.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

//import org.apache.http.util.EncodingUtils;

import android.content.Context;


/**
 * 文件读写帮助
 * 
 * @author dell
 * 
 */
public class FileUtil {
	public static final String FILE_NAME="setting.txt";
	public static final String ENCODING = "UTF-8";
	/**
	 * 读取系统中文件
	 * 
	 * @param context
	 * @return
	 */
	public static String readFile(Context context, String fileName) {
		String msg = "";
		try {
			FileInputStream inStream = context.openFileInput(fileName);
			int length = inStream.available();
			byte[] buffer = new byte[length];
			inStream.read(buffer);
			inStream.close();
//			msg = EncodingUtils.getString(buffer, ENCODING);
			msg = new String(buffer, "UTF-8");
			msg = msg.replace("\r\n", "\n");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return msg;
	}
	/**
	 * 文件写入
	 * @param context
	 * @param fileName
	 * @param list
	 */
	public static void writeFile(Context context,String fileName,List list){
		String json="";
		if(fileName.equals("weightinfo.txt"))
			json=WeightJsonUtil.writeJson(list).toString();
		if(fileName.equals("personalinfo.txt"))
			json=PersonalJsonUtil.writeJson(list).toString();
		if(fileName.equals("foodinfo.txt"))
			json=CalorieJsonUtil.writeJson(list).toString();
		byte[] by=json.getBytes();
		try {
			clear(context, fileName);
			/*
			 * openFileOutput(String name, int mode); 
			 * 第一个参数，代表文件名称，注意这里的文件名称不能包括任何的/或者/这种分隔符，只能是文件名
			 * 该文件会被保存在/data/data/应用名称/files/chenzheng_java.txt,如果文件不存在，将会创建文件
			 * 第二个参数，代表文件的操作模式
			 * MODE_PRIVATE 私有（只能创建它的应用访问） 重复写入时会文件覆盖
			 * MODE_APPEND 私有重复写入时会在文件的末尾进行追加，而不是覆盖掉原来的文件
			 * MODE_WORLD_READABLE 公用 可读
			 * MODE_WORLD_WRITEABLE 公用 可读写
			 */
			FileOutputStream outStream= context.openFileOutput(fileName, Context.MODE_PRIVATE);
			for (int i = 0; i < by.length; i++) {
				outStream.write(by[i]);
			}
			outStream.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	/**
	 * 清空文件
	 * 
	 * @param context
	 * @return
	 */
	public static void clear(Context context, String fileName) {
		FileOutputStream outStream = null;
		String msg = "";
		try {
			outStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
			outStream.write(msg.getBytes());
			outStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 文件写入
	 * @param context
	 * @param fileName
	 * @param list
	 */
	public static void writeFile(Context context,String fileName,String value){
		
		try {
			byte[] by=value.getBytes("UTF-8");
//			clear(context, fileName);
			/*
			 * openFileOutput(String name, int mode); 
			 * 第一个参数，代表文件名称，注意这里的文件名称不能包括任何的/或者/这种分隔符，只能是文件名
			 * 该文件会被保存在/data/data/应用名称/files/chenzheng_java.txt,如果文件不存在，将会创建文件
			 * 第二个参数，代表文件的操作模式
			 * MODE_PRIVATE 私有（只能创建它的应用访问） 重复写入时会文件覆盖
			 * MODE_APPEND 私有重复写入时会在文件的末尾进行追加，而不是覆盖掉原来的文件
			 * MODE_WORLD_READABLE 公用 可读
			 * MODE_WORLD_WRITEABLE 公用 可读写
			 */
			FileOutputStream outStream= context.openFileOutput(fileName, Context.MODE_APPEND);
			for (int i = 0; i < by.length; i++) {
				outStream.write(by[i]);			
			}
//			outStream.flush();
			outStream.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
