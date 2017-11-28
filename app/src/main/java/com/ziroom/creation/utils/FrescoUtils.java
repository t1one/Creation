package com.ziroom.creation.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.TextUtils;

import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.common.executors.UiThreadImmediateExecutorService;
import com.facebook.common.logging.FLog;
import com.facebook.common.memory.MemoryTrimType;
import com.facebook.common.memory.MemoryTrimmable;
import com.facebook.common.memory.MemoryTrimmableRegistry;
import com.facebook.common.memory.NoOpMemoryTrimmableRegistry;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.BaseDataSubscriber;
import com.facebook.datasource.DataSource;
import com.facebook.datasource.DataSubscriber;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.AbstractDraweeController;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.backends.okhttp3.OkHttpImagePipelineConfigFactory;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.decoder.ProgressiveJpegConfig;
import com.facebook.imagepipeline.image.CloseableBitmap;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.image.ImmutableQualityInfo;
import com.facebook.imagepipeline.image.QualityInfo;
import com.facebook.imagepipeline.listener.RequestListener;
import com.facebook.imagepipeline.listener.RequestLoggingListener;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.facebook.imagepipeline.request.Postprocessor;
import com.ziroom.creation.base.CreationApplication;
import com.ziroom.creation.listener.OnBitmapGetListener;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import okhttp3.OkHttpClient;

import static com.ziroom.creation.base.Constant.BITMAP_COMFIG;
import static com.ziroom.creation.base.Constant.IMAGE_PIPELINE_CACHE_DIR;
import static com.ziroom.creation.base.Constant.IMAGE_PIPELINE_SMALL_CACHE_DIR;
import static com.ziroom.creation.base.Constant.MAX_DISK_CACHE_LOW_SIZE;
import static com.ziroom.creation.base.Constant.MAX_DISK_CACHE_SIZE;
import static com.ziroom.creation.base.Constant.MAX_DISK_CACHE_VERYLOW_SIZE;
import static com.ziroom.creation.base.Constant.MAX_SMALL_DISK_CACHE_SIZE;
import static com.ziroom.creation.base.Constant.MAX_SMALL_DISK_LOW_CACHE_SIZE;
import static com.ziroom.creation.base.Constant.MAX_SMALL_DISK_VERYLOW_CACHE_SIZE;
import static com.ziroom.creation.base.Constant.PIC_LOAD_ERROR;

/**
 * Fresco工具类
 * Created by lmnrenbc on 2017/11/26.
 */

public class FrescoUtils {

    //加载策略
    private static ImagePipelineConfig sImagePipelineConfig;
    //默认加载图片和失败图片
    public static Drawable sPlaceholderDrawable;
    public static Drawable sErrorDrawable;
    private static FrescoUtils instance;

    private FrescoUtils() {
    }

    public static FrescoUtils getInstance() {
        if (instance == null) {
            synchronized (FrescoUtils.class) {
                instance = new FrescoUtils();
            }
        }
        return instance;
    }

    /**
     * 初始化默认图片
     *
     * @param resources
     * @param id
     */
    @SuppressWarnings("deprecation")
    public void initDefaultPic(final Resources resources, int id) {
        if (sPlaceholderDrawable == null) {
            sPlaceholderDrawable = resources.getDrawable(id);
        }
        if (sErrorDrawable == null) {
            sErrorDrawable = resources.getDrawable(id);
        }
    }

    /**
     * 初始化Fresco
     */
    public void initFresco() {
        Fresco.initialize(CreationApplication.getInstance(), getImagePipelineConfig(CreationApplication.getInstance()));
        FLog.setMinimumLoggingLevel(FLog.VERBOSE);
    }

    /**
     * 初始化配置，单例
     */
    private ImagePipelineConfig getImagePipelineConfig(Context context) {
        if (sImagePipelineConfig == null) {
            sImagePipelineConfig = initConfig(context);
        }
        return sImagePipelineConfig;
    }

    /**
     * 设置配置项
     *
     * @param context
     * @return
     */
    private ImagePipelineConfig initConfig(Context context) {
        Set<RequestListener> listeners = new HashSet<>();
        listeners.add(new RequestLoggingListener());

        //默认图片的磁盘配置
        DiskCacheConfig diskCacheConfig = DiskCacheConfig.newBuilder(context)
                .setBaseDirectoryPath(new File(FileUtils.getInstance().createDefaultFolder(context)))//缓存图片基路径
                .setBaseDirectoryName(IMAGE_PIPELINE_CACHE_DIR)//文件夹名
                .setMaxCacheSize(MAX_DISK_CACHE_SIZE)//默认缓存的最大大小。
                .setMaxCacheSizeOnLowDiskSpace(MAX_DISK_CACHE_LOW_SIZE)//缓存的最大大小,使用设备时低磁盘空间。
                .setMaxCacheSizeOnVeryLowDiskSpace(MAX_DISK_CACHE_VERYLOW_SIZE)//缓存的最大大小,当设备极低磁盘空间
                .build();

        //小图片的磁盘配置
        DiskCacheConfig diskSmallCacheConfig = DiskCacheConfig.newBuilder(context)
                .setBaseDirectoryPath(new File(FileUtils.getInstance().createDefaultFolder(context)))//缓存图片基路径
                .setBaseDirectoryName(IMAGE_PIPELINE_SMALL_CACHE_DIR)//文件夹名
                .setMaxCacheSize(MAX_SMALL_DISK_CACHE_SIZE)//默认缓存的最大大小。
                .setMaxCacheSizeOnLowDiskSpace(MAX_SMALL_DISK_LOW_CACHE_SIZE)//缓存的最大大小,使用设备时低磁盘空间。
                .setMaxCacheSizeOnVeryLowDiskSpace(MAX_SMALL_DISK_VERYLOW_CACHE_SIZE)//缓存的最大大小,当设备极低磁盘空间
                .build();

        // 当内存紧张时采取的措施
        MemoryTrimmableRegistry memoryTrimmableRegistry = NoOpMemoryTrimmableRegistry.getInstance();
        memoryTrimmableRegistry.registerMemoryTrimmable(new MemoryTrimmable() {
            @Override
            public void trim(MemoryTrimType trimType) {
                final double suggestedTrimRatio = trimType.getSuggestedTrimRatio();
                LogUtils.e("Fresco_log", String.format("Fresco onCreate suggestedTrimRatio : %d", suggestedTrimRatio));
                if (MemoryTrimType.OnCloseToDalvikHeapLimit.getSuggestedTrimRatio() == suggestedTrimRatio
                        || MemoryTrimType.OnSystemLowMemoryWhileAppInBackground.getSuggestedTrimRatio() == suggestedTrimRatio
                        || MemoryTrimType.OnSystemLowMemoryWhileAppInForeground.getSuggestedTrimRatio() == suggestedTrimRatio
                        ) {
                    // 清除内存缓存
                    Fresco.getImagePipeline().clearMemoryCaches();
                }
            }
        });

        // 设置Jpeg格式的图片支持渐进式显示
        ProgressiveJpegConfig pjpegConfig = new ProgressiveJpegConfig() {
            @Override
            public int getNextScanNumberToDecode(int scanNumber) {
                return scanNumber + 2;
            }

            public QualityInfo getQualityInfo(int scanNumber) {
                boolean isGoodEnough = (scanNumber >= 5);
                return ImmutableQualityInfo.of(scanNumber, isGoodEnough, false);
            }
        };

        ImagePipelineConfig frescoConfig = OkHttpImagePipelineConfigFactory
                .newBuilder(context, new OkHttpClient.Builder().build())
                .setBitmapsConfig(BITMAP_COMFIG)
                .setRequestListeners(listeners)
                //修改内存图片缓存数量，空间策略（这个方式有点恶心）
                .setBitmapMemoryCacheParamsSupplier(new BitmapMemoryCacheParamsSupplier(
                        (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)))
                .setMemoryTrimmableRegistry(memoryTrimmableRegistry)
                .setProgressiveJpegConfig(pjpegConfig)
                .setMainDiskCacheConfig(diskCacheConfig)
                .setSmallImageDiskCacheConfig(diskSmallCacheConfig)
                .setResizeAndRotateEnabledForNetwork(true)//已对网络图也能进行resize处理，减少内存开销
                .setDownsampleEnabled(true)//以打开对png等图片的自动缩放特性（缩放必须要设置ResizeOptions）
                .build();
        return frescoConfig;
    }

    public void getBitmapFromFresco(Context context, String url, final OnBitmapGetListener listener) {
        getBitmapFromFresco(context, url, false, listener);
    }

    /**
     * 从fresco获取图片
     *
     * @param url
     * @param local
     * @param listener
     */
    public void getBitmapFromFresco(final Context context, String url, boolean local, final OnBitmapGetListener listener) {
        ImageRequest request = null;
        if (!local) {
            request = generateImageRequest(url, null).build();
        } else {
            request = generateImageRequest(Uri.fromFile(new File(url)), null).build();
        }
        ImagePipeline imagePipeline = Fresco.getImagePipeline();

        DataSubscriber dataSubscriber = new BaseDataSubscriber<CloseableReference<CloseableBitmap>>() {
            @Override
            public void onNewResultImpl(
                    DataSource<CloseableReference<CloseableBitmap>> dataSource) {
                if (!dataSource.isFinished()) {
                    ToastUtils.showToast(context, PIC_LOAD_ERROR);
                    LogUtils.e("TAG", "dataSource.isFinished");
                    return;
                }
                CloseableReference<CloseableBitmap> imageReference = dataSource.getResult();
                if (imageReference != null) {
                    final CloseableReference<CloseableBitmap> closeableReference = imageReference.clone();
                    try {
                        CloseableBitmap closeableBitmap = closeableReference.get();
                        Bitmap bitmap = closeableBitmap.getUnderlyingBitmap();
                        boolean recycled = bitmap.isRecycled();
                        if (bitmap != null && !recycled) {
                            listener.getBitmap(bitmap);
                        } else {
                            ToastUtils.showToast(context, PIC_LOAD_ERROR + recycled);
                            LogUtils.e("TAG", PIC_LOAD_ERROR + recycled);
                        }
                    } finally {
                        imageReference.close();
                        closeableReference.close();
                    }
                }
            }

            @Override
            public void onFailureImpl(DataSource dataSource) {
                ToastUtils.showToast(context, PIC_LOAD_ERROR);
                LogUtils.e("TAG", PIC_LOAD_ERROR);
                Throwable throwable = dataSource.getFailureCause();
                throwable.printStackTrace();
            }
        };

        DataSource<CloseableReference<CloseableImage>> dataSource = imagePipeline.fetchDecodedImage(request, this);
        dataSource.subscribe(dataSubscriber, UiThreadImmediateExecutorService.getInstance());
    }

    public AbstractDraweeController frescoController(String url) {
        return frescoController(url, null, null);
    }

    public AbstractDraweeController frescoResizeController(Uri uri) {
        return frescoResizeController(uri, null, null);
    }

    public AbstractDraweeController frescoController(String url, ControllerListener listener) {
        return frescoController(url, listener, null);
    }

    public AbstractDraweeController frescoController(String url, Postprocessor postprocessor) {
        return frescoController(url, null, postprocessor);
    }

    public AbstractDraweeController frescoResizeController(String url, int width, int height) {
        return frescoResizeController(url, null, null, width, height);
    }

    public AbstractDraweeController frescoResizeController(Uri uri, int width, int height) {
        return frescoResizeController(uri, null, null, width, height);
    }

    public AbstractDraweeController frescoResizeController(String url, ControllerListener listener, int width, int height) {
        return frescoResizeController(url, null, listener, width, height);
    }

    public AbstractDraweeController frescoResizeController(String url, Postprocessor postprocessor, int width, int height) {
        return frescoResizeController(url, postprocessor, null, width, height);
    }

    /**
     * 缩放图片
     *
     * @param url
     * @param postprocessor
     * @param listener
     * @param width
     * @param height
     * @return
     */
    public AbstractDraweeController frescoResizeController(String url, Postprocessor postprocessor, ControllerListener listener, int width, int height) {
        ImageRequest request;
        if (width > 0 && height > 0) {
            request = generateImageRequest(url, postprocessor)
                    .setResizeOptions(new ResizeOptions(width, height))
                    .build();
        } else {
            request = generateImageRequest(url, postprocessor)
                    .build();
        }

        AbstractDraweeController controller = generateController(listener, request);
        return controller;
    }

    /**
     * 缩放图片
     *
     * @param uri
     * @param postprocessor
     * @param listener
     * @param width
     * @param height
     * @return
     */
    public AbstractDraweeController frescoResizeController(Uri uri, Postprocessor postprocessor, ControllerListener listener, int width, int height) {
        ImageRequest request;
        if (width > 0 && height > 0) {
            request = generateImageRequest(uri, postprocessor)
                    .setResizeOptions(new ResizeOptions(width, height))
                    .build();
        } else {
            request = generateImageRequest(uri, postprocessor)
                    .build();
        }

        AbstractDraweeController controller = generateController(listener, request);
        return controller;
    }

    /**
     * onFailed 失败调用  onFinalImageSet 成功调用
     *
     * @param url
     * @param listener
     * @return
     */
    public AbstractDraweeController frescoController(String url, ControllerListener listener, Postprocessor postprocessor) {
        if (!url.contains("http") && !url.contains("file")) {
            url += "file://";
        }

        ImageRequest request = generateImageRequest(url, postprocessor).build();

        AbstractDraweeController controller = generateController(listener, request);
        return controller;
    }

    public AbstractDraweeController frescoResizeController(Uri uri, Postprocessor postprocessor, ControllerListener listener) {
        ImageRequest request = generateImageRequest(uri, postprocessor).build();
        AbstractDraweeController controller = generateController(listener, request);
        return controller;
    }

    public AbstractDraweeController generateController(ControllerListener listener, ImageRequest request) {
        AbstractDraweeController controller;
        if (listener != null) {
            controller = Fresco.newDraweeControllerBuilder()
                    .setImageRequest(request)
                    .setControllerListener(listener)
                    .setAutoPlayAnimations(true)
                    .build();
        } else {
            controller = Fresco.newDraweeControllerBuilder()
                    .setImageRequest(request)
                    .setAutoPlayAnimations(true)
                    .build();
        }
        return controller;
    }

    public ImageRequestBuilder generateImageRequest(Uri uri, Postprocessor postprocessor) {
        ImageRequestBuilder requestBuilder;
        if (postprocessor != null) {
            requestBuilder = ImageRequestBuilder.newBuilderWithSource(uri == null ? Uri.parse("") : uri)
                    .setProgressiveRenderingEnabled(true)
                    .setPostprocessor(postprocessor)
                    .setAutoRotateEnabled(true);
        } else {
            requestBuilder = ImageRequestBuilder.newBuilderWithSource(uri == null ? Uri.parse("") : uri)
                    .setProgressiveRenderingEnabled(true)
                    .setAutoRotateEnabled(true);
        }
        return requestBuilder;
    }

    public ImageRequestBuilder generateImageRequest(String url, Postprocessor postprocessor) {
        ImageRequestBuilder requestBuilder;
        if (postprocessor != null) {
            requestBuilder = ImageRequestBuilder.newBuilderWithSource(Uri.parse(TextUtils.isEmpty(url) ? "" : url))
                    .setProgressiveRenderingEnabled(true)
                    .setPostprocessor(postprocessor)
                    .setAutoRotateEnabled(true);
        } else {
            requestBuilder = ImageRequestBuilder.newBuilderWithSource(Uri.parse(TextUtils.isEmpty(url) ? "" : url))
                    .setProgressiveRenderingEnabled(true)
                    .setAutoRotateEnabled(true);
        }
        return requestBuilder;
    }

    public void frescoHierarchyController(SimpleDraweeView mSimpleDraweeView, int defaultDrawable) {
        frescoHierarchyController(mSimpleDraweeView, ScalingUtils.ScaleType.FIT_XY, defaultDrawable);
    }


    public void frescoHierarchyController(SimpleDraweeView mSimpleDraweeView, ScalingUtils.ScaleType type, int defaultDrawable) {
        frescoHierarchyController(mSimpleDraweeView, type, defaultDrawable, defaultDrawable);
    }

    /**
     * 设置图片填充类型，加载图片以及失败图片
     *
     * @param mSimpleDraweeView
     * @param type
     * @param holderDrawable
     * @param failureDrawable
     */
    public void frescoHierarchyController(SimpleDraweeView mSimpleDraweeView, ScalingUtils.ScaleType type, int holderDrawable, int failureDrawable) {
        mSimpleDraweeView.getHierarchy().setActualImageScaleType(type);
        mSimpleDraweeView.getHierarchy().setPlaceholderImage(holderDrawable, type);
        mSimpleDraweeView.getHierarchy().setFailureImage(failureDrawable, type);
    }

    /**
     * 从本地加载Resource资源图片
     *
     * @param resourceId
     * @return
     */
    public AbstractDraweeController frescoFromResourceController(int resourceId) {
        return frescoFromResourceController(resourceId, null);
    }

    public AbstractDraweeController frescoFromResourceController(int resourceId, ControllerListener listener) {
        ImageRequest request;
        request = ImageRequestBuilder.newBuilderWithResourceId(resourceId)
                .setProgressiveRenderingEnabled(true)
                .setAutoRotateEnabled(true)
                .build();

        AbstractDraweeController controller;
        if (listener != null) {
            controller = Fresco.newDraweeControllerBuilder()
                    .setImageRequest(request)
                    .setControllerListener(listener)
                    .setAutoPlayAnimations(true)
                    .build();
        } else {
            controller = Fresco.newDraweeControllerBuilder()
                    .setImageRequest(request)
                    .setAutoPlayAnimations(true)
                    .build();
        }
        return controller;
    }

    /**
     * 从File中加载本地图片
     *
     * @param file
     * @return
     */
    public AbstractDraweeController frescoFromFileController(File file) {
        return frescoFromFileController(file, null);
    }

    public AbstractDraweeController frescoFromFileController(File file, ControllerListener listener) {
        ImageRequest request;
        request = generateImageRequest(Uri.fromFile(file), null).build();

        AbstractDraweeController controller;
        if (listener != null) {
            controller = Fresco.newDraweeControllerBuilder()
                    .setAutoPlayAnimations(true)
                    .setImageRequest(request)
                    .setControllerListener(listener)
                    .build();
        } else {
            controller = Fresco.newDraweeControllerBuilder()
                    .setAutoPlayAnimations(true)
                    .setImageRequest(request)
                    .build();
        }

        return controller;
    }

}