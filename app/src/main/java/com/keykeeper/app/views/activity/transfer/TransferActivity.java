package com.keykeeper.app.views.activity.transfer;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.keykeeper.app.R;
import com.keykeeper.app.databinding.ActivityTransferAssetBinding;
import com.keykeeper.app.interfaces.DialogClickListener;
import com.keykeeper.app.model.bean.AssetsListResponseBean;
import com.keykeeper.app.model.bean.BaseResponse;
import com.keykeeper.app.utils.AppUtils;
import com.keykeeper.app.utils.Utils;
import com.keykeeper.app.views.adapter.TransferAssetAdapter;
import com.keykeeper.app.views.base.BaseActivity;
import com.keykeeper.app.views.custom_view.CustomActionBar;

/**
 * Created by akshaydashore on 31/8/18
 */
public class TransferActivity extends BaseActivity implements XRecyclerView.LoadingListener, TransferAssetAdapter.ActivityForResult, DialogClickListener {


    private Context context;
    private ActivityTransferAssetBinding binding;
    TransferViewModel viewModel;
    private TransferAssetAdapter myAssetAdapter;
    public AssetsListResponseBean assetsListBean;
    private ActionMode actionMode;
    boolean isMultiSelectionMode;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setCustomActionBar();
        initializeViews();
    }


    @Override
    public void setCustomActionBar() {

        super.setCustomActionBar();
        CustomActionBar customActionBar = new CustomActionBar(this);
        customActionBar.setActionbar(getString(R.string.transfer_asset), true, false, false, false, this);

    }

    @Override
    public void initializeViews() {

        binding = DataBindingUtil.setContentView(this, R.layout.activity_transfer_asset);
        viewModel = ViewModelProviders.of(this).get(TransferViewModel.class);
        binding.setViewModel(viewModel);

        LinearLayoutManager manager = new LinearLayoutManager(context);
        binding.recyclerView.setLayoutManager(manager);
        binding.recyclerView.setLoadingListener(this);
        binding.recyclerView.setLoadingMoreEnabled(false);

        binding.returnKeyTv.setOnClickListener(this);

        viewModel.validator.observe(this, observer);
        viewModel.response_validator.observe(this, response_observer);
        viewModel.mutikeySubmit_response_validator.observe(this, mutikeySubmit_response_observer);
        Utils.hideSoftKeyboard(this);
        Utils.showProgressDialog(context, getString(R.string.loading));
        viewModel.getMyAssets(binding);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.left_iv:
                finish();
                break;

            case R.id.return_key_tv:
                submitMultipleKey();
                break;
        }
    }

    private void submitMultipleKey() {

        Utils.showProgressDialog(context, getString(R.string.loading));
        viewModel.returnMultipleKey(assetsListBean);
    }


    private Observer<BaseResponse> mutikeySubmit_response_observer = new Observer<BaseResponse>() {

        @Override
        public void onChanged(@Nullable BaseResponse baseResponse) {

            if (baseResponse == null) {
                Utils.hideProgressDialog();
                Utils.showSnackBar(binding, getString(R.string.server_error));
                return;
            }

            actionMode.finish();
            Utils.showAlert(context, "", baseResponse.getMessage(), "Ok", "", AppUtils.dialog_ok_click, TransferActivity.this);


        }
    };


    Observer<AssetsListResponseBean> response_observer = new Observer<AssetsListResponseBean>() {

        @Override
        public void onChanged(@Nullable AssetsListResponseBean assetsListResponseBean) {

            assetsListBean = assetsListResponseBean;
            if (actionMode != null) {
                actionMode.finish();
            }
            if (assetsListResponseBean != null && assetsListResponseBean.getResult() != null && assetsListResponseBean.getResult().size() > 0) {
                LinearLayoutManager manager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
                binding.recyclerView.setLayoutManager(manager);
                myAssetAdapter = new TransferAssetAdapter(context, assetsListResponseBean, TransferActivity.this, longClickListener);
                binding.recyclerView.setAdapter(myAssetAdapter);
            } else {
                noDataView();
            }
        }
    };

    Observer<Integer> observer = new Observer<Integer>() {

        @Override
        public void onChanged(@Nullable Integer value) {

            switch (value) {

                case AppUtils.NO_INTERNET:
                    Utils.hideProgressDialog();
                    Utils.showSnackBar(binding, getString(R.string.internet_connection));
                    break;

                case AppUtils.SERVER_ERROR:
                    Utils.hideProgressDialog();
                    Utils.showSnackBar(binding, getString(R.string.server_error));
                    break;

                case AppUtils.NO_ITEM_SELECT:
                    Utils.hideProgressDialog();
                    Utils.showSnackBar(binding, getString(R.string.please_check_one_item));
                    break;
            }

        }
    };

    @Override
    public void onRefresh() {
        Utils.showProgressDialog(context, getString(R.string.loading));
        viewModel.getMyAssets(binding);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.recyclerView.refreshComplete();
            }
        }, 1000);
    }

    @Override
    public void onLoadMore() {
        Log.e("onLoadMore: ", "call load more");
    }


    private void noDataView() {
        binding.recyclerView.setVisibility(View.GONE);
        binding.noDataFountLayout.setVisibility(View.VISIBLE);
        binding.tvNoRecords.setText(getString(R.string.txt_no_records_avialable));
    }


    @Override
    public void onCallActivityResult(Intent intent) {
        startActivityForResult(intent, AppUtils.REQ_REFRESH_VIEW);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppUtils.REQ_REFRESH_VIEW) {
            viewModel.getMyAssets(binding);
            if (actionMode != null){
                actionMode.finish();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    View.OnLongClickListener longClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View view) {
            myAssetAdapter.enableMultiSelectionMode(true);
            actionMode = startActionMode(mActionModeCallback);
            myAssetAdapter.notifyDataSetChanged();
            return false;

        }
    };


    private Menu context_menu;
    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            // Inflate a menu resource providing context menu items
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.manu_multiselect, menu);
            context_menu = menu;
            binding.returnKeyTv.setVisibility(View.VISIBLE);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false; // Return false if nothing is done
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_select:
                    for (int i = 0; i < assetsListBean.getResult().size(); i++) {
                        assetsListBean.getResult().get(i).isSelected = true;
                    }
                    myAssetAdapter.notifyDataSetChanged();
                    return true;

                case R.id.action_deselect:
                    for (int i = 0; i < assetsListBean.getResult().size(); i++) {
                        assetsListBean.getResult().get(i).isSelected = false;
                    }
                    myAssetAdapter.notifyDataSetChanged();
                    return true;

                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            binding.returnKeyTv.setVisibility(View.GONE);
            for (int i = 0; i < assetsListBean.getResult().size(); i++) {
                assetsListBean.getResult().get(i).isSelected = false;
            }
            myAssetAdapter.enableMultiSelectionMode(false);
            myAssetAdapter.notifyDataSetChanged();
//            mActionMode = null;
//            isMultiSelect = false;
//            multiselect_list = new ArrayList<SampleModel>();
//            refreshAdapter();
        }
    };


    @Override
    public void onDialogClick(int which, int requestCode) {

    }

}

