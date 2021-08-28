package cn.hayring.caseanalyst.view.casemanager.casevaluesetter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import cn.hayring.caseanalyst.R;
import cn.hayring.caseanalyst.view.casemanager.relationship.RelationshipListActivity;
import cn.hayring.caseanalyst.view.ValueSetter;
import cn.hayring.caseanalyst.domain.Organization;
import cn.hayring.caseanalyst.domain.Relationship;
import cn.hayring.caseanalyst.utils.Pointer;

@Deprecated
public class OrganizationValueSetter extends ValueSetter<Organization> {

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
     * 组织-组织关系入口
     */
    TextView orgOrgRelationshipEnter;

    /***
     * 人-组织关系入口
     */
    TextView manOrgRelationshipEnter;

    /***
     * 组织-事关系入口
     */
    TextView orgEventRelationshipEnter;

    /***
     * 组织-物关系入口
     */
    TextView orgThingRelationshipEnter;

    /***
     * 加载页面
     */
    @Override
    protected void initView() {
        super.initView();
        //加载layout实例
        LayoutInflater inflater = getLayoutInflater();
        sonView = (ScrollView) inflater.inflate(R.layout.org_value_setter, null);
        rootLayout.addView(sonView);

        //注册控件,信息显示
        nameInputer = findViewById(R.id.org_name_inputer);
        infoInputer = findViewById(R.id.org_info_inputer);
        saveButton = findViewById(R.id.org_save_button);
        headImage = findViewById(R.id.org_image);

        orgEventRelationshipEnter = sonView.findViewById(R.id.org_event_relationship_text_view);
        orgOrgRelationshipEnter = sonView.findViewById(R.id.org_org_relationship_text_view);
        manOrgRelationshipEnter = sonView.findViewById(R.id.man_org_relationship_text_view);
        orgThingRelationshipEnter = sonView.findViewById(R.id.org_thing_relationship_text_view);


        if (!isCreate) {
            saveButton.setEnabled(false);
            saveButton.setVisibility(View.GONE);
            //instance = (Organization) requestInfo.getSerializableExtra(DATA);
            instance = (Organization) Pointer.getPoint();


            nameInputer.setText(instance.getName());
            infoInputer.setText(instance.getInfo());

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
            orgEventRelationshipEnter.setVisibility(View.GONE);
            orgOrgRelationshipEnter.setVisibility(View.GONE);
            orgThingRelationshipEnter.setVisibility(View.GONE);
            manOrgRelationshipEnter.setVisibility(View.GONE);
            instance = caseInstance.createOrganization();
        }

        //设置监听器
        saveButton.setOnClickListener(new FinishEditListener());
        headImage.setOnClickListener(view -> {
            Intent importIntent = new Intent(Intent.ACTION_GET_CONTENT);
            importIntent.setType("image/*");//选择图片
            importIntent.addCategory(Intent.CATEGORY_OPENABLE);
            setResult(Activity.RESULT_OK, importIntent);
            startActivityForResult(importIntent, 2); //对应onActivityResult()
        });

        View.OnClickListener relationshipEnterListener = new EditRelationshipListener();

        orgEventRelationshipEnter.setOnClickListener(relationshipEnterListener);
        orgThingRelationshipEnter.setOnClickListener(relationshipEnterListener);
        orgOrgRelationshipEnter.setOnClickListener(relationshipEnterListener);
        manOrgRelationshipEnter.setOnClickListener(relationshipEnterListener);

    }


    @Override
    protected void writeInstance() {
        instance.setName(nameInputer.getText().toString());
        instance.setInfo(infoInputer.getText().toString());
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
    }

    /***
     * 关系编辑入口监听器
     */
    class EditRelationshipListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Intent request = new Intent(OrganizationValueSetter.this, RelationshipListActivity.class);
            switch (view.getId()) {
                case R.id.org_event_relationship_text_view: {
                    //request.putExtra(ValueSetter.DATA, instance.getOrgEventRelationships());
                    Pointer.setPoint(instance.getOrgEventRelationships());
                    request.putExtra(ValueSetter.RELATIONSHIP_TYPE, Relationship.ORG_EVENT);
                }
                break;
                case R.id.org_org_relationship_text_view: {
                    //request.putExtra(ValueSetter.DATA, instance.getOrgOrgRelationships());
                    Pointer.setPoint(instance.getOrgOrgRelationships());
                    request.putExtra(ValueSetter.RELATIONSHIP_TYPE, Relationship.ORG_ORG);
                }
                break;
                case R.id.man_org_relationship_text_view: {
                    //request.putExtra(ValueSetter.DATA, instance.getManOrgRelationships());
                    Pointer.setPoint(instance.getManOrgRelationships());
                    request.putExtra(ValueSetter.RELATIONSHIP_TYPE, Relationship.MAN_ORG);
                }
                break;
                case R.id.org_thing_relationship_text_view: {
                    //request.putExtra(ValueSetter.DATA, instance.getOrgThingRelationships());
                    Pointer.setPoint(instance.getOrgThingRelationships());
                    request.putExtra(ValueSetter.RELATIONSHIP_TYPE, Relationship.ORG_EVIDENCE);
                }
                break;
                default:
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

/*        //未改变就结束
        if (!itemTransporter.getBooleanExtra(ValueSetter.CHANGED, true)) {
            return;
        }
        ArrayList data = (ArrayList) itemTransporter.getSerializableExtra(ValueSetter.DATA);
        switch (itemTransporter.getIntExtra(ValueSetter.RELATIONSHIP_TYPE, -1)) {
            case Relationship.ORG_EVENT: {
                instance.setOrgEventRelationships(data);
            }
            break;
            case Relationship.ORG_ORG: {
                instance.setOrgOrgRelationships(data);
            }
            break;
            case Relationship.MAN_ORG: {
                instance.setManOrgRelationships(data);
            }
            break;
            case Relationship.ORG_EVIDENCE: {
                instance.setOrgThingRelationships(data);
            }
            break;
            default:
                throw new IllegalArgumentException("Error relationship type!");
        }*/


    }

    @Override
    protected void onDestory() {
        if (instance.getImageIndex() != null) {
            deleteFile(instance.getImageIndex() + ".jpg");
        }
    }


}
