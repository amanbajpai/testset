package com.lotview.app.views.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.lotview.app.R;
import com.lotview.app.databinding.NotificationRecyclerItemBinding;
import com.lotview.app.model.bean.NotificationsResponseBean;

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
        NotificationRecyclerItemBinding commentsHeaderBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.notification_recycler_item,
                parent,
                false);

        return new BindingHolder(commentsHeaderBinding);
    }

    @Override
    public void onBindViewHolder(BindingHolder holder, final int position) {

        NotificationsResponseBean.Result notificationsResponseBean = notificationsResponseBeanArrayList.get(position);

        holder.binding.tvTittle.setText(notificationsResponseBean.getNotificationTitle());
        holder.binding.tvBody.setText(notificationsResponseBean.getNotificationDescription());
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
