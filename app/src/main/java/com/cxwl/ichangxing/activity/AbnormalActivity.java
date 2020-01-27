package com.cxwl.ichangxing.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cxwl.ichangxing.R;
import com.cxwl.ichangxing.app.Constants;
import com.cxwl.ichangxing.entity.TrackException;
import com.cxwl.ichangxing.entity.TrackLoadEntity;
import com.cxwl.ichangxing.utils.GlideImageLoader;
import com.cxwl.ichangxing.utils.RequestManager;
import com.cxwl.ichangxing.utils.ResultObserver;
import com.cxwl.ichangxing.utils.SPreferenceUtil;
import com.cxwl.ichangxing.view.ContainsEmojiEditText;
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
import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class AbnormalActivity extends BaseActivity implements View.OnClickListener {
    public static final int REQUEST_CODE_SELECT = 100;
    public static final int REQUEST_CODE_PREVIEW = 101;
    public static final int IMAGE_ITEM_ADD = -1;
    public static final int RESULT_OK = 1002;
    private ImageView mImgBack;
    private TextView mTextTitle;
    private String trackId;
    private ContainsEmojiEditText mEdit;
    private ImageView img1;
    private ImageView img2;
    private ImageView img3;
    private Button mBtn;
    private int selectType;
    TrackException entity = new TrackException();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_abnormal);
        init();
    }

    @Override
    public void init() {
        trackId = getIntent().getStringExtra("trackId");
        initViews();
        initEvent();
        initImagePicker();
        try {
            initDatas();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void initDatas() throws JSONException {
        String token = SPreferenceUtil.getInstance(AbnormalActivity.this).getToken();
        if (TextUtils.isEmpty(token)) {
            Toast.makeText(AbnormalActivity.this, "token为空，请重新登录", Toast.LENGTH_SHORT).show();
            return;
        }
        JSONObject object = new JSONObject();
        object.put("trackId", trackId);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json"), object.toString());
        RequestManager.getInstance()
                .mServiceStore
                .getException(token, body)
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResultObserver(new RequestManager.onRequestCallBack() {
                    @Override
                    public void onSuccess(String msg) {
                        Log.e("getLoad", "getLoad==" + msg);
                        if (!TextUtils.isEmpty(msg)) {
                            try {
                                JSONObject object = new JSONObject(msg);
                                if (object.getInt("code") == 0) {
                                    //成功
                                    JSONArray array = object.getJSONArray("data");
                                    if (array.length() > 0) {
                                        JSONObject jsonObject = array.getJSONObject(0);
                                        Gson gson = new Gson();
                                        TrackException trackLoadEntity = gson.fromJson(jsonObject.toString(), TrackException.class);
                                        setLoadInfo(trackLoadEntity);

                                    }


                                } else {
                                    Toast.makeText(AbnormalActivity.this, object.getString("message"),
                                            Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else {
                            Toast.makeText(AbnormalActivity.this, "获取失败！", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(String msg) {
                        Log.e("getLoad", "onError==" + msg);
                        Toast.makeText(AbnormalActivity.this, "获取失败！" + msg, Toast.LENGTH_SHORT).show();

                    }
                }));
    }

    private void setLoadInfo(TrackException trackLoadEntity) {
        String reas = trackLoadEntity.getDescription();
        mEdit.setText(reas);
        String url1 = trackLoadEntity.getImg1();
        String url2 = trackLoadEntity.getImg2();
        String url3 = trackLoadEntity.getImg3();
        if (!TextUtils.isEmpty(url1)) {
            Glide.with(this)
                    .load(Constants.PICTURE_HOST + url1)
                    .into(img1);
        }
        if (!TextUtils.isEmpty(url2)) {
            Glide.with(this)
                    .load(Constants.PICTURE_HOST + url2)
                    .into(img2);
        }
        if (!TextUtils.isEmpty(url3)) {
            Glide.with(this)
                    .load(Constants.PICTURE_HOST + url3)
                    .into(img3);
        }

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

    private void initViews() {
        mImgBack = findViewById(R.id.imgBack);
        mTextTitle = findViewById(R.id.tv_title);
        mTextTitle.setText("异常磅单");
        mEdit = findViewById(R.id.editText);
        img1 = findViewById(R.id.img1);
        img2 = findViewById(R.id.img2);
        img3 = findViewById(R.id.img3);
        mBtn = findViewById(R.id.btn_sc);
        img1.setOnClickListener(this);
        img2.setOnClickListener(this);
        img3.setOnClickListener(this);
        mBtn.setOnClickListener(this);
    }

    private void initEvent() {
        mImgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img1:
                selectType = 1;
                selectImg();
                break;
            case R.id.img2:
                selectType = 2;
                selectImg();
                break;
            case R.id.img3:
                selectType = 3;
                selectImg();
                break;
            case R.id.btn_sc:
                doSC();
                break;
        }

    }

    private void selectImg() {
        List<String> names = new ArrayList<>();
        names.add("拍照");
        names.add("相册");
        showDialog(new SelectDialog.SelectDialogListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        Intent intent = new Intent(AbnormalActivity.this, ImageGridActivity.class);
                        intent.putExtra(ImageGridActivity.EXTRAS_TAKE_PICKERS, true); // 是否是直接打开相机
                        startActivityForResult(intent, REQUEST_CODE_SELECT);
                        break;
                    case 1:
                        Intent intent1 = new Intent(AbnormalActivity.this, ImageGridActivity.class);
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
                    ImageItem imageItem = imgs.get(0);
                    upLoadImg(imageItem);
                }
            }
        } else if (resultCode == ImagePicker.RESULT_CODE_BACK) {
            if (data != null && requestCode == REQUEST_CODE_PREVIEW) {
                ArrayList<ImageItem> imgs = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_IMAGE_ITEMS);
                if (imgs != null && imgs.size() > 0) {
                    ImageItem imageItem = imgs.get(0);
                    upLoadImg(imageItem);
                }
            }
        }
    }

    private void upLoadImg(ImageItem imageItem) {
        if (imageItem == null) {
            Toast.makeText(this, "请选择照片！", Toast.LENGTH_SHORT).show();
            return;
        }
        if (selectType == 1) {
            img1.setScaleType(ImageView.ScaleType.CENTER_CROP);
            ImagePicker.getInstance().getImageLoader().displayImage(AbnormalActivity.this, imageItem.path, img1, 0, 0);
        } else if (selectType == 2) {
            img2.setScaleType(ImageView.ScaleType.CENTER_CROP);
            ImagePicker.getInstance().getImageLoader().displayImage(AbnormalActivity.this, imageItem.path, img2, 0, 0);
        } else if (selectType == 3) {
            img3.setScaleType(ImageView.ScaleType.CENTER_CROP);
            ImagePicker.getInstance().getImageLoader().displayImage(AbnormalActivity.this, imageItem.path, img3, 0, 0);
        }

        File file = new File(imageItem.path);
        RequestBody fileRQ = RequestBody.create(MediaType.parse("image/*"), file);
        Log.e("fileName", file.getName());
        MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), fileRQ);

        String token = SPreferenceUtil.getInstance(AbnormalActivity.this).getToken();
        if (TextUtils.isEmpty(token)) {
            Toast.makeText(AbnormalActivity.this, "token为空，请重新登录", Toast.LENGTH_SHORT).show();
            return;
        }
        final LoadingDialogFragment dialogFragment = LoadingDialogFragment.getInstance();
        dialogFragment.showF(getSupportFragmentManager(), "uploadCargo");
        RequestManager.getInstance()
                .mServiceStore
                .uploadCargo(token, part)
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResultObserver(new RequestManager.onRequestCallBack() {
                    @Override
                    public void onSuccess(String msg) {
                        dialogFragment.dismissAllowingStateLoss();
                        Log.e("uploadCargo", "drive==" + msg);
                        if (!TextUtils.isEmpty(msg)) {
                            try {
                                JSONObject object = new JSONObject(msg);
                                if (object.getInt("code") == 0) {
                                    //上传成功
                                    Toast.makeText(AbnormalActivity.this, "上传成功！", Toast.LENGTH_SHORT).show();
                                    String url = object.getString("data");
                                    if (selectType == 1) {
                                        entity.setImg1(url);
                                    } else if (selectType == 2) {
                                        entity.setImg2(url);
                                    } else if (selectType == 3) {
                                        entity.setImg3(url);
                                    }
                                } else {
                                    Toast.makeText(AbnormalActivity.this, object.getString("message"),
                                            Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else {
                            Toast.makeText(AbnormalActivity.this, "上传失败！", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(String msg) {
                        dialogFragment.dismissAllowingStateLoss();
                        Log.e("uploadCargo", "onError==" + msg);
                        Toast.makeText(AbnormalActivity.this, "上传失败！" + msg, Toast.LENGTH_SHORT).show();

                    }
                }));
    }


    private void doSC() {
        String token = SPreferenceUtil.getInstance(AbnormalActivity.this).getToken();
        if (TextUtils.isEmpty(token)) {
            Toast.makeText(AbnormalActivity.this, "token为空，请重新登录", Toast.LENGTH_SHORT).show();
            return;
        }
        String reas = mEdit.getText().toString().trim();
        if (TextUtils.isEmpty(reas)) {
            Toast.makeText(this, "请输入异常内容", Toast.LENGTH_SHORT).show();
            return;
        }
        Gson gson = new Gson();
        entity.setTrackId(trackId);
        entity.setDescription(reas);
        final LoadingDialogFragment dialogFragment = LoadingDialogFragment.getInstance();
        dialogFragment.showF(getSupportFragmentManager(), "trackException");
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json"), gson.toJson(entity));
        RequestManager.getInstance()
                .mServiceStore
                .trackException(token, body)
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResultObserver(new RequestManager.onRequestCallBack() {
                    @Override
                    public void onSuccess(String msg) {
                        dialogFragment.dismissAllowingStateLoss();
                        Log.e("trackLoad", "drive==" + msg);
                        if (!TextUtils.isEmpty(msg)) {
                            try {
                                JSONObject object = new JSONObject(msg);
                                if (object.getInt("code") == 0) {
                                    //上传成功
                                    Toast.makeText(AbnormalActivity.this, "上传成功！", Toast.LENGTH_SHORT).show();

                                } else {
                                    Toast.makeText(AbnormalActivity.this, object.getString("message"),
                                            Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else {
                            Toast.makeText(AbnormalActivity.this, "上传失败！", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(String msg) {
                        dialogFragment.dismissAllowingStateLoss();
                        Log.e("uploadCargo", "onError==" + msg);
                        Toast.makeText(AbnormalActivity.this, "上传失败！" + msg, Toast.LENGTH_SHORT).show();

                    }
                }));
    }

}
