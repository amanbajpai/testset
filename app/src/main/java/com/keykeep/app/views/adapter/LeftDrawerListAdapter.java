package com.keykeep.app.views.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.keykeep.app.R;
import com.keykeep.app.model.LeftMenuDrawerItems;

import java.util.List;

/**
 */
public class LeftDrawerListAdapter extends RecyclerView.Adapter<LeftDrawerListAdapter.MyViewHolder> {

    Context context;
    private List<LeftMenuDrawerItems> leftDrawerMenuList;
    OnItemClickListener onItemClickListener;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView leftMenuTxt;
        LinearLayout rootLayout;
        ImageView menuIcon;

        public MyViewHolder(View view) {
            super(view);
            leftMenuTxt = (TextView) view.findViewById(R.id.leftDrawerTxt);
            rootLayout = (LinearLayout) view.findViewById(R.id.rootLayout);
            menuIcon = (ImageView) view.findViewById(R.id.left_drawer_iv);
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


    public LeftDrawerListAdapter(Context context, List<LeftMenuDrawerItems> leftDrawerMenuList) {
        this.context = context;
        this.leftDrawerMenuList = leftDrawerMenuList;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.left_drawer_items, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        LeftMenuDrawerItems leftMenuDrawerItems = leftDrawerMenuList.get(position);

//        if (position != leftDrawerMenuList.size() - 2) {

        holder.leftMenuTxt.setText(leftMenuDrawerItems.getTitle());

        if (leftDrawerMenuList.get(position).isMenuIsSelected()) {

            holder.leftMenuTxt.setTextColor(context.getResources().getColor(R.color.white));
            holder.rootLayout.setBackgroundColor(context.getResources().getColor(R.color.grayseperator));
            holder.menuIcon.setImageResource(leftDrawerMenuList.get(position).getSelectedIcon());
        } else {

            holder.leftMenuTxt.setTextColor(context.getResources().getColor(R.color.black));
            holder.rootLayout.setBackgroundColor(context.getResources().getColor(R.color.white));
            holder.menuIcon.setImageResource(leftDrawerMenuList.get(position).getDeSelectedIcon());
        }

        holder.rootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onItemClick(position);
            }
        });
        holder.leftMenuTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onItemClick(position);
            }
        });

//        }

    }

    @Override
    public int getItemCount() {
        return leftDrawerMenuList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
