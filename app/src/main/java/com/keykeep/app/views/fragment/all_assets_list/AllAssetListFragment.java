package com.keykeep.app.views.fragment.all_assets_list;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.util.SortedList;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.keykeep.app.R;
import com.keykeep.app.databinding.AllAssetListFragmentBinding;
import com.keykeep.app.model.bean.AssetsListResponseBean;
import com.keykeep.app.utils.Utils;
import com.keykeep.app.views.adapter.AllAssetsAdapter;
import com.keykeep.app.views.base.BaseFragment;

/**
 * Created by akshaydashore on 23/8/18
 */
public class AllAssetListFragment extends BaseFragment implements XRecyclerView.LoadingListener {

    private Context context;
    private AllAssetListFragmentBinding binding;
    AllAssetListFragmentViewModel viewModel;
    private AllAssetsAdapter allAssetAdapter;

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

        LinearLayoutManager manager = new LinearLayoutManager(context);
        binding.recyclerView.setLayoutManager(manager);
        binding.recyclerView.setLoadingListener(this);
        viewModel.response_validator.observe(this, response_observer);
        Utils.hideSoftKeyboard(getActivity());
        binding.simpleSearchView.setQueryHint("Search here");

        binding.simpleSearchView.setOnQueryTextListener(new android.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
//                Toast.makeText(context, query, Toast.LENGTH_LONG).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
//                Toast.makeText(context, newText, Toast.LENGTH_LONG).show();
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

            if (assetsListResponseBean != null && assetsListResponseBean.getResult() != null && assetsListResponseBean.getResult().size() > 0) {
                allAssetAdapter = new AllAssetsAdapter(context, assetsListResponseBean);
                binding.recyclerView.setAdapter(allAssetAdapter);
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

//    private final SortedList.Callback<AssetsListResponseBean> mCallback = new SortedList.Callback<AssetsListResponseBean>() {
//
//        @Override
//        public void onInserted(int position, int count) {
//            allAssetAdapter.notifyItemRangeInserted(position, count);
//        }
//
//        @Override
//        public void onRemoved(int position, int count) {
//            allAssetAdapter.notifyItemRangeRemoved(position, count);
//        }
//
//        @Override
//        public void onMoved(int fromPosition, int toPosition) {
//            allAssetAdapter.notifyItemMoved(fromPosition, toPosition);
//        }
//
//        @Override
//        public void onChanged(int position, int count) {
//            allAssetAdapter.notifyItemRangeChanged(position, count);
//        }
//
//        @Override
//        public int compare(AssetsListResponseBean a, AssetsListResponseBean b) {
//            return mComparator.compare(a, b);
//        }
//
//        @Override
//        public boolean areContentsTheSame(AssetsListResponseBean oldItem, AssetsListResponseBean newItem) {
//            return oldItem.equals(newItem);
//        }
//
//        @Override
//        public boolean areItemsTheSame(AssetsListResponseBean item1, AssetsListResponseBean item2) {
//            return item1.getId() == item2.getId();
//        }
//    };

}
