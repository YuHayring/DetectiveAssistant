package cn.hayring.caseanalyst.view;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import cn.hayring.caseanalyst.R;
import cn.hayring.caseanalyst.domain.Case;

public class Splash extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //隐藏状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //隐藏标题栏
        getSupportActionBar().hide();

        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(readTask, 1000L);

    }

    Runnable readTask = new Runnable() {

        @Override
        public void run() {
            final AlertDialog.Builder normalDialog =
                    new AlertDialog.Builder(Splash.this);
            normalDialog.setTitle("系统选择");
            normalDialog.setMessage("请选择基于不同持久化方案的系统");
            normalDialog.setPositiveButton("Neo4j方案",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {


//            Intent it = new Intent(getApplicationContext(), CaseListActivity.class);
//            it.putExtra("DATA", caseList);
                            Intent it = new Intent(getApplicationContext(), cn.hayring.caseanalyst.view.caselist.CaseListActivity.class);

                            try {
                                Thread.sleep(2000L);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            startActivity(it);
                            finish();
                        }
                    });
            normalDialog.setNegativeButton("序列化方案",
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
                    });
            // 显示
            normalDialog.show();

        }
    };


}

