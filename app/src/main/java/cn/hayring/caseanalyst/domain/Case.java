package cn.hayring.caseanalyst.domain;


import com.google.gson.annotations.Expose;

import java.util.ArrayList;

import cn.hayring.caseanalyst.CaseAnalystApplication;

/***
 * 案件
 * @author Hayring
 */
public class Case implements Listable {

    /**
     * id
     */
    private Long id;


    /***
     * 名字
     */
    @Expose
    protected String name;

    /***
     * 案件信息
     */
    @Expose
    protected String info;

    /***
     * 证物集合
     */
    protected ArrayList<Evidence> evidences;

    /***
     * 事件集合
     */
    protected ArrayList<Event> events;

    /***
     * 人物集合
     */
    protected ArrayList<Person> persons;

    /***
     * 组织集合
     */
    protected ArrayList<Organization> organizations;

    /***
     * 是否是短期案件
     */
    boolean isShortTimeCase = true;

    public Case() {
        if (!CaseAnalystApplication.getInstance().isNeo4j()) {
            organizations = new ArrayList<Organization>();
            persons = new ArrayList<Person>();
            evidences = new ArrayList<Evidence>();
            events = new ArrayList<Event>();

            //-------------
            nonOrgPersons = new ArrayList<Person>();
        }
    }

    public Case(String name, String info) {
        this();
        this.name = name;
        this.info = info;
    }


    /***
     * 事件生成并注册
     * Create Event and reg it
     * @param name
     * @param info
     * @return new Event
     */
    public Event createEvent(String name, String info) {
        Event event = new Event(name, info);
        //events.add(event);
        event.setParentCase(this);
        return event;
    }

    public Event createEvent() {
        Event event = new Event();
        //events.add(event);
        event.setParentCase(this);
        return event;
    }

    /***
     * 人生成并注册
     * Create Person and reg it
     * @param name
     * @param suspect
     * @param info
     * @return
     */
    public Person createPerson(String name, Boolean suspect, String info) {
        Person person = new Person(name, suspect, info);
        //persons.add(person);
        person.setParentCase(this);
        //----------
        nonOrgPersons.add(person);
        return person;
    }


    public Person createPerson() {
        Person person = new Person();
        //persons.add(person);
        person.setParentCase(this);
        //-------------
        nonOrgPersons.add(person);
        return person;
    }

    /***
     * 组织生成并注册
     * Create Org and reg it
     * @param name
     * @param info
     * @return
     */
    public Organization createOrganization(String name, String info) {
        Organization org = new Organization(name, info);
        //organizations.add(org);
        org.setParentCase(this);
        return org;
    }

    public Organization createOrganization() {
        Organization org = new Organization();
        //organizations.add(org);
        org.setParentCase(this);
        return org;
    }


    /***
     * 证物生成并注册
     * Create Evidenct and reg it
     */
    public Evidence createEvidence() {
        Evidence evidence = new Evidence();
        //evidences.add(evidence);
        evidence.setParentCase(this);
        return evidence;
    }


    /***
     * 新建并注册证据
     * @param name
     * @param info
     * @return
     */
    public Evidence createEvidence(String name, String info) {
        Evidence evidence = new Evidence(name, info);
        //evidences.add(evidence);
        evidences.add(evidence);
        return evidence;
    }

    /***
     * 显示名字
     * @return it's name
     */
    @Override
    public String toString() {
        return name;
    }


    /***
     * 按所需类型获取List
     * @return ListbaleList
     */
    public ArrayList getListableList(Class clazz) {
        if (clazz.equals(Person.class)) {
            return persons;
        }

        if (clazz.equals(Organization.class)) {
            return organizations;
        }

        if (clazz.equals(Event.class)) {
            return events;
        }

        if (clazz.equals(Evidence.class)) {
            return evidences;
        }

        throw new IllegalArgumentException("Error class! Not listable");
    }






    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }



    public ArrayList<Evidence> getEvidences() {
        return evidences;
    }

    public void setEvidences(ArrayList<Evidence> evidences) {
        this.evidences = evidences;
    }

    public ArrayList<Event> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<Event> events) {
        this.events = events;
    }

    public ArrayList<Person> getPersons() {
        return persons;
    }

    public void setPersons(ArrayList<Person> persons) {
        this.persons = persons;
    }

    public ArrayList<Organization> getOrganizations() {
        return organizations;
    }

    public void setOrganizations(ArrayList<Organization> organizations) {
        this.organizations = organizations;
    }

    public boolean isShortTimeCase() {
        return isShortTimeCase;
    }

    public void setShortTimeCase(boolean shortTimeCase) {
        isShortTimeCase = shortTimeCase;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /***
     * 无组织成员
     */
    protected ArrayList<Person> nonOrgPersons;


    public ArrayList<Person> getNonOrgPersons() {
        return nonOrgPersons;
    }
}
