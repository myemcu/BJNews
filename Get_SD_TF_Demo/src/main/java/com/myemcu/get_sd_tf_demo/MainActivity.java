package com.myemcu.get_sd_tf_demo;


        import java.io.BufferedReader;
        import java.io.File;
        import java.io.InputStream;
        import java.io.InputStreamReader;
        import java.util.ArrayList;
        import java.util.List;

        import android.content.Context;
        import android.os.Bundle;
        import android.os.Environment;
        import android.app.Activity;
        import android.util.Log;
        import android.view.Menu;

public class MainActivity extends Activity {

    private File value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StringBuilder log = new StringBuilder();
        String inPath = getInnerSDCardPath();
        log.append("内置SD卡路径：" + inPath + "\r\n");

        List<String> extPaths = getExtSDCardPath();
        for (String path : extPaths) {
            log.append("外置SD卡路径：" + path + "\r\n");
        }
        System.out.println(log.toString());

        value=getExternalCacheDir();
        Log.d("123","value"+value);
    }

    /**
     * 获取内置SD卡路径
     * @return
     */
    public String getInnerSDCardPath() {
        return Environment.getExternalStorageDirectory().getPath();
    }

    /**
     * 获取外置SD卡路径
     * @return  应该就一条记录或空
     */
    public List<String> getExtSDCardPath()
    {
        List<String> lResult = new ArrayList<String>();
        try {
            Runtime rt = Runtime.getRuntime();
            Process proc = rt.exec("mount");
            InputStream is = proc.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains("extSdCard"))
                {
                    String [] arr = line.split(" ");
                    String path = arr[1];
                    File file = new File(path);
                    if (file.isDirectory())
                    {
                        lResult.add(path);
                    }
                }
            }
            isr.close();
        } catch (Exception e) {
        }
        return lResult;
    }

    public static String getExternalCacheDir(Context context) {

       /* if (!isMounted())
            return null;*/

        StringBuilder sb = new StringBuilder();

        File file = context.getExternalCacheDir();

        // In some case, even the sd card is mounted,
        // getExternalCacheDir will return null
        // may be it is nearly full.

        if (file != null) {
            sb.append(file.getAbsolutePath()).append(File.separator);
        } else {
            sb.append(Environment.getExternalStorageDirectory().getPath()).append("/Android/data/").append(context.getPackageName())
                    .append("/cache/").append(File.separator).toString();
        }

        return sb.toString();
    }

}