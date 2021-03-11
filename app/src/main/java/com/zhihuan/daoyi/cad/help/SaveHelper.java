package com.zhihuan.daoyi.cad.help;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import com.zhihuan.daoyi.cad.views.DragFrameLayoutView;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author: daoyi(yanwen)
 * @Emaill: 1966287146
 * @params: “details”
 * @date :
 */
public class SaveHelper {

    private SaveHelper(){

    }

    private static class  SaveHelperInstance{
        private  static final SaveHelper SAVE_HELPER=new SaveHelper();
    }
    public static SaveHelper newInstance(){
        return SaveHelperInstance.SAVE_HELPER;
    }

    public  String FileName(Context context){

        Log.e("daoyi_fileState",Environment.getExternalStorageState());
        if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
            Log.e("daoyi_filePath",""+Environment.getExternalStorageDirectory());
            File file=new File(Environment.getExternalStorageDirectory().getAbsolutePath());
            File[] files = file.listFiles();
            for (File file1 : files) {
                Log.e("daoyi_fileName",""+file1.getName());
            }

        }
        return "";
    }

    // 创建本地缓存文件

    public  void createCache(){
        String CachFileName=Environment.getExternalStorageDirectory().getAbsolutePath()+"/CadShow";
        File CachFile = new File(CachFileName);
        if(!CachFile.exists()){
            CachFile.mkdirs();
        }
    }
    public  String getCachePath(){
        String CachFileName=Environment.getExternalStorageDirectory().getAbsolutePath()+"/CadShow";
        return CachFileName;
    }

    public  int SaveBitmap(Bitmap drawingCache, String FileName){

        File file=new File(FileName);
        int stutas = 0;
        BufferedOutputStream outputStream = null;
        try {
            outputStream=new BufferedOutputStream(new FileOutputStream(file));
            if(drawingCache!=null){
                Bitmap bitmap = Bitmap.createBitmap( drawingCache,0, 0,drawingCache.getWidth(), drawingCache.getHeight());
                bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
                stutas=1;
            }else{
                stutas=0;
            }
            outputStream.flush();
            outputStream.close();
            return stutas;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stutas;
    }

}
