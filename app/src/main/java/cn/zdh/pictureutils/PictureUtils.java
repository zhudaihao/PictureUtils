package cn.zdh.pictureutils;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.File;

import kr.co.namee.permissiongen.PermissionGen;

/**
 * 封装从相机或者相册获取图片工具
 */

public class PictureUtils implements PopupShow.OnPhotoAlbumClick, PopupShow.OnPhotoClick {
    private Activity activity;
    private PopupShow popupShow;

    public PictureUtils(Activity activity) {
        this.activity = activity;
    }


    //图片工具
    public LQRPhotoSelectUtils mLqrPhotoSelectUtils;

    public void setPicture(final ImageView imageView) {
        //图片回调
        mLqrPhotoSelectUtils = new LQRPhotoSelectUtils(activity, new LQRPhotoSelectUtils.PhotoSelectListener() {
            @Override
            public void onFinish(File outputFile, Uri outputUri) {
                // 4、当拍照或从图库选取图片成功后回调
                Glide.with(activity)
                        .load(outputFile)
                        .transform(new GlideRoundTransform(activity))
                        .into(imageView);
            }
        }, false);//true裁剪，false不裁剪

        //显示选择视图
        popupShow = PopupShow.getInstance();
        popupShow.showPopWindowIcon(activity);

        //点击相册/相机回调
        popupShow.setOnPhotoAlbumClick(this);
        popupShow.setOnPhotoClick(this);


    }


    public void showDialog() {
        //创建对话框创建器
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(activity);
        //设置对话框显示小图标
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        //设置标题
        builder.setTitle("权限申请");
        //设置正文
        builder.setMessage("在设置-应用-虎嗅-权限 中开启相机、存储权限，才能正常使用拍照或图片选择功能");

        //添加确定按钮点击事件
        builder.setPositiveButton("去设置", new DialogInterface.OnClickListener() {
            //点击完确定后，触发这个事件
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //这里用来跳到手机设置页，方便用户开启权限
                Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData(Uri.parse("package:" + activity.getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity(intent);
            }
        });

        //添加取消按钮点击事件
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        //使用构建器创建出对话框对象
        android.support.v7.app.AlertDialog dialog = builder.create();
        dialog.show();//显示对话框
    }

    /**
     * 点击回调
     */
    @Override
    public void setOnPhotoAlbumClick() {
        PermissionGen.needPermission(activity,
                LQRPhotoSelectUtils.REQ_SELECT_PHOTO,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE}
        );
    }

    @Override
    public void setOnPhotoClick() {
        PermissionGen.with(activity)
                .addRequestCode(LQRPhotoSelectUtils.REQ_TAKE_PHOTO)
                .permissions(Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA
                ).request();
    }


}
