package cn.hayring.caseanalyst.net;

import android.content.SharedPreferences;
import android.util.Base64;

import androidx.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import cn.hayring.caseanalyst.CaseAnalystApplication;
import cn.hayring.caseanalyst.dao.CaseApi;
import cn.hayring.caseanalyst.dao.CaseApiImpl;
import cn.hayring.caseanalyst.dao.Neo4jDBApi;
import cn.hayring.caseanalyst.dao.Neo4jResponseAdapter;
import cn.hayring.caseanalyst.dao.NodeApi;
import cn.hayring.caseanalyst.dao.NodeApiImpl;
import cn.hayring.caseanalyst.domain.Event;
import cn.hayring.caseanalyst.domain.Person;
import cn.hayring.caseanalyst.domain.Place;
import cn.hayring.caseanalyst.domain.Thing;
import cn.hayring.caseanalyst.domain.neo4jdb.SummaryResult;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author Hayring
 * @date 2021/8/24
 * @description
 */
public class NetworkInterface {

    /**
     * okhttp客户端
     */
    public static OkHttpClient okHttpClient;

    /**
     * 包装retrofit对象
     */
    public static Retrofit retrofit;

    /**
     * neo4j 数据库操作对象
     */
    public static Neo4jDBApi neo4jApi;

    /**
     * 案件信息操作对象
     */
    public static CaseApi caseApi = new CaseApiImpl();


    /**
     * neo4j设置的key
     */

    public static final String NEO4J_SP_URL_KEY = "url";
    public static final String NEO4J_SP_USERNAME_KEY = "username";
    public static final String NEO4J_SP_PASSWORD_KEY = "password";


    /**
     * 单例内部类
     */
    private static class Singleton {
        private static Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .registerTypeAdapter(SummaryResult.class, new Neo4jResponseAdapter())
                .create();

        /**
         * 人物信息操作对象
         */
        private static NodeApi<Person> personApi = new NodeApiImpl<Person>(Person.class);

        /**
         * 地点信息操作对象
         */
        private static NodeApi<Place> placeApi = new NodeApiImpl<Place>(Place.class);

        /**
         * 事件信息操作对象
         */
        private static NodeApi<Event> eventApi = new NodeApiImpl<Event>(Event.class);

        /**
         * 物品信息操作对象
         */
        private static NodeApi<Thing> thingApi = new NodeApiImpl<Thing>(Thing.class);


    }

    /**
     * 初始化网络请求框架
     */
    public static void init() {
//        if (okHttpClient == null && retrofit == null && neo4jApi == null) {
        synchronized (NetworkInterface.class) {

            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
//                if (okHttpClient == null && retrofit == null && neo4jApi == null) {

            SharedPreferences neo4jsp = PreferenceManager.getDefaultSharedPreferences(CaseAnalystApplication.getInstance());
            String baseUrl = neo4jsp.getString(NEO4J_SP_URL_KEY, "");
            String username = neo4jsp.getString(NEO4J_SP_USERNAME_KEY, "");
            String password = neo4jsp.getString(NEO4J_SP_PASSWORD_KEY, "");
            String result = "Basic " + Base64.encodeToString((username + ':' + password).getBytes(), Base64.NO_WRAP | Base64.NO_PADDING | Base64.URL_SAFE);

            //初始化OKHTTP
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient httpClient = new OkHttpClient.Builder()
                    .writeTimeout(500, TimeUnit.SECONDS)
                    .readTimeout(500, TimeUnit.SECONDS)
                    .connectTimeout(5, TimeUnit.SECONDS)
                    .addInterceptor(interceptor)
                    //身份验证拦截器
                    .addInterceptor(new Interceptor() {

                        String auth = result;

                        @NotNull
                        @Override
                        public Response intercept(@NotNull Chain chain) throws IOException {
                            Request compressedRequest = chain.request().newBuilder()
                                    .addHeader("Authorization", auth)
                                    .addHeader("Content-Type", "application/json")
                                    .build();
                            return chain.proceed(compressedRequest);
                        }
                    })
                    .build();
            //初始化Retrofit
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(httpClient)
                    .addConverterFactory(GsonConverterFactory.create(Singleton.gson))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
            neo4jApi = retrofit.create(Neo4jDBApi.class);

//                }
        }
//        }
    }

    public static NodeApi<Person> getPersonApi() {
        return Singleton.personApi;
    }

    public static NodeApi<Place> getPlaceApi() {
        return Singleton.placeApi;
    }

    public static NodeApi<Event> getEventApi() {
        return Singleton.eventApi;
    }

    public static NodeApi<Thing> getThingApi() {
        return Singleton.thingApi;
    }


}
