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
import android.widget.TextView;

import com.keykeeper.app.R;
import com.keykeeper.app.model.bean.AssetsListResponseBean;
import com.keykeeper.app.preferences.AppSharedPrefs;
import com.keykeeper.app.utils.AppUtils;
import com.keykeeper.app.utils.Connectivity;
import com.keykeeper.app.utils.Utils;
import com.keykeeper.app.views.activity.assetDetail.AssetDetailActivity;
import com.keykeeper.app.views.custom_view.StyledTextViewBold;

import java.util.ArrayList;

/**
 * Created by akshaydashore on 23/8/18
 */
public class MyAssetsAdapter extends RecyclerView.Adapter<MyAssetsAdapter.Holder> implements Filterable {

    Context context;
    private ArrayList<AssetsListResponseBean.Result> assetLists;
    private ArrayList<AssetsListResponseBean.Result> assetfilterList;
    private ArrayList<AssetsListResponseBean.Result> assetfilteredFinalList;
    private MyAssetsAdapter.AllAssetsSearchFilter filter;
    int REQ_TYPE;
    OnActivityResult listener;

    public MyAssetsAdapter(Context context, ArrayList<AssetsListResponseBean.Result> resultAssetList, int REQ_TYPE,OnActivityResult listener) {
        this.context = context;
        this.assetLists = resultAssetList;
        this.assetfilteredFinalList = resultAssetList;
        this.REQ_TYPE = REQ_TYPE;
        this.listener = listener;
    }

    public void setAssetList(Context context, ArrayList<AssetsListResponseBean.Result> resultAssetList) {
        this.context = context;
        this.assetLists = resultAssetList;
        this.assetfilteredFinalList = resultAssetList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyAssetsAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.key_list_my_item, null);
        MyAssetsAdapter.Holder holder = new MyAssetsAdapter.Holder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyAssetsAdapter.Holder holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Connectivity.isConnected()) {
                    AssetsListResponseBean.Result bean = assetLists.get(position);
                    Intent intent = new Intent(context, AssetDetailActivity.class);
                    intent.putExtra(AppUtils.ASSET_STATUS_CODE, REQ_TYPE);
                    intent.putExtra(AppUtils.ASSET_ID,bean.getAssetId());
                    intent.putExtra(AppUtils.SCANED_QR_CODE, bean.getQrCodeNumber());
                    listener.CallOnActivityResult(intent);
                }else {
                    Utils.showSnackBar(context,view, context.getString(R.string.internet_connection));
                }

            }
        });

        AssetsListResponseBean.Result bean = assetLists.get(position);

        Log.e(bean.getAssetType() + "<<", "onBindViewHolder: " + AppUtils.ASSET_NEW);

        if (Utils.validateIntValue(bean.getAssetType()).equals(AppUtils.ASSET_CUSTOMER)) {
            holder.vinNumber.setText(Utils.validateStringToValue(bean.getCustomerName()));
        } else {
            holder.vinNumber.setText("VIN Number: " + assetLists.get(position).getVin());
        }

        // stock number
        holder.tv_stock_number.setText(assetLists.get(position).getAssetName());

        if (bean.getAssetAssginedStatus().equals("1")) {
            String mEmp_id = AppSharedPrefs.getEmployeeID();
            if (Utils.validateStringToValue(bean.getEmployeeId()).equals(mEmp_id)) {
                holder.availability_tv.setText(context.getString(R.string.owner_you));

            } else {
                holder.availability_tv.setText(context.getString(R.string.owner) + " " + bean.getEmployeeName());
            }
        } else {
            holder.availability_tv.setText(context.getString(R.string.txt_status_available));
        }

        String date = Utils.formattedDateFromString(Utils.INPUT_DATE_TIME_FORMATE, Utils.OUTPUT_DATE_TIME_FORMATE, bean.getAssigned_approved_or_decline_at());
        String time = Utils.formattedDateFromString(Utils.INPUT_TIME_FORMATE, Utils.OUTPUT_HOUR_TIME_FORMATE, bean.getAssets_hold_remain_time(),false);

        //currently use to show owner
        holder.assigned_at_tv.setText("Assigned At: " + date);
        holder.remaining_time.setText("Remaining Time: " + time);

    }


    @Override
    public int getItemCount() {
        return assetLists.size();
    }

    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new MyAssetsAdapter.AllAssetsSearchFilter();
        }
        return filter;
    }

    public ArrayList<AssetsListResponseBean.Result> getAssetLists() {
        return assetfilterList;
    }

    class Holder extends RecyclerView.ViewHolder {

        private TextView vinNumber, assigned_at_tv, availability_tv, remaining_time;
        StyledTextViewBold tv_stock_number;

        public Holder(View itemView) {
            super(itemView);
            tv_stock_number = itemView.findViewById(R.id.tv_stock_number);
            assigned_at_tv = itemView.findViewById(R.id.assigned_at_tv);
            vinNumber = itemView.findViewById(R.id.tv_vin_number);
            availability_tv = itemView.findViewById(R.id.availability_tv);
            remaining_time = itemView.findViewById(R.id.remaining_time);
        }

    }


//    private class AllAssetsSearchFilter extends Filter {
//
//        FilterResults results = new FilterResults();
//
//        @Override
//        protected FilterResults performFiltering(CharSequence constraint) {
//            if (constraint != null && constraint.length() > 0) {
//
//                assetfilterList = new ArrayList<AssetsListResponseBean.Result>();
//                for (int i = 0; i < assetfilteredFinalList.size(); i++) {
//                    if (assetfilteredFinalList.get(i).getAssetName() != null) {
//                        if ((assetfilteredFinalList.get(i).getAssetName().toUpperCase())
//                                .contains(constraint.toString().toUpperCase())) {
//                            assetfilterList.add(assetfilteredFinalList.get(i));
//                        }
//                    }
//                }
//                results.count = assetfilterList.size();
//                results.values = assetfilterList;
//            } else {
//                results.count = assetfilteredFinalList.size();
//                results.values = assetfilteredFinalList;
//            }
//            return results;
//        }
//
//        @Override
//        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
//            assetLists = (ArrayList<AssetsListResponseBean.Result>) filterResults.values;
//            notifyDataSetChanged();
//            Intent intent = new Intent(AppUtils.IS_ASSET_LIST_AVAILABLE);
//            if (assetLists.isEmpty()) {
//                intent.putExtra(AppUtils.ASSET_AVAILABLE_STATUS, false);
//            } else {
//                intent.putExtra(AppUtils.ASSET_AVAILABLE_STATUS, true);
//            }
//            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
//        }
//    }


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
