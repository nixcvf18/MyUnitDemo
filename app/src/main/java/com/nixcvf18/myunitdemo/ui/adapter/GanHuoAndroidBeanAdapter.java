package com.nixcvf18.myunitdemo.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.nixcvf18.myunitdemo.R;
import com.nixcvf18.myunitdemo.data.GanHuoAndroidBean;
import com.nixcvf18.myunitdemo.data.GirlBean;
import com.nixcvf18.myunitdemo.ui.activity.GanHuoAndroidActivity;
import com.nixcvf18.myunitdemo.ui.activity.GirlActivity;

import java.util.ArrayList;

public class GanHuoAndroidBeanAdapter extends RecyclerView.Adapter<GanHuoAndroidBeanAdapter.ViewHolder
        > {


    private  String trueUrl;
    private Context context;


    private ArrayList<GanHuoAndroidBean> androidBeanArrayList = new ArrayList<>();

    public GanHuoAndroidBeanAdapter(Context context, ArrayList<GanHuoAndroidBean> androidBeanArrayList) {
        this.context = context;
        this.androidBeanArrayList = androidBeanArrayList;
    }



    public void addAll(ArrayList<GanHuoAndroidBean> ganHuoAndroidBeans) {

        androidBeanArrayList.clear();
        androidBeanArrayList.addAll(ganHuoAndroidBeans);
        //通知所有的观察者  数据集发生了改变
        notifyDataSetChanged();

    }




    public void loadMore(ArrayList<GanHuoAndroidBean>  androidBeans) {


        androidBeanArrayList.addAll(androidBeans);

    }




    /**
     * Called when RecyclerView needs a new {@link ViewHolder} of the given type to represent
     * an item.
     * <p>
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     * <p>
     * The new ViewHolder will be used to display items of the adapter using
     * {@link#onBindViewHolder(ViewHolder, int, List)}. Since it will be re-used to display
     * different items in the data set, it is a good idea to cache references to sub views of
     * the View to avoid unnecessary {@link View#findViewById(int)} calls.
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     * @see #getItemViewType(int)
     * @see #onBindViewHolder(ViewHolder, int)
     */
    @NonNull
    @Override
    public GanHuoAndroidBeanAdapter.ViewHolder  onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //将xml文件转换成view 然后将view 作为参数 传给ViewHolder实例
        return new GanHuoAndroidBeanAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_gan_huo_item, parent, false));




    }

    /**
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the {@link ViewHolder#itemView} to reflect the item at the given
     * position.
     * <p>
     * Note that unlike {@link ListView}, RecyclerView will not call this method
     * again if the position of the item changes in the data set unless the item itself is
     * invalidated or the new position cannot be determined. For this reason, you should only
     * use the <code>position</code> parameter while acquiring the related data item inside
     * this method and should not keep a copy of it. If you need the position of an item later
     * on (e.g. in a click listener), use {@link ViewHolder#getAdapterPosition()} which will
     * have the updated adapter position.
     * <p>
     * Override {@link#onBindViewHolder(ViewHolder, int, List)} instead if Adapter can
     * handle efficient partial bind.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        holder.bindMetaData(androidBeanArrayList.get(position));


    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return androidBeanArrayList.size() ;
    }




    class  ViewHolder extends  RecyclerView.ViewHolder{

        ImageView iv_image;
        TextView tv_title;
        TextView tv_views_created_time;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            iv_image = itemView.findViewById(R.id.iv_image);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_views_created_time = itemView.findViewById(R.id.tv_views_created_time);


            //textView = itemView.findViewById(R.id.textView);
        }




        void bindMetaData(GanHuoAndroidBean  bean) {
            if (bean.getImages() != null && bean.getImages().size() > 0) {

                trueUrl = bean.getImages().get(0);
            }





            //返回一个应用程序级别的 请求管理者
            Glide.with(context)
                    //返回一个用于 加载图片的  请求构造器
                    .load(trueUrl)
                    .apply(new RequestOptions().centerCrop())
                    //将资源对象 填充进imageview控件中
                    .into(iv_image)
            ;


            tv_title.setText(bean.getTitle());

            tv_views_created_time.setText(bean.getViews() + "浏览     " + bean.getCreatedAt());





            iv_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    Intent intent = new Intent(context, GanHuoAndroidActivity.class);

                    intent.putExtra("true_id", bean.get_id());
                    context.startActivity(intent);



                }
            });

        }








    }
}
