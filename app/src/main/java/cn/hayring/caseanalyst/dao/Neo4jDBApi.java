package cn.hayring.caseanalyst.dao;

import cn.hayring.caseanalyst.domain.neo4jdb.BaseRequest;
import cn.hayring.caseanalyst.domain.neo4jdb.BaseResult;
import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * @author Hayring
 * @date 2021/8/24
 * @description
 */
public interface Neo4jDBApi {

    /**
     * 数据库探测接口
     *
     * @return 响应
     */
    @GET("/")
    Observable<Response<ResponseBody>> discovery();

    /**
     * 数据库事务调用接口
     *
     * @param request json请求
     * @return json响应
     */
    @POST("db/neo4j/tx/commit")
    Observable<BaseResult> commitTransaction(@Body BaseRequest request);

    /**
     * 数据库事务调用接口
     *
     * @param request json请求
     * @return http响应
     */
    @POST("db/neo4j/tx/commit")
    Observable<Response<ResponseBody>> commitTransactionForStatusCode(@Body BaseRequest request);


}
