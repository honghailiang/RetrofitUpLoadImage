package com.hhl.jtt.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Toast类,防止连续多次调用toast时,toast超时显示.
 */
public class ToastUtil {
	private static Toast t;
	private static int duration;

	private static void makeText(Context context, String msg, int duration) {
		if (ToastUtil.duration != duration) {
			if (t != null) {
				t.cancel();
			}
			t = Toast.makeText(context, msg, duration);
		} else {
			if (t == null) {
				t = Toast.makeText(context, msg, duration);
			} else {
				t.setText(msg);
			}
		}
		ToastUtil.duration = duration;
		t.show();
	}

	public static void makeText(Context context, int resId, int duration) {
		makeText(context, context.getResources().getString(resId), duration);
	}

	public static void makeShortText(Context context, String msg) {
		makeText(context, msg, Toast.LENGTH_SHORT);
	}

	public static void makeShortText(Context context, int resId) {
		makeText(context, resId, Toast.LENGTH_SHORT);
	}

	public static void makeLongText(Context context, String msg) {
		makeText(context, msg, Toast.LENGTH_LONG);
	}

	public static void makeLongText(Context context, int resId) {
		makeText(context, resId, Toast.LENGTH_LONG);
	}
}
