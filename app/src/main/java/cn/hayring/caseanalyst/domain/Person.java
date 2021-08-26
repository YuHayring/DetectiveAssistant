package cn.hayring.caseanalyst.domain;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

/***
 * 案件参与的人
 * @author Hayring
 */
public class Person implements Avatars, Listable {

    /**
     * id
     */
    private Long id;

    /***
     * 姓名
     */
    @Expose
    protected String name;

    /***
     * 昵称
     */
    protected String nickName;

    /***
     * 年龄
     */
    @Expose
    protected Integer age;

    /***
     * 是否为嫌犯
     */
    protected Boolean suspect;

    /**
     * neo4j性别
     */
    @Expose
    protected String sex;
    public static final String SEX_MALE = "male";
    public static final String SEX_FEMALE = "female";


    /***
     * 性别
     */
    protected Boolean gender;
    public static final Boolean MALE = true;
    public static final Boolean FEMALE = false;

    /***
     * 血型
     */
    @Expose
    protected String bloodType;
    public static final String A = "A";
    public static final String AB = "AB";
    public static final String B = "B";
    public static final String O = "O";
    public static final String RHAB = "RHAB";

    /***
     * 头像资源id
     */
    protected Integer imageIndex;

    @Override
    public String getName() {
        return name;
    }

    /***
     * 人物描述
     */
    @Expose
    protected String info;

    /***
     * 人与证物关系集合
     */
    protected ArrayList<Relationship<Person, Evidence>> manThingRelationships;

    /***
     * 事件与人关系集合
     */
    protected ArrayList<Relationship<Person, Event>> manEventRelationships;

    /***
     * 其他身份
     */
    protected ArrayList<Person> multipleIdentities;

    /***
     * 人与人关系集合
     */
    protected ArrayList<Relationship<Person, Person>> manManRelationships;

    /***
     * 人与组织关系集合
     */
    protected ArrayList<Relationship<Person, Organization>> manOrgRelationships;

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        organization.getMembers().add(this);
        parentCase.getNonOrgPersons().remove(this);
        this.organization = organization;
    }

    public void clearOrganization() {
        organization.getMembers().remove(this);
        parentCase.getNonOrgPersons().add(this);
        organization = null;
    }

    /***
     * 所属组织
     */
    protected Organization organization;




    /***
     * 所属案件
     */
    protected Case parentCase;

    public void setParentCase(Case parentCase) {
        this.parentCase = parentCase;
    }

    public Case getParentCase() {
        return parentCase;
    }

    /***
     * 保护构造器，初始化各种集合
     */
    public Person() {
        manEventRelationships = new ArrayList<Relationship<Person, Event>>();
        manThingRelationships = new ArrayList<Relationship<Person, Evidence>>();
        multipleIdentities = new ArrayList<Person>();
        manManRelationships = new ArrayList<Relationship<Person, Person>>();
        manOrgRelationships = new ArrayList<Relationship<Person, Organization>>();
    }

    public Person(String name, Boolean suspect, String info) {
        this();
        this.name = name;
        this.suspect = suspect;
        this.info = info;
    }

    /***
     * 显示名字
     * @return it's name
     */
    @Override
    public String toString() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Boolean getGender() {
        return gender;
    }

    public void setGender(Boolean gender) {
        this.gender = gender;
    }

    @Override
    public Integer getImageIndex() {
        return imageIndex;
    }

    /***
     * 删除时调用
     */
    @Override
    public void removeSelf() {
        Relationship.removeAllRelationship(manEventRelationships);
        Relationship.removeAllRelationship(manManRelationships);
        Relationship.removeAllRelationship(manOrgRelationships);
        Relationship.removeAllRelationship(manThingRelationships);
        if (organization == null) {
            parentCase.getNonOrgPersons().remove(this);
        } else {
            organization.getMembers().remove(this);
        }
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

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public ArrayList<Relationship<Person, Evidence>> getManThingRelationships() {
        return manThingRelationships;
    }

    public void setManThingRelationships(ArrayList<Relationship<Person, Evidence>> manThingRelationships) {
        this.manThingRelationships = manThingRelationships;
    }

    public ArrayList<Relationship<Person, Event>> getManEventRelationships() {
        return manEventRelationships;
    }

    public void setManEventRelationships(ArrayList<Relationship<Person, Event>> manEventRelationships) {
        this.manEventRelationships = manEventRelationships;
    }

    public Boolean getSuspect() {
        return suspect;
    }

    public void setSuspect(Boolean suspect) {
        this.suspect = suspect;
    }

    public String getBloodType() {
        return bloodType;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    public ArrayList<Person> getMultipleIdentities() {
        return multipleIdentities;
    }

    public void setMultipleIdentities(ArrayList<Person> multipleIdentities) {
        this.multipleIdentities = multipleIdentities;
    }

    public ArrayList<Relationship<Person, Person>> getManManRelationships() {
        return manManRelationships;
    }

    public void setManManRelationships(ArrayList<Relationship<Person, Person>> manManRelationships) {
        this.manManRelationships = manManRelationships;
    }

    public ArrayList<Relationship<Person, Organization>> getManOrgRelationships() {
        return manOrgRelationships;
    }

    public void setManOrgRelationships(ArrayList<Relationship<Person, Organization>> manOrgRelationships) {
        this.manOrgRelationships = manOrgRelationships;
    }


    /***
     * 关系注册
     * @param instance
     */
    @Override
    public void regRelationship(Relationship instance) {
        switch (instance.getType()) {
            case Relationship.MAN_EVENT: {
                manEventRelationships.add(instance);
            }
            break;
            case Relationship.MAN_EVIDENCE: {
                manThingRelationships.add(instance);
            }
            break;
            case Relationship.MAN_ORG: {
                manOrgRelationships.add(instance);
            }
            break;
            case Relationship.MAN_MAN: {
                manManRelationships.add(instance);
            }
            break;
            default:
                throw new IllegalArgumentException("ERROR Relationship type");
        }
    }
}
