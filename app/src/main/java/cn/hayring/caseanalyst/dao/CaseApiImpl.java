package cn.hayring.caseanalyst.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.hayring.caseanalyst.domain.Case;
import cn.hayring.caseanalyst.domain.neo4jdb.BaseRequest;
import cn.hayring.caseanalyst.domain.neo4jdb.BaseResult;
import cn.hayring.caseanalyst.domain.neo4jdb.Result;
import cn.hayring.caseanalyst.net.NetworkInterface;
import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * @author Hayring
 * @date 2021/8/25
 * @description
 */
public class CaseApiImpl implements CaseApi {

    /**
     * Cypher语句：查询所有案件
     */
    private static final String CYPHER_QUERY_ALL_CASE = "MATCH (c:Case) RETURN c";

    /**
     * Cypher语句：创建新案件
     */
    private static final String CYPHER_CREATE_NEW_CASE = "CREATE (c:Case $props) RETURN id(c)";


    /**
     * Cypher语句：删除案件
     */
    private static final String CYPHER_DELETE_CASE = "MATCH (c) WHERE id(c) = $id DELETE c";

    /**
     * Cypher语句：更新案件
     */
    private static final String CYPHER_UPDATE_CASE = "MATCH (c) WHERE id(c) = $id SET c = $case";

    /**
     * 一步创建新案件请求
     *
     * @param newCaseName 新案件名
     * @return 查询请求
     */
    public static BaseRequest createNewCaseRequest(String newCaseName) {
        BaseRequest request = new BaseRequest();
        Map<String, Object> statements = new HashMap<>();
        statements.put(BaseRequest.STATEMENT_KEY, CYPHER_CREATE_NEW_CASE);
        Map<String, Map<String, String>> parameters = Collections.singletonMap(BaseRequest.PROPS_KEY,
                Collections.singletonMap(BaseRequest.NAME_KEY, newCaseName));
        statements.put(BaseRequest.PARAMETERS_KEY, parameters);
        request.setStatements(new Map[]{statements});
        return request;
    }

    /**
     * 一步创建删除案件请求
     *
     * @param caseId 案件id
     * @return 查询请求
     */
    public static BaseRequest createDeleteCaseRequest(Long caseId) {
        BaseRequest request = new BaseRequest();
        Map<String, Object> statements = new HashMap<>();
        statements.put(BaseRequest.STATEMENT_KEY, CYPHER_DELETE_CASE);
        Map<String, Long> parameters = Collections.singletonMap(BaseRequest.ID_KEY, caseId);
        statements.put(BaseRequest.PARAMETERS_KEY, parameters);
        request.setStatements(new Map[]{statements});
        return request;
    }


    /**
     * 一步创建更新案件请求
     *
     * @param caxe 案件
     * @return 查询请求
     */
    public static BaseRequest createUpdateCaseRequest(Case caxe) {
        BaseRequest request = new BaseRequest();
        Map<String, Object> statements = new HashMap<>();
        statements.put(BaseRequest.STATEMENT_KEY, CYPHER_UPDATE_CASE);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(BaseRequest.ID_KEY, caxe.getId());
        parameters.put(BaseRequest.CASE_KEY, caxe);
        statements.put(BaseRequest.PARAMETERS_KEY, parameters);
        request.setStatements(new Map[]{statements});
        return request;
    }


    /**
     * 获取案件信息
     * Didn't set observe Thread
     * 处理线程未设置
     *
     * @return 可以观察的案件列表Observable
     */
    @Override
    public Observable<List<Case>> getCaseList() {
        //创建单个查询请求
        BaseRequest request = BaseRequest.createSingleStatement(CYPHER_QUERY_ALL_CASE);
        return
                NetworkInterface.neo4jApi.commitTransaction(request)
                        .map(new Function<BaseResult, List<Case>>() {
                            @Override
                            public List<Case> apply(@NonNull BaseResult baseResult) throws Exception {
                                List<Result> results = baseResult.getResults().get(0).getData();
                                List<Case> cases = new ArrayList<>(results.size());
                                for (Result result : results) {
                                    Result<Case> actualResult = (Result<Case>) result;
                                    Case caxe = actualResult.getRow()[0];
                                    caxe.setId(actualResult.getMeta()[0].getId());
                                    cases.add(caxe);
                                }
                                return cases;
                            }
                        });
    }


    /**
     * 创建新案件
     *
     * @return 案件id
     */
    @Override
    public Observable<Long> createNewCase(String newCaseName) {
        //创建单个查询请求
        BaseRequest request = createNewCaseRequest(newCaseName);
        return NetworkInterface.neo4jApi.commitTransaction(request)
                .map(new Function<BaseResult, Long>() {
                    @Override
                    public Long apply(@NonNull BaseResult baseResult) throws Exception {
                        return Long.valueOf(((Result<String>) baseResult.getResults().get(0).getData().get(0)).getRow()[0]);
                    }
                });
    }

    /**
     * 删除案件
     *
     * @param caseId 案件id
     * @return 成功与否
     */
    @Override
    public Observable<Boolean> deleteCase(Long caseId) {
        BaseRequest request = createDeleteCaseRequest(caseId);
        return NetworkInterface.neo4jApi.commitTransaction(request)
                .map(ensureNoErrorFunction);
    }

    /**
     * 更新案件
     *
     * @param caxe
     * @return 成功与否
     */
    @Override
    public Observable<Boolean> updateCase(Case caxe) {
        BaseRequest request = createUpdateCaseRequest(caxe);
        return NetworkInterface.neo4jApi.commitTransaction(request)
                .map(ensureNoErrorFunction);
    }

    /**
     * 将无错误的response 转化为true
     * 有错误转化为false
     *
     * @return baseResult.getErrors().length == 0
     */
    public static Function<BaseResult, Boolean> ensureNoErrorFunction = new Function<BaseResult, Boolean>() {
        @Override
        public Boolean apply(@NonNull BaseResult baseResult) throws Exception {
            return baseResult.getErrors().length == 0;
        }
    };
}
