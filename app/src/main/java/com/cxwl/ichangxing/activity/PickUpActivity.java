package com.cxwl.ichangxing.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cxwl.ichangxing.R;
import com.cxwl.ichangxing.app.Constants;
import com.cxwl.ichangxing.entity.AssignParamsEntity;
import com.cxwl.ichangxing.entity.OrdLocationDto;
import com.cxwl.ichangxing.entity.TrackLoadEntity;
import com.cxwl.ichangxing.utils.GlideImageLoader;
import com.cxwl.ichangxing.utils.RequestManager;
import com.cxwl.ichangxing.utils.ResultObserver;
import com.cxwl.ichangxing.utils.SPreferenceUtil;
import com.cxwl.ichangxing.view.LoadingDialogFragment;
import com.cxwl.ichangxing.view.SelectDialog;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.hdgq.locationlib.LocationOpenApi;
import com.hdgq.locationlib.entity.ShippingNoteInfo;
import com.hdgq.locationlib.listener.OnResultListener;
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

public class PickUpActivity extends BaseActivity {
    public static final int REQUEST_CODE_SELECT = 100;
    public static final int REQUEST_CODE_PREVIEW = 101;
    public static final int IMAGE_ITEM_ADD = -1;
    public static final int RESULT_OK = 1002;
    private LinearLayout mLayoutMain;
    private ImageView mImgBack;
    private TextView mTextTitle;
    private LayoutInflater inflater;
    String trackId;
    String orderNo;
    private List<TrackLoadEntity> entities = new ArrayList<>();
    private List<View> views = new ArrayList<>();
    private int imgIndex;

    private OrdLocationDto ordLocationDto=new OrdLocationDto();
    public AMapLocationClient mLocationClient = null;
    public AMapLocationClientOption mLocationOption = null;
    public AMapLocationListener mLocationListener = new AMapLocationListener() {

        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if (aMapLocation != null) {
                if (aMapLocation.getErrorCode() == 0) {
                    ordLocationDto.setStartCountrySubdivisionCode(aMapLocation.getAdCode());
                    ordLocationDto.setLatitude(aMapLocation.getLatitude()+"");
                    ordLocationDto.setLongitude(aMapLocation.getLongitude()+"");
                    ordLocationDto.setType("0");
                    ordLocationDto.setShippingNoteNumber(orderNo);
                    Log.e("ordLocationDto",ordLocationDto.toString());
                } else {
                    Log.e("AmapError", "location Error, ErrCode:"
                            + aMapLocation.getErrorCode() + ", errInfo:"
                            + aMapLocation.getErrorInfo());
                }
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_pickup);
        inflater = LayoutInflater.from(this);
        init();
    }

    @Override
    public void init() {
        trackId = getIntent().getStringExtra("trackId");
        orderNo=getIntent().getStringExtra("orderNo");
        initViews();
        initEvent();
        initLocation();
        initImagePicker();
        try {
            initDatas();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void initLocation() {
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
        mLocationOption = new AMapLocationClientOption();
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        mLocationOption.setOnceLocation(true);
        mLocationOption.setNeedAddress(true);
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();

    }

    private void initDatas() throws JSONException {
        String token = SPreferenceUtil.getInstance(PickUpActivity.this).getToken();
        if (TextUtils.isEmpty(token)) {
            Toast.makeText(PickUpActivity.this, "token为空，请重新登录", Toast.LENGTH_SHORT).show();
            return;
        }
        JSONObject object = new JSONObject();
        object.put("trackId", trackId);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json"), object.toString());
        RequestManager.getInstance()
                .mServiceStore
                .getLoad(token, body)
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
                                        for (int i = 0; i < array.length(); i++) {
                                            JSONObject jsonObject = array.getJSONObject(i);
                                            Gson gson = new Gson();
                                            TrackLoadEntity trackLoadEntity = gson.fromJson(jsonObject.toString(), TrackLoadEntity.class);
                                            entities.add(trackLoadEntity);
                                            setLoadInfo(trackLoadEntity, i);
                                        }
                                    }

                                } else {
                                    Toast.makeText(PickUpActivity.this, object.getString("message"),
                                            Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else {
                            Toast.makeText(PickUpActivity.this, "获取失败！", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onError(String msg) {
                        Log.e("getLoad", "onError==" + msg);
                        Toast.makeText(PickUpActivity.this, "获取失败！" + msg, Toast.LENGTH_SHORT).show();

                    }
                }));
    }

    private void setLoadInfo(TrackLoadEntity trackLoadEntity, int pos) {
        View view = getMainView(trackLoadEntity, pos);
        mLayoutMain.addView(view);
        views.add(view);
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
        mTextTitle.setText("装货磅单");
        mLayoutMain = findViewById(R.id.layout_main);
        mLayoutMain.removeAllViews();

    }

    private View getMainView(TrackLoadEntity trackLoadEntity, int pos) {
        View view = inflater.inflate(R.layout.layout_pickup_view, null);
        TextView mTextZHDZ = view.findViewById(R.id.tv_zhdz);
        ImageView mImg = view.findViewById(R.id.img_thz);
        EditText mEdit = view.findViewById(R.id.editdw);
        Button mBtn = view.findViewById(R.id.btn_sc);
        mTextZHDZ.setText(trackLoadEntity.getLoadAddr());
        int enterWeight = trackLoadEntity.getEnterWeight();
        String url = trackLoadEntity.getBeforeLoadCommodityImg();
        mEdit.setText(enterWeight + "");
        if (!TextUtils.isEmpty(url)) {
            Glide.with(this)
                    .load(Constants.PICTURE_HOST + url)
                    .into(mImg);
        }

        mBtn.setOnClickListener(new MySaveClickListener(pos));
        mImg.setOnClickListener(new MySetImgClickListener(pos));
        return view;
    }


    class MySaveClickListener implements View.OnClickListener {
        private int pos;

        public MySaveClickListener(int pos) {
            this.pos = pos;
        }

        @Override
        public void onClick(View v) {
            doSave(pos);
        }
    }

    class MySetImgClickListener implements View.OnClickListener {
        private int pos;

        public MySetImgClickListener(int pos) {
            this.pos = pos;
        }

        @Override
        public void onClick(View v) {
            imgIndex = pos;
            setImg();
        }
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

    private void setImg() {
        List<String> names = new ArrayList<>();
        names.add("拍照");
        names.add("相册");
        showDialog(new SelectDialog.SelectDialogListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        Intent intent = new Intent(PickUpActivity.this, ImageGridActivity.class);
                        intent.putExtra(ImageGridActivity.EXTRAS_TAKE_PICKERS, true); // 是否是直接打开相机
                        startActivityForResult(intent, REQUEST_CODE_SELECT);
                        break;
                    case 1:
                        Intent intent1 = new Intent(PickUpActivity.this, ImageGridActivity.class);
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
        ImageView mImg = views.get(imgIndex).findViewById(R.id.img_thz);
        mImg.setScaleType(ImageView.ScaleType.CENTER_CROP);
        ImagePicker.getInstance().getImageLoader().displayImage(PickUpActivity.this, imageItem.path, mImg, 0, 0);
        File file = new File(imageItem.path);
        RequestBody fileRQ = RequestBody.create(MediaType.parse("image/*"), file);
        Log.e("fileName", file.getName());
        MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), fileRQ);
        String token = SPreferenceUtil.getInstance(PickUpActivity.this).getToken();
        if (TextUtils.isEmpty(token)) {
            Toast.makeText(PickUpActivity.this, "token为空，请重新登录", Toast.LENGTH_SHORT).show();
            return;
        }
        final LoadingDialogFragment dialogFragment = LoadingDialogFragment.getInstance();
        dialogFragment.showF(getSupportFragmentManager(), "uploadCargo" + imgIndex);
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
                                    Toast.makeText(PickUpActivity.this, "上传成功！", Toast.LENGTH_SHORT).show();
                                    String url = object.getString("data");
                                    entities.get(imgIndex).setBeforeLoadCommodityImg(url);
                                } else {
                                    Toast.makeText(PickUpActivity.this, object.getString("message"),
                                            Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else {
                            Toast.makeText(PickUpActivity.this, "上传失败！", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(String msg) {
                        dialogFragment.dismissAllowingStateLoss();
                        Log.e("uploadCargo", "onError==" + msg);
                        Toast.makeText(PickUpActivity.this, "上传失败！" + msg, Toast.LENGTH_SHORT).show();

                    }
                }));

    }

    private void doSave(int pos) {
        View view = views.get(pos);
        EditText mEdit = view.findViewById(R.id.editdw);
        final Button btn = view.findViewById(R.id.btn_sc);
        final String strEnterWeight = mEdit.getText().toString().trim();
        if (TextUtils.isEmpty(strEnterWeight)) {
            Toast.makeText(this, "请填写净重！", Toast.LENGTH_SHORT).show();
            return;
        }
        int intWeught = Integer.parseInt(strEnterWeight);
        if (intWeught == 0) {
            Toast.makeText(this, "净重不能为0！", Toast.LENGTH_SHORT).show();
            return;
        }
        TrackLoadEntity entity = entities.get(pos);
        if (TextUtils.isEmpty(entity.getBeforeLoadCommodityImg())) {
            Toast.makeText(this, "请上传装货磅单照片", Toast.LENGTH_SHORT).show();
            return;
        }
        final String token = SPreferenceUtil.getInstance(PickUpActivity.this).getToken();
        if (TextUtils.isEmpty(token)) {
            Toast.makeText(PickUpActivity.this, "token为空，请重新登录", Toast.LENGTH_SHORT).show();
            return;
        }

        Gson gson = new Gson();
        entity.setTrackId(entity.getTrackId());
        entity.setEnterWeight(Integer.parseInt(strEnterWeight));
        final LoadingDialogFragment dialogFragment = LoadingDialogFragment.getInstance();
        dialogFragment.showF(getSupportFragmentManager(), "trackLoad");
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json"), gson.toJson(entity));
        final TrackLoadEntity trackLoadEntity=entity;
        RequestManager.getInstance()
                .mServiceStore
                .trackLoad(token, body)
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
                                    startOpenApi(trackLoadEntity);
                                    locationUnLoad(token);
                                    btn.setBackgroundResource(R.drawable.btn_noclick_bg);
                                    btn.setEnabled(false);
                                    btn.setClickable(false);
                                    Toast.makeText(PickUpActivity.this, "装货成功！", Toast.LENGTH_SHORT).show();

                                } else {
                                    Toast.makeText(PickUpActivity.this, object.getString("message"),
                                            Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else {
                            Toast.makeText(PickUpActivity.this, "装货失败！", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onError(String msg) {
                        dialogFragment.dismissAllowingStateLoss();
                        Log.e("uploadCargo", "onError==" + msg);
                        Toast.makeText(PickUpActivity.this, "装货失败！" + msg, Toast.LENGTH_SHORT).show();

                    }
                }));
    }

    private void locationUnLoad(String token){
        Gson gson = new Gson();
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json"), gson.toJson(ordLocationDto));
        RequestManager.getInstance()
                .mServiceStore
                .locationUnload(token, body)
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResultObserver(new RequestManager.onRequestCallBack() {
                    @Override
                    public void onSuccess(String msg) {
                        Log.e("locationUnload", "locationUnload==" + msg);
                    }
                    @Override
                    public void onError(String msg) {
                        Log.e("locationUnload", "onError==" + msg);

                    }
                }));

    }

    private void startOpenApi(TrackLoadEntity entity){
        ShippingNoteInfo[] shippingNoteInfos=new ShippingNoteInfo[1];
        ShippingNoteInfo shippingNoteInfo=new ShippingNoteInfo();
        shippingNoteInfo.setShippingNoteNumber(orderNo);
        shippingNoteInfo.setSerialNumber("");
        shippingNoteInfo.setStartCountrySubdivisionCode(ordLocationDto.getStartCountrySubdivisionCode());
        shippingNoteInfo.setEndCountrySubdivisionCode("");
        shippingNoteInfos[0]=shippingNoteInfo;
        LocationOpenApi.start(PickUpActivity.this, shippingNoteInfos, new OnResultListener() {
            @Override
            public void onSuccess() {
                Log.e("LocationOpenApi","onApiStart onSuccess");
            }

            @Override
            public void onFailure(String s, String s1) {
                Log.e("LocationOpenApi","onApiStart onFailure="+s+";"+s1);
            }
        });

    }

}
