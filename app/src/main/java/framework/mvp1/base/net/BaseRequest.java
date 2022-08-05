package framework.mvp1.base.net;

import android.content.Context;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import framework.mvp1.base.annotation.AnnBaseReq;
import framework.mvp1.base.annotation.AnnReqPara;
import framework.mvp1.base.util.T;
import framework.mvp1.base.util.ToolUtils;

/**
 * Created by lzj on 2017/6/3.
 */

public class BaseRequest implements java.io.Serializable {

    public boolean needEncode = true;//是否需要解密

    public boolean needHandleResponse = true;
    /**
     * 单媒体
     */
    public SignMulitRequest signMulitRequest;

    /**
     * 媒体集合
     */
    public List<BaseMulitRequest> baseMulitRequests;

    /**
     * 丢入参数
     *
     * @return
     */
    public Map<String, Object> bulitReqMap() {
//        return new HashMap<String, String>();
        boolean hasAnnotation = this.getClass().isAnnotationPresent(AnnBaseReq.class);
        if (hasAnnotation) {
            return bulitReqMapAuto();
        } else {
            return new HashMap<>();
        }
    }

    /**
     * 自动注入参数
     *
     * @return
     */
    public Map<String, Object> bulitReqMapAuto() {
        Map map = new HashMap<String, Object>();
        Class<?> cls = this.getClass();
        Field fields[] = cls.getDeclaredFields();
        for (Field field : fields) {
            try {
//                AnnReqPara annBReq = field.getAnnotation(AnnReqPara.class);
                String fldSetName = field.getName();//判断是否使用注解
                boolean hasAnnotation = field.isAnnotationPresent(AnnReqPara.class);
                if(hasAnnotation){
                    AnnReqPara annBReq = field.getAnnotation(AnnReqPara.class);
                    String annName = annBReq.name();
                    if(!ToolUtils.isNull(annName)){
                        map.put(annName, field.get(this));
                    }else{
                        map.put(fldSetName, field.get(this));
                    }
                }else {
                    map.put(fldSetName, field.get(this));
                }
            } catch (Exception e) {
            }
        }
        return map;
    }

    /**
     * @param context
     * @return
     */
    public boolean checkNullTip(Context context) {
        if (context == null) {
            return false;
        }
        //获取成员变量的注解
        Class<?> cls = this.getClass();
        Field fields[] = cls.getDeclaredFields();
        for (Field field : fields) {
            try {
                //msg成员变量为private,故必须进行此操作
                field.setAccessible(true);
                AnnReqPara annBReq = field.getAnnotation(AnnReqPara.class);
                //这里把注解的内容赋值给变量
                if (annBReq.isCheckNull()) {
                    if (field.get(this) instanceof String) {
                        if (ToolUtils.isNull((String) field.get(this))) {
                            int rstring = annBReq.nullRTip();
                            if (rstring != -1) {
                                T.showShort(context, context.getString(rstring));
                            } else {
                                String tip = annBReq.nullTip();
                                if (!ToolUtils.isNull(tip)) {
                                    T.showShort(context, tip);
                                }
                            }
                            return false;
                        }
                    } else if (field.get(this) instanceof List) {
                        if (((List) field.get(this)) == null || ((List) field.get(this)).size() <= 0) {
                            int rstring = annBReq.nullRTip();
                            if (rstring != -1) {
                                T.showShort(context, context.getString(rstring));
                            } else {
                                String tip = annBReq.nullTip();
                                if (!ToolUtils.isNull(tip)) {
                                    T.showShort(context, tip);
                                }
                            }
                        }
                    } else {

                    }
                }
            } catch (Exception e) {
            }
        }
        return true;
    }

    /**
     *
     * @param object
     * @return true  通过；false 不通过
     */
    public boolean checkObjectNull(Context context, Object object) {
        //获取成员变量的注解
        Class<?> cls = object.getClass();
        Field fields[] = cls.getDeclaredFields();
        for (Field field : fields) {
            try {
                //msg成员变量为private,故必须进行此操作
                field.setAccessible(true);
                AnnReqPara annBReq = field.getAnnotation(AnnReqPara.class);
                //这里把注解的内容赋值给变量
                if (annBReq.isCheckNull()) {
                    if (field.get(this) instanceof String) {
                        if (ToolUtils.isNull((String) field.get(this))) {
                            int rstring = annBReq.nullRTip();
                            if (rstring != -1) {
                                T.showShort(context, context.getString(rstring));
                            } else {
                                String tip = annBReq.nullTip();
                                if (!ToolUtils.isNull(tip)) {
                                    T.showShort(context, tip);
                                }
                            }
                            return false;
                        }
                    } else if (field.get(this) instanceof List) {
                        if (((List) field.get(this)) == null || ((List) field.get(this)).size() <= 0) {
                            int rstring = annBReq.nullRTip();
                            if (rstring != -1) {
                                T.showShort(context, context.getString(rstring));
                            } else {
                                String tip = annBReq.nullTip();
                                if (!ToolUtils.isNull(tip)) {
                                    T.showShort(context, tip);
                                }
                            }
                            return false;
                        }
                    } else {
                        return checkObjectNull(context,field.get(this));
                    }
                }
            } catch (Exception e) {
            }
        }
        return true;
    }

        /**
         * 拼接某属性set 方法
         *
         * @param fldname
         * @return
         */
        private static String pareSetName (String fldname){
            if (null == fldname || "".equals(fldname)) {
                return null;
            }
            String pro = "get" + fldname.substring(0, 1).toUpperCase()
                    + fldname.substring(1);
            return pro;
        }


        /**
         * 判断该方法是否存在
         *
         * @param methods
         * @param met
         * @return
         */
        private static boolean checkMethod (Method methods[], String met){
            if (null != methods) {
                for (Method method : methods) {
                    if (met.equals(method.getName())) {
                        return true;
                    }
                }
            }
            return false;
        }


    }
