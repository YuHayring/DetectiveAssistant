package cn.hayring.caseanalyst.view;


import static cn.hayring.caseanalyst.net.NetworkInterface.NEO4J_SP_PASSWORD_KEY;
import static cn.hayring.caseanalyst.net.NetworkInterface.NEO4J_SP_URL_KEY;
import static cn.hayring.caseanalyst.net.NetworkInterface.NEO4J_SP_USERNAME_KEY;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import cn.hayring.caseanalyst.R;
import cn.hayring.caseanalyst.domain.Case;
import cn.hayring.caseanalyst.net.NetworkInterface;
import es.dmoral.toasty.Toasty;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class Splash extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //隐藏状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //隐藏标题栏
        getSupportActionBar().hide();

        setContentView(R.layout.activity_splash);
        normalDialog =
                new AlertDialog.Builder(Splash.this)
                        .setTitle("系统选择")
                        .setMessage("请选择基于不同持久化方案的系统")
                        .setPositiveButton("Neo4j方案",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        neo4jSelected();

                                    }
                                }).setNeutralButton("序列化方案",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ArrayList<Case> caseList = new ArrayList();
                                int i = 0;
                                FileInputStream fi;
                                ObjectInputStream ois;
                                while (true) {
                                    try {
                                        fi = openFileInput(Integer.toString(i++) + ".case");
                                        ois = new ObjectInputStream(fi);
                                        caseList.add((Case) ois.readObject());
                                    } catch (FileNotFoundException e) {
                                        break;
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        AlertDialog.Builder builder = new AlertDialog.Builder(Splash.this);
                                        builder.setTitle("错误！");
                                        builder.setMessage("数据版本过旧，请在系统设置清除所有数据再启动。");
                                        builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                throw new RuntimeException(e);
                                            }
                                        });
                                        builder.show();
                                        return;
                                    }


                                }

                                Intent it = new Intent(getApplicationContext(), CaseListActivity.class);
                                it.putExtra("DATA", caseList);
                                startActivity(it);
                                finish();
                            }
                        }).setNegativeButton("取消", (d, v) -> {
                    finish();
                });
        new Handler().postDelayed(readTask, 1000L);


    }

    AlertDialog.Builder normalDialog;

    Runnable readTask = new Runnable() {

        @Override
        public void run() {
            normalDialog.show();
        }
    };

    /**
     * neo4j设置的key
     */
    private static final String NEO4J_SP_NAME = "neo4j";

    /**
     * neo4j使用说明是否阅读
     */
    private static final String NEO4j_SP_READED_VERSION_KEY = "readed";

    /**
     * neo4j使用说明版本
     */
    private static final int NEO4J_INSTRUCTION_CURRENT_VERSION = 1;


    /**
     * 选择了neo4j方案的处理流程
     */
    private void neo4jSelected() {
        SharedPreferences neo4jsp = getSharedPreferences(NEO4J_SP_NAME, MODE_PRIVATE);
        //neo4j使用方案未阅读
        if (neo4jsp.getInt(NEO4j_SP_READED_VERSION_KEY, 0) < NEO4J_INSTRUCTION_CURRENT_VERSION) {
            final AlertDialog.Builder instructionDialog =
                    new AlertDialog.Builder(Splash.this)
                            .setTitle(R.string.splash_neo4j_instructions_title)
                            .setMessage(R.string.splash_neo4j_instructions)
                            .setPositiveButton("确认", (d, w) -> {
                                //已阅读，传入版本
                                neo4jsp.edit().putInt(NEO4j_SP_READED_VERSION_KEY, NEO4J_INSTRUCTION_CURRENT_VERSION).apply();
                                neo4jInit();
                            })
                            .setNegativeButton("取消", (d, w) -> {
                                normalDialog.show();
                            });
            instructionDialog.show();
        } else {
            neo4jInit();
        }
    }


    /**
     * neo4j 初始化
     */
    private void neo4jInit() {
        SharedPreferences neo4jsp = getSharedPreferences(NEO4J_SP_NAME, MODE_PRIVATE);
        if (!(neo4jsp.contains(NEO4J_SP_URL_KEY) &&
                neo4jsp.contains(NEO4J_SP_USERNAME_KEY) &&
                neo4jsp.contains(NEO4J_SP_PASSWORD_KEY)
        )) {
            showNeo4jInputDialog();
        } else {
            ensureNeo4j();
        }
    }

    private void showNeo4jInputDialog() {
        SharedPreferences neo4jsp = getSharedPreferences(NEO4J_SP_NAME, MODE_PRIVATE);
        LayoutInflater factory = LayoutInflater.from(this);
        final View neo4jInputView = factory.inflate(R.layout.splash_neo4j_input_dialog, null);
        final EditText urlInput = neo4jInputView.findViewById(R.id.url_input);
        final EditText usernameInput = neo4jInputView.findViewById(R.id.username_input);
        final EditText passwordInput = neo4jInputView.findViewById(R.id.password_input);
        String baseUrl = neo4jsp.getString(NEO4J_SP_URL_KEY, "");
        urlInput.setText(baseUrl);
        String username = neo4jsp.getString(NEO4J_SP_USERNAME_KEY, "");
        usernameInput.setText(username);
        String password = neo4jsp.getString(NEO4J_SP_PASSWORD_KEY, "");
        passwordInput.setText(password);
        AlertDialog.Builder neo4jInputDialog = new AlertDialog.Builder(this);
        neo4jInputDialog.setTitle(R.string.splash_neo4j_input_dialog_title);
        neo4jInputDialog.setIcon(android.R.drawable.ic_dialog_info);
        neo4jInputDialog.setView(neo4jInputView);
        neo4jInputDialog.setPositiveButton("是", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int i) {
                SharedPreferences.Editor editor = neo4jsp.edit();
                editor.putString(NEO4J_SP_URL_KEY, urlInput.getText().toString());
                editor.putString(NEO4J_SP_USERNAME_KEY, usernameInput.getText().toString());
                editor.putString(NEO4J_SP_PASSWORD_KEY, passwordInput.getText().toString());
                editor.commit();
                ensureNeo4j();

            }
        });
        neo4jInputDialog.setNegativeButton("否", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int i) {
                normalDialog.show();
            }
        });
        neo4jInputDialog.show();
    }

    private void ensureNeo4j() {
        NetworkInterface.init();
        NetworkInterface.neo4jApi.discovery().map(new Function<Response<ResponseBody>, Boolean>() {

            @Override
            public Boolean apply(@NonNull Response response) throws Exception {
                return response.code() == 200;
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull Boolean discovered) {
                        if (discovered) {
                            Intent it = new Intent(getApplicationContext(), cn.hayring.caseanalyst.view.caselist.CaseListActivity.class);
                            startActivity(it);
                            finish();
                        } else {
                            Toasty.error(Splash.this, "Neo4j is not running!");
                            showNeo4jInputDialog();
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Toasty.error(Splash.this, e.getMessage()).show();
                        e.printStackTrace();
                        showNeo4jInputDialog();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


}

