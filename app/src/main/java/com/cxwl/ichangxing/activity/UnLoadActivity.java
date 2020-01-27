package com.cxwl.ichangxing.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cxwl.ichangxing.R;
import com.cxwl.ichangxing.app.Constants;
import com.cxwl.ichangxing.entity.TrackLoadEntity;
import com.cxwl.ichangxing.fragment.LandTrackUnload;
import com.cxwl.ichangxing.utils.GlideImageLoader;
import com.cxwl.ichangxing.utils.RequestManager;
import com.cxwl.ichangxing.utils.ResultObserver;
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
import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class UnLoadActivity extends BaseActivity {
    public static final int REQUEST_CODE_SELECT = 100;
    public static final int REQUEST_CODE_PREVIEW = 101;
    public static final int IMAGE_ITEM_ADD = -1;
    public static final int RESULT_OK=1002;
    private ImageView mImgBack;
    private TextView mTextTitle;
    private TextView mTextZHDZ;
    private ImageView mImg;
    private EditText mEdit;
    private Button mBtn;
    private LandTrackUnload entity=new LandTrackUnload();
    private String trackId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_unload);
        init();
    }

    @Override
    public void init() {
        trackId=getIntent().getStringExtra("trackId");

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
        String token= SPreferenceUtil.getInstance(UnLoadActivity.this).getToken();
        if(TextUtils.isEmpty(token)){
            Toast.makeText(UnLoadActivity.this, "token为空，请重新登录", Toast.LENGTH_SHORT).show();
            return;
        }
        JSONObject object=new JSONObject();
        object.put("trackId",trackId);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json"), object.toString());
        RequestManager.getInstance()
                .mServiceStore
                .getUnload(token,body)
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResultObserver(new RequestManager.onRequestCallBack() {
                    @Override
                    public void onSuccess(String msg) {
                        Log.e("getLoad", "getLoad==" + msg);
                        if(!TextUtils.isEmpty(msg)){
                            try {
                                JSONObject object=new JSONObject(msg);
                                if(object.getInt("code")==0){
                                    //成功
                                    JSONArray array=object.getJSONArray("data");
                                    if(array.length()>0){
                                        JSONObject jsonObject=array.getJSONObject(0);
                                        Gson gson=new Gson();
                                        LandTrackUnload trackLoadEntity=gson.fromJson(jsonObject.toString(),LandTrackUnload.class);
                                        setLoadInfo(trackLoadEntity);

                                    }


                                }else {
                                    Toast.makeText(UnLoadActivity.this, object.getString("message"),
                                            Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }else {
                            Toast.makeText(UnLoadActivity.this, "获取失败！", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(String msg) {
                        Log.e("getLoad", "onError==" + msg);
                        Toast.makeText(UnLoadActivity.this, "获取失败！"+msg, Toast.LENGTH_SHORT).show();

                    }
                }));
    }

    private void setLoadInfo(LandTrackUnload trackLoadEntity) {
        entity=trackLoadEntity;
        int enterWeight=trackLoadEntity.getOutWeight();
        String url=trackLoadEntity.getAfterTruckBodyImg();
        mEdit.setText(enterWeight+"");
        mTextZHDZ.setText(entity.getUnloadAddr());
        if(!TextUtils.isEmpty(url)){
            Glide.with(this)
                    .load(Constants.PICTURE_HOST+url)
                    .into(mImg);
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
        mImgBack=findViewById(R.id.imgBack);
        mTextTitle=findViewById(R.id.tv_title);
        mTextTitle.setText("卸货磅单");
        mTextZHDZ=findViewById(R.id.tv_zhdz);
        mImg=findViewById(R.id.img_thz);
        mEdit=findViewById(R.id.editdw);
        mBtn=findViewById(R.id.btn_sc);

    }

    private void initEvent() {
        mImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setImg();
            }
        });
        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSave();
            }
        });
        mImgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
            }
        });
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
                        Intent intent = new Intent(UnLoadActivity.this, ImageGridActivity.class);
                        intent.putExtra(ImageGridActivity.EXTRAS_TAKE_PICKERS, true); // 是否是直接打开相机
                        startActivityForResult(intent, REQUEST_CODE_SELECT);
                        break;
                    case 1:
                        Intent intent1 = new Intent(UnLoadActivity.this, ImageGridActivity.class);
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
                    ImageItem imageItem=imgs.get(0);
                    upLoadImg(imageItem);
                }
            }
        } else if (resultCode == ImagePicker.RESULT_CODE_BACK) {
            if (data != null && requestCode == REQUEST_CODE_PREVIEW) {
                ArrayList<ImageItem> imgs = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_IMAGE_ITEMS);
                if (imgs != null && imgs.size() > 0) {
                    ImageItem imageItem=imgs.get(0);
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
        mImg.setScaleType(ImageView.ScaleType.CENTER_CROP);
        ImagePicker.getInstance().getImageLoader().displayImage(UnLoadActivity.this, imageItem.path, mImg, 0, 0);
        File file=new File(imageItem.path);
        RequestBody fileRQ = RequestBody.create(MediaType.parse("image/*"), file);
        Log.e("fileName",file.getName());
        MultipartBody.Part part =MultipartBody.Part.createFormData("file", file.getName(), fileRQ);

        String token= SPreferenceUtil.getInstance(UnLoadActivity.this).getToken();
        if(TextUtils.isEmpty(token)){
            Toast.makeText(UnLoadActivity.this, "token为空，请重新登录", Toast.LENGTH_SHORT).show();
            return;
        }
        final LoadingDialogFragment dialogFragment=LoadingDialogFragment.getInstance();
        dialogFragment.showF(getSupportFragmentManager(),"uploadCargo");
        RequestManager.getInstance()
                .mServiceStore
                .uploadCargo(token,part)
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResultObserver(new RequestManager.onRequestCallBack() {
                    @Override
                    public void onSuccess(String msg) {
                        dialogFragment.dismissAllowingStateLoss();
                        Log.e("uploadCargo", "drive==" + msg);
                        if(!TextUtils.isEmpty(msg)){
                            try {
                                JSONObject object=new JSONObject(msg);
                                if(object.getInt("code")==0){
                                    //上传成功
                                    Toast.makeText(UnLoadActivity.this, "上传成功！", Toast.LENGTH_SHORT).show();
                                    String url=object.getString("data");
                                    entity.setAfterTruckBodyImg(url);
                                }else {
                                    Toast.makeText(UnLoadActivity.this, object.getString("message"),
                                            Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }else {
                            Toast.makeText(UnLoadActivity.this, "上传失败！", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(String msg) {
                        dialogFragment.dismissAllowingStateLoss();
                        Log.e("uploadCargo", "onError==" + msg);
                        Toast.makeText(UnLoadActivity.this, "上传失败！"+msg, Toast.LENGTH_SHORT).show();
                    }
                }));

    }

    private void doSave() {
        final String strEnterWeight=mEdit.getText().toString().trim();
        if(TextUtils.isEmpty(strEnterWeight)){
            Toast.makeText(this, "请填写净重！", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(entity.getAfterTruckBodyImg())){
            Toast.makeText(this, "请上传卸货货磅单照片", Toast.LENGTH_SHORT).show();
            return;
        }
        String token= SPreferenceUtil.getInstance(UnLoadActivity.this).getToken();
        if(TextUtils.isEmpty(token)){
            Toast.makeText(UnLoadActivity.this, "token为空，请重新登录", Toast.LENGTH_SHORT).show();
            return;
        }
        Gson gson=new Gson();
        entity.setTrackId(entity.getTrackId());
        entity.setOutWeight(Integer.parseInt(strEnterWeight));
        final LoadingDialogFragment dialogFragment=LoadingDialogFragment.getInstance();
        dialogFragment.showF(getSupportFragmentManager(),"trackUnLoad");
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json"), gson.toJson(entity));
        RequestManager.getInstance()
                .mServiceStore
                .trackUnLoad(token,body)
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResultObserver(new RequestManager.onRequestCallBack() {
                    @Override
                    public void onSuccess(String msg) {
                        dialogFragment.dismissAllowingStateLoss();
                        Log.e("trackLoad", "drive==" + msg);
                        if(!TextUtils.isEmpty(msg)){
                            try {
                                JSONObject object=new JSONObject(msg);
                                if(object.getInt("code")==0){
                                    //上传成功
                                    Toast.makeText(UnLoadActivity.this, "卸货成功！", Toast.LENGTH_SHORT).show();

                                }else {
                                    Toast.makeText(UnLoadActivity.this, object.getString("message"),
                                            Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }else {
                            Toast.makeText(UnLoadActivity.this, "卸货失败！", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(String msg) {
                        dialogFragment.dismissAllowingStateLoss();
                        Log.e("uploadCargo", "onError==" + msg);
                        Toast.makeText(UnLoadActivity.this, "卸货失败！"+msg, Toast.LENGTH_SHORT).show();

                    }
                }));


    }
}
