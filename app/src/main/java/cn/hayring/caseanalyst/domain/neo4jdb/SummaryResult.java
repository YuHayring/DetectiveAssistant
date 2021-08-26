package cn.hayring.caseanalyst.domain.neo4jdb;

import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * @author Hayring
 * @date 2021/8/25
 * @description 查询结果
 */
public class SummaryResult<T> {
    @Expose
    private String[] columns;

    /**
     * 具体查询结果
     */
    @Expose
    private List<Result<T>> data;


    public String[] getColumns() {
        return columns;
    }

    public void setColumns(String[] columns) {
        this.columns = columns;
    }

    public List<Result<T>> getData() {
        return data;
    }

    public void setData(List<Result<T>> data) {
        this.data = data;
    }
}
