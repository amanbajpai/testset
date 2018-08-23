package com.keykeep.app.views.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.keykeep.app.R;
import com.keykeep.app.views.adapter.AssetAdapter;

/**
 * Created by akshaydashore on 23/8/18
 */

public class AllAssetListFragment extends BaseFragment {


    private Context context;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.all_asset_list_fragment, null);
        initializeViews(view);
        Log.e( "onCreateView: ","alllll" );
        return view;
    }

    @Override
    public void initializeViews(View rootView) {

        RecyclerView recyclerView = rootView.findViewById(R.id.recycler_view);
        LinearLayoutManager manager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(manager);
        AssetAdapter assetAdapter = new AssetAdapter(context);
        recyclerView.setAdapter(assetAdapter);
    }

    @Override
    public void onClick(View view) {
    }

}
