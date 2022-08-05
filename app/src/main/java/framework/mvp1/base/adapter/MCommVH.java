package framework.mvp1.base.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.View;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.recyclerview.widget.RecyclerView;

import framework.mvp1.base.util.GlideUtils;

public class MCommVH<Bx extends Object> extends RecyclerView.ViewHolder {

    private int viewType;

    private Context context;
    public MCommAdapter adapter;

    /**
     * Views indexed with their IDs
     */
    private final SparseArray<View> views;

    public interface MCommVHInterface<B extends Object> {

        int setLayout();

        void initViews(Context context, MCommVH mCommVH, View itemView);

        void bindData(Context context, MCommVH mCommVH, int position, B b);
    }


    private MCommVHInterface mCommVHInterface;

    private MCommVH(@NonNull View itemView) {
        super(itemView);
        this.views = new SparseArray<>();
    }

//    public int getViewType() {
//        return viewType;
//    }
//
//    public void setViewType(int viewType) {
//        this.viewType = viewType;
//    }

    public MCommVH(@NonNull View itemView, Context context, MCommVHInterface mCommVHInterface, MCommAdapter mCommAdapter) {
//        this(itemView);
//        this.context = context;
//        this.mCommVHInterface = mCommVHInterface;
//        this.mCommVHInterface.initViews(context,this, itemView);
        this(itemView, context, mCommVHInterface, 0, mCommAdapter);
    }

    public MCommVH(@NonNull View itemView, Context context, MCommVHInterface mCommVHInterface, int viewType, MCommAdapter mCommAdapter) {
        this(itemView);
        this.context = context;
        this.mCommVHInterface = mCommVHInterface;
        this.mCommVHInterface.initViews(context, this, itemView);
        this.viewType = viewType;
        this.adapter = mCommAdapter;
    }

    public void bindData(int position, Bx b) {
        this.mCommVHInterface.bindData(context, this, position, b);
    }


    @SuppressWarnings("unchecked")
    public <T extends View> T getView(@IdRes int viewId) {
        View view = views.get(viewId);
        if (view == null) {
            view = itemView.findViewById(viewId);
            views.put(viewId, view);
        }
        return (T) view;
    }


    /**
     * Will set the text of a TextView.
     *
     * @param viewId The view id.
     * @param value  The text to put in the text view.
     * @return The BaseViewHolder for chaining.
     */
    public MCommVH setText(@IdRes int viewId, CharSequence value) {
        try {
            TextView view = getView(viewId);
            view.setText(value);
        } catch (Exception e) {
        }
        return this;
    }

    public MCommVH setText(@IdRes int viewId, @StringRes int strId) {
        try {
            TextView view = getView(viewId);
            view.setText(strId);
        } catch (Exception e) {
        }
        return this;
    }


    /**
     * Will set text color of a TextView.
     *
     * @param viewId    The view id.
     * @param textColor The text color (not a resource id).
     * @return The BaseViewHolder for chaining.
     */
    public MCommVH setTextColor(@IdRes int viewId, @ColorInt int textColor) {
        try {
            TextView view = getView(viewId);
            view.setTextColor(textColor);
        } catch (Exception e) {
        }
        return this;
    }


    /**
     * Will set the image of an ImageView from a resource id.
     *
     * @param viewId     The view id.
     * @param imageResId The image resource id.
     * @return The BaseViewHolder for chaining.
     */
    public MCommVH setImageResource(@IdRes int viewId, @DrawableRes int imageResId) {
        try {
            ImageView view = getView(viewId);
            view.setImageResource(imageResId);
        } catch (Exception e) {
        }
        return this;
    }

    /**
     * Will set the image of an ImageView from a resource id.
     *
     * @param viewId The view id.
     * @param url    The image resource id.
     * @return The BaseViewHolder for chaining.
     */
    public MCommVH loadImageResourceByGilde(@IdRes int viewId, String url) {
        try {
            ImageView view = getView(viewId);
            GlideUtils.loadImageDefult(context, url, view);
//            view.setImageResource(imageResId);
        } catch (Exception e) {

        }
        return this;
    }


    /**
     * Set a view visibility to VISIBLE (true) or INVISIBLE (false).
     *
     * @param viewId  The view id.
     * @param visible True for VISIBLE, false for INVISIBLE.
     * @return The BaseViewHolder for chaining.
     */
    public MCommVH setVisibleInvisible(@IdRes int viewId, boolean visible) {
        try {
            View view = getView(viewId);
            view.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
        } catch (Exception e) {
        }
        return this;
    }


    /**
     * Set a view visibility to VISIBLE (true) or INVISIBLE (false).
     *
     * @param viewId  The view id.
     * @param visible True for VISIBLE, false for INVISIBLE.
     * @return The BaseViewHolder for chaining.
     */
    public MCommVH setVisible(@IdRes int viewId, boolean visible) {
        try {
            View view = getView(viewId);
            view.setVisibility(visible ? View.VISIBLE : View.GONE);
        } catch (Exception e) {
        }
        return this;
    }

    /**
     * Sets the on click listener of the view.
     *
     * @param viewId   The view id.
     * @param listener The on click listener;
     * @return The BaseViewHolder for chaining.
     */
    @Deprecated
    public MCommVH setOnClickListener(@IdRes int viewId, View.OnClickListener listener) {
        try {
            View view = getView(viewId);
            view.setOnClickListener(listener);
        } catch (Exception e) {
        }
        return this;
    }


    /**
     * Will set the text of a TextView.
     *
     * @param viewId The view id.
     * @param value  The text to put in the text view.
     * @return The BaseViewHolder for chaining.
     */
    public MCommVH setViewSelect(@IdRes int viewId, boolean select) {
        try {
            View view = getView(viewId);
            view.setSelected(select);
        } catch (Exception e) {
        }
        return this;
    }

}
