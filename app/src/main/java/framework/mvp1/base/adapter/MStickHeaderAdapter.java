package framework.mvp1.base.adapter;

import android.content.Context;

public class MStickHeaderAdapter<B extends Object> extends MCommAdapter<B> {
    public MStickHeaderAdapter(Context context, MCommVH.MCommVHInterface<B> mCommVHInterface) {
        super(context, mCommVHInterface);
    }


    /**
     * 判断position对应的Item是否是组的第一项
     *
     * @param position
     * @return
     */
    public boolean isItemHeader(int position) {
        if (position == 0) {
            return true;
        } else {
            String lastGroupName = getBeanList().get(position - 1).toString();
            String currentGroupName = getBeanList().get(position).toString();
            //判断上一个数据的组别和下一个数据的组别是否一致，如果不一致则是不同组，也就是为第一项（头部）
            if (lastGroupName.equals(currentGroupName)) {
                return false;
            } else {
                return true;
            }
        }
    }

    /**
     * 获取position对应的Item组名
     *
     * @param position
     * @return
     */
    public String getGroupName(int position) {
        return getBeanList().get(position).toString();
    }


}
