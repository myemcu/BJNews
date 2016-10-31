package com.example.administrator.bjnews.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.example.administrator.bjnews.GuideActivity;
import com.example.administrator.bjnews.SplashActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Created by Administrator on 2016/9/7 0007.
 * 保存软件参数
 */
public class CacheUtil {
    // 设置软件保存参数
    public static void putBoolean(Context context, String key, boolean value) {

        SharedPreferences sp = context.getSharedPreferences("myemcu",Context.MODE_PRIVATE); // 创建数据持久化对象
        sp.edit().putBoolean(key,value).commit();

    }

    // 获取软件参数
    public static boolean getBoolean(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences("myemcu",Context.MODE_PRIVATE); // 创建数据持久化对象

        return sp.getBoolean(key,false);
    }

    // 设置数据缓存
    public static void putString(Context context, String key, String values) {

        // 复制LocalCacheUtils中的代码
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            // mnt/sdcard/BJNews/lllslllllkll(MD5加密后的名字)(不要有后缀，不然就会整到Android自带的图片浏览器中)
            try {
                String filename = key;
//                String filename = MD5Encoder.encode(key);
//                File file = new File(Environment.getExternalStorageDirectory()+"/BJNews",filename);   // 文件保存到外部存储目录
                File file = new File("mnt/sdcard/BJNews",filename);   // 文件保存到外部存储目录
//                File file = new File("storage/sdcard1/BJNews",filename);   // 文件保存到外部存储目录
//                File file = new File(Environment.getExternalStorageDirectory()+"BJNews",filename);
                File file1Parent = file.getParentFile();
                if(!file1Parent.exists()) {
                    file1Parent.mkdirs();
                }

                if (!file.exists()) {
                    file.createNewFile();
                }

                FileOutputStream fos = new FileOutputStream(file);
                fos.write(values.getBytes());
                fos.flush();
                fos.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            SharedPreferences sp = context.getSharedPreferences("myemcu",Context.MODE_PRIVATE); // 创建数据持久化对象
            sp.edit().putString(key,values).commit();
        }


    }

    // 获取数据缓存
    public static String getString(Context context, String key) {   // key就是请求的Url

        String result="";

        // 复制LocalCacheUtils中的代码
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            // mnt/sdcard/BJNews/lllslllllkll(MD5加密后的名字)(不要有后缀，不然就会整到Android自带的图片浏览器中)
            try {
                String filename = key;
//                String filename = MD5Encoder.encode(key);
//                File file = new File(Environment.getExternalStorageDirectory()+"/BJNews",filename);   // 文件保存到外部存储目录
                File file = new File("mnt/sdcard/BJNews",filename);   // 文件保存到外部存储目录
//                File file = new File("storage/sdcard1/BJNews",filename);   // 文件保存到外部存储目录
//                File file = new File(Environment.getExternalStorageDirectory()+"BJNews",filename);
//                File file1Parent = file.getParentFile();

                if(file.exists()) { // 如果文件存在，就读取
                   FileInputStream fis = new FileInputStream(file);
                    byte[] buffer = new byte[1024];
                    int len;
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    while ((len=fis.read(buffer))!= -1) {
                        stream.write(buffer,0,len);
                    }
                    result = stream.toString();
                    fis.close();
                    stream.close();
                }

                /*if (!file.exists()) {
                    file.createNewFile();
                }获取的时候，不需要创建*/

            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            SharedPreferences sp = context.getSharedPreferences("myemcu",Context.MODE_PRIVATE); // 创建数据持久化对象
            result=sp.getString(key,"");
        }

        return result;    // 若key没去到，则返回""(空字符串)
    }
}
