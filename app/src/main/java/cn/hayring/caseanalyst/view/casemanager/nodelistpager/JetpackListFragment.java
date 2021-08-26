package cn.hayring.caseanalyst.view.casemanager.nodelistpager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.CallSuper;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.hayring.caseanalyst.R;
import cn.hayring.caseanalyst.domain.Listable;
import cn.hayring.caseanalyst.view.ValueSetter;
import cn.hayring.caseanalyst.view.casemanager.nodelistpager.viewmodel.NodeViewModel;
import es.dmoral.toasty.Toasty;

public abstract class JetpackListFragment<T extends Listable> extends Fragment {
    /***
     * 获得本Activity所对应的元素类型
     * @return
     */
    public abstract Class<T> getTClass();

    /***
     * 获取元素布局id
     * @return
     */
    public abstract int getSingleLayoutId();

    /***
     * 获得本Activity所对应的元素类型
     * @return
     */
    public abstract Class getValueSetterClass();

    /**
     * 获取ViewModel类型
     *
     * @return {@link NodeViewModel}
     */
    protected abstract Class<? extends NodeViewModel> getViewModelClass();

    /***
     * 获得本Activity对应的布局管理器
     */
    public abstract RecyclerView.LayoutManager getLayoutManager();

    /**
     * 结点ViewModel
     */
    private NodeViewModel<T> viewModel;


    public static final int REQUEST_CODE = 1;

    Toolbar toolbar;

    /***
     * 添加元素按钮
     */
    FloatingActionButton createItemButton;

    /***
     * 列表View
     */
    RecyclerView itemListRecycler;
    /***
     * 列表适配器
     */
    JetpackFragmentListAdapter<T> mainItemListAdapter;

    public CaseManagerActivity getMainActivity() {
        return mainActivity;
    }

    /***
     * activity引用
     */
    protected CaseManagerActivity mainActivity;


    public RecyclerView getItemListRecycler() {
        return itemListRecycler;
    }

    /***
     * 生命周期加载方法
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    /***
     * 初始化view
     */
    @CallSuper
    protected void initView(View view) {


        toolbar = view.findViewById(R.id.toolbar);
        mainActivity.setSupportActionBar(toolbar);

        //注册
        createItemButton = view.findViewById(R.id.add_item_button);
        createItemButton.setOnClickListener(new CreateNewItemListener());
        //新建元素未保存前禁用
        createItemButton.setEnabled(mainActivity.isSaved() || !mainActivity.isCreate());
        //绑定数据源
        itemListRecycler = view.findViewById(R.id.recycler_list);
        itemListRecycler.setLayoutManager(getLayoutManager());
        itemListRecycler.setItemAnimator(new DefaultItemAnimator());
        mainItemListAdapter = new JetpackFragmentListAdapter(new ArrayList<T>(), this);


        itemListRecycler.setAdapter(mainItemListAdapter);/*
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(this, R.drawable.divider));
        itemListRecycler.addItemDecoration(dividerItemDecoration);*/


    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = (CaseManagerActivity) getContext();
        viewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(getViewModelClass());
        viewModel.getNodeList(getCaseId());
    }

    @Override
    public void onHiddenChanged(boolean isHidden) {
        if (!isHidden && mainActivity.isCreate() && mainActivity.isSaved()) {
            createItemButton.setEnabled(true);
        }
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        viewModel.getNodeListData().observe(getViewLifecycleOwner(), nodeListObserver);
        viewModel.getDeleteResponse().observe(getViewLifecycleOwner(), nodeDeletedObserver);
    }


    /***
     * 新元素点击监听器
     */
    class CreateNewItemListener implements View.OnClickListener {

        /***
         * 创建新元素
         * Create new Item
         * @param view
         */
        @Override
        public void onClick(View view) {

            Intent itemTransporter = new Intent(mainActivity, getValueSetterClass());
            //行为:新建数据行为
            itemTransporter.putExtra(ValueSetter.CREATE_OR_NOT, true);
            //启动新Activity
            startActivityForResult(itemTransporter, REQUEST_CODE);

        }
    }

    private final Observer<List<T>> nodeListObserver = new Observer<List<T>>() {
        @Override
        public void onChanged(List<T> nodes) {
            if (nodes == null) {
                Log.i("NodeListObserver", getTClass().getName() + " is null");
                Toasty.error(requireContext(), "网络错误").show();
                nodes = Collections.emptyList();
            } else {
                if (nodes.size() == 0) {
                    Log.i("NodeListObserver", getTClass().getName() + " is empty");
                    Toasty.info(requireContext(), "没有结果").show();
                } else {
                    Log.i("NodeListObserver", getTClass().getName() + "  contain objects");
                }
            }
            mainItemListAdapter.deleteAll();
            mainItemListAdapter.addAllItem(nodes);

        }
    };

    /**
     * 结点删除观察者
     */
    private final Observer<Boolean> nodeDeletedObserver = new Observer<Boolean>() {
        @Override
        public void onChanged(Boolean success) {
            if (success) {
                mainItemListAdapter.deleteLastTryDeleteItem();
            } else {
                Toasty.error(requireContext(), "删除失败，还有其他关系存在！").show();
            }
        }
    };


    /***
     * 编辑完成调用
     * @author Hayring
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent itemTransporter) {
        super.onActivityResult(requestCode, resultCode, itemTransporter);
        T node = (T) itemTransporter.getSerializableExtra(ValueSetter.DATA);
        if (itemTransporter.getBooleanExtra(ValueSetter.CREATE_OR_NOT, true)) {
            mainItemListAdapter.addItem(node);
        } else {
            int index = itemTransporter.getIntExtra(ValueSetter.INDEX, mainItemListAdapter.getItemCount());
            mainItemListAdapter.setItem(index, node);
        }


    }

    /**
     * 获取当前案件id
     *
     * @return 案件id
     */
    public Long getCaseId() {
        return mainActivity.getCaseInstance().getId();
    }

    public NodeViewModel<T> getViewModel() {
        return viewModel;
    }
}
