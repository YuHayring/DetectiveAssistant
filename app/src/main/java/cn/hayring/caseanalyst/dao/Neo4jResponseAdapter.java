package cn.hayring.caseanalyst.dao;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import cn.hayring.caseanalyst.domain.Case;
import cn.hayring.caseanalyst.domain.neo4jdb.Result;
import cn.hayring.caseanalyst.domain.neo4jdb.SummaryResult;

/**
 * @author Hayring
 * @date 2021/8/25
 * @description neo4j响应解析器
 */
public class Neo4jResponseAdapter implements JsonDeserializer<SummaryResult> {

//    TypeToken caseType = new TypeToken<Result<Case>>().getType();

    @Override
    public SummaryResult deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = (JsonObject) json;
        SummaryResult summaryResult = new SummaryResult();
        String[] types = context.deserialize(jsonObject.get("columns"), String[].class);
        List<Result> results = new ArrayList<>(types.length);
        if (types.length == 0) return summaryResult;
        JsonArray resultJsonArray = (JsonArray) jsonObject.get("data");
        Type actualType;
        String typeSub = types[0].substring(0, 1);
        switch (typeSub) {
            case "c": {
                actualType = new ParameterizedTypeImpl(Result.class, new Class[]{Case.class});
            }
            break;
            case "i": {
                actualType = new ParameterizedTypeImpl(Result.class, new Class[]{String.class});
            }
            break;
            default:
                throw new JsonParseException("Type doesn't exist");
        }

        for (JsonElement resultJson : resultJsonArray) {
            results.add(context.deserialize(resultJson, actualType));
        }
        summaryResult.setData(results);
        return summaryResult;

    }

}