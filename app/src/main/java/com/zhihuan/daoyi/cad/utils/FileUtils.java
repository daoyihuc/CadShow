package com.zhihuan.daoyi.cad.utils;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * @author: daoyi(yanwen)
 * @Emaill: 1966287146
 * @params: “details”
 * @date :
 */
public class FileUtils {

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static Long getFileCreateTime(String filePath){
        File file = new File(filePath);
        try {
            Path path= Paths.get(filePath);
            BasicFileAttributeView basicview= Files.getFileAttributeView(path, BasicFileAttributeView.class, LinkOption.NOFOLLOW_LINKS );
            BasicFileAttributes attr = basicview.readAttributes();
            return attr.creationTime().toMillis();
        } catch (Exception e) {
            e.printStackTrace();
            return file.lastModified();
        }
    }
}
