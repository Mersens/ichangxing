package com.cxwl.ichangxing.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.bumptech.glide.Glide;
import com.cxwl.ichangxing.R;
import com.cxwl.ichangxing.adapter.CCSpinnerAdapter;
import com.cxwl.ichangxing.adapter.CXSpinnerAdapter;
import com.cxwl.ichangxing.app.Constants;
import com.cxwl.ichangxing.entity.CCEntity;
import com.cxwl.ichangxing.entity.CXEntity;
import com.cxwl.ichangxing.entity.EventEntity;
import com.cxwl.ichangxing.entity.User;
import com.cxwl.ichangxing.entity.UserBaseEntity;
import com.cxwl.ichangxing.utils.GlideImageLoader;
import com.cxwl.ichangxing.utils.RequestManager;
import com.cxwl.ichangxing.utils.ResultObserver;
import com.cxwl.ichangxing.utils.RxBus;
import com.cxwl.ichangxing.utils.SPreferenceUtil;
import com.cxwl.ichangxing.view.LoadingDialogFragment;
import com.cxwl.ichangxing.view.SelectDialog;
import com.google.gson.Gson;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.view.CropImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class UserRzActivity extends BaseActivity implements View.OnClickListener {
    public static final int REQUEST_CODE_SELECT = 100;
    public static final int REQUEST_CODE_PREVIEW = 101;
    public static final int JSZ_ZFY = 1;//驾驶证政府也
    public static final int SFZ_Z = 2;//身份证正面
    public static final int SFZ_F = 3;//身份证反面
    public static final int SFZ_ALL = 4;//身份证人证合一
    public static final int XSZ_ALL = 5;//行驶证正副页
    public static final int CTZ = 6;//车头照
    public static final int CDZ = 7;//车斗照
    public static final int CLBX = 8;//车辆保险
    public static final int CLYYZ = 9;//车辆营运证
    public static final int CYZGZ = 10;//从业资格证
    private int selectType = -1;
    private TimePickerView endPicker;
    private TimePickerView startPicker;
    private TimePickerView zcsjPicker;
    private ImageView mImgBack;
    private TextView mTextTitle;
    private TextView mTextRZZT;
    private EditText mEditRealName;
    private EditText mEditJSZNum;
    private EditText mEditSFZNum;
    private EditText mEditCPH;
    private ImageView mImgJSZ_ZFY;
    private ImageView mImgSFZ_Z;
    private ImageView mImgSFZ_F;
    private ImageView mImgSFZ_ALL;

    private ImageView mImgXSZ_ALL;
    private ImageView mImgCTZ;
    private ImageView mImgCDZ;
    private ImageView mImgCLBX;
    private ImageView mImgCLYYZ;
    private EditText mEditDLYSZNum;
    private EditText mEditCYZGZNum;
    private ImageView mImgCYZGZ;
    private TextView mTextBX_Start_Time;
    private TextView mTextBX_End_Time;
    private Spinner cxSpiner;
    private Spinner ccSpiner;
    private EditText mEditLoadP;
    private EditText mEditSCDH;

    private EditText mEditCYRName;
    private EditText mEditCYR_DLYSZNum;
    private EditText mEditLXR;
    private EditText mEditLXDH;
    private RelativeLayout mLayoutZCSJ;
    private TextView mTextZCSJ;
    private Button mBtn;

    private UserBaseEntity userBaseEntity = new UserBaseEntity();
    private List<CXEntity> cxEntities=new ArrayList<>();
    private List<CCEntity> ccEntities=new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_user_rz);
        init();
    }

    @Override
    public void init() {
        initView();
        initEvent();
        initDatas();
        initImagePicker();
        initTimePicker();

    }
    private void initImagePicker() {
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideImageLoader());   //设置图片加载器
        imagePicker.setShowCamera(true);                      //显示拍照按钮
        imagePicker.setCrop(false);                            //允许裁剪（单选才有效）
        imagePicker.setSaveRectangle(true);                   //是否按矩形区域保存
        imagePicker.setSelectLimit(1);                        //选中数量限制
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
        imagePicker.setFocusWidth(800);                       //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(800);                      //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setOutPutX(1000);                         //保存文件的宽度。单位像素
        imagePicker.setOutPutY(1000);                         //保存文件的高度。单位像素
    }

    private void initTimePicker() {
        zcsjPicker = new TimePickerBuilder(UserRzActivity.this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                mTextZCSJ.setText(getTime(date));
                userBaseEntity.setCarrierRegisterTime(getTime(date));

            }
        }).build();
        startPicker = new TimePickerBuilder(UserRzActivity.this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                mTextBX_Start_Time.setText(getTime(date));
                userBaseEntity.getTruckBo().setInsuranceStart(getTime(date));

            }
        }).build();
        endPicker = new TimePickerBuilder(UserRzActivity.this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                mTextBX_End_Time.setText(getTime(date));
                userBaseEntity.getTruckBo().setInsuranceEnd(getTime(date));
            }
        }).build();
    }

    private void initDatas() {
        String token = SPreferenceUtil.getInstance(this).getToken();
        if (TextUtils.isEmpty(token)) {
            Toast.makeText(this, "用户信息为空，请重新登录！", Toast.LENGTH_SHORT).show();
            return;
        }
        getUserInfo(token);



    }

    private void getCXInfo(String token) {
        RequestManager.getInstance()
                .mServiceStore
                .dict(token,"captain_management")
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResultObserver(new RequestManager.onRequestCallBack() {
                    @Override
                    public void onSuccess(String msg) {
                        Log.e("dict", "incomeSt==" + msg);
                        if (!TextUtils.isEmpty(msg)) {
                            try {
                                JSONObject object = new JSONObject(msg);
                                if (object.getInt("code") == 0) {
                                    JSONArray array=object.getJSONArray("data");
                                    Gson gson=new Gson();
                                    for (int i = 0; i <array.length() ; i++) {
                                        String string=array.getJSONObject(i).toString();
                                        CXEntity entity=gson.fromJson(string,CXEntity.class);
                                        cxEntities.add(entity);
                                    }
                                    setCXInfo();
                                } else {
                                    Toast.makeText(UserRzActivity.this, object.getString("message"),
                                            Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else {
                            Toast.makeText(UserRzActivity.this, "获取失败！", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(String msg) {
                        Log.e("getLoad", "onError==" + msg);
                        Toast.makeText(UserRzActivity.this, "获取失败！" + msg, Toast.LENGTH_SHORT).show();
                    }
                }));

    }


    private void getCCInfo(String token) {
        RequestManager.getInstance()
                .mServiceStore
                .dict(token,"captain_management")
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResultObserver(new RequestManager.onRequestCallBack() {
                    @Override
                    public void onSuccess(String msg) {
                        Log.e("incomeSt", "incomeSt==" + msg);
                        if (!TextUtils.isEmpty(msg)) {
                            try {
                                JSONObject object = new JSONObject(msg);
                                if (object.getInt("code") == 0) {
                                    JSONArray array=object.getJSONArray("data");
                                    Gson gson=new Gson();
                                    for (int i = 0; i <array.length() ; i++) {
                                        String string=array.getJSONObject(i).toString();
                                        CCEntity entity=gson.fromJson(string,CCEntity.class);
                                        ccEntities.add(entity);
                                    }
                                    setCCInfo();
                                } else {
                                    Toast.makeText(UserRzActivity.this, object.getString("message"),
                                            Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else {
                            Toast.makeText(UserRzActivity.this, "获取失败！", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(String msg) {
                        Log.e("getLoad", "onError==" + msg);
                        Toast.makeText(UserRzActivity.this, "获取失败！" + msg, Toast.LENGTH_SHORT).show();
                    }
                }));

    }
    //车型，车长



    private void setCCInfo() {
        CCSpinnerAdapter adapter=new CCSpinnerAdapter(UserRzActivity.this,ccEntities);
        ccSpiner.setAdapter(adapter);
        ccSpiner.setSelection(0, true);
        ccSpiner.setDropDownVerticalOffset(dp2px(UserRzActivity.this, 36));
        String truckLengthName = userBaseEntity.getTruckBo().getLength();
        if (!TextUtils.isEmpty(truckLengthName)) {
            int index = getCCIndexByName(truckLengthName);
            ccSpiner.setSelection(index);
        } else {
            userBaseEntity.getTruckBo().setLength(ccEntities.get(0).getValue());
        }

    }
    private void setCXInfo() {
        CXSpinnerAdapter adapter=new CXSpinnerAdapter(UserRzActivity.this,cxEntities);
        cxSpiner.setAdapter(adapter);
        cxSpiner.setSelection(0, true);
        cxSpiner.setDropDownVerticalOffset(dp2px(UserRzActivity.this, 36));
        String truckTypeName = userBaseEntity.getTruckBo().getTruckType();

        if (!TextUtils.isEmpty(truckTypeName)) {
            int index = getCXIndexByName(truckTypeName);
            cxSpiner.setSelection(index);
        } else {
            if(userBaseEntity!=null &&userBaseEntity.getTruckBo()!=null){
                if(cxEntities.size()>0){
                    userBaseEntity.getTruckBo().setTruckType(cxEntities.get(0).getValue());
                }
            }
        }
    }

    private void getUserInfo(final String token) {
        RequestManager.getInstance()
                .mServiceStore
                .getUserInfo(token)
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResultObserver(new RequestManager.onRequestCallBack() {
                    @Override
                    public void onSuccess(String msg) {
                        Log.e("incomeSt", "incomeSt==" + msg);
                        if (!TextUtils.isEmpty(msg)) {
                            try {
                                JSONObject object = new JSONObject(msg);
                                if (object.getInt("code") == 0) {
                                    JSONObject jsonObject = object.getJSONObject("data");
                                    SPreferenceUtil.getInstance(UserRzActivity.this)
                                            .setUserinfo(jsonObject.toString());
                                    Gson gson = new Gson();
                                    User user = gson.fromJson(jsonObject.toString(), User.class);
                                    setUseInfo(user);
                                    getUserBaseInfo(token);

                                } else {
                                    Toast.makeText(UserRzActivity.this, object.getString("message"),
                                            Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else {
                            Toast.makeText(UserRzActivity.this, "获取失败！", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(String msg) {
                        Log.e("getLoad", "onError==" + msg);
                        Toast.makeText(UserRzActivity.this, "获取失败！" + msg, Toast.LENGTH_SHORT).show();
                    }
                }));

    }

    private void setUseInfo(User user) {
        if (user != null) {
            if (user.getVerificationStatus() == 0) {
                //未认证
                mTextRZZT.setText("未认证");
            } else if (user.getVerificationStatus() == 1) {
                //认证中
                mTextRZZT.setText("认证中");
            } else if (user.getVerificationStatus() == 2) {
                //认证通过
                mTextRZZT.setText("认证通过");
            } else if (user.getVerificationStatus() == 3) {
                //认证失败
                mTextRZZT.setText("认证失败");
            }
        }
    }

    private void getUserBaseInfo(final String token) {
        RequestManager.getInstance()
                .mServiceStore
                .getBaseInfo(token)
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResultObserver(new RequestManager.onRequestCallBack() {
                    @Override
                    public void onSuccess(String msg) {

                        Log.e("getBaseInfo", "getBaseInfo==" + msg);
                        if (!TextUtils.isEmpty(msg)) {
                            try {
                                JSONObject object = new JSONObject(msg);
                                if (object.getInt("code") == 0) {
                                    Gson gson = new Gson();
                                    JSONObject datas = object.getJSONObject("data");
                                    userBaseEntity = gson.fromJson(datas.toString(), UserBaseEntity.class);
                                    setUserDatas(userBaseEntity);
                                }
                                getCXInfo(token);
                                getCCInfo(token);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onError(String msg) {
                        Log.e("getBaseInfo", "getBaseInfo==" + msg);
                    }
                }));
    }

    private void setUserDatas(UserBaseEntity userBaseEntity) {
        mEditRealName.setText(userBaseEntity.getRealName());
        mEditJSZNum.setText(userBaseEntity.getDriverLicense());
        mEditSFZNum.setText(userBaseEntity.getIdCard());
        mEditCPH.setText(userBaseEntity.getTruckBo().getPlateNo());
        String licenseImg = userBaseEntity.getLicenseImg();
        if (!TextUtils.isEmpty(licenseImg)) {
            Glide.with(this)
                    .load(Constants.PICTURE_HOST + licenseImg)
                    .error(R.mipmap.ic_no_img)
                    .into(mImgJSZ_ZFY);

        }

        String idCardFrontImg = userBaseEntity.getIdCardFrontImg();
        if (!TextUtils.isEmpty(idCardFrontImg)) {
            Glide.with(this)
                    .load(Constants.PICTURE_HOST + idCardFrontImg)
                    .error(R.mipmap.ic_no_img)
                    .into(mImgSFZ_Z);

        }

        String idCardBackImg = userBaseEntity.getIdCardBackImg();
        if (!TextUtils.isEmpty(idCardBackImg)) {
            Glide.with(this)
                    .load(Constants.PICTURE_HOST + idCardBackImg)
                    .error(R.mipmap.ic_no_img)
                    .into(mImgSFZ_F);

        }
        String personAndIdCardImg = userBaseEntity.getPersonAndIdCardImg();
        if (!TextUtils.isEmpty(personAndIdCardImg)) {
            Glide.with(this)
                    .load(Constants.PICTURE_HOST + personAndIdCardImg)
                    .error(R.mipmap.ic_no_img)
                    .into(mImgSFZ_ALL);

        }

        String trucklicenseImg = userBaseEntity.getTruckBo().getLicenseImg();
        if (!TextUtils.isEmpty(trucklicenseImg)) {
            Glide.with(this)
                    .load(Constants.PICTURE_HOST + trucklicenseImg)
                    .error(R.mipmap.ic_no_img)
                    .into(mImgXSZ_ALL);

        }

        String headImg = userBaseEntity.getTruckBo().getHeadImg();
        if (!TextUtils.isEmpty(headImg)) {
            Glide.with(this)
                    .load(Constants.PICTURE_HOST + headImg)
                    .error(R.mipmap.ic_no_img)
                    .into(mImgCTZ);

        }
        String hopperImg = userBaseEntity.getTruckBo().getHopperImg();
        if (!TextUtils.isEmpty(hopperImg)) {
            Glide.with(this)
                    .load(Constants.PICTURE_HOST + hopperImg)
                    .error(R.mipmap.ic_no_img)
                    .into(mImgCDZ);
        }

        String insuranceImg = userBaseEntity.getTruckBo().getInsuranceImg();
        if (!TextUtils.isEmpty(insuranceImg)) {
            Glide.with(this)
                    .load(Constants.PICTURE_HOST + insuranceImg)
                    .error(R.mipmap.ic_no_img)
                    .into(mImgCLBX);
        }
        String operationImg = userBaseEntity.getTruckBo().getOperationImg();
        if (!TextUtils.isEmpty(operationImg)) {
            Glide.with(this)
                    .load(Constants.PICTURE_HOST + operationImg)
                    .error(R.mipmap.ic_no_img)
                    .into(mImgCLYYZ);
        }

        mEditDLYSZNum.setText(userBaseEntity.getTruckBo().getTransportNum());
        mEditCYZGZNum.setText(userBaseEntity.getTruckBo().getQualificationCertificate());
        String qualificationCertificateImg = userBaseEntity.getTruckBo().getQualificationCertificateImg();
        if (!TextUtils.isEmpty(qualificationCertificateImg)) {
            Glide.with(this)
                    .load(Constants.PICTURE_HOST + qualificationCertificateImg)
                    .error(R.mipmap.ic_no_img)
                    .into(mImgCYZGZ);
        }
        mTextBX_Start_Time.setText(userBaseEntity.getTruckBo().getInsuranceStart());
        mTextBX_End_Time.setText(userBaseEntity.getTruckBo().getInsuranceEnd());


        mEditLoadP.setText(userBaseEntity.getTruckBo().getLoadWeight() + "");
        mEditSCDH.setText(userBaseEntity.getTruckBo().getMobile());

        mEditCYRName.setText(userBaseEntity.getCarrier());
        mEditCYR_DLYSZNum.setText(userBaseEntity.getRoadTransportLicenseNo());
        mEditLXR.setText(userBaseEntity.getCarrierContact());
        mEditLXDH.setText(userBaseEntity.getCarrierMobile());
        mTextZCSJ.setText(userBaseEntity.getCarrierRegisterTime());

    }

    private void initView() {
        mImgBack = findViewById(R.id.imgBack);
        mTextTitle = findViewById(R.id.tv_title);
        mTextTitle.setText("实名认证");
        mTextRZZT = findViewById(R.id.tv_status);
        mEditRealName = findViewById(R.id.editName);
        mEditJSZNum = findViewById(R.id.editJSZ);
        mEditSFZNum = findViewById(R.id.editSFZ);
        mEditCPH = findViewById(R.id.editCPH);
        mImgJSZ_ZFY = findViewById(R.id.img_thz);
        mImgSFZ_Z = findViewById(R.id.img_sfz_z);
        mImgSFZ_F = findViewById(R.id.img_sfz_f);
        mImgSFZ_ALL = findViewById(R.id.img_all);

        mImgXSZ_ALL = findViewById(R.id.img_xsz);
        mImgCTZ = findViewById(R.id.img_ctz);
        mImgCDZ = findViewById(R.id.img_cdz);
        mImgCLBX = findViewById(R.id.img_clbx);
        mImgCLYYZ = findViewById(R.id.img_clyyz);
        mEditDLYSZNum = findViewById(R.id.editDlyszh);
        mEditCYZGZNum = findViewById(R.id.editCyzgzh);
        mImgCYZGZ = findViewById(R.id.img_cyzgz);
        mTextBX_Start_Time = findViewById(R.id.tv_bxrq_start);
        mTextBX_End_Time = findViewById(R.id.tv_bxrq_end);
        cxSpiner = findViewById(R.id.cxSpinner);
        ccSpiner = findViewById(R.id.ccSpinner);
        mEditLoadP = findViewById(R.id.editHdzz);
        mEditSCDH = findViewById(R.id.editScdh);

        mEditCYRName = findViewById(R.id.editSscyr);
        mEditCYR_DLYSZNum = findViewById(R.id.editcyr_Sscyr);
        mEditLXR = findViewById(R.id.editlxr);
        mEditLXDH = findViewById(R.id.editlxrdh);
        mLayoutZCSJ = findViewById(R.id.layout_zcsj);
        mTextZCSJ = findViewById(R.id.tv_zcsj);
        mBtn = findViewById(R.id.btnSave);



    }

    private static int dp2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    private void initEvent() {
        mImgJSZ_ZFY.setOnClickListener(this);
        mImgSFZ_Z.setOnClickListener(this);
        mImgSFZ_F.setOnClickListener(this);
        mImgSFZ_ALL.setOnClickListener(this);

        mImgXSZ_ALL.setOnClickListener(this);
        mImgCTZ.setOnClickListener(this);
        mImgCDZ.setOnClickListener(this);
        mImgCLBX.setOnClickListener(this);
        mImgCLYYZ.setOnClickListener(this);

        mImgCYZGZ.setOnClickListener(this);
        mLayoutZCSJ.setOnClickListener(this);
        mTextBX_Start_Time.setOnClickListener(this);
        mTextBX_End_Time.setOnClickListener(this);
        mBtn.setOnClickListener(this);

        cxSpiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CXEntity entity=cxEntities.get(position);
                userBaseEntity.getTruckBo().setTruckType(entity.getValue());

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ccSpiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CCEntity entity=ccEntities.get(position);
                userBaseEntity.getTruckBo().setLength(entity.getValue());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mImgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //返回
                finish();
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_thz:
                selectType = JSZ_ZFY;
                setImg();
                break;
            case R.id.img_sfz_z:
                selectType = SFZ_Z;
                setImg();
                break;
            case R.id.img_sfz_f:
                selectType = SFZ_F;
                setImg();
                break;
            case R.id.img_all:
                selectType = SFZ_ALL;
                setImg();
                break;
            case R.id.img_xsz:
                selectType = XSZ_ALL;
                setImg();
                break;
            case R.id.img_ctz:
                selectType = CTZ;
                setImg();
                break;
            case R.id.img_cdz:
                selectType = CDZ;
                setImg();
                break;
            case R.id.img_clbx:
                selectType = CLBX;
                setImg();
                break;
            case R.id.img_clyyz:
                selectType = CLYYZ;
                setImg();
                break;
            case R.id.img_cyzgz:
                selectType = CYZGZ;
                setImg();
                break;
            case R.id.tv_bxrq_start:
                startPicker.show();
                break;
            case R.id.tv_bxrq_end:
                endPicker.show();
                break;
            case R.id.btnSave:
                doSave();
                break;
            case R.id.layout_zcsj:
                zcsjPicker.show();
                break;
        }

    }

    private void setImg() {
        List<String> names = new ArrayList<>();
        names.add("拍照");
        names.add("相册");
        showDialog(new SelectDialog.SelectDialogListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        ImagePicker.getInstance().setSelectLimit(1);
                        Intent intent = new Intent(UserRzActivity.this, ImageGridActivity.class);
                        intent.putExtra(ImageGridActivity.EXTRAS_TAKE_PICKERS, true); // 是否是直接打开相机
                        startActivityForResult(intent, REQUEST_CODE_SELECT);
                        break;
                    case 1:
                        ImagePicker.getInstance().setSelectLimit(1);
                        Intent intent1 = new Intent(UserRzActivity.this, ImageGridActivity.class);
                        startActivityForResult(intent1, REQUEST_CODE_SELECT);
                        break;
                    default:
                        break;
                }
            }
        }, names);
    }


    private SelectDialog showDialog(SelectDialog.SelectDialogListener listener, List<String> names) {
        SelectDialog dialog = new SelectDialog(this, R.style
                .transparentFrameWindowStyle,
                listener, names);
        if (!this.isFinishing()) {
            dialog.show();
        }
        return dialog;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            if (data != null && requestCode == REQUEST_CODE_SELECT) {
                ArrayList<ImageItem> imgs = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                if (imgs != null && imgs.size() > 0) {
                    setImage(imgs.get(0));
                }
            }
        } else if (resultCode == ImagePicker.RESULT_CODE_BACK) {
            if (data != null && requestCode == REQUEST_CODE_PREVIEW) {
                ArrayList<ImageItem> imgs = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_IMAGE_ITEMS);
                if (imgs != null && imgs.size() > 0) {
                    setImage(imgs.get(0));
                }
            }
        }
    }


    private void setImage(ImageItem item) {
        ImageView imageView = null;
        if (item == null) {
            return;
        }
        if (TextUtils.isEmpty(item.path)) {
            return;
        }
        switch (selectType) {

            case JSZ_ZFY:
                imageView = mImgJSZ_ZFY;
                break;
            case SFZ_Z:
                imageView = mImgSFZ_Z;
                break;
            case SFZ_F:
                imageView = mImgSFZ_F;
                break;
            case SFZ_ALL:
                imageView = mImgSFZ_ALL;
                break;
            case XSZ_ALL:
                imageView = mImgXSZ_ALL;
                break;
            case CTZ:
                imageView = mImgCTZ;
                break;
            case CDZ:
                imageView = mImgCDZ;
                break;
            case CLBX:
                imageView = mImgCLBX;
                break;
            case CLYYZ:
                imageView = mImgCLYYZ;
                break;
            case CYZGZ:
                imageView = mImgCYZGZ;
                break;
        }
        if (imageView != null) {
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            ImagePicker.getInstance().getImageLoader().displayImage(UserRzActivity.this, item.path, imageView, 0, 0);
            upLoadImg(selectType, item.path);

        }
    }

    private void upLoadImg(final int selectType, final String path) {
        File file = new File(path);
        RequestBody fileRQ = RequestBody.create(MediaType.parse("image/*"), file);
        Log.e("fileName", file.getName());
        MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), fileRQ);

        String token = SPreferenceUtil.getInstance(UserRzActivity.this).getToken();
        if (TextUtils.isEmpty(token)) {
            Toast.makeText(UserRzActivity.this, "token为空，请重新登录", Toast.LENGTH_SHORT).show();
            return;
        }
        final LoadingDialogFragment dialogFragment = LoadingDialogFragment.getInstance();
        dialogFragment.showF(getSupportFragmentManager(), "uploadCargo");
        RequestManager.getInstance()
                .mServiceStore
                .upload(token, part)
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResultObserver(new RequestManager.onRequestCallBack() {
                    @Override
                    public void onSuccess(String msg) {
                        dialogFragment.dismissAllowingStateLoss();
                        Log.e("upload", "drive==" + msg);
                        if (!TextUtils.isEmpty(msg)) {
                            try {
                                JSONObject object = new JSONObject(msg);
                                if (object.getInt("code") == 0) {
                                    //上传成功
                                    Toast.makeText(UserRzActivity.this, "上传成功！", Toast.LENGTH_SHORT).show();
                                    String url = object.getString("data");
                                    setPicUrl(selectType, url);
                                } else {
                                    Toast.makeText(UserRzActivity.this, object.getString("message"),
                                            Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else {
                            Toast.makeText(UserRzActivity.this, "上传失败！", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(String msg) {
                        dialogFragment.dismissAllowingStateLoss();
                        Log.e("uploadCargo", "onError==" + msg);
                        Toast.makeText(UserRzActivity.this, "上传失败！" + msg, Toast.LENGTH_SHORT).show();
                    }
                }));
    }


    private void setPicUrl(int selectType, String url) {
        switch (selectType) {
            case JSZ_ZFY:
                userBaseEntity.setLicenseImg(url);
                break;
            case SFZ_Z:
                userBaseEntity.setIdCardFrontImg(url);
                break;
            case SFZ_F:
                userBaseEntity.setIdCardBackImg(url);
                break;
            case SFZ_ALL:
                userBaseEntity.setPersonAndIdCardImg(url);
                break;
            case XSZ_ALL:
                userBaseEntity.getTruckBo().setLicenseImg(url);
                break;
            case CTZ:
                userBaseEntity.getTruckBo().setHeadImg(url);
                break;
            case CDZ:
                userBaseEntity.getTruckBo().setHopperImg(url);
                break;
            case CLBX:
                userBaseEntity.getTruckBo().setInsuranceImg(url);
                break;
            case CLYYZ:
                userBaseEntity.getTruckBo().setOperationImg(url);
                break;
            case CYZGZ:
                userBaseEntity.getTruckBo().setQualificationCertificateImg(url);
                break;
        }
    }

    private void doSave() {
        String token = SPreferenceUtil.getInstance(this).getToken();
        if (TextUtils.isEmpty(token)) {
            Toast.makeText(this, "用户信息为空，请重新登录！", Toast.LENGTH_SHORT).show();
            return;
        }
        String realName = mEditRealName.getText().toString().trim();
        if(TextUtils.isEmpty(realName)){
            Toast.makeText(this, "请输入真实姓名！", Toast.LENGTH_SHORT).show();
            return;
        }
        userBaseEntity.setRealName(realName);
        String jszNum = mEditJSZNum.getText().toString();
        if(TextUtils.isEmpty(jszNum)){
            Toast.makeText(this, "请输入驾驶证号！", Toast.LENGTH_SHORT).show();
            return;
        }
        userBaseEntity.setDriverLicense(jszNum);
        String sfzNum = mEditSFZNum.getText().toString();
        if(TextUtils.isEmpty(sfzNum)){
            Toast.makeText(this, "请输入身份证号！", Toast.LENGTH_SHORT).show();
            return;
        }
        userBaseEntity.setIdCard(sfzNum);
        String cph = mEditCPH.getText().toString().trim();
        if(TextUtils.isEmpty(cph)){
            Toast.makeText(this, "请输入车牌号！", Toast.LENGTH_SHORT).show();
            return;
        }
        userBaseEntity.getTruckBo().setPlateNo(cph);
        if(TextUtils.isEmpty(userBaseEntity.getLicenseImg())){
            Toast.makeText(this, "请上传驾驶证正副页！", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(userBaseEntity.getIdCardFrontImg())){
            Toast.makeText(this, "请上传身份证正面照！", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(userBaseEntity.getIdCardBackImg())){
            Toast.makeText(this, "请上传身份证反面照！", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(userBaseEntity.getPersonAndIdCardImg())){
            Toast.makeText(this, "请上传手持身份证照！", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(userBaseEntity.getTruckBo().getLicenseImg())){
            Toast.makeText(this, "请上传行驶证正副页照！", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(userBaseEntity.getTruckBo().getOperationImg())){
            Toast.makeText(this, "请上传车辆运营证照！", Toast.LENGTH_SHORT).show();
            return;
        }

        String dlyszh=mEditDLYSZNum.getText().toString();
        if(TextUtils.isEmpty(dlyszh)){
            Toast.makeText(this, "请输入道路运输证号", Toast.LENGTH_SHORT).show();
            return;
        }
        userBaseEntity.getTruckBo().setTransportNum(dlyszh);
        String cyzgzh=mEditCYZGZNum.getText().toString();
        if(TextUtils.isEmpty(cyzgzh)){
            Toast.makeText(this, "请输入从业资格证号", Toast.LENGTH_SHORT).show();
            return;
        }
        userBaseEntity.getTruckBo().setQualificationCertificate(cyzgzh);
        String qualificationCertificateImg = userBaseEntity.getTruckBo().getQualificationCertificateImg();
        if(TextUtils.isEmpty(qualificationCertificateImg)){
            Toast.makeText(this, "请输入从业资格证照片", Toast.LENGTH_SHORT).show();
            return;

        }
        if( TextUtils.isEmpty(userBaseEntity.getTruckBo().getLength())){
            Toast.makeText(this, "请选择车长", Toast.LENGTH_SHORT).show();
            return;
        }
        if( TextUtils.isEmpty(userBaseEntity.getTruckBo().getTruckType())){
            Toast.makeText(this, "请选择车型", Toast.LENGTH_SHORT).show();
            return;
        }

        String hdzh=mEditLoadP.getText().toString().trim();
        if(TextUtils.isEmpty(hdzh)){
            Toast.makeText(this, "请输入核定载重", Toast.LENGTH_SHORT).show();
            return;

        }
        double d_hdzh=Double.parseDouble(hdzh);
        userBaseEntity.getTruckBo().setLoadWeight(d_hdzh);
        if(userBaseEntity.getTruckBo().getLoadWeight()==0.0){
            Toast.makeText(this, "请输入核定载重", Toast.LENGTH_SHORT).show();
            return;
        }
        String scdh=mEditSCDH.getText().toString().trim();
        if(TextUtils.isEmpty(scdh)){
            Toast.makeText(this, "请输入随车电话", Toast.LENGTH_SHORT).show();
            return;
        }
        userBaseEntity.getTruckBo().setMobile(scdh);

        String sycyr=mEditCYRName.getText().toString().trim();
        userBaseEntity.setCarrier(sycyr);
        String cyr_dlysxkz=mEditCYR_DLYSZNum.getText().toString().trim();
        userBaseEntity.setRoadTransportLicenseNo(cyr_dlysxkz);
        String lxr=mEditLXR.getText().toString().trim();
        userBaseEntity.setCarrierContact(lxr);
        String lxdh=mEditLXDH.getText().toString().trim();
        userBaseEntity.setCarrierMobile(lxdh);
        String zcsj=mTextZCSJ.getText().toString().trim();
        userBaseEntity.setCarrierRegisterTime(zcsj);
        mEditCYRName.setText(userBaseEntity.getCarrier());
        mEditCYR_DLYSZNum.setText(userBaseEntity.getRoadTransportLicenseNo());
        mEditLXR.setText(userBaseEntity.getCarrierContact());
        mEditLXDH.setText(userBaseEntity.getCarrierMobile());
        mTextZCSJ.setText(userBaseEntity.getCarrierRegisterTime());

        final LoadingDialogFragment loadingDialogFragment = LoadingDialogFragment.getInstance();
        loadingDialogFragment.showF(getSupportFragmentManager(), "setBaseInfo");
        Gson gson = new Gson();
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json"), gson.toJson(userBaseEntity));
        RequestManager.getInstance()
                .mServiceStore
                .setBaseInfo(token, body)
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResultObserver(new RequestManager.onRequestCallBack() {
                    @Override
                    public void onSuccess(String msg) {
                        loadingDialogFragment.dismissAllowingStateLoss();
                        Log.e("incomeSt", "incomeSt==" + msg);
                        if (!TextUtils.isEmpty(msg)) {
                            try {
                                JSONObject object = new JSONObject(msg);
                                if (object.getInt("code") == 0) {
                                    Toast.makeText(UserRzActivity.this, "设置成功！", Toast.LENGTH_SHORT).show();
                                    RxBus.getInstance().send(new EventEntity(EventEntity.USER_INFO_REFRESH));
                                    mBtn.setEnabled(false);
                                    mBtn.setClickable(false);
                                    mBtn.setBackgroundResource(R.drawable.btn_noclick_bg);

                                } else {
                                    Toast.makeText(UserRzActivity.this, object.getString("message"),
                                            Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else {
                            Toast.makeText(UserRzActivity.this, "设置失败！", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(String msg) {
                        Log.e("getLoad", "onError==" + msg);
                        loadingDialogFragment.dismissAllowingStateLoss();
                        Toast.makeText(UserRzActivity.this, "设置失败！" + msg, Toast.LENGTH_SHORT).show();
                    }
                }));

    }

    private int getCXIndexByName(String name) {
        int index = 0;
        for (int i = 0; i < cxEntities.size(); i++) {
            CXEntity entity=cxEntities.get(i);
            if (name.equals(entity.getValue())) {
                index = i;
                return index;
            }
        }
        return index;

    }

    private int getCCIndexByName(String name) {
        int index = 0;
        for (int i = 0; i < cxEntities.size(); i++) {
            CCEntity entity=ccEntities.get(i);
            if (name.equals(entity.getValue())) {
                index = i;
                return index;
            }
        }
        return index;

    }

    private String getTime(Date date) {
        SimpleDateFormat format0 = new SimpleDateFormat("yyyy-MM-dd");
        String time = format0.format(date);
        return time;
    }
}
