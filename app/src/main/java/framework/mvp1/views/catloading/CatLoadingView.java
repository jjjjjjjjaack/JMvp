package framework.mvp1.views.catloading;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;
import com.jjjjjjjjaack.jmvp.R;

public class CatLoadingView extends DialogFragment {

    public static CatLoadingView catLoadingView;

    public CatLoadingView() {
    }

    public static CatLoadingView createInstance() {
        catLoadingView = new CatLoadingView();      // 3
        catLoadingView.dismissDelayTime = 100;
        catLoadingView.timeOutTime = 25000;
////        if (catLoadingView == null) {
////            catLoadingView = new CatLoadingView();
////            catLoadingView.dismissDelayTime = 100;
////            catLoadingView.timeOutTime = 25000;
////        }
////        else {
////            if (catLoadingView.mDialog != null) {
////                catLoadingView.mDialog.dismiss();
//////                catLoadingView.mDialog = null;
////            }
////        }
//        if (catLoadingView == null) {
//            synchronized (CatLoadingView.class) {       // 1
//                // 只需在第一次创建实例时才同步
//                if (catLoadingView == null) {       // 2
//                    catLoadingView = new CatLoadingView();      // 3
//                    catLoadingView.dismissDelayTime = 100;
//                    catLoadingView.timeOutTime = 25000;
//                }
//            }
//        }
//        if(catLoadingView.isAdded()) {
//            catLoadingView.dismiss();
//            FragmentTransaction ft = catLoadingView.getFragmentManager().beginTransaction();
//            /*
//             * 如果不执行remove()，对话框即不会进入onDismiss()状态。会被显示在新的对话框下方，是可见的。
//             * 主要考虑美观的问题，如果下面的对话框大于上面的对话框就很难看了。 对于Dialog，container为0或者null。
//             */
//            ft.remove(catLoadingView);
//        }
        return catLoadingView;
    }

    /**
     * 消失延迟时间 ms
     */
    public long dismissDelayTime = 100;
    /**
     * 最长等待时间
     */
    public long timeOutTime = -1;

    private Animation operatingAnim, eye_left_Anim, eye_right_Anim;

    private Dialog mDialog;

    private View mouse, eye_left, eye_right;

    private EyelidView eyelid_left, eyelid_right;

    private GraduallyTextView mGraduallyTextView;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (mDialog == null) {
            mDialog = new Dialog(getActivity(), R.style.cart_dialog);
            mDialog.setContentView(R.layout.catloading_main);
            mDialog.setCanceledOnTouchOutside(true);
            mDialog.getWindow().setGravity(Gravity.CENTER);

            operatingAnim = new RotateAnimation(360f, 0f,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f);
            operatingAnim.setRepeatCount(Animation.INFINITE);
            operatingAnim.setDuration(2000);

            eye_left_Anim = new RotateAnimation(360f, 0f,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f);
            eye_left_Anim.setRepeatCount(Animation.INFINITE);
            eye_left_Anim.setDuration(2000);

            eye_right_Anim = new RotateAnimation(360f, 0f,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f);
            eye_right_Anim.setRepeatCount(Animation.INFINITE);
            eye_right_Anim.setDuration(2000);

            LinearInterpolator lin = new LinearInterpolator();
            operatingAnim.setInterpolator(lin);
            eye_left_Anim.setInterpolator(lin);
            eye_right_Anim.setInterpolator(lin);

            View view = mDialog.getWindow().getDecorView();

            mouse = view.findViewById(R.id.mouse);

            eye_left = view.findViewById(R.id.eye_left);

            eye_right = view.findViewById(R.id.eye_right);

            eyelid_left = (EyelidView) view.findViewById(R.id.eyelid_left);

            eyelid_left.setColor(Color.parseColor("#d0ced1"));

            eyelid_left.setFromFull(true);

            eyelid_right = (EyelidView) view.findViewById(R.id.eyelid_right);

            eyelid_right.setColor(Color.parseColor("#d0ced1"));

            eyelid_right.setFromFull(true);

            mGraduallyTextView = (GraduallyTextView) view
                    .findViewById(R.id.graduallyTextView);
//            ImageView iv_loading=view.findViewById(R.id.iv_loading);
//            Glide.with(this).asGif().load(R.mipmap.ic_loading_gif).into(iv_loading);
            operatingAnim
                    .setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {
                            eyelid_left.resetAnimator();
                            eyelid_right.resetAnimator();
                        }
                    });
        }
        mDialog.setCanceledOnTouchOutside(true);// 设置点击屏幕Dialog不消失
        return mDialog;
    }

    @Override
    public void onResume() {
        super.onResume();
        mouse.setAnimation(operatingAnim);
        eye_left.setAnimation(eye_left_Anim);
        eye_right.setAnimation(eye_right_Anim);
        eyelid_left.startLoading();
        eyelid_right.startLoading();
        mGraduallyTextView.startLoading();
    }

    @Override
    public void onPause() {
        super.onPause();

        operatingAnim.reset();
        eye_left_Anim.reset();
        eye_right_Anim.reset();

        mouse.clearAnimation();
        eye_left.clearAnimation();
        eye_right.clearAnimation();

        eyelid_left.stopLoading();
        eyelid_right.stopLoading();
        mGraduallyTextView.stopLoading();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
//        mDialog = null;
//        System.gc();
    }

    public void showView(final Context context) {
        try {
            if(!((FragmentActivity) context).isFinishing()) {
                super.show(((FragmentActivity) context).getSupportFragmentManager(), "catloading");
            }
            //在每个add事务前增加一个remove事务，防止连续的add
//            ((FragmentActivity) context).getSupportFragmentManager().beginTransaction().remove(this).commit();
//            super.show(((FragmentActivity) context).getSupportFragmentManager(), "");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void hideView(final Context context) {
        try {
            if (!((Activity) context).isFinishing()) {
                dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
