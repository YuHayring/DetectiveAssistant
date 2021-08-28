package cn.hayring.caseanalyst.view.casemanager.nodevaluesetter;

import android.view.LayoutInflater;

import cn.hayring.caseanalyst.databinding.NodeValueSetterEventBinding;
import cn.hayring.caseanalyst.domain.Event;
import cn.hayring.caseanalyst.view.casemanager.nodelistpager.viewmodel.EventViewModel;
import cn.hayring.caseanalyst.view.casemanager.nodelistpager.viewmodel.NodeViewModel;

/**
 * @author Hayring
 * @date 2021/8/28
 * @description 事件设置器
 */
public class EventValueSetter extends NodeValueSetter<Event> {
    @Override
    protected Class<? extends NodeViewModel> getViewModelClass() {
        return EventViewModel.class;
    }

    /**
     * 视图绑定
     */
    NodeValueSetterEventBinding viewBinding;


    /**
     * 加载页面
     */
    @Override
    protected void initView() {
        super.initView();

        //加载layout实例
        viewBinding = NodeValueSetterEventBinding.inflate(LayoutInflater.from(this));
        sonView = viewBinding.getRoot();
        rootLayout.addView(sonView);

        //基本信息设置
        //名称
        viewBinding.eventNameInputer.setText(node.getName());
        //信息
        viewBinding.eventInfoInputer.setText(node.getInfo());
    }

    /**
     * 数据写入
     */
    @Override
    protected void writeNode() {
        String name = viewBinding.eventNameInputer.getText().toString();
        String info = viewBinding.eventInfoInputer.getText().toString();

        node.setName(name);
        node.setInfo(info);


    }
}
