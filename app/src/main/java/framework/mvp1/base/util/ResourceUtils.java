package framework.mvp1.base.util;


import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;

public class ResourceUtils {

	private static TypedValue mTmpValue = new TypedValue();

	public final static int TXSIZE_SPUERBIG = 20;
	public final static int TXSIZE_BIG = 18;
	public final static int TXSIZE_MIDDLE = 16;
	public final static int TXSIZE_SMALL = 14;

	private ResourceUtils() {
	}

	/**
	 * 获取设备屏幕长度
	 * */
	public static int getScreenWidth(Context context){
		return context.getResources().getDisplayMetrics().widthPixels;
	}

	/**
	 * 获取设备屏幕高度
	 * */
	public static int getScreenHeight(Context context){
		return context.getResources().getDisplayMetrics().heightPixels;
	}

	/** 获取屏幕的高度 */
	public final static int getWindowsHeight(Activity activity) {
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.heightPixels;
	}

	/** 获取屏幕的宽度 */
	public final static int getWindowsWidth(Activity activity) {
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.widthPixels;
	}

	public static int getXmlDef(Context context, int id) {
		synchronized (mTmpValue) {
			TypedValue value = mTmpValue;
			context.getResources().getValue(id, value, true);
			return (int) TypedValue.complexToFloat(value.data);
		}
	}

	public static int dip2px2(Context context, float dpValue) {
		float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	public static int dp2px(Context context, int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				context.getResources().getDisplayMetrics());
	}
	public static int sp2px(Context context, int sp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp,
				context.getResources().getDisplayMetrics());
	}

	/**
	 * px = dp * (dpi / 160)
	 *
	 * @param ctx
	 * @param dip
	 * @return
	 */
	public static int dipToPX(final Context ctx, float dip) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				dip, ctx.getResources().getDisplayMetrics());
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}




	public static ColorStateList createColorStateList(int normal, int pressed,
                                                      int focused, int unable) {
		int[] colors = new int[] { pressed, focused, normal, focused, unable,
				normal };
		int[][] states = new int[6][];
		states[0] = new int[] { android.R.attr.state_pressed,
				android.R.attr.state_enabled };
		states[1] = new int[] { android.R.attr.state_enabled,
				android.R.attr.state_focused };
		states[2] = new int[] { android.R.attr.state_enabled };
		states[3] = new int[] { android.R.attr.state_focused };
		states[4] = new int[] { android.R.attr.state_window_focused };
		states[5] = new int[] { android.R.attr.state_selected };
		states[6] = new int[] {};
		return new ColorStateList(states, colors);
	}

	public static Drawable getDrawable(Context context, String resName,
                                       boolean isForce) {
		int resId;
		if (isForce) // 这里使用isForce参数主要是为了一些主题切换时共用的图片被匹配
		{
			// 约定，黑夜图片带_night
			resId = context.getResources().getIdentifier(resName + "_night",
					"drawable", context.getPackageName());
		} else {
			resId = context.getResources().getIdentifier(resName, "drawable",
					context.getPackageName());
		}

		return context.getResources().getDrawable(resId);
	}

	public static Drawable getDrawable(Context context, int resid,
                                       boolean isForce) {
		String resName = context.getResources().getResourceEntryName(resid);
		if (isForce) {
			resName = resName + "_night";
		}
		int resId = context.getResources().getIdentifier(resName, "drawable",
				context.getPackageName());
		return context.getResources().getDrawable(resId);
	}

	public static int getPixelsFromDp(Context context, int size) {
		DisplayMetrics metrics = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay()
				.getMetrics(metrics);

		return (size * metrics.densityDpi) / DisplayMetrics.DENSITY_DEFAULT;

	}


	public static int getViewHeight(View view, boolean isHeight){
		int result;
		if(view==null)return 0;
		if(isHeight){
			int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
			view.measure(h,0);
			result =view.getMeasuredHeight();
		}else{
			int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
			view.measure(0,w);
			result =view.getMeasuredWidth();
		}
		return result;
	}

}