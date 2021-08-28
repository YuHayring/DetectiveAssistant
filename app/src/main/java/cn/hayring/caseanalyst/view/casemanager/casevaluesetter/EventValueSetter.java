package cn.hayring.caseanalyst.view.casemanager.casevaluesetter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import cn.hayring.caseanalyst.R;
import cn.hayring.caseanalyst.view.casemanager.relationship.RelationshipListActivity;
import cn.hayring.caseanalyst.view.magic.TimeAxis;
import cn.hayring.caseanalyst.view.ValueSetter;
import cn.hayring.caseanalyst.domain.Event;
import cn.hayring.caseanalyst.domain.Relationship;
import cn.hayring.caseanalyst.utils.Pointer;

@Deprecated
public class EventValueSetter extends ValueSetter<Event> {

    /***
     * 组织-事关系入口
     */
    TextView orgEventRelationshipEnter;

    /***
     * 人-事关系入口
     */
    TextView manEventRelationshipEnter;

    /***
     * 时间线入口
     */
    TextView timeAxisEnter;


    /***
     * 加载页面
     */
    @Override
    protected void initView() {
        super.initView();
        //加载layout实例
        LayoutInflater inflater = getLayoutInflater();
        sonView = (ScrollView) inflater.inflate(R.layout.event_value_setter, null);
        rootLayout.addView(sonView);

        //注册控件,信息显示
        nameInputer = findViewById(R.id.event_name_inputer);
        infoInputer = findViewById(R.id.event_info_inputer);
        saveButton = findViewById(R.id.event_save_button);
        timeAxisEnter = findViewById(R.id.time_axis_enter);
        orgEventRelationshipEnter = sonView.findViewById(R.id.org_event_relationship_text_view);
        manEventRelationshipEnter = sonView.findViewById(R.id.man_event_relationship_text_view);

        if (!isCreate) {

            saveButton.setEnabled(false);
            saveButton.setVisibility(View.GONE);
            //instance = (Event) requestInfo.getSerializableExtra(DATA);
            instance = (Event) Pointer.getPoint();

            nameInputer.setText(instance.getName());
            infoInputer.setText(instance.getInfo());
        } else {
            manEventRelationshipEnter.setVisibility(View.GONE);
            orgEventRelationshipEnter.setVisibility(View.GONE);
            instance = caseInstance.createEvent();
        }


        //设置监听器
        saveButton.setOnClickListener(new FinishEditListener());
        timeAxisEnter.setOnClickListener(new TimeAxisEnterListener());

        View.OnClickListener relationshipEnterListener = new EditRelationshipListener();
        orgEventRelationshipEnter.setOnClickListener(relationshipEnterListener);
        manEventRelationshipEnter.setOnClickListener(relationshipEnterListener);


    }


    @Override
    protected void writeInstance() {
        instance.setName(nameInputer.getText().toString());
        instance.setInfo(infoInputer.getText().toString());
    }

    /***
     * 关系编辑入口监听器
     */
    class EditRelationshipListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Intent request = new Intent(EventValueSetter.this, RelationshipListActivity.class);
            if (view.getId() == R.id.org_event_relationship_text_view) {
                //request.putExtra(ValueSetter.DATA, instance.getOrgEventRelationships());
                Pointer.setPoint(instance.getOrgEventRelationships());
                request.putExtra(ValueSetter.RELATIONSHIP_TYPE, Relationship.ORG_EVENT);
            } else if (view.getId() == R.id.man_event_relationship_text_view) {
                //request.putExtra(ValueSetter.DATA, instance.getManEventRelationships());
                Pointer.setPoint(instance.getManEventRelationships());
                request.putExtra(ValueSetter.RELATIONSHIP_TYPE, Relationship.MAN_EVENT);
            } else {
                throw new IllegalArgumentException("Error View Id");
            }
            //request.putExtra(ValueSetter.CONNECTOR, instance);
            Pointer.setConnector(instance);
            startActivityForResult(request, 1);
        }
    }


    class TimeAxisEnterListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(EventValueSetter.this, TimeAxis.class);
            Pointer.setPoint(instance.getTimeAxis());
            startActivity(intent);
        }
    }


}
