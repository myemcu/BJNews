package com.example.administrator.bjnews.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/9/21 0021.
 * 手动设计JavaBean，用于装载Json解析结果(设计完成先测试)
 */

// 说明：凡是定义的内部类，必须Public，不然外层无法访问。

public class NewsCenterBean_Hand {

    // 1 定义对象
    private List<NewsCenterBean_Data> data; // NewsCenterBean_Data代表data中，0~3的任何一个
    private List<Integer> extend;
    private int retcode;

    // 2 Set、Get(全选)
    public List<NewsCenterBean_Data> getData() {
        return data;
    }

    public void setData(List<NewsCenterBean_Data> data) {
        this.data = data;
    }

    public List<Integer> getExtend() {
        return extend;
    }

    public void setExtend(List<Integer> extend) {
        this.extend = extend;
    }

    public int getRetcode() {
        return retcode;
    }

    public void setRetcode(int retcode) {
        this.retcode = retcode;
    }

    // 3 创建内部类(必须Public)
    public static class NewsCenterBean_Data {  // 左侧菜单的详情页面数据(定义成Static可以不用new)
        private List<Children_Data> children; // 遇到[]，代表的就是集合
        private int id;
        private String title;
        private int type;
        private String url;
        private String url1;
        private String dayurl;
        private String excurl;
        private String weekurl;

        public List<Children_Data> getChildren() {
            return children;
        }

        public void setChildren(List<Children_Data> children) {
            this.children = children;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getUrl1() {
            return url1;
        }

        public void setUrl1(String url1) {
            this.url1 = url1;
        }

        public String getDayurl() {
            return dayurl;
        }

        public void setDayurl(String dayurl) {
            this.dayurl = dayurl;
        }

        public String getExcurl() {
            return excurl;
        }

        public void setExcurl(String excurl) {
            this.excurl = excurl;
        }

        public String getWeekurl() {
            return weekurl;
        }

        public void setWeekurl(String weekurl) {
            this.weekurl = weekurl;
        }

        public static class Children_Data {
            private int id;
            private String title;
            private int type;
            private String url;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            // Alt+Insert(toString())

            @Override
            public String toString() {  // 方便Debug看数据
                return "Children_Data{" +
                        "id=" + id +
                        ", title='" + title + '\'' +
                        ", type=" + type +
                        ", url='" + url + '\'' +
                        '}';
            }
        }

        @Override
        public String toString() {
            return "NewsCenterBean_Data{" +
                    "children=" + children +
                    ", id=" + id +
                    ", title='" + title + '\'' +
                    ", type=" + type +
                    ", url='" + url + '\'' +
                    ", url1='" + url1 + '\'' +
                    ", dayurl='" + dayurl + '\'' +
                    ", excurl='" + excurl + '\'' +
                    ", weekurl='" + weekurl + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "NewsCenterBean_Hand{" +
                "data=" + data +
                ", extend=" + extend +
                ", retcode=" + retcode +
                '}';
    }
}
