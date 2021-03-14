package com.zhihuan.daoyi.cad.ui.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.zhihuan.daoyi.cad.ui.room.daoyi.FileDao;
import com.zhihuan.daoyi.cad.ui.room.entity.FileBeans;

/**
 * @author: daoyi(yanwen)
 * @Emaill: 1966287146
 * @params: “details”
 * @date :
 */

@Database(entities = {FileBeans.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract FileDao fileDao();
}
