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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cxwl.ichangxing.R;
import com.cxwl.ichangxing.app.Constants;
import com.cxwl.ichangxing.entity.EventEntity;
import com.cxwl.ichangxing.entity.ExpressBoEntity;
import com.cxwl.ichangxing.entity.OrderBoEntity;
import com.cxwl.ichangxing.fragment.LandTrackUnload;
import com.cxwl.ichangxing.utils.GlideImageLoader;
import com.cxwl.ichangxing.utils.RequestManager;
import com.cxwl.ichangxing.utils.ResultObserver;
import com.cxwl.ichangxing.utils.RxBus;
import com.cxwl.ichangxing.utils.SPreferenceUtil;
import com.cxwl.ichangxing.view.ExpressSelectFragment;
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

public class ReceiptActivity extends BaseActivity{
    public static final int REQUEST_CODE_SELECT = 100;
    public static final int REQUEST_CODE_PREVIEW = 101;
    public static final int IMAGE_ITEM_ADD = -1;
    public static final int RESULT_OK=1002;
    private ImageView mImgBack;
    private TextView mTextTitle;
    private TextView mTexJSDZ;
    private RelativeLayout mLayoutKDGS;
    private EditText mEditKDGS;
    private EditText mEditKDDH;
    private ImageView mImg;
    private Button mBtn;
    private String addr;
    private String orderNo;
    private String expressName;
    private String expressNo;
    private String url;
    private ImageView img_arr;
    ExpressBoEntity entity=new ExpressBoEntity();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_receipt);
        init();
    }

    @Override
    public void init() {
        orderNo=getIntent().getStringExtra("orderNo");
        addr=getIntent().getStringExtra("addr");
        expressName=getIntent().getStringExtra("expressName");
        expressNo=getIntent().getStringExtra("expressNo");
        url=getIntent().getStringExtra("url");
        entity.setOrderNo(orderNo);
        initView();
        initEvent();
        initImagePicker();
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
    private void initView() {
        mImgBack=findViewById(R.id.imgBack);
        mTextTitle=findViewById(R.id.tv_title);
        mTextTitle.setText("回单寄送");
        mTexJSDZ=findViewById(R.id.tv_jsdz);
        mLayoutKDGS=findViewById(R.id.layout_kdgs);
        mEditKDGS=findViewById(R.id.editkdgs);
        mEditKDDH=findViewById(R.id.editkddh);
        mImg=findViewById(R.id.img_thz);
        mBtn=findViewById(R.id.btn_sc);
        mTexJSDZ.setText(addr);
        mEditKDGS.setText(expressName);
        mEditKDDH.setText(expressNo);
        img_arr=findViewById(R.id.img_arr);
        if(!TextUtils.isEmpty(url)){
            Glide.with(ReceiptActivity.this)
                    .load(Constants.PICTURE_HOST+url)
                    .into(mImg);
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
        mLayoutKDGS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSelectView();
            }
        });
        img_arr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSelectView();
            }
        });
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
    }

    private void showSelectView() {
        final ExpressSelectFragment selectFragment=ExpressSelectFragment.getInstance();
        selectFragment.showF(getSupportFragmentManager(),"showSelectView");
        selectFragment.setOnDialogClickListener(new ExpressSelectFragment.OnDialogClickListener() {
            @Override
            public void onSelect(String name) {
                mEditKDGS.setText(name);

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
                        Intent intent = new Intent(ReceiptActivity.this, ImageGridActivity.class);
                        intent.putExtra(ImageGridActivity.EXTRAS_TAKE_PICKERS, true); // 是否是直接打开相机
                        startActivityForResult(intent, REQUEST_CODE_SELECT);
                        break;
                    case 1:
                        Intent intent1 = new Intent(ReceiptActivity.this, ImageGridActivity.class);
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
        ImagePicker.getInstance().getImageLoader().displayImage(ReceiptActivity.this, imageItem.path, mImg, 0, 0);
        File file=new File(imageItem.path);
        RequestBody fileRQ = RequestBody.create(MediaType.parse("image/*"), file);
        Log.e("fileName",file.getName());
        MultipartBody.Part part =MultipartBody.Part.createFormData("file", file.getName(), fileRQ);

        String token= SPreferenceUtil.getInstance(ReceiptActivity.this).getToken();
        if(TextUtils.isEmpty(token)){
            Toast.makeText(ReceiptActivity.this, "token为空，请重新登录", Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(ReceiptActivity.this, "上传成功！", Toast.LENGTH_SHORT).show();
                                    String url=object.getString("data");
                                    entity.setExpressImg(url);
                                }else {
                                    Toast.makeText(ReceiptActivity.this, object.getString("message"),
                                            Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }else {
                            Toast.makeText(ReceiptActivity.this, "上传失败！", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(String msg) {
                        dialogFragment.dismissAllowingStateLoss();
                        Log.e("uploadCargo", "onError==" + msg);
                        Toast.makeText(ReceiptActivity.this, "上传失败！"+msg, Toast.LENGTH_SHORT).show();
                    }
                }));

    }

    private void doSave() {
        final String kdgs=mEditKDGS.getText().toString().trim();
        final String kddh=mEditKDDH.getText().toString().trim();
        entity.setExpressName(kdgs);
        entity.setExpressNo(kddh);
        if(!TextUtils.isEmpty(entity.getExpressImg())){
            Toast.makeText(this, "请上传照片", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!TextUtils.isEmpty(entity.getExpressName())){
            Toast.makeText(this, "请填写快递公司名称", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!TextUtils.isEmpty(entity.getExpressNo())){
            Toast.makeText(this, "请填写快递单号", Toast.LENGTH_SHORT).show();
            return;
        }
        String token= SPreferenceUtil.getInstance(ReceiptActivity.this).getToken();
        if(TextUtils.isEmpty(token)){
            Toast.makeText(ReceiptActivity.this, "token为空，请重新登录", Toast.LENGTH_SHORT).show();
            return;
        }
        Gson gson=new Gson();
        final LoadingDialogFragment dialogFragment=LoadingDialogFragment.getInstance();
        dialogFragment.showF(getSupportFragmentManager(),"receiptView");
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json"), gson.toJson(entity));
        RequestManager.getInstance()
                .mServiceStore
                .receipt(token,body)
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResultObserver(new RequestManager.onRequestCallBack() {
                    @Override
                    public void onSuccess(String msg) {
                        dialogFragment.dismissAllowingStateLoss();
                        Log.e("receipt", "drive==" + msg);
                        if(!TextUtils.isEmpty(msg)){
                            try {
                                JSONObject object=new JSONObject(msg);
                                if(object.getInt("code")==0){
                                    //上传成功
                                    RxBus.getInstance().send(new EventEntity(EventEntity.RECORD_REFRESH));
                                    Toast.makeText(ReceiptActivity.this, "成功！", Toast.LENGTH_SHORT).show();
                                    finish();
                                    overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                                }else {
                                    Toast.makeText(ReceiptActivity.this, object.getString("message"),
                                            Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }else {
                            Toast.makeText(ReceiptActivity.this, "失败！", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(String msg) {
                        dialogFragment.dismissAllowingStateLoss();
                        Log.e("uploadCargo", "onError==" + msg);
                        Toast.makeText(ReceiptActivity.this, "失败！"+msg, Toast.LENGTH_SHORT).show();

                    }
                }));


    }
}
