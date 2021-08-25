package cn.hayring.caseanalyst.view.casemanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.lang.reflect.Constructor;

import cn.hayring.caseanalyst.R;
import cn.hayring.caseanalyst.domain.Case;
import cn.hayring.caseanalyst.view.ValueSetter;
import cn.hayring.caseanalyst.view.caselist.CaseViewModel;

public class CaseManagerActivity extends AppCompatActivity {

    /***
     * 五个Fragment
     */
    protected Fragment persons;
    protected Fragment events;
    protected Fragment evidences;
    protected Fragment organizations;
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
//                    switchFragment(persons);
                    return true;
                case R.id.event_list:
//                    switchFragment(events);
                    return true;
                case R.id.evidence_list:
//                    switchFragment(evidences);
                    return true;
                case R.id.org_list:
//                    switchFragment(organizations);
                    return true;
                case R.id.info_settings:
//                    switchFragment(info);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestInfo = getIntent();
        isCreate = requestInfo.getBooleanExtra(ValueSetter.CREATE_OR_NOT, true);

        caseViewModel = new ViewModelProvider(this, videoViewModelFactory).get(CaseViewModel.class);
        caseViewModel.getNewCase().observe(this, insertIdObserver);


        initView();
        initFragment();
        //获取案件数据
        if (!isCreate) {
            caseInstance = (Case) requestInfo.getSerializableExtra(ValueSetter.CASE);
        } else {
            caseViewModel.addCase();
        }
    }

    protected void initView() {
        bottomNavigationView = findViewById(R.id.navigation);
    }

    private void initFragment() {

//        persons = new PersonListFragment();
//        events = new EventListFragment();
//        evidences = new EvidenceListFragment();
//        organizations = new OrgListFragment();
        info = new InfoFragment();
//        if (isCreate) {
        lastFragment = info;
        getSupportFragmentManager().beginTransaction().replace(R.id.mainview, info).show(info).commit();
        bottomNavigationView.setSelectedItemId(R.id.info_settings);
//        } else {
//            lastFragment = persons;
//            getSupportFragmentManager().beginTransaction().replace(R.id.mainview, persons).show(persons).commitNow();
//        }


        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }

//
//    private void switchFragment(Fragment nextfragment) {
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.hide(lastFragment);//隐藏上个Fragment
//        if (nextfragment.isAdded() == false) {
//            transaction.add(R.id.mainview, nextfragment);
//
//
//        }
//
//        transaction.show(nextfragment).commitAllowingStateLoss();
//
//        lastFragment = nextfragment;
//    }


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

    @Override
    public void finish() {
//        if (isCreate) {
//            requestInfo.putExtra(ValueSetter.CHANGED, isSaved);
//            if (isSaved) {
//                Pointer.setPoint(caseInstance);
//            }
//        } else {
//            if (info.isAdded()) {
//                ((InfoSetterFragment) info).writeInstance();
//            }
//        }
        ((InfoFragment) info).writeInstance();
        requestInfo.putExtra(ValueSetter.CASE, caseInstance);
        setResult(2, requestInfo);
        super.finish();
    }


    ViewModelProvider.Factory videoViewModelFactory = new ViewModelProvider.Factory() {

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            try {
                Constructor constructor = modelClass.getConstructor();
                return (T) constructor.newInstance();
            } catch (Exception e) {
                IllegalArgumentException ile = new IllegalArgumentException("" + modelClass + "is not" + CaseViewModel.class);
                ile.initCause(e);
                throw ile;
            }

        }
    };

    private final Observer<Case> insertIdObserver = new Observer<Case>() {

        @Override
        public void onChanged(@Nullable Case caxe) {
            caseInstance = caxe;
        }
    };
}
