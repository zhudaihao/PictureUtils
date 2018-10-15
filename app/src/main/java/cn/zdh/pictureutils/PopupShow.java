package cn.zdh.pictureutils;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

/**
 * 底部弹出 PopupWindow
 */

public class PopupShow {
    private PopupShow() {
    }

    private static final PopupShow popupShow = new PopupShow();

    public static final PopupShow getInstance() {

        return popupShow;
    }

    /**
     * 底部对话框
     */

    private RadioButton rb_photo_album, rb_photo, rb_cancel;
    private PopupWindow popWindow;

    public void showPopWindowIcon(final Activity activity) {
        View parent = ((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0);
        View view = LayoutInflater.from(activity).inflate(R.layout.pop_button_icon, null);

        popWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        //设置动画
        popWindow.setAnimationStyle(R.style.AnimBottom);
        //设置是否获取焦点
        popWindow.setFocusable(true);
        //设置点击外部是否关闭对话框
        popWindow.setOutsideTouchable(false);
        //设置背景颜色 透明#00000000  半透明30000000
        ColorDrawable dw = new ColorDrawable(0x00000000);
        popWindow.setBackgroundDrawable(dw);

        //获取控件
        rb_photo_album = (RadioButton) view.findViewById(R.id.rb_photo_album);
        rb_photo = (RadioButton) view.findViewById(R.id.rb_photo);
        rb_cancel = (RadioButton) view.findViewById(R.id.rb_cancel);
        RelativeLayout rl_tag = (RelativeLayout) view.findViewById(R.id.rl_tag);


        //点击监听
        rl_tag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popWindow.dismiss();
            }
        });

        rb_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissPopWindow();
            }
        });


        //点击相册
        rb_photo_album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onPhotoAlbumClick != null) {
                    onPhotoAlbumClick.setOnPhotoAlbumClick();
                }
                popWindow.dismiss();
            }
        });
        //点击相机
        rb_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onPhotoClick != null) {
                    onPhotoClick.setOnPhotoClick();
                }
                popWindow.dismiss();
            }
        });

        popWindow.showAtLocation(parent, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }


    /**
     * 点击相册 和 相机 回调
     */
    //相机
    public interface OnPhotoClick {
        void setOnPhotoClick();
    }

    private static OnPhotoClick onPhotoClick;

    public static void setOnPhotoClick(OnPhotoClick onPhotoClick) {
        PopupShow.onPhotoClick = onPhotoClick;
    }

    //相册
    public interface OnPhotoAlbumClick {
        void setOnPhotoAlbumClick();
    }

    private static OnPhotoAlbumClick onPhotoAlbumClick;

    public static void setOnPhotoAlbumClick(OnPhotoAlbumClick onPhotoAlbumClick) {
        PopupShow.onPhotoAlbumClick = onPhotoAlbumClick;
    }

    /**
     * 关闭对话框
     */
    public void dismissPopWindow() {
        if (popWindow != null) {
            popWindow.dismiss();
            popWindow = null;
        }
    }
}
