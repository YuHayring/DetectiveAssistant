package cn.hayring.caseanalyst.view.casemanager.casevaluesetter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.GregorianCalendar;

import cn.hayring.caseanalyst.R;
import cn.hayring.caseanalyst.view.casemanager.relationship.RelationshipListActivity;
import cn.hayring.caseanalyst.view.ValueSetter;
import cn.hayring.caseanalyst.domain.Evidence;
import cn.hayring.caseanalyst.domain.Relationship;
import cn.hayring.caseanalyst.utils.Pointer;

@Deprecated
public class EvidenceValueSetter extends ValueSetter<Evidence> {

    //标记头像是否改过
    protected boolean imageChanged;

    /***
     * 图片存储
     */
    Bitmap image;

    /***
     * 头像显示View
     */
    ImageView headImage;

    /***
     * 数量编辑器
     */
    EditText countInputer;


    /***
     * 人-物关系入口
     */
    TextView manThingRelationshipEnter;


    /***
     * 组织-物关系入口
     */
    TextView orgThingRelationshipEnter;

    /***
     * 产生时间临时变量
     */
    Calendar time;

    /***
     * 产生日期显示设置
     *
     */
    TextView createDate;

    /***
     * 产生时间显示设置
     */
    TextView createTime;

    @Override
    protected void initView() {
        super.initView();
        //加载layout实例
        LayoutInflater inflater = getLayoutInflater();
        sonView = (ScrollView) inflater.inflate(R.layout.evidence_value_setter, null);
        rootLayout.addView(sonView);
        //注册控件,信息显示
        nameInputer = findViewById(R.id.evidence_name_inputer);
        infoInputer = findViewById(R.id.evidence_info_inputer);
        countInputer = findViewById(R.id.evidence_count_inputer);
        saveButton = findViewById(R.id.evidence_save_button);
        headImage = findViewById(R.id.evidence_image);
        createDate = findViewById(R.id.create_date);
        createTime = findViewById(R.id.create_time);
        manThingRelationshipEnter = sonView.findViewById(R.id.man_thing_relationship_text_view);
        orgThingRelationshipEnter = sonView.findViewById(R.id.org_thing_relationship_text_view);


        if (!isCreate) {
            saveButton.setEnabled(false);
            saveButton.setVisibility(View.GONE);
            //instance = (Evidence) requestInfo.getSerializableExtra(DATA);
            instance = (Evidence) Pointer.getPoint();


            nameInputer.setText(instance.getName());
            infoInputer.setText(instance.getInfo());
            Integer count = instance.getCount();
            if (count != null) {
                countInputer.setText(count.toString());
            }
            if ((time = instance.getCreatedTime()) != null) {
                if (!caseInstance.isShortTimeCase()) {
                    createDate.setText(dateFormatter.format(time.getTime()));
                } else {
                    createDate.setText("第" + time.get(Calendar.DATE) + "天");
                }
                createTime.setText(timeFormatter.format(time.getTime()));
            } else {
                time = new GregorianCalendar(1970, 1, 1, 0, 0);
            }

            //判断是否有头像并加载
            /*if (instance.getImageIndex() != null) {
                try {
                    FileInputStream headIS = openFileInput(instance.getImageIndex() + ".jpg");
                    image = BitmapFactory.decodeStream(headIS);
                    headImage.setImageBitmap(image);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }*/
            image = loadHeadImage(instance, headImage, this);


        } else {
            manThingRelationshipEnter.setVisibility(View.GONE);
            orgThingRelationshipEnter.setVisibility(View.GONE);
            instance = caseInstance.createEvidence();
            //2000-1-1 00:00
            time = new GregorianCalendar(2000, 1, 1, 0, 0);
        }


        //设置监听器
        saveButton.setOnClickListener(new FinishEditListener());
        headImage.setOnClickListener(view -> {
            Intent importIntent = new Intent(Intent.ACTION_GET_CONTENT);
            importIntent.setType("image/*");//选择图片
            importIntent.addCategory(Intent.CATEGORY_OPENABLE);
            setResult(Activity.RESULT_OK, importIntent);
            //importIntent.putExtra("return-data", true);
            startActivityForResult(importIntent, 2); //对应onActivityResult()
        });

        setOnClickListenerForTimeSetter(createDate, createTime, this, instance.getCreatedTime());

        View.OnClickListener relationshipEnterListener = new EditRelationshipListener();
        orgThingRelationshipEnter.setOnClickListener(relationshipEnterListener);
        orgThingRelationshipEnter.setOnClickListener(relationshipEnterListener);
    }


    @Override
    protected void writeInstance() {
        instance.setName(nameInputer.getText().toString());
        instance.setInfo(infoInputer.getText().toString());
        String countStr = countInputer.getText().toString();
        if (!"".equals(countStr) && isInteger(countStr)) {
            instance.setCount(Integer.valueOf(countStr));
        }
        //requestInfo.putExtra(DATA, instance);

        //判断头像是否变化，是则保存并写入
        if (imageChanged) {
            try {
                FileOutputStream fo;
                File f;
                int imageIndex;
                if (instance.getImageIndex() == null) {
                    do {
                        imageIndex = random.nextInt();
                        f = new File(getFilesDir().getPath() + "/" + imageIndex + ".jpg");
                    } while (f.exists());
                    instance.setImageIndex(imageIndex);
                } else {
                    imageIndex = instance.getImageIndex();
                }
                fo = openFileOutput(imageIndex + ".jpg", Context.MODE_PRIVATE);
                image.compress(Bitmap.CompressFormat.JPEG, 100, fo);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (time.get(Calendar.YEAR) != 1970) {
            instance.setCreatedTime(time);
        }
    }


    /***
     * 关系编辑入口监听器
     */
    class EditRelationshipListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Intent request = new Intent(EvidenceValueSetter.this, RelationshipListActivity.class);
            if (view.getId() == R.id.org_thing_relationship_text_view) {
                //request.putExtra(ValueSetter.DATA, instance.getOrgThingRelationships());
                Pointer.setPoint(instance.getOrgThingRelationships());
                request.putExtra(ValueSetter.RELATIONSHIP_TYPE, Relationship.ORG_EVIDENCE);
            } else if (view.getId() == R.id.man_thing_relationship_text_view) {
                //request.putExtra(ValueSetter.DATA, instance.getManThingRelationships());
                Pointer.setPoint(instance.getManThingRelationships());
                request.putExtra(ValueSetter.RELATIONSHIP_TYPE, Relationship.MAN_EVIDENCE);
            } else {
                throw new IllegalArgumentException("Error View Id");
            }
            //request.putExtra(ValueSetter.CONNECTOR, instance);
            Pointer.setConnector(instance);
            startActivityForResult(request, 1);
        }
    }

    /***
     * 编辑完成调用
     * @author Hayring
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //将返回的头像存进内存
        if (requestCode == 2 && resultCode == Activity.RESULT_OK) {

            //Bundle extras = data.getExtras();
            try {

                Uri uri = data.getData();
                InputStream is = getContentResolver().openInputStream(uri);
                image = BitmapFactory.decodeStream(is);
                //Bitmap headBitmap = data.getParcelableExtra("data");
                headImage.setImageBitmap(image);

                imageChanged = true;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }


    }

    @Override
    protected void onDestory() {
        if (instance.getImageIndex() != null) {
            deleteFile(instance.getImageIndex() + ".jpg");
        }
    }


}
