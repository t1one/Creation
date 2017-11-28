package com.ziroom.creation.utils;

import android.util.Log;

import com.ziroom.creation.base.Constant;
import com.ziroom.creation.base.CreationApplication;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.ziroom.creation.base.Constant.DATE_COMMON;
import static com.ziroom.creation.base.Constant.DATE_CONNECTED;
import static com.ziroom.creation.base.Constant.LOG_DEFULT_TYPE;
import static com.ziroom.creation.base.Constant.LOG_FILE_NAME;
import static com.ziroom.creation.base.Constant.LOG_FILE_SUFFIX;
import static com.ziroom.creation.base.Constant.LOG_PATH_SDCARD_DIR;
import static com.ziroom.creation.base.Constant.LOG_SDCARD_FILE_SAVE_DAYS;

/**
 * 日志工具类
 * Created by lmnrenbc on 2017/5/3.
 */
public class LogUtils {
    private static Boolean LOG_SWITCH = CreationApplication.isRelease; // 日志文件总开关
    private static Boolean LOG_WRITE_TO_FILE = !CreationApplication.isRelease; // 日志写入文件开关

    private static SimpleDateFormat LogSdf = new SimpleDateFormat(DATE_COMMON); // 日志的输出格式
    private static SimpleDateFormat logfile = new SimpleDateFormat(DATE_CONNECTED); // 日志文件格式

    public static void w(String tag, Object msg) { // 警告信息
        log(tag, msg.toString(), 'w');
    }

    public static void w(String tag, String msg, Throwable t) {
        if (LOG_SWITCH) {
            if (t != null) {
                msg += "\n" + Log.getStackTraceString(t);
            }
            log(tag, msg, 'w');
        }
    }

    public static void e(String tag, String msg, Throwable t) {
        if (LOG_SWITCH) {
            if (t != null) {
                msg += "\n" + Log.getStackTraceString(t);
            }
            log(tag, msg, 'e');
        }
    }

    public static void i(String tag, String msg, Throwable t) {
        if (LOG_SWITCH) {
            if (t != null) {
                msg += "\n" + Log.getStackTraceString(t);
            }
            log(tag, msg, 'i');
        }
    }

    public static void e(String tag, Object msg) { // 错误信息
        log(tag, msg.toString(), 'e');
    }

    public static void d(String tag, Object msg) {// 调试信息
        log(tag, msg.toString(), 'd');
    }

    public static void i(String tag, Object msg) {//
        log(tag, msg.toString(), 'i');
    }

    public static void v(String tag, Object msg) {
        log(tag, msg.toString(), 'v');
    }

    public static void w(String tag, String text) {
        log(tag, text, 'w');
    }

    public static void e(String tag, String text) {
        log(tag, text, 'e');
    }

    public static void d(String tag, String text) {
        log(tag, text, 'd');
    }

    public static void i(String tag, String text) {
        log(tag, text, 'i');
    }

    public static void v(String tag, String text) {
        log(tag, text, 'v');
    }

    /**
     * 根据tag, msg和等级，输出日志
     *
     * @param tag
     * @param msg
     * @param level
     * @return void
     * @since v 1.0
     */
    private static void log(String tag, String msg, char level) {
        tag = Constant.LOG_TAG + tag;
        if (LOG_SWITCH) {
            if ('e' == level && ('e' == LOG_DEFULT_TYPE || 'v' == LOG_DEFULT_TYPE)) { // 输出错误信息
                Log.e(tag, msg);
            } else if ('w' == level && ('w' == LOG_DEFULT_TYPE || 'v' == LOG_DEFULT_TYPE)) {
                Log.w(tag, msg);
            } else if ('d' == level && ('d' == LOG_DEFULT_TYPE || 'v' == LOG_DEFULT_TYPE)) {
                Log.d(tag, msg);
            } else if ('i' == level && ('d' == LOG_DEFULT_TYPE || 'v' == LOG_DEFULT_TYPE)) {
                Log.i(tag, msg);
            } else {
                Log.v(tag, msg);
            }
            if (LOG_WRITE_TO_FILE) {
                delFile();
                // writeLogtoFile(String.valueOf(level), tag, msg);
            }
        }
    }

    /**
     * 打开日志文件并写入日志
     *
     * @return
     **/
    private static void writeLogtoFile(String logtype, String tag, String text) {// 新建或打开日志文件
        Date nowtime = new Date();
        String needWriteFiel = logfile.format(nowtime);
        String needWriteMessage = LogSdf.format(nowtime) + "    " + logtype
                + "    " + tag + "    " + text;
        File file = new File(LOG_PATH_SDCARD_DIR, LOG_FILE_NAME + needWriteFiel + LOG_FILE_SUFFIX);
        try {
            FileWriter filerWriter = new FileWriter(file, true);// 后面这个参数代表是不是要接上文件中原来的数据，不进行覆盖
            BufferedWriter bufWriter = new BufferedWriter(filerWriter);
            bufWriter.write(needWriteMessage);
            bufWriter.newLine();
            bufWriter.close();
            filerWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除制定的日志文件
     */
    public static void delFile() {// 删除日志文件
        String needDelFiel = logfile.format(getDateBefore());
        File file = new File(LOG_PATH_SDCARD_DIR, LOG_FILE_NAME + needDelFiel + LOG_FILE_SUFFIX);
        if (file.exists()) {
            file.delete();
        }
    }

    /**
     * 得到现在时间前的几天日期，用来得到需要删除的日志文件名
     */
    private static Date getDateBefore() {
        Date nowtime = new Date();
        Calendar now = Calendar.getInstance();
        now.setTime(nowtime);
        now.set(Calendar.DATE, now.get(Calendar.DATE) - LOG_SDCARD_FILE_SAVE_DAYS);
        return now.getTime();
    }
}