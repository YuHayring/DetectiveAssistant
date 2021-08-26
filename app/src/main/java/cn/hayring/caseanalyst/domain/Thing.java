package cn.hayring.caseanalyst.domain;

import com.google.gson.annotations.Expose;

/**
 * @author Hayring
 * @date 2021/8/26
 * @description 代替 {@link Evidence}
 */
public class Thing implements Listable {
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
     * 证物信息
     */
    @Expose
    protected String info;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    @Override
    public String toString() {
        return name;
    }
}
