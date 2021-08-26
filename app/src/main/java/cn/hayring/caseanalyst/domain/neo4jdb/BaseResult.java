package cn.hayring.caseanalyst.domain.neo4jdb;

import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * @author Hayring
 * @date 2021/8/25
 * @description 查询结果
 */
public class BaseResult {
    /**
     * 结果集合
     */
    @Expose
    private List<SummaryResult> results;

    /**
     * 错误信息
     */
    @Expose
    private Object[] errors;


    public List<SummaryResult> getResults() {
        return results;
    }

    public void setResults(List<SummaryResult> results) {
        this.results = results;
    }

    public Object[] getErrors() {
        return errors;
    }

    public void setErrors(Object[] errors) {
        this.errors = errors;
    }
}
