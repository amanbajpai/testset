package com.keykeep.app.views.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.keykeep.app.R;
import com.keykeep.app.model.bean.AssetsListResponseBean;
import com.keykeep.app.utils.AppUtils;
import com.keykeep.app.views.activity.assetDetail.AssetDetailActivity;

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

    public AllAssetsAdapter(Context context, ArrayList<AssetsListResponseBean.Result> resultAssetList) {
        this.context = context;
        this.assetLists = resultAssetList;
        this.assetfilteredFinalList = resultAssetList;

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
        View view = inflater.inflate(R.layout.asset_recycler_item, null);
        Holder holder = new Holder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AssetsListResponseBean.Result bean = assetLists.get(position);
                Intent intent = new Intent(context, AssetDetailActivity.class);
                intent.putExtra(AppUtils.ASSET_STATUS_CODE, AppUtils.STATUS_ASSET_LIST);
                intent.putExtra(AppUtils.SCANED_QR_CODE, bean.getQrCodeNumber());
                context.startActivity(intent);
            }
        });
        holder.assetName.setText(assetLists.get(position).getAssetName());
        holder.modelNumber.setText(assetLists.get(position).getModelNumber());
        holder.vinNumber.setText(assetLists.get(position).getVin());
        holder.versionNumber.setText(assetLists.get(position).getVersionNumber());
        if (assetLists.get(position).getAssetAssginedStatus().equalsIgnoreCase("1")) {
            holder.availableStatus.setText(context.getString(R.string.txt_status_available));
        } else {
            holder.availableStatus.setText(context.getString(R.string.txt_status_unavailable));
        }

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

        private AppCompatTextView assetName, modelNumber, vinNumber, versionNumber, availableStatus;

        public Holder(View itemView) {
            super(itemView);
            assetName = itemView.findViewById(R.id.tv_asset_name);
            modelNumber = itemView.findViewById(R.id.tv_model_number);
            vinNumber = itemView.findViewById(R.id.tv_vin_number);
            versionNumber = itemView.findViewById(R.id.tv_version_number);
            availableStatus = itemView.findViewById(R.id.tv_available_status);
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
        }
    }

}
