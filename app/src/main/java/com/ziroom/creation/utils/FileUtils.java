package com.ziroom.creation.utils;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.format.Formatter;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.util.Date;
import java.util.List;

import static com.ziroom.creation.base.Constant.FILE_ROOT;

/**
 * 文件工具类
 * Created by lmnrenbc on 2017/5/2.
 */
public class FileUtils {

    private static FileUtils instance;
    private final static String TAG = FileUtils.class.getSimpleName();

    private FileUtils() {
    }

    public static FileUtils getInstance() {
        if (instance == null) {
            synchronized (FileUtils.class) {
                instance = new FileUtils();
            }
        }
        return instance;
    }

    public String getPhotoPath(Context context) {
        String defaultFolder = createDefaultFolder(context);
        //文件名使用时间戳
        long fileName = new Date().getTime();
        String photoPath = defaultFolder + File.separator + fileName + ".jpg";
        File photoFile = new File(photoPath);
        return photoFile.getAbsolutePath();
    }

    /**
     * 创建缺省的文件夹
     *
     * @return
     */
    public String createDefaultFolder(Context context) {
        String folderPath = getDefaultFolderPath(context);
        if (folderPath == null) {
            return null;
        }
        File file = new File(folderPath);
        if (!file.exists()) {
            file.mkdir();
        }
        return folderPath;
    }

    /**
     * 得到文件夹缺省路径 因为有的时候 可能没有sdcard
     *
     * @return 注意判断是否为null
     */
    private String getDefaultFolderPath(Context context) {
        String folderUrl = null;
        if (isSDCardWritable()) {
            // 有读写权限
            String pathFile = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
            folderUrl = pathFile + File.separator + FILE_ROOT;

        } else {
            // 没有读写能力
            LogUtils.e(TAG, "sd卡没有读写能力1， 使用内部安装文件路径");
            folderUrl = context.getFilesDir().getAbsolutePath() + File.separator + FILE_ROOT;
        }
        return folderUrl;
    }

    /**
     * 判断sd卡读写能力
     *
     * @return
     */
    private boolean isSDCardWritable() {
        return android.os.Environment.MEDIA_MOUNTED.equals(android.os.Environment.getExternalStorageState());
    }

    /**
     * @param filePath
     * @return
     */
    public boolean deleteFile(String filePath) {
        File file = new File(filePath);
        return deleteFile(file);
    }

    /**
     * @param file
     * @return
     */
    public boolean deleteFile(File file) {
        if (file.exists()) {
            return file.delete();
        }
        return true;
    }


    /**
     * @param dirPath
     * @return
     */
    public boolean deleteDirectory(String dirPath) {
        return deleteDirectory(new File(dirPath));
    }

    /**
     * @param dir
     * @return
     */
    public boolean deleteDirectory(File dir) {
        if (dir.exists()) {
            File[] files = dir.listFiles();
            if (files != null) {
                for (File file : files) {
                    deleteDirectory(file);
                }
            }
        }
        return deleteFile(dir);
    }

    /**
     * 判断 该路径的文件或者文件夹是否存在
     */
    public boolean isFileExist(String filePath) {
        try {
            File folder = new File(filePath);
            if (folder.exists()) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * get file size which is human readable
     *
     * @param context
     * @param files
     * @return
     */
    public String getFilesSize(Context context, List<String> files) {
        long result = 0;
        for (String filePath : files) {
            File file = new File(filePath);
            result += file.length();
        }
        return Formatter.formatFileSize(context, result);
    }

    public void copyFile(File source, File dest) {
        FileChannel inputChannel = null;
        FileChannel outputChannel = null;
        try {
            inputChannel = new FileInputStream(source).getChannel();
            outputChannel = new FileOutputStream(dest).getChannel();
            outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputChannel != null) {
                    inputChannel.close();
                }
                if (outputChannel != null) {
                    outputChannel.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void closeSilently(@Nullable Closeable c) {
        if (c == null) {
            return;
        }
        try {
            c.close();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public String inputStream2String(InputStream is) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int i = -1;
        try {
            while ((i = is.read()) != -1) {
                baos.write(i);
            }
        } catch (IOException e) {
        }
        return baos.toString();
    }

    /**
     * 根据url获取文件名
     *
     * @param url
     * @return
     */
    public String getFileName(String url) {
        String[] split = url.split("/");
        return split[split.length - 1];
    }

    /**
     * 根据url获取文件uuid
     *
     * @param url
     * @return
     */
    public String getUUID(String url) {
        String fileName = getFileName(url);
        if (url.contains("png")) {
            return fileName.replace(".png", "");
        } else if (url.contains("jpg")) {
            return fileName.replace(".jpg", "");
        } else if (url.contains("mp4")) {
            return fileName.replace(".mp4", "");
        } else {
            String[] strings = fileName.split("\\.");
            return strings[0];
        }
    }
}
