package cn.hayring.caseanalyst.view.casemanager.nodelistpager.fragment;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import cn.hayring.caseanalyst.R;
import cn.hayring.caseanalyst.domain.Event;
import cn.hayring.caseanalyst.view.casemanager.nodelistpager.JetpackListFragment;
import cn.hayring.caseanalyst.view.casemanager.nodelistpager.viewmodel.EventViewModel;
import cn.hayring.caseanalyst.view.casemanager.nodelistpager.viewmodel.NodeViewModel;
import cn.hayring.caseanalyst.view.casemanager.nodevaluesetter.EventValueSetter;

public class EventListFragment extends JetpackListFragment<Event> {
    @Override
    public Class<Event> getTClass() {
        return Event.class;
    }

    @Override
    public int getSingleLayoutId() {
        return R.layout.single_event;
    }

    @Override
    public Class getValueSetterClass() {
        return EventValueSetter.class;
    }

    @Override
    protected Class<? extends NodeViewModel> getViewModelClass() {
        return EventViewModel.class;
    }

    @Override
    public RecyclerView.LayoutManager getLayoutManager() {
        return new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
    }

}
