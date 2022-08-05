package framework.mvp1.base.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lzj on 2017/8/24.
 */

public class MMulitCommAdapter<B extends Object> extends MBaseAdapter<MMulitCommVH> {


    private Context mContext;
    private List<B> beanList = new ArrayList<B>();
    private LayoutInflater mLayoutInflater;

    private MCommAdapterInterface<B> mCommAdapterInterface;
    private MMulitCommVH.MCommVHInterface<B> mCommVHInterface;

    public interface MCommAdapterInterface<C> {
        MMulitCommVH onCreateViewHolder(LayoutInflater mLayoutInflater, ViewGroup parent, int viewType, Context context, MMulitCommVH.MCommVHInterface mCommVHInterface);

        int getItemViewType(int position, C b);
    }

    public MMulitCommAdapter(Context context, MCommAdapterInterface mCommAdapterInterface, MMulitCommVH.MCommVHInterface<B> mCommVHInterface) {
        this.mContext = context;
        this.mLayoutInflater = LayoutInflater.from(mContext);
        this.mCommAdapterInterface = mCommAdapterInterface;
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
        notifyDataSetChanged();
//        mCommAdapterInterface.isNoData(getItemCount() <= 0);
    }

    public void addData(List<B> list) {
        if (list == null) {
            return;
        }
        this.beanList.addAll(list);
        notifyDataSetChanged();
//        mCommAdapterInterface.isNoData(getItemCount() <= 0);
    }


    @Override
    public int getItemCount() {
        return beanList.size();
    }


    @Override
    public MMulitCommVH onCreateViewHolder(ViewGroup parent, int viewType) {
//        return new MCommVH(mLayoutInflater.inflate(mCommVHInterface.setLayout(), parent, false), mContext, mCommVHInterface);
        return mCommAdapterInterface.onCreateViewHolder(mLayoutInflater, parent, viewType, mContext, mCommVHInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull MMulitCommVH holder,final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClick != null) {
                    onItemClick.onItemClick(position, beanList.get(position));
                }
            }
        });
        holder.bindData(position, beanList.get(position));
    }


    @Override
    public int getItemViewType(int position) {
        try {
            return mCommAdapterInterface.getItemViewType(position, beanList.get(position));
        } catch (Exception e) {
            return -1;
        }
    }

    public B getLastBean() {
        try {
            return getBeanList().get(getBeanList().size() - 1);
        } catch (Exception e) {
            return null;
        }
    }
}
