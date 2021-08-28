package cn.hayring.caseanalyst.view.casemanager.casevaluesetter;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import cn.hayring.caseanalyst.R;
import cn.hayring.caseanalyst.view.ValueSetter;
import cn.hayring.caseanalyst.domain.EventClip;
import cn.hayring.caseanalyst.utils.Pointer;

@Deprecated
public class EventClipValueSetter extends ValueSetter<EventClip> {

    TextView happenDate;

    TextView happenTime;

    Calendar calendar;

    /***
     * 加载页面
     */
    @Override
    protected void initView() {
        super.initView();
        //加载layout实例
        LayoutInflater inflater = getLayoutInflater();
        sonView = (ScrollView) inflater.inflate(R.layout.event_clip_value_setter, null);
        rootLayout.addView(sonView);

        //注册控件,信息显示
        infoInputer = findViewById(R.id.event_clip_info_inputer);
        saveButton = findViewById(R.id.event_clip_save_button);
        happenDate = findViewById(R.id.happen_date);
        happenTime = findViewById(R.id.happen_time);

        if (!isCreate) {
            saveButton.setEnabled(false);
            saveButton.setVisibility(View.GONE);
            //instance = (Event) requestInfo.getSerializableExtra(DATA);
            instance = (EventClip) Pointer.getPoint();
            infoInputer.setText(instance.getInfo());
            happenDate.setText(dateFormatter.format(instance.getTimePoint().getTime()));
            happenTime.setText(timeFormatter.format(instance.getTimePoint().getTime()));
        } else {
            instance = new EventClip();
        }

        setOnClickListenerForTimeSetter(happenDate, happenTime, this, instance.getTimePoint());

        //设置监听器
        saveButton.setOnClickListener(new FinishEditListener());


    }


    @Override
    protected void writeInstance() {

    }

    @Override
    protected void save() {

        if (instance.getTimePoint().get(Calendar.YEAR) == 1970) {
            Toast.makeText(EventClipValueSetter.this, "时间未设置", Toast.LENGTH_SHORT);
            return;
        }
        instance.setInfo(infoInputer.getText().toString());
        super.save();
    }


}
