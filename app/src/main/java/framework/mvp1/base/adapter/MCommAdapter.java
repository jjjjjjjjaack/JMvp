package framework.mvp1.base.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;


import com.jjjjjjjjaack.jmvp.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lzj on 2017/8/24.
 */

public class MCommAdapter<B extends Object> extends MBaseAdapter<MCommVH> {


    private Context mContext;
    private List<B> beanList = new ArrayList<B>();
    private LayoutInflater mLayoutInflater;

    //    private MCommAdapterInterface mCommAdapterInterface;
    private MCommVH.MCommVHInterface<B> mCommVHInterface;

    private boolean showEmptyView;
    private boolean isEmpty;
    private View emptyVIew;
    public Map<String, Object> variableMap = new HashMap<>();

    public interface MCommAdapterInterface {
        void isNoData(boolean flag);
    }

    public MCommAdapter(Context context, MCommVH.MCommVHInterface<B> mCommVHInterface) {
        this.mContext = context;
        this.mLayoutInflater = LayoutInflater.from(mContext);
//        this.mCommAdapterInterface = mCommAdapterInterface;
        this.mCommVHInterface = mCommVHInterface;
    }

    public List<B> getBeanList() {
        return beanList;
    }

    public void setData(List<B> list) {
        if (list == null) {
            return;
        }
        this.beanList.clear();
        this.beanList.addAll(list);
        isEmpty = false;
        if (showEmptyView && this.beanList.size() == 0) {
            isEmpty = true;
        }
        notifyDataSetChanged();
//        mCommAdapterInterface.isNoData(getItemCount() <= 0);
    }

    public void addData(List<B> list) {
        if (list == null) {
            return;
        }
        this.beanList.addAll(list);
        isEmpty = false;
        if (showEmptyView && this.beanList.size() == 0) {
            isEmpty = true;
        }
        notifyDataSetChanged();
//        mCommAdapterInterface.isNoData(getItemCount() <= 0);
    }


    public void addOneData(B b, int index) {
        this.beanList.add(index, b);
        isEmpty = false;
        if (showEmptyView && this.beanList.size() == 0) {
            isEmpty = true;
        }
        notifyDataSetChanged();
//        mCommAdapterInterface.isNoData(getItemCount() <= 0);
    }

    public void addOneData(B b) {
        this.beanList.add(b);
        isEmpty = false;
        if (showEmptyView && this.beanList.size() == 0) {
            isEmpty = true;
        }
        notifyDataSetChanged();
//        mCommAdapterInterface.i
    }


    @Override
    public int getItemCount() {
        return isEmpty ? 1 : beanList.size();
    }

    /**
     * @param
     * @return
     * @description 返回当前数组大小
     * @author jiejack
     * @time 2022/2/4 9:36 下午
     */
    public int getSize() {
        return beanList.size();
    }

    @Override
    public MCommVH onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == -1) {
            return new MCommVH(mLayoutInflater.inflate(R.layout.view_listdata_empty,
                    parent, false), mContext, mCommVHInterface, this);
        }
        return new MCommVH(mLayoutInflater.inflate(mCommVHInterface.setLayout(), parent, false), mContext, mCommVHInterface, this);
    }

    @Override
    public void onBindViewHolder(@NonNull MCommVH holder, int position) {
        if (!isEmpty) {
            holder.bindData(position, beanList.get(position));
        }
    }


    @Override
    public int getItemViewType(int position) {
        return isEmpty ? -1 : super.getItemViewType(position);
    }

    /**
     * @param
     * @return
     * @description 设置是否显示空视图
     * @author jiejack
     * @time 2022/2/3 12:31 上午
     */
    public void setShowEmptyView(boolean showEmptyView) {
        this.showEmptyView = showEmptyView;
    }

    /**
     * @description 交换位置
     * @param
     * @return
     * @author jieja
     * @time 2022/4/24 10:09
     */
    public void move(int origin, int target) {
        if (origin < target) {
            for (int i = origin; i < target; i++) {
                Collections.swap(getBeanList(), i, i + 1);
            }
        }
        if (origin > target) {
            for (int i = origin; i > target; i--) {
                Collections.swap(getBeanList(), i, i - 1);
            }
        }
        notifyItemMoved(origin, target);
    }

    /**
     * @param
     * @return
     * @description
     * @author jieja
     * @time 2022/4/12 9:09
     */
    public void setVarValue(String key, Object value) {
        variableMap.put(key, value);
    }

    public Object getVarValue(String key) {
        return variableMap.get(key);
    }
}

