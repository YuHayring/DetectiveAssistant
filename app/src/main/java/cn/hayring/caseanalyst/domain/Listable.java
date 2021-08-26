package cn.hayring.caseanalyst.domain;


import java.io.Serializable;

/***
 * 继承Serializable 是为了putExtra(Serializable)
 * @author hayring
 */
public interface Listable extends Serializable {
    String getName();

    String getInfo();

    Long getId();

    void setId(Long id);
}
