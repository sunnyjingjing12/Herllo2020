package com.health.hl.util;

import android.content.Context;
import android.graphics.Bitmap;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.health.hl.R;

public class ImageLoaderUtil {
	private static boolean isInit = false;

	public static void init(Context context) {
		if (isInit) {
			return;
		}
		// float density = context.getResources().getDisplayMetrics().density;
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context.getApplicationContext())
				// 调用该方法，可以指定在内存中缓存的实现。
				.memoryCache(
						new LruMemoryCache(getDefaultMemoryCacheSize(context)))
				// 指定在本地的最大缓存大小。
				.discCacheSize(50 * 1024 * 1024)
				// 设置缓存文件的名字
				// HashCodeFileNameGenerator() ：通过HashCode将url生成文件的唯一名字
				// Md5FileNameGenerator()：通过Md5将url生产文件的唯一名字
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				/* 根据不同的设备指定不同的宽高 */
				/*
				 * .memoryCacheExtraOptions((int)(density * 200), (int)(density
				 * * 150)) .memoryCacheSize(getDefaultMemoryCacheSize(context))
				 */
				.build();
		ImageLoader.getInstance().init(config);
		ImageLoader.getInstance().clearDiscCache();
		ImageLoader.getInstance().clearMemoryCache();
	}

	public static DisplayImageOptions getDisplayImageOptions() {
		return new DisplayImageOptions.Builder()
				//设置图片加载/解码过程中错误时候显示的图片
				.showImageOnFail(R.color.transparent_background)
				//设置图片在下载前是否重置，复位
				.resetViewBeforeLoading()
				//设置下载的图片是否缓存在内存中
				.cacheInMemory()
				//设置下载的图片是否缓存在SD卡中
				.cacheOnDisc()
				//设置图片的显示方式(设置图片渐显的时间)
//				.displayer(new FadeInBitmapDisplayer(200))
				//设置图片在下载期间显示的图片
				.showStubImage(R.color.transparent_background)
				//设置图片的解码类型
				.bitmapConfig(Bitmap.Config.RGB_565).build();
	}
	public static DisplayImageOptions getSpecialDisplayImageOptions() {
		return new DisplayImageOptions.Builder()
				//设置图片加载/解码过程中错误时候显示的图片
				.showImageOnFail(R.color.transparent_background)
				//设置图片在下载前是否重置，复位
				.resetViewBeforeLoading()
				//设置下载的图片是否缓存在内存中
				.cacheInMemory()
				//设置下载的图片是否缓存在SD卡中
				.cacheOnDisc()
				//设置图片的显示方式(设置图片渐显的时间)
//				.displayer(new FadeInBitmapDisplayer(200))
				//设置图片在下载期间显示的图片
				.showStubImage(R.color.transparent_background)
				//设置图片的解码类型
				.bitmapConfig(Bitmap.Config.RGB_565).build();
	}

	public static int getDefaultMemoryCacheSize(Context context) {
		return context.getResources().getDisplayMetrics().heightPixels
				* context.getResources().getDisplayMetrics().widthPixels * 8;
	}
}
