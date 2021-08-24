package cn.hayring.caseanalyst;

import android.app.Application;

import wendu.dsbridge.DWebView;

/**
 * @author hayring
 * @date 6/21/21 7:39 PM
 */
public class CaseAnalystApplication extends Application {

    private static CaseAnalystApplication instance;


    private boolean neo4j = false;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        DWebView.setWebContentsDebuggingEnabled(true);
    }

    public static CaseAnalystApplication getInstance() {
        return instance;
    }

    public boolean isNeo4j() {
        return neo4j;
    }

    public void setNeo4j(boolean neo4j) {
        this.neo4j = neo4j;
    }
}
