package com.lotview.app.views.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lotview.app.R;
import com.lotview.app.model.bean.AssetsListResponseBean;
import com.lotview.app.model.bean.HistoryResponseBean;
import com.lotview.app.preferences.AppSharedPrefs;
import com.lotview.app.utils.AppUtils;
import com.lotview.app.utils.Utils;
import com.lotview.app.views.activity.assetDetail.AssetDetailActivity;
import com.lotview.app.views.activity.chat.ChatActivity;
import com.lotview.app.views.custom_view.StyledTextViewBold;

import java.util.ArrayList;

/**
 * Created by akshaydashore on 23/8/18
 */

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.Holder>  {

    Context context;
    private ArrayList<HistoryResponseBean.ResponseResult> historyList;

    public HistoryAdapter(Context context, ArrayList<HistoryResponseBean.ResponseResult> resultHistoryList) {
        this.context = context;
        this.historyList = resultHistoryList;
    }

    public void setHistoryList(Context context, ArrayList<HistoryResponseBean.ResponseResult> resultHistoryList) {
        this.context = context;
        this.historyList = resultHistoryList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.history_list_item, null);
        Holder holder = new Holder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                HistoryResponseBean.ResponseResult bean = historyList.get(position);

            }
        });

        HistoryResponseBean.ResponseResult bean = historyList.get(position);

        Log.e(bean.getAsset_transaction_log_id() + "<<", "onBindViewHolder: Asset Transaction Log Id" );

        holder.trans_disc_tv.setText(historyList.get(position).getTransaction_description());
        holder.created_at_tv.setText(historyList.get(position).getCreated_at());
    }


    @Override
    public int getItemCount() {
        return historyList.size();
    }

    class Holder extends RecyclerView.ViewHolder {

        private TextView trans_disc_tv, created_at_tv;

        public Holder(View itemView) {
            super(itemView);
            trans_disc_tv = itemView.findViewById(R.id.tv_trans_disc);
            created_at_tv = itemView.findViewById(R.id.tv_created);
        }
    }

}
