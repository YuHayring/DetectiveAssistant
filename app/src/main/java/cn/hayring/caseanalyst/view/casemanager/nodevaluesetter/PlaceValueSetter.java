package cn.hayring.caseanalyst.view.casemanager.nodevaluesetter;

import android.view.LayoutInflater;

import cn.hayring.caseanalyst.databinding.NodeValueSetterPlaceBinding;
import cn.hayring.caseanalyst.domain.Place;
import cn.hayring.caseanalyst.view.casemanager.nodelistpager.viewmodel.PlaceViewModel;
import cn.hayring.caseanalyst.view.casemanager.nodelistpager.viewmodel.NodeViewModel;

/**
 * @author Hayring
 * @date 2021/8/28
 * @description 地点设置器
 */
public class PlaceValueSetter extends NodeValueSetter<Place> {
    @Override
    protected Class<? extends NodeViewModel> getViewModelClass() {
        return PlaceViewModel.class;
    }

    /**
     * 视图绑定
     */
    NodeValueSetterPlaceBinding viewBinding;


    /**
     * 加载页面
     */
    @Override
    protected void initView() {
        super.initView();

        //加载layout实例
        viewBinding = NodeValueSetterPlaceBinding.inflate(LayoutInflater.from(this));
        sonView = viewBinding.getRoot();
        rootLayout.addView(sonView);

        //基本信息设置
        //名称
        viewBinding.placeNameInputer.setText(node.getName());
        //信息
        viewBinding.placeInfoInputer.setText(node.getInfo());
    }

    /**
     * 数据写入
     */
    @Override
    protected void writeNode() {
        String name = viewBinding.placeNameInputer.getText().toString();
        String info = viewBinding.placeInfoInputer.getText().toString();

        node.setName(name);
        node.setInfo(info);


    }
}
