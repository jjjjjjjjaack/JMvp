package framework.mvp1.base.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.media.MediaMetadataRetriever;
import android.os.AsyncTask;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class BitmapUtil {

    public static boolean saveBitmap2file(Bitmap bmp, String filename) {
        Bitmap.CompressFormat format = Bitmap.CompressFormat.JPEG;
        int quality = 100;
        OutputStream stream = null;
        try {
            stream = new FileOutputStream(filename);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return bmp.compress(format, quality, stream);
    }

    /**
     * �获取视频封面
     *
     * @param filePath
     * @return
     */
    public static Bitmap getVideoThumbnail(String filePath) {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(filePath);
            bitmap = retriever.getFrameAtTime();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            try {
                retriever.release();
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }

    public static boolean saveVideoThumbnail(String vidoePath, String savePath) {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(vidoePath);
            bitmap = retriever.getFrameAtTime();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            try {
                retriever.release();
            } catch (RuntimeException e) {
                e.printStackTrace();
            }

            if (bitmap != null) {
                Bitmap.CompressFormat format = Bitmap.CompressFormat.JPEG;
                int quality = 100;
                OutputStream stream = null;
                try {
                    stream = new FileOutputStream(savePath);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                return bitmap.compress(format, quality, stream);
            }

        }
        return false;
    }

    public static Bitmap compressImage(Bitmap image, int maxSize) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > maxSize) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();// 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;// 每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    public static Bitmap comp(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        if (baos.toByteArray().length / 1024 > 1024) {// 判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
            baos.reset();// 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, 50, baos);// 这里压缩50%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        // 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 800f;// 这里设置高度为800f
        float ww = 480f;// 这里设置宽度为480f
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放
        if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;// 设置缩放比例
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        isBm = new ByteArrayInputStream(baos.toByteArray());
        bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        return compressImage(bitmap, 100);// 压缩好比例大小后再进行质量压缩
    }

    // 计算图片的缩放值
    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    // 根据路径获得图片并压缩，返回bitmap用于显示
    public static Bitmap getSmallBitmap(String filePath) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, 480, 800);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(filePath, options);
    }

    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    public static Bitmap rotateBitmap(Bitmap bitmap, int degress) {
        if (bitmap != null) {
            Matrix m = new Matrix();
            m.postRotate(degress);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                    bitmap.getHeight(), m, true);
            return bitmap;
        }
        return bitmap;
    }

    /*
     *压缩图片，处理某些手机拍照角度旋转的问题
     */
    public static String compressImage(String filePath, String fileName, int q)
            throws FileNotFoundException {

        Bitmap bm = getSmallBitmap(filePath);

        int degree = readPictureDegree(filePath);

        if (degree != 0) {// 旋转照片角度
            bm = rotateBitmap(bm, degree);
        }

        File imageDir = CachePathUtil.getCachePathFile("picture");

        if (!imageDir.exists()) {
            imageDir.mkdirs();
        }

        File outputFile = new File(imageDir, fileName);

        FileOutputStream out = new FileOutputStream(outputFile);

        bm.compress(Bitmap.CompressFormat.JPEG, q, out);
        bm.recycle();

        return outputFile.getPath();
    }

    /**
     * 压缩图片
     */
    public static void compressFiles(List<File> files,
                                     CompressImageResponse response) {

        CompressImageCacheTask cacheTask = new CompressImageCacheTask(files,
                response);
        cacheTask.execute(100);

    }

    public interface CompressImageResponse {

        void onSuccess(List<File> imgs);

        void onDo();

        void onFail();

        void onFinish();
    }

    static class CompressImageCacheTask extends
            AsyncTask<Integer, Integer, Boolean> {
        // 后面尖括号内分别是参数（例子里是线程休息时间），进度(publishProgress用到)，返回值 类型

        public CompressImageResponse response;
        public List<File> files;
        public List<File> resultFile;

        public CompressImageCacheTask(List<File> files,
                                      CompressImageResponse response) {
            this.files = files;
            this.response = response;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Integer... params) {
            publishProgress(0);
            resultFile = new ArrayList<>();
            for (int i = 0; i < files.size(); i++) {

                File of = files.get(i);
                try {
                    String key = String.valueOf(System.currentTimeMillis());
                    File file = new File(compressImage(of.getAbsolutePath(),
                            key + "_token.jpg", 50));
                    resultFile.add(file);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            return true;
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            // 这个函数在doInBackground调用publishProgress时触发，虽然调用时只有一个参数
            // 但是这里取到的是一个数组,所以要用progesss[0]来取值
            // 第n个参数就用progress[n]来取值
            response.onDo();
            super.onProgressUpdate(progress);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            // doInBackground返回时触发，换句话说，就是doInBackground执行完后触发
            // 这里的result就是上面doInBackground执行后的返回值，所以这里是"执行完毕"
            if (result) {
                response.onSuccess(resultFile);
            } else {
                response.onFail();
            }

            response.onFinish();

            super.onPostExecute(result);
        }

    }

    /***
     * 图片的缩放方法
     *
     * @param bgimage
     *            源图片资源
     * @param newWidth
     *            缩放后宽度
     * @param newHeight
     *           缩放后高度
     * @return
     */
    public static Bitmap zoomImage(Bitmap bgimage, double newWidth,
                                   double newHeight) {
        // 获取这个图片的宽和高
        float width = bgimage.getWidth();
        float height = bgimage.getHeight();
        //创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        // 计算宽高缩放率
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 缩放图片动作
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width,
                (int) height, matrix, true);
        return bitmap;
    }

    /**
     * base64转为bitmap
     *
     * @param base64Data
     * @return
     */
    public static Bitmap base64ToBitmap(String base64Data) {
        if (ToolUtils.isNull(base64Data)) {
            return null;
        }
        byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    /**
     * bitmap转为base64
     * @param bitmap
     * @return
     */
    public static String bitmapToBase64(Bitmap bitmap, int quality) {
        String result = "";
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
                baos.flush();
                baos.close();
                byte[] bitmapBytes = baos.toByteArray();
                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }


    public static Bitmap bimapRound(Bitmap mBitmap, float index) {
        Bitmap bitmap = Bitmap.createBitmap(mBitmap.getWidth(), mBitmap.getHeight(), Bitmap.Config.ARGB_4444);

        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);

        //设置矩形大小
        Rect rect = new Rect(0, 0, mBitmap.getWidth(), mBitmap.getHeight());
        RectF rectf = new RectF(rect);

        // 相当于清屏
        canvas.drawARGB(0, 0, 0, 0);
        //画圆角
        canvas.drawRoundRect(rectf, index, index, paint);
        // 取两层绘制，显示上层
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        // 把原生的图片放到这个画布上，使之带有画布的效果
        canvas.drawBitmap(mBitmap, rect, rect, paint);
        return bitmap;

    }

    /**
     * 裁剪
     * @param bitmap 原图
     * @return 裁剪后的图像
     */
    public static Bitmap cropBitmap(Bitmap bitmap, int a_width, int a_height) {
        int w = bitmap.getWidth(); // 得到图片的宽，高
        int h = bitmap.getHeight();
        if (w < a_width || h < a_height) {
            return bitmap;
        }
        int x = (w / 2 - a_width / 2);
        int y = (h / 2 - a_height / 2);
        return Bitmap.createBitmap(bitmap, x, y, a_width, a_height, null, false);
    }

    /**
     * 多张图片拼接9宫格
     * @param margin
     * @param bitmaps
     * @return
     */
    public static Bitmap addBitmaps(int margin, Bitmap... bitmaps) {
        int row=1;
        int col=0;
        int width = 0;
        int height = 0;
        int totalHeight = 0;
        int length = bitmaps.length;
        if(length>3){
            row=length%3==0?length/3:length/3+1;
            col=3;
        }else{
            row=1;
            col=length;
        }
        for (int i = 0; i < length; i++) {
            height = Math.max(height, bitmaps[i].getHeight());
        }
        totalHeight=height*row;
        totalHeight+=(row-1)*margin;

        for (int i = 0; i < col; i++) {
            width += bitmaps[i].getWidth();
            width += margin;
        }
        width -= margin;
        Bitmap result = Bitmap.createBitmap(width, totalHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        for (int i = 0; i < row; i++) {
            int left = 0;
            for (int i1 = 0; i1 < col; i1++) {
                if(i*col+i1 >=length){
                    break;
                }
                if(i>0){
                    if(i1>0){
                        left += bitmaps[i*col+i1-1].getWidth();
                        left += margin;
                        int top=(height+margin)*i;
                        canvas.drawBitmap(bitmaps[i*col+i1], left,top, null);
                    }else{
                        left =0;
                        left += margin;
                        int top=(height+margin)*i;
                        canvas.drawBitmap(bitmaps[i*col+i1], left,top, null);
                    }
                }else{
                    //第1行
                    if(i1>0){
                        left += bitmaps[i1-1].getWidth();
                        left += margin;
                        canvas.drawBitmap(bitmaps[i1], left,0, null);
                    }else{
                        left =0;
                        left += margin;
                        canvas.drawBitmap(bitmaps[i1], left,0, null);
                    }
                }
            }
        }
        return result;
    }
}
