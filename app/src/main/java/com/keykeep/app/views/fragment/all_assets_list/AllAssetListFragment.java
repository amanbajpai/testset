package com.keykeep.app.views.fragment.all_assets_list;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.keykeep.app.R;
import com.keykeep.app.databinding.AllAssetListFragmentBinding;
import com.keykeep.app.model.bean.AssetsListResponseBean;
import com.keykeep.app.utils.Utils;
import com.keykeep.app.views.adapter.AllAssetsAdapter;
import com.keykeep.app.views.base.BaseFragment;

import java.util.ArrayList;

/**
 * Created by akshaydashore on 23/8/18
 */
public class AllAssetListFragment extends BaseFragment implements XRecyclerView.LoadingListener {

    AllAssetListFragmentViewModel viewModel;
    private Context context;
    private AllAssetListFragmentBinding binding;
    private AllAssetsAdapter allAssetAdapter;
    private ArrayList<AssetsListResponseBean.Result> resultArrayList;

    Observer<AssetsListResponseBean> response_observer = new Observer<AssetsListResponseBean>() {

        @Override
        public void onChanged(@Nullable AssetsListResponseBean assetsListResponseBean) {

            if (assetsListResponseBean != null && assetsListResponseBean.getResult() != null && assetsListResponseBean.getResult().size() > 0) {
                resultArrayList = assetsListResponseBean.getResult();
                allAssetAdapter = new AllAssetsAdapter(context, resultArrayList);
                binding.recyclerView.setAdapter(allAssetAdapter);
            }
        }

    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.all_asset_list_fragment, container, false);
        viewModel = ViewModelProviders.of(this).get(AllAssetListFragmentViewModel.class);
        binding.setViewModel(viewModel);
        initializeViews(binding.getRoot());
        viewModel.getAllAssets(binding);
        return binding.getRoot();
    }

    @Override
    public void initializeViews(View rootView) {
        resultArrayList = new ArrayList<>();
        LinearLayoutManager manager = new LinearLayoutManager(context);
        binding.recyclerView.setLayoutManager(manager);
        binding.recyclerView.setLoadingListener(this);
        binding.recyclerView.setLoadingMoreEnabled(false);
        binding.recyclerView.setPullRefreshEnabled(false);
        viewModel.response_validator.observe(this, response_observer);
        Utils.hideSoftKeyboard(getActivity());
        binding.simpleSearchView.setQueryHint("Search here");

        binding.simpleSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
//                Toast.makeText(context, query, Toast.LENGTH_LONG).show();
                /*allAssetAdapter.getFilter().filter(query);
                allAssetAdapter.notifyDataSetChanged();*/
                allAssetAdapter.notifyDataSetChanged();

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.equalsIgnoreCase("")) {
                    Utils.hideSoftKeyboard(getActivity());
                    binding.recyclerView.setVisibility(View.VISIBLE);
                    binding.tvNoRecords.setVisibility(View.GONE);
                    allAssetAdapter.setAssetList(getContext(), resultArrayList);
                } else {
                    allAssetAdapter.getFilter().filter(newText);
                    allAssetAdapter.notifyDataSetChanged();
                   // setSearchAssetStatus();

                }

                return false;
            }
        });

    }

    public void setSearchAssetStatus() {
        if (allAssetAdapter.getAssetLists() != null && allAssetAdapter.getAssetLists().size() == 0) {
            binding.recyclerView.setVisibility(View.GONE);
            binding.tvNoRecords.setVisibility(View.VISIBLE);
            binding.tvNoRecords.setText(getString(R.string.txt_no_records_avialable));
            allAssetAdapter.notifyDataSetChanged();
        } else if (allAssetAdapter.getAssetLists() != null && allAssetAdapter.getAssetLists().size() > 0) {
            binding.recyclerView.setVisibility(View.VISIBLE);
            binding.tvNoRecords.setVisibility(View.GONE);
            allAssetAdapter.notifyDataSetChanged();
        } else {
            binding.recyclerView.setVisibility(View.VISIBLE);
            binding.tvNoRecords.setVisibility(View.GONE);
            allAssetAdapter.notifyDataSetChanged();
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                allAssetAdapter.notifyDataSetChanged();
            }
        }, 1000);

    }

    @Override
    public void onClick(View view) {
    }

    @Override
    public void onRefresh() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.recyclerView.refreshComplete();
            }
        }, 2000);
    }

    @Override
    public void onLoadMore() {
        Log.e("onLoadMore: ", "call load more");
    }

}
