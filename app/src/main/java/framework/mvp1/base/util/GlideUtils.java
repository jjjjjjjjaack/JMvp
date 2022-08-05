package framework.mvp1.base.util;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.jjjjjjjjaack.jmvp.R;

public class GlideUtils {

    //设置加载中以及加载失败图片
    public static void loadImageDefult(Context mContext, Object path, ImageView mImageView) {
        Glide.with(mContext).load(path).placeholder(R.mipmap.ic_noimage2).dontAnimate().error(R.mipmap.ic_noimage2)
                .into(mImageView);
    }

    //设置加载中以及加载失败图片
    public static void loadImageDeviceDefult(Context mContext, String path, ImageView mImageView) {
        Glide.with(mContext).load(path).placeholder(R.mipmap.ic_transparent).dontAnimate().error(R.mipmap.ic_transparent)
                .into(mImageView);
    }

    //设置加载中以及加载失败图片
    public static void loadImageRoomDefult(Context mContext, String path, ImageView mImageView) {
//        Glide.with(mContext).load(path).placeholder(R.mipmap.bg_room_list_item).dontAnimate().error(R.mipmap.bg_room_list_item)
//                .into(mImageView);
        Glide.with(mContext).load(path).into(mImageView);
    }

}
