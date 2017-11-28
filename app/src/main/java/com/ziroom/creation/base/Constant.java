package com.ziroom.creation.base;

import android.graphics.Bitmap;

import com.facebook.common.util.ByteConstants;

/**
 * 常量定义
 * Created by lmnrenbc on 2017/11/26.
 */
public class Constant {

    /***  权限相关   ***/
    public static final int PERMISSIONS_REQUEST_CODE = 0x1314;
    public static final int PERMISSIONS_GRANTED = 0; // 权限授权
    public static final int PERMISSIONS_DENIED = 1; // 权限拒绝
    public static final String PERMISSIONS_EXTRA = "extra_permission"; // 权限参数

    /***日期相关***/
    public static final String DATE_COMMON = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_CONNECTED = "yyyyMMdd";


    /***日志相关***/
    public static String LOG_FILE_NAME = "CreationLog"; // 本类输出的日志文件名称
    public static String LOG_PATH_SDCARD_DIR = "/sdcard/"; // 日志文件在sdcard中的路径
    public static String LOG_TAG = "Creation_"; // 输出的日志统一前缀
    public static String LOG_FILE_SUFFIX = ".log"; // 本类输出的日志文件后缀
    public static int LOG_SDCARD_FILE_SAVE_DAYS = 1; // sd卡中日志文件的最多保存天数
    public static char LOG_DEFULT_TYPE = 'v'; // 输入日志类型，w代表只输出告警信息等，v代表输出所有信息


    /*** 文件相关 ***/
    public static final String FILE_ROOT = "creation";

    /**
     * 获取的后台的数据的code值：正确码
     */
    public static final String GATEWAY_SUCCESS_CODE = "200";    //网关统一个后台错误码，统一成200
    public static final String GATEWAY_ERROR_CODE = "400";    //默认错误码400，自定义

    /**
     * 网关回传的状态,标识网关层是否成功调用(后台系统不需要关注)
     * status只有  "success"  "failure"  两种状态，
     * 只要 网关返回 success代表网关层调用是成功的，有问题请直接联系后台开发人员
     * 如果返回failure,请根据报错信息进行反馈
     */
    public static final String GATEWAY_STATUS_SUCCESS = "success";
    public static final String GATEWAY_STATUS_FAILURE = "failure";


    /**
     * 所用错误的提示文案，都以Error 开头
     */
    public static final String ERROR_NORMAL = "数据异常，请退出重试";

    /**
     * 所用错误的提示文案，都以Error 开头
     */
    public static final String ERROR_NET = "网络异常，请稍后重试";

    /**
     * 下拉刷新,没有最新数据了
     */
    public static final String DATA_FRESH_NO_MORE = "已经是最新数据啦";

    /**
     * 分页加载，已经到底啦
     */
    public static final String DATA_LOAD_NO_MORE = "没有更多数据了";

    /**
     * 数据加载中，请稍后
     */
    public static final String DATA_LOADING = "加载中，请稍后...";

    public static final String PIC_LOAD_ERROR = "图片加载失败";


    /*** 图片相关 ***/
    private static final int MAX_HEAP_SIZE = (int) Runtime.getRuntime().maxMemory();//分配的可用内存
    public static final int MAX_MEMORY_CACHE_SIZE = MAX_HEAP_SIZE / 4;//使用的缓存数量
    private static final int MAX_MEMORY_PIC_COUNT = 100; //缓存图片的最大数量

    public static final String IMAGE_PIPELINE_SMALL_CACHE_DIR = "pipeline_small";//默认图所放路径的文件夹名
    public static final String IMAGE_PIPELINE_CACHE_DIR = "pipeline";//默认图所放路径的文件夹名

    public static final int MAX_SMALL_DISK_VERYLOW_CACHE_SIZE = 5 * ByteConstants.MB;//小图极低磁盘空间缓存的最大值（特性：可将大量的小图放到额外放在另一个磁盘空间防止大图占用磁盘空间而删除了大量的小图）
    public static final int MAX_SMALL_DISK_LOW_CACHE_SIZE = 10 * ByteConstants.MB;//小图低磁盘空间缓存的最大值（特性：可将大量的小图放到额外放在另一个磁盘空间防止大图占用磁盘空间而删除了大量的小图）
    public static final int MAX_SMALL_DISK_CACHE_SIZE = 20 * ByteConstants.MB;//小图磁盘缓存的最大值（特性：可将大量的小图放到额外放在另一个磁盘空间防止大图占用磁盘空间而删除了大量的小图）

    public static final int MAX_DISK_CACHE_VERYLOW_SIZE = 10 * ByteConstants.MB;//默认图极低磁盘空间缓存的最大值
    public static final int MAX_DISK_CACHE_LOW_SIZE = 30 * ByteConstants.MB;//默认图低磁盘空间缓存的最大值
    public static final int MAX_DISK_CACHE_SIZE = 50 * ByteConstants.MB;//默认图磁盘缓存的最大值
    public static final Bitmap.Config BITMAP_COMFIG = Bitmap.Config.RGB_565;


}
