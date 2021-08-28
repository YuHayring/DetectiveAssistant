package cn.hayring.caseanalyst.view.casemanager.nodevaluesetter;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import androidx.annotation.CallSuper;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.text.SimpleDateFormat;
import java.util.regex.Pattern;

import cn.hayring.caseanalyst.R;
import cn.hayring.caseanalyst.domain.Listable;
import cn.hayring.caseanalyst.utils.Pointer;
import cn.hayring.caseanalyst.view.casemanager.nodelistpager.viewmodel.NodeViewModel;

public abstract class NodeValueSetter<T extends Listable> extends AppCompatActivity {
    public static final String INTENT_KEY_NODE = "node";
    public static final String INTENT_KEY_CASE_ID = "caseId";
    public static final String INDEX = "index";
    public static final String CONNECTOR = "connector";
    public static final String CHANGED = "changed";
    public static final int PERSON_LIST = 2;
    public static final int ORG_LIST = 3;
    public static final int EVENT_LIST = 4;
    public static final int EVIDENCE_LIST = 5;
    public static final int RELATIONSHIP_LIST = 6;
    public static final int MAN_MAN_RELATIONSHIP_LIST = 7;
    public static final String RELATIONSHIP_TYPE = "relationship_type";
    public static final String TYPE = "type";
    public static final String DATA = "data";
    public static final String CREATE_OR_NOT = "create_or_not";
    public static final String ID = "id";
    public static final String POSITION = "position";
    public static final String IS_E = "is_e";
    public static final String CASE = "case";
    protected Intent requestInfo;
    protected LinearLayout rootLayout;
    protected ScrollView sonView;

    public static final String MALE = "男";
    public static final String FEMALE = "女";
    public static final String TRUE = "是";
    public static final String FALSE = "否";

    public static SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
    public static SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm");


    /**
     * 获取ViewModel类型
     *
     * @return {@link NodeViewModel}
     */
    protected abstract Class<? extends NodeViewModel> getViewModelClass();

    Toolbar toolbar;


    /***
     * 是否为新建元素
     */
    protected boolean isCreate;

    /***
     * 实例
     */
    protected T node;

    /**
     * 结点viewModel
     */
    private NodeViewModel<T> viewModel;

    /**
     * 当前caseId
     */
    private Long caseId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_value_setter);
        viewModel = new ViewModelProvider(this).get(getViewModelClass());
        viewModel.getUpdateResponse().observe(this, updateNodeObserver);
        //加载页面
        initView();
    }

    /***
     * 加载页面
     */
    @CallSuper
    protected void initView() {
        rootLayout = findViewById(R.id.value_setter_root_layout);
        //editTexts = new ArrayList<EditText>();
        requestInfo = getIntent();
        isCreate = requestInfo.getBooleanExtra(CREATE_OR_NOT, true);
        node = (T) requestInfo.getSerializableExtra(INTENT_KEY_NODE);
        caseId = requestInfo.getLongExtra(INTENT_KEY_CASE_ID, -1);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //更新失败弹窗
        updateFailedDialog = new AlertDialog.Builder(this)
                .setTitle(R.string.case_manager_activity_update_false_dialog_title)
                .setMessage(R.string.case_manager_activity_update_false_dialog_text)
                .setPositiveButton(R.string.dialog_normal_button_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        viewModel.updateNode(caseId, node);
                    }
                })
                .setNegativeButton(R.string.dialog_normal_button_no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //更新失败，直接，结束activity
                        NodeValueSetter.super.onBackPressed();
                    }
                });
    }


    /***
     * 保存
     */
    @CallSuper
    protected void save() {
        writeNode();
        requestInfo.putExtra(CHANGED, true);
        if (isCreate) {
            Pointer.setPoint(node);
        }
    }


    /***
     * 写入内存
     */
    protected abstract void writeNode();


    public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }


    /**
     * 拦截返回键方法，保存案件信息
     */
    @Override
    public void onBackPressed() {
        //保存案件信息
        writeNode();
        viewModel.updateNode(caseId, node);
    }


    /**
     * 更新结点观察者
     */
    private final Observer<Boolean> updateNodeObserver = new Observer<Boolean>() {

        @Override
        public void onChanged(@Nullable Boolean success) {
            if (success) {
                requestInfo.putExtra(NodeValueSetter.INTENT_KEY_NODE, node);
                setResult(2, requestInfo);
                NodeValueSetter.super.finish();
            } else {
                updateFailedDialog.show();
            }
        }
    };


    /**
     * 更新失败弹窗
     */
    AlertDialog.Builder updateFailedDialog;


}
