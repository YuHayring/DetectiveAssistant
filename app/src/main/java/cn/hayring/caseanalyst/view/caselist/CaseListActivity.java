package cn.hayring.caseanalyst.view.caselist;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.hayring.caseanalyst.R;
import cn.hayring.caseanalyst.databinding.ActivityListBinding;
import cn.hayring.caseanalyst.domain.Case;
import cn.hayring.caseanalyst.net.NetworkInterface;
import cn.hayring.caseanalyst.view.MyListActivity;
import cn.hayring.caseanalyst.view.ValueSetter;
import cn.hayring.caseanalyst.view.casemanager.nodelistpager.CaseManagerActivity;
import cn.hayring.caseanalyst.view.settings.DBNetworkSettingsActivity;
import es.dmoral.toasty.Toasty;

/**
 * @author hayring
 * @date 6/21/21 5:40 PM
 */
public class CaseListActivity extends MyListActivity<Case> {


    ActivityListBinding viewBinding;

    SQLiteDatabase caseDb;

    CaseViewModel caseViewModel;

    /**
     * 数据库设置请求代码
     */
    public static final int DB_SETTING_REQUEST_CODE = 5;


    @Override
    public Class<Case> getTClass() {
        return Case.class;
    }

    @Override
    public int getSingleLayoutId() {
        return R.layout.single_background_frame;
    }

    /**
     * 设置编辑案件的activity
     *
     * @return {@link CaseManagerActivity}.class
     */
    @Override
    public Class getValueSetterClass() {
        return CaseManagerActivity.class;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding = ActivityListBinding.inflate(LayoutInflater.from(this));
        setContentView(viewBinding.getRoot());
        initView();


    }





    /***
     * 重写初始化view
     */
    @Override
    protected void initView() {
        //注册
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        viewBinding.addItemButton.setOnClickListener(new CreateNewItemListener());
        //初始化数据源
        List<Case> items = new ArrayList<Case>();
        //绑定数据源
        viewBinding.contentList.recyclerList.setLayoutManager(new LinearLayoutManager(this));
        viewBinding.contentList.recyclerList.setItemAnimator(new DefaultItemAnimator());
        mainItemListAdapter = new CaseListAdapter(this, items);


        viewBinding.contentList.recyclerList.setAdapter(mainItemListAdapter);

        caseViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(CaseViewModel.class);
        caseViewModel.getCaseListData().observe(this, caseListObserver);
        caseViewModel.getDeleteResponse().observe(this, caseDeletedObserver);
        caseViewModel.getCaseList();
    }

    private final Observer<List<Case>> caseListObserver = new Observer<List<Case>>() {
        @Override
        public void onChanged(List<Case> cases) {
            if (cases == null) {
                Log.i("CaseListObserver", "cases is null");
                Toasty.error(CaseListActivity.this, "网络错误").show();
                cases = Collections.emptyList();
            } else {
                if (cases.size() == 0) {
                    Log.i("CaseListObserver", "cases is empty");
                    Toasty.info(CaseListActivity.this, "没有结果").show();
                } else {
                    Log.i("CaseListObserver", "cases contain objects");
                }
            }
            mainItemListAdapter.deleteAll();
            mainItemListAdapter.addAllItem(cases);

        }
    };

    /**
     * 案件删除观察者
     */
    private final Observer<Boolean> caseDeletedObserver = new Observer<Boolean>() {
        @Override
        public void onChanged(Boolean success) {
            if (success) {
                ((CaseListAdapter) mainItemListAdapter).deleteLastTryDeleteItem();
            } else {
                Toasty.error(CaseListActivity.this, "删除失败，还有其他关系存在！").show();
            }
        }
    };

    public CaseViewModel getCaseViewModel() {
        return caseViewModel;
    }



//    @Override
//    protected void onStart() {
//        super.onStart();
//        caseViewModel.getCaseList();
//    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("数据库设置");
        return true;
    }

    /**
     * 菜单的点击事件
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getTitle() ==)
        Intent intent = new Intent(this, DBNetworkSettingsActivity.class);
        startActivityForResult(intent, DB_SETTING_REQUEST_CODE);
        return true;
    }

    /***
     * 编辑完成调用
     * @author Hayring
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent itemTransporter) {

        if (requestCode == DB_SETTING_REQUEST_CODE) {
            NetworkInterface.init();
            caseViewModel.getCaseList();
            return;
        }
        super.onActivityResult(requestCode, resultCode, itemTransporter);
        Case caxe = (Case) itemTransporter.getSerializableExtra(ValueSetter.CASE);
        if (itemTransporter.getBooleanExtra(ValueSetter.CREATE_OR_NOT, true)) {
            mainItemListAdapter.addItem(caxe);
        } else {
            int index = itemTransporter.getIntExtra(ValueSetter.INDEX, mainItemListAdapter.getItemCount());
            mainItemListAdapter.setItem(index, caxe);
        }
    }

}
