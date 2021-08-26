package cn.hayring.caseanalyst.domain;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.Calendar;

/***
 * 物件，痕迹，证物
 * @author Hayring
 */
public class Evidence implements Avatars, Listable {

    //Todo证物合成

    /**
     * id
     */
    private Long id;

    /***
     * 证物名称
     */
    @Expose
    protected String name;

    /***
     * 图片资源id
     */
    protected Integer imageIndex;

    /***
     * 证物产生的时间
     */
    protected Calendar createdTime;

    /***
     * 数量
     */
    protected Integer count;

    /***
     * 产生地点
     */
    protected Place createdPlace;

    /***
     * 证物与人关系集合,
     */
    protected ArrayList<Relationship<Person, Evidence>> manThingRelationships;

    /***
     * 证物与组织关系集合
     */
    protected ArrayList<Relationship<Organization, Evidence>> orgThingRelationships;

    /***
     * 证物信息
     */
    @Expose
    protected String info;

    /***
     * 保护构造器，初始化各种集合
     */
    protected Evidence() {
        manThingRelationships = new ArrayList<Relationship<Person, Evidence>>();
        orgThingRelationships = new ArrayList<Relationship<Organization, Evidence>>();
        events = new ArrayList<Event>();
    }

    public Evidence(String name, String info) {
        this();
        this.name = name;
        this.info = info;
    }

    protected ArrayList<Event> events;

    /***
     * 所属案件
     */
    protected Case parentCase;

    public void setParentCase(Case parentCase) {
        this.parentCase = parentCase;
    }

    @Override
    public String toString() {
        return name;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Calendar getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Calendar createdTime) {
        this.createdTime = createdTime;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    @Override
    public Integer getImageIndex() {
        return imageIndex;
    }

    @Override
    public void removeSelf() {
        Relationship.removeAllRelationship(manThingRelationships);
        Relationship.removeAllRelationship(orgThingRelationships);
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setImageIndex(Integer imageIndex) {
        this.imageIndex = imageIndex;
    }

    public Place getCreatedPlace() {
        return createdPlace;
    }

    public void setCreatedPlace(Place createdPlace) {
        this.createdPlace = createdPlace;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public ArrayList<Event> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<Event> events) {
        this.events = events;
    }

    public ArrayList<Relationship<Person, Evidence>> getManThingRelationships() {
        return manThingRelationships;
    }

    public void setManThingRelationships(ArrayList<Relationship<Person, Evidence>> manThingRelationships) {
        this.manThingRelationships = manThingRelationships;
    }

    public ArrayList<Relationship<Organization, Evidence>> getOrgThingRelationships() {
        return orgThingRelationships;
    }

    public void setOrgThingRelationships(ArrayList<Relationship<Organization, Evidence>> orgThingRelationships) {
        this.orgThingRelationships = orgThingRelationships;
    }

    public Case getParentCase() {
        return parentCase;
    }

    /***
     * 关系注册
     * @param instance
     */
    @Override
    public void regRelationship(Relationship instance) {
        if (instance.getItemE().equals(this)) {
            if (instance.getItemT().getClass().equals(Person.class)) {
                manThingRelationships.add(instance);
            } else {
                orgThingRelationships.add(instance);
            }
        } else {
            if (instance.getItemE().getClass().equals(Person.class)) {
                manThingRelationships.add(instance);
            } else {
                orgThingRelationships.add(instance);
            }
        }
    }
}
