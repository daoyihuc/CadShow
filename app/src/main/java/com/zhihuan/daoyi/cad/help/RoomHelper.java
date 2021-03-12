package com.zhihuan.daoyi.cad.help;

import androidx.room.Room;
import androidx.room.RoomDatabase;

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
                AppDatabase.class, "database-cad").build();
        return d;
    }


}
