package com.keykeeper.app.views.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.keykeeper.app.R;
import com.keykeeper.app.databinding.NotificationRecyclerItemBinding;
import com.keykeeper.app.model.bean.NotificationsResponseBean;
import com.keykeeper.app.utils.Utils;

import java.util.ArrayList;

/**
 */
public class NotificationsListAdapter extends RecyclerView.Adapter<NotificationsListAdapter.BindingHolder> {

    Context context;
    private ArrayList<NotificationsResponseBean.Result> notificationsResponseBeanArrayList;
    OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public NotificationsListAdapter(Context context, ArrayList<NotificationsResponseBean.Result> resultArrayList) {
        this.context = context;
        this.notificationsResponseBeanArrayList = resultArrayList;
    }

    @Override
    public BindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        NotificationRecyclerItemBinding commentsHeaderBinding = DataBindingUtil.inflate(
                inflater,
                R.layout.notification_recycler_item,
                parent, false);

        return new BindingHolder(commentsHeaderBinding);
    }

    @Override
    public void onBindViewHolder(BindingHolder holder, final int position) {

        NotificationsResponseBean.Result notificationsResponseBean = notificationsResponseBeanArrayList.get(position);

        holder.binding.tvTittle.setText(notificationsResponseBean.getNotificationTitle());
        holder.binding.tvBody.setText(notificationsResponseBean.getNotificationDescription());
        holder.binding.tvTime.setText(Utils.formattedDateFromString(Utils.INPUT_DATE_TIME_FORMATE, Utils.OUTPUT_DATE_TIME_FORMATE, notificationsResponseBean.getNotification_created_time()));
    }

    public void setNotificationList(Context context, ArrayList<NotificationsResponseBean.Result> resultArrayList) {
        this.context = context;
        this.notificationsResponseBeanArrayList = resultArrayList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return notificationsResponseBeanArrayList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public static class BindingHolder extends RecyclerView.ViewHolder {
        private NotificationRecyclerItemBinding binding;

        public BindingHolder(NotificationRecyclerItemBinding binding) {
            super(binding.rootlayout);
            this.binding = binding;
        }
    }

}
