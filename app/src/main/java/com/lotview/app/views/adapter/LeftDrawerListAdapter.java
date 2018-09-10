package com.lotview.app.views.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lotview.app.R;
import com.lotview.app.databinding.LeftDrawerItemsBinding;
import com.lotview.app.model.LeftMenuDrawerItems;

import java.util.List;

/**
 */
public class LeftDrawerListAdapter extends RecyclerView.Adapter<LeftDrawerListAdapter.BindingHolder> {

    Context context;
    private List<LeftMenuDrawerItems> leftDrawerMenuList;
    OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public LeftDrawerListAdapter(Context context, List<LeftMenuDrawerItems> leftDrawerMenuList) {
        this.context = context;
        this.leftDrawerMenuList = leftDrawerMenuList;
    }

    @Override
    public BindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LeftDrawerItemsBinding commentsHeaderBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.left_drawer_items,
                parent,
                false);
        return new BindingHolder(commentsHeaderBinding);
    }

    @Override
    public void onBindViewHolder(BindingHolder holder, final int position) {

        LeftMenuDrawerItems leftMenuDrawerItems = leftDrawerMenuList.get(position);
        holder.binding.leftDrawerTxt.setText(leftMenuDrawerItems.getTitle());

        if (leftDrawerMenuList.get(position).isMenuIsSelected()) {
            holder.binding.leftDrawerTxt.setTextColor(context.getResources().getColor(R.color.black));
            holder.binding.rootLayout.setBackgroundColor(context.getResources().getColor(R.color.white));
            holder.binding.leftDrawerIv.setImageResource(leftDrawerMenuList.get(position).getSelectedIcon());
        } else {
            holder.binding.leftDrawerTxt.setTextColor(context.getResources().getColor(R.color.white));
            holder.binding.rootLayout.setBackgroundColor(context.getResources().getColor(R.color.app_blue));
            holder.binding.leftDrawerIv.setImageResource(leftDrawerMenuList.get(position).getDeSelectedIcon());
        }

        holder.binding.rootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onItemClick(position);
            }
        });

        holder.binding.leftDrawerTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return leftDrawerMenuList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }


    public static class BindingHolder extends RecyclerView.ViewHolder {
        private LeftDrawerItemsBinding binding;

        public BindingHolder(LeftDrawerItemsBinding binding) {
            super(binding.rootLayout);
            this.binding = binding;
        }
    }

//    public class MyViewHolder extends RecyclerView.ViewHolder {
//
//        public TextView leftMenuTxt;
//        LinearLayout rootLayout;
//        ImageView menuIcon;
//
//        public MyViewHolder(View view) {
//            super(view);
//            leftMenuTxt = (TextView) view.findViewById(R.id.leftDrawerTxt);
//            rootLayout = (LinearLayout) view.findViewById(R.id.rootLayout);
//            menuIcon = (ImageView) view.findViewById(R.id.left_drawer_iv);
//        }
//    }

}
