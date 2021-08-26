package cn.hayring.caseanalyst.dao

import cn.hayring.caseanalyst.dao.Neo4jCypherConst
import cn.hayring.caseanalyst.domain.Event
import cn.hayring.caseanalyst.domain.Person
import cn.hayring.caseanalyst.domain.Place
import cn.hayring.caseanalyst.domain.Thing
import java.lang.IllegalArgumentException

/**
 * @author Hayring
 * @date 2021/8/26
 * @description
 */
object Neo4jCypherConst {

    var cyphers = Array(4) { arrayOfNulls<String>(4) }
    const val SELECT_INDEX = 0
    const val CREATE_INDEX = 1
    const val DELETE_INDEX = 2
    const val UPDATE_INDEX = 3

    const val PERSON_INDEX = 0
    const val EVENT_INDEX = 1
    const val THING_INDEX = 2
    const val PLACE_INDEX = 3

    @Synchronized
    fun getCypher(nodeType: Int, actionType: Int): String {
        if (cyphers[nodeType][actionType] == null) {
            val paramName: String
            val paramValue: String
            when (nodeType) {
                PERSON_INDEX -> {
                    paramName = "p"
                    paramValue = "Person"
                }
                EVENT_INDEX -> {
                    paramName = "e"
                    paramValue = "Event"
                }
                THING_INDEX -> {
                    paramName = "t"
                    paramValue = "Thing"
                }
                PLACE_INDEX -> {
                    paramName = "l"
                    paramValue = "Place"
                }
                else -> throw IllegalArgumentException("Illegal arguement \"nodeType\"")
            }
            when (actionType) {
                SELECT_INDEX -> {
                    cyphers[nodeType][actionType] = "MATCH (c)-[:INCLUDE]->($paramName:$paramValue) " +
                            "WHERE id(c) = \$caseId RETURN $paramName"
                }
                CREATE_INDEX -> {
                    cyphers[nodeType][actionType] = "MATCH (c) WHERE id(c) = \$caseId " +
                            "CREATE (c)-[:INCLUDE]->($paramName:$paramValue \$props) RETURN id($paramName)"
                }
                DELETE_INDEX -> {
                    cyphers[nodeType][actionType] = "MATCH (c)-[r:INCLUDE]->($paramName:$paramValue) " +
                            "WHERE id(c) = \$caseId and id($paramName) = \$${paramValue.toLowerCase()}Id DELETE r,$paramName"
                }
                UPDATE_INDEX -> {
                    cyphers[nodeType][actionType] = "MATCH (c)-[:INCLUDE]->($paramName:$paramValue) WHERE id(c) = \$caseId and id($paramName) = \$${paramValue.toLowerCase()}Id SET $paramName = \$${paramValue.toLowerCase()}"
                }
                else -> throw IllegalArgumentException("Illegal arguement \"actionType\"")
            }
        }
        return cyphers[nodeType][actionType]!!
    }
}