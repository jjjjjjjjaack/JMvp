package framework.mvp1.base.bean;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class BaseBean implements java.io.Serializable {

    @JSONField(serialize = false,deserialize = false)
    public String orginJson;

    public static <T extends BaseBean> T fromJSONAuto(String data, Class clazz) {
        try {
            T t = (T) clazz.newInstance();
            JSONObject jsonObject = JSONObject.parseObject(data);
            t.fromJSONAuto(jsonObject);
            return t;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void fromJSONAuto(String data) {
        JSONObject jsonObject = JSONObject.parseObject(data);
        fromJSONAuto(jsonObject);
    }

    public void fromJSONAuto(JSONObject json) {
        if (json == null) {
//            return;
            json = new JSONObject();
        }
        this.orginJson = json.toJSONString();
        Class<?> cls = this.getClass();
        Method methods[] = cls.getDeclaredMethods();
        Field fields[] = cls.getDeclaredFields();
        for (Field field : fields) {
            try {
                String fldSetName = field.getName();
                String setMethod = pareSetName(fldSetName);
                if (!checkMethod(methods, setMethod)) {
                    continue;
                }
                Object value = json.getString(fldSetName);
                Method method = cls.getMethod(setMethod, field.getType());
//                if (null != value) {
                if (isBaseDataType(field.getType())) {
                    if (field.getType().equals(boolean.class)||field.getType().equals(Boolean.class)) {
                        if (value == null) {
                            method.invoke(this, false);
                        } else {
                            method.invoke(this, json.getBooleanValue(fldSetName));
                        }
                    } else if (field.getType().equals(int.class) || field.getType().equals(Integer.class)) {
                        if (value == null) {
                            method.invoke(this, 0);
                        } else {
                            method.invoke(this, json.getInteger(fldSetName));
                        }
                    } else if (field.getType().equals(float.class) || field.getType().equals(Float.class)) {
                        if (value == null) {
                            method.invoke(this, 0);
                        } else {
                            method.invoke(this, json.getFloat(fldSetName));
                        }
                    }else if (field.getType().equals(double.class) || field.getType().equals(Double.class)) {
                        if (value == null) {
                            method.invoke(this, 0.0);
                        } else {
                            method.invoke(this, json.getDouble(fldSetName));
                        }
                    }else if (field.getType().equals(long.class) || field.getType().equals(Long.class)) {
                        if (value == null) {
                            method.invoke(this, 0f);
                        } else {
                            method.invoke(this, json.getLong(fldSetName));
                        }
                    } else if (field.getType().equals(String.class)) {
                        if (value == null) {
                            value = "";
                        }
                        method.invoke(this, value);
                    } else {
                        method.invoke(this, value);
                    }
                }
                else if (BaseBean.class.isAssignableFrom(field.getType())) {
                    try {
                        Object obj = field.getType().newInstance();
                        Class objClass = obj.getClass();
                        Method fromJSONMethod = objClass.getMethod(
                                "fromJSONAuto", String.class);
                        fromJSONMethod.invoke(obj, ((String) value));
                        method.invoke(this, obj);
                    } catch (Exception e) {
//                        e.printStackTrace();
                    }
                }
                else if (List.class.isAssignableFrom(field.getType())) {// 【2】
                    Type fc = (Type) field.getGenericType(); // 关键的地方，如果是List类型，得到其Generic的类型
                    if (fc instanceof ParameterizedType) // 【3】如果是泛型参数的类型
                    {
                        ParameterizedType pt = (ParameterizedType) fc;
                        Class genericClazz = (Class) pt
                                .getActualTypeArguments()[0]; // 【4】
                        if (isBaseDataType(genericClazz)) {
                            try {
                                List ja = JSONArray.parseArray((String) value, genericClazz);
                                method.invoke(this, ja);
                            } catch (Exception e) {
//                                e.printStackTrace();
                            }
                        } else
                            // 得到泛型里的class类型对象。
                            if (BaseBean.class.isAssignableFrom(genericClazz)) {
                                try {
                                    Method fromJSONMethod = genericClazz
                                            .getMethod("fromJSONListAuto",
                                                    String.class, Class.class);
                                    Object olist = fromJSONMethod.invoke(null,
                                            (String) value, genericClazz);
                                    method.invoke(this, olist);
                                } catch (Exception e) {
//                                    e.printStackTrace();
                                }
                            }
                    }
                }
                else{
                    try {
//                        Object obj = field.getType().newInstance();
//                        Class objClass = obj.getClass();
//                        Method fromJSONMethod = objClass.getMethod(
//                                "fromJSONAuto", String.class);
//                        fromJSONMethod.invoke(obj, ((String) value));
                        method.invoke(this, JSONObject.parse(value.toString()));
                    } catch (Exception e) {
//                        e.printStackTrace();
                    }
                }
//                }
            } catch (Exception e) {
//                e.printStackTrace();
            }
        }
    }

    public static List<?> fromJSONListAuto(String data, Class clazz) {
        List list = new ArrayList();
        try {
            JSONArray ja = JSONArray.parseArray(data);
            for (int i = 0; i < ja.size(); i++) {
                JSONObject json = ja.getJSONObject(i);
                Object obj = clazz.newInstance();
                Class objClass = obj.getClass();
                Method fromJSONMethod = objClass.getMethod(
                        "fromJSONAuto", JSONObject.class);
                fromJSONMethod.invoke(obj, json);
                list.add(obj);
            }
        } catch (Exception e) {
        }
//        for (int i = 0; i < 6; i++) {
//            list.addAll(list);
//        }
        return list;
    }

    /**
     * 拼接某属性get 方法
     *
     * @param fldname
     * @return
     */
    private static String pareGetName(String fldname) {
        if (null == fldname || "".equals(fldname)) {
            return null;
        }
        String pro = "get" + fldname.substring(0, 1).toUpperCase()
                + fldname.substring(1);
        return pro;
    }

    /**
     * 拼接某属性set 方法
     *
     * @param fldname
     * @return
     */
    private static String pareSetName(String fldname) {
        if (null == fldname || "".equals(fldname)) {
            return null;
        }
        String pro = "set" + fldname.substring(0, 1).toUpperCase()
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
    private static boolean checkMethod(Method methods[], String met) {
        if (null != methods) {
            for (Method method : methods) {
                if (met.equals(method.getName())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 把date 类转换成string
     *
     * @param date
     * @return
     */
    private static String fmlDate(Date date) {
        if (null != date) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                    Locale.US);
            return sdf.format(date);
        }
        return null;
    }

    /**
     * 判断一个类是否为基本数据类型。
     *
     * @param clazz 要判断的类。
     * @return true 表示为基本数据类型。
     */
    private static boolean isBaseDataType(Class clazz) throws Exception {
        return (clazz.equals(String.class) || clazz.equals(Integer.class)
                || clazz.equals(Byte.class) || clazz.equals(Long.class)
                || clazz.equals(Double.class) || clazz.equals(Float.class)
                || clazz.equals(Character.class) || clazz.equals(Short.class)
                || clazz.equals(BigDecimal.class)
                || clazz.equals(BigInteger.class)
                || clazz.equals(Boolean.class) || clazz.equals(Date.class) || clazz
                .isPrimitive());
    }


    public String getOrginJson() {
        return orginJson;
    }

    public void setOrginJson(String orginJson) {
        this.orginJson = orginJson;
    }
}
