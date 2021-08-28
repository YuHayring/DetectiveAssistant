package cn.hayring.caseanalyst.view.casemanager.nodevaluesetter;

import android.view.LayoutInflater;

import cn.hayring.caseanalyst.databinding.NodeValueSetterPersonBinding;
import cn.hayring.caseanalyst.domain.Person;
import cn.hayring.caseanalyst.view.casemanager.nodelistpager.viewmodel.NodeViewModel;
import cn.hayring.caseanalyst.view.casemanager.nodelistpager.viewmodel.PersonViewModel;

/**
 * @author Hayring
 * @date 2021/8/28
 * @description
 */
public class PersonValueSetter extends NodeValueSetter<Person> {
    @Override
    protected Class<? extends NodeViewModel> getViewModelClass() {
        return PersonViewModel.class;
    }

    /**
     * 绑定视图
     */
    NodeValueSetterPersonBinding viewBinding;

    /***
     * 加载页面
     */
    @Override
    protected void initView() {
        super.initView();
        //加载layout实例
        viewBinding = NodeValueSetterPersonBinding.inflate(LayoutInflater.from(this));
        sonView = viewBinding.getRoot();
        rootLayout.addView(sonView);

        //基本信息设置
        //姓名
        viewBinding.personNameInputer.setText(node.getName());
        //信息
        viewBinding.personInfoInputer.setText(node.getInfo());
        //年龄
        Integer age = node.getAge();
        if (age != null) {
            viewBinding.personAgeInputer.setText(age.toString());
        }
        //设置性别
        if (null == node.getSex()) {
            viewBinding.genderSwitcher.setSelection(2);
        } else if (Person.SEX_MALE.equals(node.getSex())) {
            viewBinding.genderSwitcher.setSelection(0);
        } else {
            viewBinding.genderSwitcher.setSelection(1);
        }
        //设置血型
        String bloodType = node.getBloodType();
        if (bloodType == null) {
            viewBinding.bloodTypeSwitcher.setSelection(5);
        } else {
            switch (bloodType) {
                case Person.A: {
                    viewBinding.bloodTypeSwitcher.setSelection(0);
                }
                break;
                case Person.AB: {
                    viewBinding.bloodTypeSwitcher.setSelection(2);
                }
                break;
                case Person.B: {
                    viewBinding.bloodTypeSwitcher.setSelection(1);
                }
                break;
                case Person.O: {
                    viewBinding.bloodTypeSwitcher.setSelection(3);
                }
                break;
                case Person.RHAB: {
                    viewBinding.bloodTypeSwitcher.setSelection(4);
                }
                break;
                default:
                    viewBinding.bloodTypeSwitcher.setSelection(5);
            }

        }
    }

    @Override
    protected void writeNode() {

        String name = viewBinding.personNameInputer.getText().toString();
        String info = viewBinding.personInfoInputer.getText().toString();
        String age = viewBinding.personAgeInputer.getText().toString();
        String sex = viewBinding.genderSwitcher.getSelectedItem().toString();
        String bloodType = viewBinding.bloodTypeSwitcher.getSelectedItem().toString();

        node.setName(name);
        node.setInfo(info);
        if (!"".equals(age) && isInteger(age)) {
            node.setAge(Integer.valueOf(age));
        }
        node.setSex(sex);
        node.setBloodType(bloodType);
    }
}
