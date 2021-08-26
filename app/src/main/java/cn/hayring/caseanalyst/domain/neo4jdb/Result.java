package cn.hayring.caseanalyst.domain.neo4jdb;

import com.google.gson.annotations.Expose;

/**
 * @author Hayring
 * @date 2021/8/25
 * @description 结点查询结果
 */
public class Result<T> {

    /**
     * 结点信息集合
     */
    @Expose
    T[] row;

    /**
     * 结点元数据集合
     */
    @Expose
    SingleMeta[] meta;

    public T[] getRow() {
        return row;
    }

    public void setRow(T[] row) {
        this.row = row;
    }

    public SingleMeta[] getMeta() {
        return meta;
    }

    public void setMeta(SingleMeta[] meta) {
        this.meta = meta;
    }
}
