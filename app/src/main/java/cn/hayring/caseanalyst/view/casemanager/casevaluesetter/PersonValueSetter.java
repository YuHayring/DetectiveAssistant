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
import android.widget.Spinner;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import cn.hayring.caseanalyst.R;
import cn.hayring.caseanalyst.view.casemanager.relationship.RelationshipListActivity;
import cn.hayring.caseanalyst.view.ValueSetter;
import cn.hayring.caseanalyst.domain.Person;
import cn.hayring.caseanalyst.domain.Relationship;
import cn.hayring.caseanalyst.utils.Pointer;

@Deprecated
public class PersonValueSetter extends ValueSetter<Person> {

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
     * 性别选择器
     */
    Spinner genderSwitcher;

    /***
     * 血型选择器
     */
    Spinner bloodTypeSwitcher;

    /***
     * 嫌犯属性选择器
     */
    Spinner suspectSwitcher;

    /***
     * 昵称输入框
     */
    EditText nickNameInputer;

    /***
     * 年龄输入框
     */
    EditText ageInputer;

    /***
     * 人际关系入口
     */
    TextView manManRelationshipEnter;

    /***
     * 人-组织关系入口
     */
    TextView manOrgRelationshipEnter;

    /***
     * 人-事关系入口
     */
    TextView manEventRelationshipEnter;

    /***
     * 人-物关系入口
     */
    TextView manThingRelationshipEnter;

    /***
     * View加载
     */
    protected void initView() {
        super.initView();
        //加载layout实例
        LayoutInflater inflater = getLayoutInflater();
        sonView = (ScrollView) inflater.inflate(R.layout.person_value_setter, null);
        rootLayout.addView(sonView);

        //注册控件
        nameInputer = findViewById(R.id.person_name_inputer);
        infoInputer = findViewById(R.id.person_info_inputer);
        nickNameInputer = findViewById(R.id.person_nick_name_inputer);
        ageInputer = findViewById(R.id.person_age_inputer);
        genderSwitcher = findViewById(R.id.gender_switcher);
        bloodTypeSwitcher = findViewById(R.id.blood_type_switcher);
        suspectSwitcher = findViewById(R.id.suspect_switcher);
        saveButton = findViewById(R.id.person_save_button);
        headImage = findViewById(R.id.person_image);

        manEventRelationshipEnter = sonView.findViewById(R.id.man_event_relationship_text_view);
        manManRelationshipEnter = sonView.findViewById(R.id.man_man_relationship_text_view);
        manOrgRelationshipEnter = sonView.findViewById(R.id.man_org_relationship_text_view);
        manThingRelationshipEnter = sonView.findViewById(R.id.man_thing_relationship_text_view);

        View.OnClickListener relationshipEnterListener = new EditRelationshipListener();

        manEventRelationshipEnter.setOnClickListener(relationshipEnterListener);
        manThingRelationshipEnter.setOnClickListener(relationshipEnterListener);
        manManRelationshipEnter.setOnClickListener(relationshipEnterListener);
        manOrgRelationshipEnter.setOnClickListener(relationshipEnterListener);


        //设置监听器
        saveButton.setOnClickListener(new FinishEditListener());
        headImage.setOnClickListener(view -> {
            Intent importIntent = new Intent(Intent.ACTION_GET_CONTENT);
            importIntent.setType("image/*");//选择图片
            importIntent.addCategory(Intent.CATEGORY_OPENABLE);
            setResult(Activity.RESULT_OK, importIntent);
            startActivityForResult(importIntent, 2); //对应onActivityResult()
        });

        //设置属性显示
        if (!isCreate) {
            saveButton.setEnabled(false);
            saveButton.setVisibility(View.GONE);
            instance = (Person) Pointer.getPoint();


            //基本信息设置
            nameInputer.setText(instance.getName());
            infoInputer.setText(instance.getInfo());
            nickNameInputer.setText(instance.getNickName());
            Integer age = instance.getAge();
            if (age != null) {
                ageInputer.setText(age.toString());
            }


            //设置性别
            if (null == instance.getGender()) {
                genderSwitcher.setSelection(2);
            } else if (instance.getGender()/*==Person.MALE*/) {
                genderSwitcher.setSelection(0);
            } else {
                genderSwitcher.setSelection(1);
            }

            //设置是否为嫌犯
            if (null == instance.getSuspect()) {
                suspectSwitcher.setSelection(2);
            } else if (instance.getGender()) {
                suspectSwitcher.setSelection(0);
            } else {
                suspectSwitcher.setSelection(1);
            }
            String bloodType = instance.getBloodType();
            if (bloodType != null) {
                switch (bloodType) {
                    case Person.A: {
                        bloodTypeSwitcher.setSelection(0);
                    }
                    break;
                    case Person.AB: {
                        bloodTypeSwitcher.setSelection(2);
                    }
                    break;
                    case Person.B: {
                        bloodTypeSwitcher.setSelection(1);
                    }
                    break;
                    case Person.O: {
                        bloodTypeSwitcher.setSelection(3);
                    }
                    break;
                    case Person.RHAB: {
                        bloodTypeSwitcher.setSelection(4);
                    }
                    break;
                    default:
                        bloodTypeSwitcher.setSelection(5);
                }
            } else {
                bloodTypeSwitcher.setSelection(5);
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
            manEventRelationshipEnter.setVisibility(View.GONE);
            manManRelationshipEnter.setVisibility(View.GONE);
            manOrgRelationshipEnter.setVisibility(View.GONE);
            manThingRelationshipEnter.setVisibility(View.GONE);
            instance = caseInstance.createPerson();
        }
    }


    @Override
    protected void writeInstance() {
        String name = nameInputer.getText().toString();
        String info = infoInputer.getText().toString();
        String nickName = nickNameInputer.getText().toString();
        String genderStr = genderSwitcher.getSelectedItem().toString();
        Boolean gender;
        if (genderStr.equals(MALE)) {
            gender = Person.MALE;
        } else if (genderStr.equals(FEMALE)) {
            gender = Person.FEMALE;
        } else {
            gender = null;
        }

        //嫌犯属性
        String suspectStr = suspectSwitcher.getSelectedItem().toString();
        Boolean suspect;
        if (suspectStr.equals(TRUE)) {
            suspect = true;
        } else if (suspectStr.equals(FALSE)) {
            suspect = false;
        } else {
            suspect = null;
        }


        //设置已输入的数据
        instance.setName(name);
        instance.setInfo(info);
        instance.setSuspect(suspect);
        instance.setGender(gender);
        instance.setNickName(nickName);


        String ageStr = ageInputer.getText().toString();
        if (!"".equals(ageStr) && isInteger(ageStr)) {
            instance.setAge(Integer.valueOf(ageStr));
        }


        switch (bloodTypeSwitcher.getSelectedItem().toString()) {
            case Person.A:
            case Person.B:
            case Person.AB:
            case Person.O:
            case Person.RHAB:
                instance.setBloodType(bloodTypeSwitcher.getSelectedItem().toString());
            default:
        }

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
            Intent request = new Intent(PersonValueSetter.this, RelationshipListActivity.class);
            switch (view.getId()) {
                case R.id.man_event_relationship_text_view: {
                    //request.putExtra(ValueSetter.DATA, instance.getManEventRelationships());
                    Pointer.setPoint(instance.getManEventRelationships());
                    request.putExtra(ValueSetter.RELATIONSHIP_TYPE, Relationship.MAN_EVENT);
                }
                break;
                case R.id.man_man_relationship_text_view: {
                    //request.putExtra(ValueSetter.DATA, instance.getManManRelationships());
                    Pointer.setPoint(instance.getManManRelationships());
                    request.putExtra(ValueSetter.RELATIONSHIP_TYPE, Relationship.MAN_MAN);
                }
                break;
                case R.id.man_org_relationship_text_view: {
                    //request.putExtra(ValueSetter.DATA, instance.getManOrgRelationships());
                    Pointer.setPoint(instance.getManOrgRelationships());
                    request.putExtra(ValueSetter.RELATIONSHIP_TYPE, Relationship.MAN_ORG);
                }
                break;
                case R.id.man_thing_relationship_text_view: {
                    //request.putExtra(ValueSetter.DATA, instance.getManThingRelationships());
                    Pointer.setPoint(instance.getManThingRelationships());
                    request.putExtra(ValueSetter.RELATIONSHIP_TYPE, Relationship.MAN_EVIDENCE);
                }
                break;
                default:
                    throw new IllegalArgumentException("Error View Id");
            }
            //request.putExtra(ValueSetter.CONNECTOR, instance);
            Pointer.setConnector(instance);
            startActivity(request);
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


    }

    @Override
    protected void onDestory() {
        if (instance.getImageIndex() != null) {
            deleteFile(instance.getImageIndex() + ".jpg");
        }
    }


}
