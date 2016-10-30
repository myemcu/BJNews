package com.example.administrator.bjnews.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Created by Administrator on 2016/10/30 0030.
 * 本地缓存(存到SD卡中)
 */

public class LocalCacheUtils {
    // 根据imageUrl保存图片到SD卡上
    public void putBitmap(String imageUrl, Bitmap bitmap) {
        // 判断SD卡是否存在
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            // mnt/sdcard/BJNews/lllslllllkll(MD5加密后的名字)(不要有后缀，不然就会整到Android自带的图片浏览器中)
            try {
                  String filename = imageUrl;
//                String filename = MD5Encoder.encode(imageUrl);
//                File file = new File(Environment.getExternalStorageDirectory()+"/BJNews",filename);   // 文件保存到外部存储目录
                File file = new File("mnt/sdcard/BJNews",filename);   // 文件保存到外部存储目录
//                File file = new File("storage/sdcard1/BJNews",filename);   // 文件保存到外部存储目录
//                File file = new File(Environment.getExternalStorageDirectory()+"BJNews",filename);

                File parentFile = file.getParentFile(); // 获取父层目录(mnt/sdcard/BJNews)
                if (!parentFile.exists()) {             // 若不存在
                    parentFile.mkdirs();                // 创建出来
                }

                FileOutputStream fop = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG,100,fop); // 100代表高质量图片
                fop.close();                                        // 之后图片就保存到SD卡中的这个路径了
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public Bitmap getBitmap(String imageUrl) {

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            // mnt/sdcard/BJNews/lllslllllkll(MD5加密后的名字)(不要有后缀，不然就会整到Android自带的图片浏览器中)
            try {
                String filename = imageUrl;
//                String filename = MD5Encoder.encode(imageUrl);
//                File file = new File(Environment.getExternalStorageDirectory()+"/BJNews",filename);   // 文件保存到外部存储目录
                File file = new File("mnt/sdcard/BJNews",filename);   // 文件保存到外部存储目录
//                File file = new File("storage/sdcard1/BJNews",filename);   // 文件保存到外部存储目录
//                File file = new File(Environment.getExternalStorageDirectory()+"BJNews",filename);
                if (file.exists()) {
                    FileInputStream fileInputStream = new FileInputStream(file);
                    Bitmap bitmap = BitmapFactory.decodeStream(fileInputStream);
                    fileInputStream.close();
                    return bitmap;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }
}
