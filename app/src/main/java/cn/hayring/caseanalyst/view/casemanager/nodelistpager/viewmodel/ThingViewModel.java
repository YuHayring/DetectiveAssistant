package cn.hayring.caseanalyst.view.casemanager.nodelistpager.viewmodel;

import cn.hayring.caseanalyst.dao.NodeApi;
import cn.hayring.caseanalyst.domain.Thing;
import cn.hayring.caseanalyst.net.NetworkInterface;

/**
 * @author Hayring
 * @date 2021/8/26
 * @description
 */
public class ThingViewModel extends NodeViewModel<Thing> {
    @Override
    public Thing newInstance(Long id, String name) {
        Thing thing = new Thing();
        thing.setId(id);
        thing.setName(name);
        return thing;
    }

    public static final String DEFAULT_NEW_NAME = "新物品";

    @Override
    public String getDefaultNodeName() {
        return DEFAULT_NEW_NAME;
    }

    @Override
    protected NodeApi<Thing> getNodeApi() {
        return NetworkInterface.getThingApi();
    }
}
