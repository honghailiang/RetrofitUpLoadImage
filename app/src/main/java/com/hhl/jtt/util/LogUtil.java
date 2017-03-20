package com.hhl.jtt.util;

import android.util.Log;

/**
 * 日志工具类
 * 
 */
public class LogUtil {
	// 打印开关
	public static boolean isOpen = true;
	// 打印的标签
	public static final String TAG = "RETROFITUPLOADIMAGE";

	// 封装方法
	public static void i(String tag, String msg) {
		if (isOpen) {
			Log.i(tag, msg);
		}

	}

	// 封装方法
	public static void i(String msg) {
		if (isOpen) {
			Log.i(TAG, msg);

		}
	}

	// 封装方法
	public static void d(String tag, String msg) {
		if (isOpen) {
			Log.d(tag, msg);
		}

	}

	// 封装方法
	public static void d(String msg) {
		if (isOpen) {
			Log.d(TAG, msg);

		}
	}

	// 封装方法
	public static void w(String tag, String msg) {
		if (isOpen) {
			Log.d(tag, msg);
		}

	}

	// 封装方法
	public static void w(String msg) {
		if (isOpen) {
			Log.d(TAG, msg);

		}
	}

	// 封装方法
	public static void e(String msg) {
		if (isOpen) {
			Log.e(TAG, msg);

		}
	}

	// 封装方法
	public static void e(String tag, String msg) {
		if (isOpen) {
			Log.e(tag, msg);
		}

	}

}
