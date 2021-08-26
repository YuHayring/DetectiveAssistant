package cn.hayring.caseanalyst.view.casemanager.nodelistpager.viewmodel;

import cn.hayring.caseanalyst.dao.NodeApi;
import cn.hayring.caseanalyst.domain.Person;
import cn.hayring.caseanalyst.net.NetworkInterface;

/**
 * @author Hayring
 * @date 2021/8/26
 * @description
 */
public class PersonViewModel extends NodeViewModel<Person> {
    @Override
    public Person newInstance(Long id, String name) {
        Person person = new Person();
        person.setId(id);
        person.setName(name);
        return person;
    }

    public static final String DEFAULT_NEW_NAME = "新人物";

    @Override
    public String getDefaultNodeName() {
        return DEFAULT_NEW_NAME;
    }

    @Override
    protected NodeApi<Person> getNodeApi() {
        return NetworkInterface.getPersonApi();
    }
}
