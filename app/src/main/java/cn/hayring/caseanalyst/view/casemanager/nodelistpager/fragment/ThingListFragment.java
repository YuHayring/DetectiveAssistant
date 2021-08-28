package cn.hayring.caseanalyst.view.casemanager.nodelistpager.fragment;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import cn.hayring.caseanalyst.R;
import cn.hayring.caseanalyst.domain.Thing;
import cn.hayring.caseanalyst.view.casemanager.nodelistpager.JetpackListFragment;
import cn.hayring.caseanalyst.view.casemanager.nodelistpager.viewmodel.NodeViewModel;
import cn.hayring.caseanalyst.view.casemanager.nodelistpager.viewmodel.ThingViewModel;
import cn.hayring.caseanalyst.view.casemanager.nodevaluesetter.ThingValueSetter;

public class ThingListFragment extends JetpackListFragment<Thing> {
    @Override
    public Class getTClass() {
        return Thing.class;
    }

    @Override
    public int getSingleLayoutId() {
        //TODO
        return R.layout.single_evidence;
    }

    @Override
    public Class getValueSetterClass() {
        //TODO
        return ThingValueSetter.class;
    }

    @Override
    protected Class<? extends NodeViewModel> getViewModelClass() {
        return ThingViewModel.class;
    }

    @Override
    public RecyclerView.LayoutManager getLayoutManager() {
        return new GridLayoutManager(requireContext(), 3);
    }

}
