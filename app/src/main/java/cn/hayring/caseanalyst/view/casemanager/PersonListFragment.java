package cn.hayring.caseanalyst.view.casemanager;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import cn.hayring.caseanalyst.R;
import cn.hayring.caseanalyst.domain.Person;
import cn.hayring.caseanalyst.utils.Pointer;
import cn.hayring.caseanalyst.view.MyListFragment;
import cn.hayring.caseanalyst.view.casemanager.casevaluesetter.PersonValueSetter;
import cn.hayring.caseanalyst.view.magic.PersonGraphV3;

public class PersonListFragment extends MyListFragment<Person> {


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
    public RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(mainActivity);
    }

    @Override
    protected ArrayList<Person> getData() {
        return getMainActivity().getCaseInstance().getPersons();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        menu.add("查看人物关系图");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //设置人物数据
        Pointer.setPoint(mainActivity.getCaseInstance());
        //绘制关系图
        startActivity(new Intent(mainActivity, PersonGraphV3.class));
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        setHasOptionsMenu(true);
    }
}
