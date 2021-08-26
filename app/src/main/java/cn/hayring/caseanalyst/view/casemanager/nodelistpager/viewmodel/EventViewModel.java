package cn.hayring.caseanalyst.view.casemanager.nodelistpager.viewmodel;

import cn.hayring.caseanalyst.dao.NodeApi;
import cn.hayring.caseanalyst.domain.Event;
import cn.hayring.caseanalyst.net.NetworkInterface;

/**
 * @author Hayring
 * @date 2021/8/26
 * @description
 */
public class EventViewModel extends NodeViewModel<Event> {
    @Override
    public Event newInstance(Long id, String name) {
        Event event = new Event();
        event.setId(id);
        event.setName(name);
        return event;
    }

    public static final String DEFAULT_NEW_NAME = "新事件";

    @Override
    public String getDefaultNodeName() {
        return DEFAULT_NEW_NAME;
    }

    @Override
    protected NodeApi<Event> getNodeApi() {
        return NetworkInterface.getEventApi();
    }
}
