package cn.hayring.caseanalyst.domain.neo4jdb;

import com.google.gson.annotations.Expose;

/**
 * @author Hayring
 * @date 2021/8/25
 * @description 单个元数据
 */
public class SingleMeta {
    /**
     * id
     */
    @Expose
    private Long id;

    /**
     * 类型（结点或边）
     */
    @Expose
    private String type;


    @Expose
    private Boolean deleted;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }
}