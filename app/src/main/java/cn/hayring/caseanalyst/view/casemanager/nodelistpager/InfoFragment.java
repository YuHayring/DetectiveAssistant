package cn.hayring.caseanalyst.view.casemanager.nodelistpager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.lang.reflect.Constructor;

import cn.hayring.caseanalyst.R;
import cn.hayring.caseanalyst.view.caselist.CaseViewModel;
import cn.hayring.caseanalyst.view.casemanager.CaseManagerActivity;

public class InfoFragment extends Fragment {


    /***
     * activity引用
     */
    protected CaseManagerActivity mainActivity;

    public CaseManagerActivity getMainActivity() {
        return mainActivity;
    }

    /***
     * 保存按钮
     */
    protected Button saveButton;

    /***
     * 名称键入器
     */
    protected EditText nameInputer;

    /***
     * 信息键入器
     */
    protected EditText infoInputer;


    /***
     * 短期案件设置器
     */
    Spinner shortTimeCaseSetter;

    /***
     * 生命周期加载方法
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.info_setter_fragment, container, false);
    }

    ViewModelProvider.Factory factory = new ViewModelProvider.Factory() {

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


    CaseViewModel caseViewModel;

    /***
     * 初始化view
     */
    protected void initView(View view) {
        mainActivity = (CaseManagerActivity) getContext();

        caseViewModel = new ViewModelProvider(getActivity(), factory).get(CaseViewModel.class);
        //关闭旧入口
        view.findViewById(R.id.event_list_enter).setVisibility(View.GONE);
        view.findViewById(R.id.evidence_list_enter).setVisibility(View.GONE);
        view.findViewById(R.id.person_list_enter).setVisibility(View.GONE);
        view.findViewById(R.id.org_list_enter).setVisibility(View.GONE);


        //注册控件
        nameInputer = view.findViewById(R.id.case_name_inputer);
        infoInputer = view.findViewById(R.id.case_info_inputer);
        saveButton = view.findViewById(R.id.case_save_button);
        saveButton.setVisibility(View.INVISIBLE);
        view.findViewById(R.id.label_is_short_time_case).setVisibility(View.INVISIBLE);
        shortTimeCaseSetter = view.findViewById(R.id.short_time_case_switcher);
        shortTimeCaseSetter.setVisibility(View.INVISIBLE);
        //信息显示
        nameInputer.setText(mainActivity.getCaseInstance().getName());
        infoInputer.setText(mainActivity.getCaseInstance().getInfo());

    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }


    /**
     * 将输入框的值写入内存
     */
    public void setCaseValue() {
        if (mainActivity != null && nameInputer != null) {
            mainActivity.getCaseInstance().setName(nameInputer.getText().toString());
            mainActivity.getCaseInstance().setInfo(infoInputer.getText().toString());
        }
    }

    /***
     * 写入数据库
     */
    public void writeInstance() {
        setCaseValue();
        caseViewModel.updateCase(mainActivity.getCaseInstance());
    }




    @Override
    public void onPause() {
        setCaseValue();
        super.onPause();
    }
}
