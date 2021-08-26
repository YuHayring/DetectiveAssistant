package cn.hayring.caseanalyst.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import cn.hayring.caseanalyst.R;
import cn.hayring.caseanalyst.domain.Case;
import cn.hayring.caseanalyst.view.casemanager.old.CaseValueSetter;

/***
 * 案件列表活动
 */
@Deprecated
public class CaseListActivity extends MyListActivity<Case> {

    @Override
    public int getSingleLayoutId() {
        return R.layout.single_background_frame;
    }

    boolean isEditWayNew = true;


    /***
     * 数据存储handler
     */
    protected Handler saveHandler;

    /***
     * 获得本Activity所对应的元素类型
     * @return
     */
    @Override
    public Class<Case> getTClass() {
        return Case.class;
    }

    /***
     * 获得本Activity所对应的ValueSetter
     * @return
     */
    @Override
    public Class getValueSetterClass() {
        if (isEditWayNew) {
            return MainActivity.class;
        } else {
            return CaseValueSetter.class;
        }
    }


    /***
     * 重写初始化view
     */
    @Override
    protected void initView() {
        //注册
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        createItemButton = findViewById(R.id.add_item_button);
        createItemButton.setOnClickListener(new CreateNewItemListener());
        //初始化数据源
        List<Case> items = new ArrayList<Case>();
        //绑定数据源
        itemListRecycler = findViewById(R.id.recycler_list);
        itemListRecycler.setLayoutManager(new LinearLayoutManager(this));
        itemListRecycler.setItemAnimator(new DefaultItemAnimator());
        mainItemListAdapter = new MyListAdapter(this, items);


        itemListRecycler.setAdapter(mainItemListAdapter);/*
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(this, R.drawable.divider));
        itemListRecycler.addItemDecoration(dividerItemDecoration);*/
    }


    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        //------------------
        setContentView(R.layout.activity_list);

        //初始化view
        initView();


        //原始数据添加
        //Case item = cn.hayring.caseanalyst.pojo.PojoInstanceCreater.getConanCase();
        //mainItemListAdapter.addItem(item);


        //保存修改的案件
        saveHandler = new SaveHandler();
        Intent instance = getIntent();
        ArrayList<Case> data = (ArrayList<Case>) instance.getSerializableExtra("DATA");
        mainItemListAdapter.addAllItem(data);


        ////-----------------------------debug code
        //ValueSetter.list = (ArrayList<Case>) mainItemListAdapter.getItems();

    }

    /***
     * 注册菜单方法
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.case_list_menu, menu);
        return true;
    }

    /***
     * 菜单点击监听器
     */

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_all_case_button: {
                //new SaveThread().start();
                Message msg = saveHandler.obtainMessage();
                msg.obj = mainItemListAdapter.getItems();
                msg.arg1 = mainItemListAdapter.getItemCount();
                saveHandler.sendMessage(msg);
            }
            break;
            case R.id.create_example_button: {
                Case caseInstance = cn.hayring.caseanalyst.domain.PojoInstanceCreater.getConanCase();
                mainItemListAdapter.addItem(caseInstance);
            }
            break;
            case R.id.ui_switch_button: {
                //startActivity(new Intent(this,MainActivity.class));
                if (isEditWayNew) {
                    item.setTitle(R.string.to_old_ui);
                } else {
                    item.setTitle(R.string.to_new_ui);
                }
                isEditWayNew = !isEditWayNew;
            }
            break;
            case R.id.test_button: {
                //startActivity(new Intent(this, PersonGraphV2.class));
                Toast.makeText(this, "null", Toast.LENGTH_SHORT).show();
            }
            break;
            default:
                throw new IllegalArgumentException("Error item id");
        }

        return super.onOptionsItemSelected(item);
    }


    /***
     * 保存案件
     */
    @Override
    public void finish() {
        //new SaveThread().start();
        Message msg = saveHandler.obtainMessage();
        msg.obj = mainItemListAdapter.getItems();
        msg.arg1 = mainItemListAdapter.getItemCount();
        saveHandler.sendMessage(msg);
        super.finish();
    }


    /***
     * 保存案件线程
     */
/*    class SaveThread extends Thread {
        @Override
        public void run() {
            //发送消息
            Message msg = saveHandler.obtainMessage();
            msg.obj = mainItemListAdapter.getItems();
            msg.arg1 = mainItemListAdapter.getItemCount();
            saveHandler.sendMessage(msg);
        }
    }*/

    //保存Handler
    class SaveHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            int i = msg.arg1;
            //获取待保存的案件
            List<Case> caseList = (List<Case>) msg.obj;
            for (int j = 0; j < i; j++) {
                try {
                    //输出案件
                    FileOutputStream fo = openFileOutput(Integer.toString(j) + ".case", Context.MODE_PRIVATE);
                    ObjectOutputStream oos = new ObjectOutputStream(fo);
                    oos.writeObject(caseList.get(j));
                } catch (IOException e) {
                    e.printStackTrace();
                    //失败提示
                    Toast.makeText(CaseListActivity.this, "保存失败", Toast.LENGTH_LONG).show();
                }

            }
        }
    }
}
