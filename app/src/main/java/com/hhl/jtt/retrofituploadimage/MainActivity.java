package com.hhl.jtt.retrofituploadimage;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hhl.jtt.bean.BaseBean;
import com.hhl.jtt.retrofit.HttpConst;
import com.hhl.jtt.retrofit.HttpStaticApi;
import com.hhl.jtt.retrofit.RetrofitCallBack;
import com.hhl.jtt.retrofit.RetrofitHttpUpLoad;
import com.hhl.jtt.util.LogUtil;
import com.hhl.jtt.util.StringUtil;
import com.hhl.jtt.util.ToastUtil;

import java.io.File;
import java.util.Map;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.RequestBody;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements RetrofitCallBack{

    @BindView(R.id.upLoad_image1)
    ImageView upLoadImage1;
    @BindView(R.id.upLoad_image2)
    ImageView upLoadImage2;
    @BindView(R.id.image_desc)
    EditText imageDesc;
    @BindView(R.id.upLoad)
    TextView upLoad;

    private final int IMAGE_OPEN = 1; //打开图片标记
    private final int CAPTURE_OPEN = 3; //打开相机
    private final int CAIJIAN = 4; //裁剪
    private String[] pathImage = {"", ""};
    private int imageNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.upLoad_image1, R.id.upLoad_image2, R.id.upLoad})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.upLoad_image1:
                imageNumber = 1;
                showChoise();
                break;
            case R.id.upLoad_image2:
                imageNumber = 2;
                showChoise();
                break;
            case R.id.upLoad:
                upLoadImage();
                break;
        }
    }

    private String temppic = "";
    private String[] cities = {"相机拍照", "本地图片"};
    private void showChoise() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("选择图片来源");
        builder.setItems(cities, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(cities[which].equals("相机拍照")){
                    File file = null;
                    temppic = UUID.randomUUID()+".png";

                    try {
                        file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                                + File.separator + temppic);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Uri uri = Uri.fromFile(file);
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                    startActivityForResult(intent, CAPTURE_OPEN);
                }else if(cities[which].equals("本地图片")){
                    openFile(IMAGE_OPEN);
                }
            }
        });
        builder.show();
    }

    private void openFile(int openType){
        Intent intent = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, openType);
    }

    private void setImageView(String url,ImageView iv){
        Glide.with(this).load(url).error(R.drawable.addimage)
                .placeholder(R.drawable.addimage).into(iv);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //获取图片路径 响应startActivityForResult
        super.onActivityResult(requestCode, resultCode, data);
        //打开图片
        if(resultCode == RESULT_OK && requestCode == IMAGE_OPEN) {
            Uri uri = data.getData();
            if (!TextUtils.isEmpty(uri.getAuthority())) {
                //查询选择图片
                Cursor cursor = getContentResolver().query(uri,
                        new String[] { MediaStore.Images.Media.DATA }, null, null, null);
                //返回 没找到选择图片
                if (cursor == null) {
                    ToastUtil.makeShortText(this,"没找到选择图片");
                    return;
                }
                //光标移动至开头 获取图片路径
                cursor.moveToFirst();
                if(imageNumber == 1) {
                    pathImage[0] = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    setImageView(pathImage[0], upLoadImage1);
                }else if(imageNumber==2){
                    pathImage[1] = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    setImageView(pathImage[1], upLoadImage2);
                }
            }else{
                ToastUtil.makeLongText(this,"未找到文件");
            }
        } else if(resultCode == RESULT_OK && requestCode == CAPTURE_OPEN){
            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + temppic);
            Intent intent = new Intent("com.android.camera.action.CROP");
            intent.setDataAndType(Uri.fromFile(file), "image/*");
            intent.putExtra("crop", "true");
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("outputX", 600);
            intent.putExtra("outputY", 600);
            intent.putExtra("output", Uri.fromFile(file));
            intent.putExtra("return-data", true);
            this.startActivityForResult(intent, CAIJIAN);
        } else if (requestCode == CAIJIAN){
            if(imageNumber == 1) {
                pathImage[0] = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + temppic;
                setImageView(pathImage[0], upLoadImage1);
            }else if(imageNumber == 2){
                pathImage[1] = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + temppic;
                setImageView(pathImage[1], upLoadImage2);
            }
        }
    }

    protected ProgressDialog waitDialog;
    private void showWaitDialog() {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (waitDialog == null || !waitDialog.isShowing()) {
                    waitDialog = new ProgressDialog(MainActivity.this);
                    waitDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    waitDialog.setCanceledOnTouchOutside(false);
                    ImageView view = new ImageView(MainActivity.this);
                    view.setLayoutParams(new ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT));
                    Animation loadAnimation = AnimationUtils.loadAnimation(
                            MainActivity.this, R.anim.rotate);
                    view.startAnimation(loadAnimation);
                    loadAnimation.start();
                    view.setImageResource(R.drawable.loading);
                    // waitDialog.setCancelable(false);
                    waitDialog.show();
                    waitDialog.setContentView(view);
                    LogUtil.i("waitDialong.......");
                }

            }
        });

    }

    public void dismissWaitDialog() {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (waitDialog != null && waitDialog.isShowing()) {
                    waitDialog.dismiss();
                    waitDialog = null;
                }
            }
        });

    }

    private void upLoadImage(){
        showWaitDialog();
        RetrofitHttpUpLoad retrofitHttpUpLoad = RetrofitHttpUpLoad.getInstance();
        retrofitHttpUpLoad.clear();
        if (!StringUtil.isEmpty(pathImage[0])){
            retrofitHttpUpLoad = retrofitHttpUpLoad.addParameter("pic1",new File(pathImage[0]));
        }
        if (!StringUtil.isEmpty(pathImage[1])){
            retrofitHttpUpLoad = retrofitHttpUpLoad.addParameter("pic2", new File(pathImage[1]));
        }

        Map<String, RequestBody> params = retrofitHttpUpLoad
                .addParameter("status", "4")
                .addParameter("pickupId", "105329")
                .addParameter("cause", "质量有异议")
                .addParameter("connectname", "洪海亮")
                .addParameter("connectphone", "12345678901")
                .addParameter("details", imageDesc.getText().toString())
                .bulider();
        retrofitHttpUpLoad.addToEnqueue(retrofitHttpUpLoad.mHttpService.upLoadAgree(params),
                this, HttpStaticApi.HTTP_UPLOADIMAGE);
    }

    @Override
    public void onResponse(Response response, int method) {
        dismissWaitDialog();
        switch (method) {
            case HttpStaticApi.HTTP_UPLOADIMAGE:
                BaseBean baseBean1 = (BaseBean) response.body();
                BaseBean.ResDataBean baseBeanRDB1 = baseBean1.getResData();
                if (HttpConst.STATUS_SUC.equals(baseBeanRDB1.getStatus())) {
                    ToastUtil.makeShortText(this, baseBeanRDB1.getMessage());
                } else {
                    ToastUtil.makeShortText(this, baseBeanRDB1.getMessage());
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onFailure(Response response, int method) {
        dismissWaitDialog();
    }
}
