package com.keykeep.app.views.fragment.ownAssetsFragment;

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
import com.keykeep.app.databinding.MyAssetListFragmentBinding;
import com.keykeep.app.model.bean.AssetsListResponseBean;
import com.keykeep.app.utils.Utils;
import com.keykeep.app.views.adapter.AllAssetsAdapter;
import com.keykeep.app.views.base.BaseFragment;

import java.util.ArrayList;

/**
 * Created by akshaydashore on 23/8/18
 */

public class MyAssetsListFragment extends BaseFragment implements XRecyclerView.LoadingListener {

    private Context context;
    private MyAssetListFragmentBinding binding;
    MyAssetsFragmentViewModel viewModel;
    private AllAssetsAdapter myAssetAdapter;
    private ArrayList<AssetsListResponseBean.Result> resultArrayList;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.my_asset_list_fragment, container, false);
        viewModel = ViewModelProviders.of(this).get(MyAssetsFragmentViewModel.class);
        binding.setViewModel(viewModel);
        initializeViews(binding.getRoot());
        Utils.showProgressDialog(context,getString(R.string.loading));
        viewModel.getMyAssets(binding);
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
        myAssetAdapter = new AllAssetsAdapter(context, resultArrayList);
        binding.recyclerView.setAdapter(myAssetAdapter);
        viewModel.response_validator.observe(this, response_observer);
        Utils.hideSoftKeyboard(getActivity());
        binding.simpleSearchView.setQueryHint("Search here");

        binding.simpleSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                myAssetAdapter.notifyDataSetChanged();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.equalsIgnoreCase("")) {
                    Utils.hideSoftKeyboard(getActivity());
                    myAssetAdapter.setAssetList(getContext(), resultArrayList);
                } else {
                    try {
                        myAssetAdapter.getFilter().filter(newText);
                        myAssetAdapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                return false;
            }
        });
    }

    @Override
    public void onClick(View view) {
    }


    Observer<AssetsListResponseBean> response_observer = new Observer<AssetsListResponseBean>() {

        @Override
        public void onChanged(@Nullable AssetsListResponseBean assetsListResponseBean) {
            Utils.hideProgressDialog();
            if (assetsListResponseBean != null && assetsListResponseBean.getResult() != null && assetsListResponseBean.getResult().size() > 0) {
                resultArrayList = assetsListResponseBean.getResult();
                myAssetAdapter.setAssetList(getActivity(),resultArrayList);

            }

        }

    };

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
