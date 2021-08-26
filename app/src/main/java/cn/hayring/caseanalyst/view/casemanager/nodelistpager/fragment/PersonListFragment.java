package cn.hayring.caseanalyst.view.casemanager.nodelistpager.fragment;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import cn.hayring.caseanalyst.R;
import cn.hayring.caseanalyst.domain.Person;
import cn.hayring.caseanalyst.view.casemanager.casevaluesetter.PersonValueSetter;
import cn.hayring.caseanalyst.view.casemanager.nodelistpager.JetpackListFragment;
import cn.hayring.caseanalyst.view.casemanager.nodelistpager.viewmodel.PersonViewModel;

/**
 * @author Hayring
 * @date 2021/8/26
 * @description
 */
public class PersonListFragment extends JetpackListFragment<Person> {
    @Override
    public Class<Person> getTClass() {
        return Person.class;
    }

    @Override
    public int getSingleLayoutId() {
        return R.layout.single_head_frame;
    }

    @Override
    public Class getValueSetterClass() {
        return PersonValueSetter.class;
    }


    @Override
    protected Class<PersonViewModel> getViewModelClass() {
        return PersonViewModel.class;
    }

    @Override
    public RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(requireContext());
    }


}
