package com.example.administrator.bjnews.utils;

/**
 * Created by Administrator on 2016/9/14 0014.
 * http://192.168.1.200:8080/zhbj/categories.json
 * 配置联网请求链接
 */
public class Url {

    /*// Tomacat基站服务包(OLD)
    public static final String BASE_URL= "http://192.168.1.200:8080/zhbj";
    // 新闻路径(OLD)
    public static final String NEWSCENTER_URL = BASE_URL+"/categories.json";

    *//*组图路径验证(OLD)*//*
    *//*http://192.168.1.200:8080/zhbj/photos/photos_1.json*//*
    public static final String PHOTOS_URL = BASE_URL+"/photos/photos_1.json";*/

    // Tomacat基站服务包(New)
    public static final String BASE_URL = "http://192.168.1.200:8080/web_home";
    // 新闻路径(New)
    public static final String NEWSCENTER_URL = BASE_URL+"/static/api/news/categories.json";
    // 组图路径验证
    public static final String PHOTOS_URL = BASE_URL+"/static/api/news/10003/list_1.json";
}
