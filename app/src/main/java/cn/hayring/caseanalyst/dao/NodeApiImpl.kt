package cn.hayring.caseanalyst.dao

import cn.hayring.caseanalyst.domain.neo4jdb.BaseRequest
import cn.hayring.caseanalyst.domain.*
import cn.hayring.caseanalyst.domain.neo4jdb.Result
import cn.hayring.caseanalyst.net.NetworkInterface
import io.reactivex.Observable
import java.lang.IllegalArgumentException
import java.util.*

/**
 * @author Hayring
 * @date 2021/8/26
 * @description
 */
class NodeApiImpl<T : Listable?>(val clazz: Class<T>) : NodeApi<T> {
    companion object {
        const val ID = "Id"
    }

    val key = when (clazz) {
        Person::class.java -> "person"
        Event::class.java -> "event"
        Thing::class.java -> "thing"
        Place::class.java -> "place"
        else -> throw IllegalArgumentException("Illegal class")
    }
    val nodeType = when (clazz) {
        Person::class.java -> Neo4jCypherConst.PERSON_INDEX
        Event::class.java -> Neo4jCypherConst.EVENT_INDEX
        Thing::class.java -> Neo4jCypherConst.THING_INDEX
        Place::class.java -> Neo4jCypherConst.PLACE_INDEX
        else -> throw IllegalArgumentException("Illegal class")
    }

    /**
     * 获取结点数据
     * @return 结点列表
     */
    override fun getNodeList(caseId: Long?): Observable<MutableList<T>> {
        val request = BaseRequest.createSingleStatementWithCaseId(Neo4jCypherConst.getCypher(nodeType, Neo4jCypherConst.SELECT_INDEX), caseId)
        return NetworkInterface.neo4jApi.commitTransaction(request)
                .map { baseResult ->
                    val results: List<Result<T>> = baseResult.results[0].getData() as List<Result<T>>
                    val nodes: MutableList<T> = ArrayList()
                    for (result in results) {
                        val node = result.row[0]
                        node!!.setId(result.meta[0].id)
                        nodes.add(node!!)
                    }
                    nodes
                }
    }

    /**
     * 创建新结点
     * @param newNodeName 结点名
     * @return 结点id
     */
    override fun createNewNode(caseId: Long?, newNodeName: String?): Observable<Long> {
        val request = createNewNodeRequest(caseId, newNodeName)
        return NetworkInterface.neo4jApi.commitTransaction(request)
                .map { baseResult -> java.lang.Long.valueOf((baseResult.results[0].data[0] as Result<String?>).row[0]!!) }
    }

    /**
     * 删除结点
     * @param caseId 案件id
     * @param personId 结点id
     * @return 删除成功
     */
    override fun deleteNode(caseId: Long?, nodeId: Long?): Observable<Boolean> {
        val request = createDeleteNodeRequest(caseId, nodeId)
        return NetworkInterface.neo4jApi.commitTransaction(request)
                .map<Boolean?>(CaseApiImpl.ensureNoErrorFunction)
    }

    override fun updateNode(caseId: Long?, node: T): Observable<Boolean> {
        val request = createUpdateNodeRequest(caseId, node)
        return NetworkInterface.neo4jApi.commitTransaction(request)
                .map<Boolean?>(CaseApiImpl.ensureNoErrorFunction)
    }

    /**
     * 一步创建新结点请求
     * @param caseId 案件id
     * @param newNodeName 新结点名
     * @return 查询请求
     */
    fun createNewNodeRequest(caseId: Long?, newNodeName: String?): BaseRequest {
        val request = BaseRequest()
        val statements: MutableMap<String, Any> = HashMap()
        statements[BaseRequest.STATEMENT_KEY] = Neo4jCypherConst.getCypher(nodeType, Neo4jCypherConst.CREATE_INDEX)
        val parameters: MutableMap<String, Any?> = HashMap()
        parameters[BaseRequest.CASE_ID_KEY] = caseId
        parameters[BaseRequest.PROPS_KEY] = Collections.singletonMap(BaseRequest.NAME_KEY, newNodeName)
        statements[BaseRequest.PARAMETERS_KEY] = parameters
        request.setStatements(arrayOf(statements))
        return request
    }

    /**
     * 一步创建删除结点请求
     *
     * @param caseId 案件id
     * @param personId 结点id
     * @return 查询请求
     */
    fun createDeleteNodeRequest(caseId: Long?, personId: Long?): BaseRequest {
        val request = BaseRequest()
        val statements: MutableMap<String, Any> = HashMap()
        statements[BaseRequest.STATEMENT_KEY] = Neo4jCypherConst.getCypher(nodeType, Neo4jCypherConst.DELETE_INDEX)
        val parameters: MutableMap<String, Long?> = HashMap()
        parameters[BaseRequest.CASE_ID_KEY] = caseId
        parameters[key + ID] = personId
        statements[BaseRequest.PARAMETERS_KEY] = parameters
        request.setStatements(arrayOf(statements))
        return request
    }

    /**
     * 一步创建更新结点请求
     * @param caseId 案件id
     * @param node 结点
     * @return 查询请求
     */
    fun createUpdateNodeRequest(caseId: Long?, node: T): BaseRequest {
        val request = BaseRequest()
        val statements: MutableMap<String, Any> = HashMap()
        statements[BaseRequest.STATEMENT_KEY] = Neo4jCypherConst.getCypher(nodeType, Neo4jCypherConst.UPDATE_INDEX)
        val parameters: MutableMap<String, Any?> = HashMap()
        parameters[BaseRequest.CASE_ID_KEY] = caseId
        parameters[key + ID] = node?.id
        parameters[key] = node
        statements[BaseRequest.PARAMETERS_KEY] = parameters
        request.setStatements(arrayOf(statements))
        return request
    }


}