package cn.hayring.caseanalyst.view.casemanager.nodevaluesetter;

import android.view.LayoutInflater;

import cn.hayring.caseanalyst.databinding.NodeValueSetterThingBinding;
import cn.hayring.caseanalyst.domain.Thing;
import cn.hayring.caseanalyst.view.casemanager.nodelistpager.viewmodel.ThingViewModel;
import cn.hayring.caseanalyst.view.casemanager.nodelistpager.viewmodel.NodeViewModel;

/**
 * @author Hayring
 * @date 2021/8/28
 * @description 物品设置器
 */
public class ThingValueSetter extends NodeValueSetter<Thing> {
    @Override
    protected Class<? extends NodeViewModel> getViewModelClass() {
        return ThingViewModel.class;
    }

    /**
     * 视图绑定
     */
    NodeValueSetterThingBinding viewBinding;


    /**
     * 加载页面
     */
    @Override
    protected void initView() {
        super.initView();

        //加载layout实例
        viewBinding = NodeValueSetterThingBinding.inflate(LayoutInflater.from(this));
        sonView = viewBinding.getRoot();
        rootLayout.addView(sonView);

        //基本信息设置
        //名称
        viewBinding.thingNameInputer.setText(node.getName());
        //信息
        viewBinding.thingInfoInputer.setText(node.getInfo());
    }

    /**
     * 数据写入
     */
    @Override
    protected void writeNode() {
        String name = viewBinding.thingNameInputer.getText().toString();
        String info = viewBinding.thingInfoInputer.getText().toString();

        node.setName(name);
        node.setInfo(info);


    }
}
