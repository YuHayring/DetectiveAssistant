package cn.hayring.caseanalyst.domain.neo4jdb;

import com.google.gson.annotations.Expose;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Hayring
 * @date 2021/8/25
 * @description 查询请求
 */
public class BaseRequest {

    /**
     * json请求的statement key常量
     */
    public static final String STATEMENT_KEY = "statement";

    /**
     * json请求的parameters key常量
     */
    public static final String PARAMETERS_KEY = "parameters";

    /**
     * json请求的props key常量
     */
    public static final String PROPS_KEY = "props";

    /**
     * json请求的name key常量
     */
    public static final String NAME_KEY = "name";

    /**
     * json请求的id key常量
     */
    public static final String ID_KEY = "id";

    /**
     * json请求的caseId key常量
     */
    public static final String CASE_ID_KEY = "caseId";

    /**
     * json请求的personId key常量
     */
    public static final String PERSON_ID_KEY = "personId";


    /**
     * json请求的info key常量
     */
    public static final String INFO_KEY = "info";


    /**
     * json请求的case key常量
     */
    public static final String CASE_KEY = "case";


    /**
     * json请求的person key常量
     */
    public static final String PERSON_KEY = "person";

    /**
     * 一步创建单个查询语句请求
     *
     * @param statement 查询语句
     * @return 查询请求
     */
    public static BaseRequest createSingleStatement(String statement) {
        BaseRequest request = new BaseRequest();
        request.setStatements(new Map[]{Collections.singletonMap(BaseRequest.STATEMENT_KEY, statement)});
        return request;
    }

    /**
     * 一步创建带caseId的单个查询语句请求
     *
     * @param statement 查询语句
     * @param caseId    案件id
     * @return 查询请求
     */
    public static BaseRequest createSingleStatementWithCaseId(String statement, Long caseId) {
        BaseRequest request = new BaseRequest();
        Map<String, Object> statementMap = new HashMap<>();
        statementMap.put(BaseRequest.STATEMENT_KEY, statement);
        Map<String, Object> parameters = Collections.singletonMap(CASE_ID_KEY, caseId);
        statementMap.put(BaseRequest.PARAMETERS_KEY, parameters);
        request.setStatements(new Map[]{statementMap});
        return request;
    }


    /**
     * 查询语句集合
     */
    @Expose
    private Map<String, Object>[] statements;


    /**
     * 查询语句集合
     *
     * @return {@link #statements}
     */
    public Map<String, Object>[] getStatements() {
        return statements;
    }


    public void setStatements(Map<String, Object>[] statements) {
        this.statements = statements;
    }

}
