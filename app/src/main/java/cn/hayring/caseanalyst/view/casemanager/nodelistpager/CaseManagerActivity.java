package cn.hayring.caseanalyst.view.casemanager.nodelistpager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import cn.hayring.caseanalyst.R;
import cn.hayring.caseanalyst.domain.Case;
import cn.hayring.caseanalyst.view.ValueSetter;
import cn.hayring.caseanalyst.view.caselist.CaseViewModel;
import cn.hayring.caseanalyst.view.casemanager.nodelistpager.fragment.EventListFragment;
import cn.hayring.caseanalyst.view.casemanager.nodelistpager.fragment.PersonListFragment;
import cn.hayring.caseanalyst.view.casemanager.nodelistpager.fragment.PlaceListFragmengt;
import cn.hayring.caseanalyst.view.casemanager.nodelistpager.fragment.ThingListFragment;

public class CaseManagerActivity extends AppCompatActivity {

    /***
     * 五个Fragment
     */
    protected Fragment persons;
    protected Fragment events;
    protected Fragment things;
    protected Fragment places;
    protected Fragment info;
    protected Fragment lastFragment;

    protected boolean isCreate;

    protected boolean isSaved;

    protected Intent requestInfo;

    protected BottomNavigationView bottomNavigationView;


    protected Case caseInstance;


    CaseViewModel caseViewModel;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.person_list:
                    switchFragment(persons);
                    return true;
                case R.id.event_list:
                    switchFragment(events);
                    return true;
                case R.id.thing_list:
                    switchFragment(things);
                    return true;
                case R.id.place_list:
                    switchFragment(places);
                    return true;
                case R.id.info_settings:
                    switchFragment(info);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_case_manager);
        requestInfo = getIntent();
        isCreate = requestInfo.getBooleanExtra(ValueSetter.CREATE_OR_NOT, true);

        caseViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(CaseViewModel.class);
        caseViewModel.getNewCase().observe(this, insertIdObserver);
        caseViewModel.getUpdateResponse().observe(this, updateCaseObserver);



        //更新失败弹窗
        updateFailedDialog = new AlertDialog.Builder(this)
                .setTitle(R.string.case_manager_activity_update_false_dialog_title)
                .setMessage(R.string.case_manager_activity_update_false_dialog_text)
                .setPositiveButton(R.string.dialog_normal_button_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        caseViewModel.updateCase(caseInstance);
                    }
                })
                .setNegativeButton(R.string.dialog_normal_button_no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //更新失败，直接，结束activity
                        CaseManagerActivity.super.onBackPressed();
                    }
                });

        //获取案件数据
        if (!isCreate) {
            caseInstance = (Case) requestInfo.getSerializableExtra(ValueSetter.CASE);
        } else {
            caseViewModel.addCase();
        }
        initView();
        initFragment();
    }

    protected void initView() {
        bottomNavigationView = findViewById(R.id.navigation);
    }

    private void initFragment() {

        persons = new PersonListFragment();
        events = new EventListFragment();
        things = new ThingListFragment();
        places = new PlaceListFragmengt();
        info = new InfoFragment();
        if (isCreate) {
            lastFragment = info;
            getSupportFragmentManager().beginTransaction().replace(R.id.mainview, info).show(info).commit();
            bottomNavigationView.setSelectedItemId(R.id.info_settings);
        } else {
            lastFragment = persons;
            getSupportFragmentManager().beginTransaction().replace(R.id.mainview, persons).show(persons).commitNow();
        }


        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }


    private void switchFragment(Fragment nextfragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.hide(lastFragment);//隐藏上个Fragment
        if (nextfragment.isAdded() == false) {
            transaction.add(R.id.mainview, nextfragment);


        }

        transaction.show(nextfragment).commitAllowingStateLoss();

        lastFragment = nextfragment;
    }


    public Case getCaseInstance() {
        return caseInstance;
    }


    public boolean isCreate() {
        return isCreate;
    }

    public void setCreate(boolean create) {
        isCreate = create;
    }

    public boolean isSaved() {
        return isSaved;
    }

    public void setSaved(boolean saved) {
        isSaved = saved;
    }





    /**
     * 创建新案件观察者
     */
    private final Observer<Case> insertIdObserver = new Observer<Case>() {

        @Override
        public void onChanged(@Nullable Case caxe) {
            caseInstance = caxe;
        }
    };

    /**
     * 更新案件观察者
     */
    private final Observer<Boolean> updateCaseObserver = new Observer<Boolean>() {

        @Override
        public void onChanged(@Nullable Boolean success) {
            if (success) {
                requestInfo.putExtra(ValueSetter.CASE, caseInstance);
                setResult(2, requestInfo);
                CaseManagerActivity.super.finish();
            } else {
                updateFailedDialog.show();
            }
        }
    };


    /**
     * 拦截返回键方法，保存案件信息
     */
    @Override
    public void onBackPressed() {
        //保存案件信息
        ((InfoFragment) info).setCaseValue();
        caseViewModel.updateCase(caseInstance);
    }


    /**
     * 更新失败弹窗
     */
    AlertDialog.Builder updateFailedDialog;


}
