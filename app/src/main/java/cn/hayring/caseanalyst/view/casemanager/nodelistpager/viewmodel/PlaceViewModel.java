package cn.hayring.caseanalyst.view.casemanager.nodelistpager.viewmodel;

import cn.hayring.caseanalyst.dao.NodeApi;
import cn.hayring.caseanalyst.domain.Place;
import cn.hayring.caseanalyst.net.NetworkInterface;

/**
 * @author Hayring
 * @date 2021/8/26
 * @description
 */
public class PlaceViewModel extends NodeViewModel<Place> {
    @Override
    public Place newInstance(Long id, String name) {
        Place place = new Place();
        place.setId(id);
        place.setName(name);
        return place;
    }

    public static final String DEFAULT_NEW_NAME = "新地点";

    @Override
    public String getDefaultNodeName() {
        return DEFAULT_NEW_NAME;
    }

    @Override
    protected NodeApi<Place> getNodeApi() {
        return NetworkInterface.getPlaceApi();
    }
}
