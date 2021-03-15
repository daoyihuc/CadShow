package com.zhihuan.daoyi.cad.help;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.zhihuan.daoyi.cad.base.CadApplication;
import com.zhihuan.daoyi.cad.ui.room.AppDatabase;

/**
 * @author: daoyi(yanwen)
 * @Emaill: 1966287146
 * @params: “details”
 * @date :
 */
public class RoomHelper {

    private static class SingleInstance{
        public static final RoomHelper ROOM_HELPER=new RoomHelper();
    }

    private RoomHelper(){

    }
    public static RoomHelper newInstance(){
        return SingleInstance.ROOM_HELPER;
    }
    public AppDatabase db(){
        AppDatabase d = Room.databaseBuilder(CadApplication.aContext(),
                AppDatabase.class, "database-cad")
//                .fallbackToDestructiveMigration()//数据库更新时删除数据重新创建
//                .addMigrations(MIGRATION__2)
                .build();
        return d;
    }
    /**
     * 版本2-3的迁移策略
     * 构造方法需传 开始版本号2 与 截止版本号3
     */
  static final Migration MIGRATION__2 = new Migration(2,3) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            //为device表增加一列
            database.execSQL("ALTER TABLE FileBeans ADD COLUMN isRecent INTEGER NOT NULL DEFAULT 0  ");
        }
    };


}
