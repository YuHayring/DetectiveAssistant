package cn.hayring.caseanalyst.view.casemanager.nodelistpager.fragment;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import cn.hayring.caseanalyst.R;
import cn.hayring.caseanalyst.domain.Place;
import cn.hayring.caseanalyst.view.casemanager.nodelistpager.JetpackListFragment;
import cn.hayring.caseanalyst.view.casemanager.nodelistpager.viewmodel.NodeViewModel;
import cn.hayring.caseanalyst.view.casemanager.nodelistpager.viewmodel.PlaceViewModel;

/**
 * @author Hayring
 * @date 2021/8/26
 * @description
 */
public class PlaceListFragmengt extends JetpackListFragment<Place> {
    @Override
    public Class<Place> getTClass() {
        return Place.class;
    }

    @Override
    public int getSingleLayoutId() {
        return R.layout.single_evidence;
    }

    @Override
    public Class getValueSetterClass() {
        return null;
    }

    @Override
    protected Class<? extends NodeViewModel> getViewModelClass() {
        return PlaceViewModel.class;
    }

    @Override
    public RecyclerView.LayoutManager getLayoutManager() {
        return new GridLayoutManager(requireContext(), 3);
    }
}
