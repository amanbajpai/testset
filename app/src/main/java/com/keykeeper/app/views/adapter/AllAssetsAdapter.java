package com.keykeeper.app.views.adapter;

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

import com.keykeeper.app.R;
import com.keykeeper.app.model.bean.AssetsListResponseBean;
import com.keykeeper.app.preferences.AppSharedPrefs;
import com.keykeeper.app.utils.AppUtils;
import com.keykeeper.app.utils.Utils;
import com.keykeeper.app.views.activity.assetDetail.AssetDetailActivity;
import com.keykeeper.app.views.activity.chat.ChatActivity;
import com.keykeeper.app.views.custom_view.StyledTextViewBold;

import java.util.ArrayList;

/**
 * Created by akshaydashore on 23/8/18
 */

public class AllAssetsAdapter extends RecyclerView.Adapter<AllAssetsAdapter.Holder> implements Filterable {

    Context context;
    private ArrayList<AssetsListResponseBean.Result> assetLists;
    private ArrayList<AssetsListResponseBean.Result> assetfilterList;
    private ArrayList<AssetsListResponseBean.Result> assetfilteredFinalList;
    private AllAssetsSearchFilter filter;
    int REQ_TYPE;
    OnActivityResult listener;

    public AllAssetsAdapter(Context context, ArrayList<AssetsListResponseBean.Result> resultAssetList, int REQ_TYPE, OnActivityResult onActivityResult) {
        this.context = context;
        this.assetLists = resultAssetList;
        this.assetfilteredFinalList = resultAssetList;
        this.REQ_TYPE = REQ_TYPE;
        this.listener = onActivityResult;
    }

    public void setAssetList(Context context, ArrayList<AssetsListResponseBean.Result> resultAssetList) {
        this.context = context;
        this.assetLists = resultAssetList;
        this.assetfilteredFinalList = resultAssetList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.key_list_item, null);
        Holder holder = new Holder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AssetsListResponseBean.Result bean = assetLists.get(position);
                Intent intent = new Intent(context, AssetDetailActivity.class);
                intent.putExtra(AppUtils.ASSET_ID, bean.getAssetId());
                intent.putExtra(AppUtils.ASSET_STATUS_CODE, REQ_TYPE);
                intent.putExtra(AppUtils.SCANED_QR_CODE, bean.getQrCodeNumber());
                listener.CallOnActivityResult(intent);

            }
        });

        AssetsListResponseBean.Result bean = assetLists.get(position);

        Log.e(bean.getAssetType() + "<<", "onBindViewHolder: " + AppUtils.ASSET_NEW);

        if (Utils.validateIntValue(bean.getAssetType()).equals(AppUtils.ASSET_CUSTOMER)) {
            holder.vinNumber.setText(Utils.validateStringToValue(bean.getCustomerName()));
        } else {
            holder.vinNumber.setText("Vin Number: " + assetLists.get(position).getVin());
        }

        // stock number
        holder.tv_stock_number.setText(assetLists.get(position).getAssetName());

        if (bean.getAssetAssginedStatus().equals("1")) {
            String mEmp_id = AppSharedPrefs.getEmployeeID();
            if (Utils.validateStringToValue(bean.getEmployeeId()).equals(mEmp_id)) {
                holder.availability_tv.setText(context.getString(R.string.owner_you));
                holder.chatIconLayout.setVisibility(View.GONE);

            } else {
                holder.availability_tv.setText(context.getString(R.string.owner) + " " + bean.getEmployeeName());
                holder.chatIconLayout.setVisibility(View.VISIBLE);
            }
        } else {
            holder.availability_tv.setText(context.getString(R.string.txt_status_available));
            holder.chatIconLayout.setVisibility(View.GONE);
        }

        //currently use to show owner
        String type = Utils.getAssetType(bean.getAssetType());
        holder.asset_type.setText(type);

        holder.chatImgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatActivity.class);
                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return assetLists.size();
    }

    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new AllAssetsSearchFilter();
        }
        return filter;
    }

    public ArrayList<AssetsListResponseBean.Result> getAssetLists() {
        return assetfilterList;
    }

    class Holder extends RecyclerView.ViewHolder {

        private final LinearLayout chatIconLayout;
        private final ImageView chatImgView;
        private TextView vinNumber, asset_type, availability_tv;
        StyledTextViewBold tv_stock_number;

        public Holder(View itemView) {
            super(itemView);
            tv_stock_number = itemView.findViewById(R.id.tv_stock_number);
            asset_type = itemView.findViewById(R.id.asset_type);
            vinNumber = itemView.findViewById(R.id.tv_vin_number);
            availability_tv = itemView.findViewById(R.id.availability_tv);
            chatIconLayout = itemView.findViewById(R.id.chat_icon_layout);
            chatImgView = itemView.findViewById(R.id.chat_img_view);
        }
    }

/*
    public class AllAssetsSearchFilter extends Filter {

        private ArrayList<AssetsListResponseBean.Result> originalList;
        private final List<AssetsListResponseBean.Result> filteredList;

        public AllAssetsSearchFilter(List<AssetsListResponseBean.Result> originalList) {
            super();
            this.originalList = new ArrayList<>(originalList);
            this.originalList = assetListsFinal;
            this.filteredList = new ArrayList<>();
        }

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            filteredList.clear();
            final FilterResults results = new FilterResults();

            if (charSequence.length() == 0) {
                filteredList.addAll(originalList);
            } else if (charSequence != null && charSequence.length() > 0) {
                final String filterPattern = charSequence.toString().toLowerCase().trim();
                for (AssetsListResponseBean.Result item : originalList) {
                    if (item.getAssetName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            } else {
                results.values = filteredList;
                results.count = filteredList.size();
            }

            results.values = filteredList;
            results.count = filteredList.size();
            return results;
        }


        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            originalList = (ArrayList<AssetsListResponseBean.Result>) filterResults.values;
            assetLists = originalList;
            notifyDataSetChanged();
        }
    }
*/


    private class AllAssetsSearchFilter extends Filter {

        FilterResults results = new FilterResults();

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null && constraint.length() > 0) {

                assetfilterList = new ArrayList<AssetsListResponseBean.Result>();
                for (int i = 0; i < assetfilteredFinalList.size(); i++) {
                    if (assetfilteredFinalList.get(i).getAssetName() != null) {
                        if ((assetfilteredFinalList.get(i).getAssetName().toUpperCase())
                                .contains(constraint.toString().toUpperCase())) {
                            assetfilterList.add(assetfilteredFinalList.get(i));
                        }
                    }
                }
                results.count = assetfilterList.size();
                results.values = assetfilterList;
            } else {
                results.count = assetfilteredFinalList.size();
                results.values = assetfilteredFinalList;
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            assetLists = (ArrayList<AssetsListResponseBean.Result>) filterResults.values;
            notifyDataSetChanged();
            Intent intent = new Intent(AppUtils.IS_ASSET_LIST_AVAILABLE);
            if (assetLists.isEmpty()) {
                intent.putExtra(AppUtils.ASSET_AVAILABLE_STATUS, false);
            } else {
                intent.putExtra(AppUtils.ASSET_AVAILABLE_STATUS, true);
            }
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
        }
    }


   public interface OnActivityResult {

        public void CallOnActivityResult(Intent intent);
    }
}
