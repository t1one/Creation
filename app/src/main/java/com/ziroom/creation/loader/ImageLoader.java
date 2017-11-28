package com.ziroom.creation.loader;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.text.TextUtils;
import android.widget.ImageView;

import com.facebook.drawee.controller.AbstractDraweeController;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.view.DraweeView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.ziroom.creation.listener.OnBitmapGetListener;
import com.ziroom.creation.utils.FrescoUtils;
import com.ziroom.creation.utils.VerifyUtils;

import java.io.File;

/**
 * 图片加载封装
 * Created by lmnrenbc on 2017/11/26.
 */

public class ImageLoader {
    private static ImageLoader instance;

    private ImageLoader() {

    }

    public static ImageLoader getInstance() {
        if (instance == null) {
            synchronized (ImageLoader.class) {
                instance = new ImageLoader();
            }
        }
        return instance;
    }

    /**
     * 初始化图片加载框架
     */
    public void initImageLoader() {
        FrescoUtils.getInstance().initFresco();
    }

    /**
     * 自动加载网络或本地图片
     *
     * @param iv
     * @param url
     */
    public void loadImageLocalOrNet(ImageView iv, String url) {
        if (TextUtils.isEmpty(url) || iv == null) {
            return;
        }
        if (VerifyUtils.getInstance().fromNet(url)) {
            loadImageFromNet(iv, url);
        } else {
            loadImageFromLocal(iv, "file://" + url);
        }
    }

    /**
     * 从网络加载图片
     *
     * @param iv
     * @param url
     */
    public void loadImageFromNet(ImageView iv, String url) {
        AbstractDraweeController draweeController = FrescoUtils.getInstance().frescoController(url);
        loadImage((SimpleDraweeView) iv, draweeController, -1, null);
    }

    public void loadImageFromNet(ImageView iv, String url, @DrawableRes int defaultDrawable) {
        AbstractDraweeController draweeController = FrescoUtils.getInstance().frescoController(url);
        loadImage((SimpleDraweeView) iv, draweeController, defaultDrawable, null);
    }

    public void loadImageFromNet(ImageView iv, String url, @DrawableRes int defaultDrawable, ScalingUtils.ScaleType type) {
        AbstractDraweeController draweeController = FrescoUtils.getInstance().frescoController(url);
        loadImage((SimpleDraweeView) iv, draweeController, defaultDrawable, type);
    }

    public void loadImageFromNet(ImageView iv, String url, int width, int height) {
        AbstractDraweeController draweeController = FrescoUtils.getInstance().frescoResizeController(url, width, height);
        loadImage((SimpleDraweeView) iv, draweeController, -1, null);
    }

    public void loadImageFromNet(ImageView iv, Uri uri, int width, int height) {
        AbstractDraweeController draweeController = FrescoUtils.getInstance().frescoResizeController(uri, width, height);
        loadImage((SimpleDraweeView) iv, draweeController, -1, null);
    }

    public void loadImageFromNet(ImageView iv, Uri uri) {
        AbstractDraweeController draweeController = FrescoUtils.getInstance().frescoResizeController(uri);
        loadImage((SimpleDraweeView) iv, draweeController, -1, null);
    }

    public void loadImageFromNet(DraweeView iv, Uri uri) {
        AbstractDraweeController draweeController = FrescoUtils.getInstance().frescoResizeController(uri);
        loadImage((DraweeView) iv, draweeController);
    }

    public void loadImageLocalOrNet(Context context, String url, OnBitmapGetListener listener) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        if (VerifyUtils.getInstance().fromNet(url)) {
            FrescoUtils.getInstance().getBitmapFromFresco(context, url, listener);
        } else {
            FrescoUtils.getInstance().getBitmapFromFresco(context, url, true, listener);
        }
    }

    public void loadImageFromNet(Context context, String url, OnBitmapGetListener listener) {
        FrescoUtils.getInstance().getBitmapFromFresco(context, url, listener);
    }

    public void loadImageFromLocal(Context context, String url, OnBitmapGetListener listener) {
        FrescoUtils.getInstance().getBitmapFromFresco(context, url, true, listener);
    }

    /**
     * 从本地路径加载图片
     *
     * @param iv
     * @param path
     */
    public void loadImageFromLocal(ImageView iv, String path) {
        AbstractDraweeController draweeController = FrescoUtils.getInstance().frescoController(path);
        loadImage((SimpleDraweeView) iv, draweeController, -1, null);
    }

    public void loadImageFromLocal(ImageView iv, String path, int width, int height) {
        AbstractDraweeController draweeController = FrescoUtils.getInstance().frescoResizeController(path, width, height);
        loadImage((SimpleDraweeView) iv, draweeController, -1, null);
    }

    public void loadImageFromLocal(ImageView iv, String path, @DrawableRes int defaultDrawable) {
        AbstractDraweeController draweeController = FrescoUtils.getInstance().frescoController(path);
        loadImage((SimpleDraweeView) iv, draweeController, defaultDrawable, null);
    }

    public void loadImageFromLocal(ImageView iv, String path, @DrawableRes int defaultDrawable, ScalingUtils.ScaleType type) {
        AbstractDraweeController draweeController = FrescoUtils.getInstance().frescoController(path);
        loadImage((SimpleDraweeView) iv, draweeController, defaultDrawable, type);
    }

    /**
     * 加载file图片
     *
     * @param iv
     * @param file
     */
    public void loadImageFromFile(ImageView iv, File file) {
        AbstractDraweeController draweeController = FrescoUtils.getInstance().frescoFromFileController(file);
        loadImage((SimpleDraweeView) iv, draweeController, -1, null);
    }

    public void loadImageFromFile(ImageView iv, File file, @DrawableRes int defaultDrawable) {
        AbstractDraweeController draweeController = FrescoUtils.getInstance().frescoFromFileController(file);
        loadImage((SimpleDraweeView) iv, draweeController, defaultDrawable, null);
    }

    public void loadImageFromFile(ImageView iv, File file, @DrawableRes int defaultDrawable, ScalingUtils.ScaleType type) {
        AbstractDraweeController draweeController = FrescoUtils.getInstance().frescoFromFileController(file);
        loadImage((SimpleDraweeView) iv, draweeController, defaultDrawable, type);
    }

    /**
     * 加载res资源图片
     *
     * @param iv
     * @param resourceId
     */
    public void loadImageFromRes(ImageView iv, @DrawableRes int resourceId) {
        AbstractDraweeController draweeController = FrescoUtils.getInstance().frescoFromResourceController(resourceId);
        loadImage((SimpleDraweeView) iv, draweeController, -1, null);
    }

    public void loadImageFromRes(ImageView iv, @DrawableRes int resourceId, @DrawableRes int defaultDrawable) {
        AbstractDraweeController draweeController = FrescoUtils.getInstance().frescoFromResourceController(resourceId);
        loadImage((SimpleDraweeView) iv, draweeController, defaultDrawable, null);
    }

    public void loadImageFromRes(ImageView iv, @DrawableRes int resourceId, @DrawableRes int defaultDrawable, ScalingUtils.ScaleType type) {
        AbstractDraweeController draweeController = FrescoUtils.getInstance().frescoFromResourceController(resourceId);
        loadImage((SimpleDraweeView) iv, draweeController, defaultDrawable, type);
    }


    public void loadImage(DraweeView iv, AbstractDraweeController controller) {
        iv.setController(controller);
    }

    public void loadImage(SimpleDraweeView iv, AbstractDraweeController controller, @DrawableRes int defaultDrawable, ScalingUtils.ScaleType type) {
        loadImage(iv, controller, defaultDrawable, defaultDrawable, type);
    }

    /**
     * 图片加载
     *
     * @param iv
     * @param controller
     * @param holderDrawable
     * @param failureDrawable
     * @param type
     */
    public void loadImage(SimpleDraweeView iv, AbstractDraweeController controller, @DrawableRes int holderDrawable, @DrawableRes int failureDrawable, ScalingUtils.ScaleType type) {
        if (holderDrawable != -1) {
            if (type != null) {
                FrescoUtils.getInstance().frescoHierarchyController(iv, type, holderDrawable, failureDrawable);
            } else {
                FrescoUtils.getInstance().frescoHierarchyController(iv, ScalingUtils.ScaleType.FIT_XY, holderDrawable, failureDrawable);
            }
        }
        iv.setController(controller);
    }

    /**
     * 创造图像组件
     *
     * @param context
     * @return
     */
    public ImageView createImageView(Context context) {
        SimpleDraweeView simpleDraweeView = new SimpleDraweeView(context);
        return simpleDraweeView;
    }
}
