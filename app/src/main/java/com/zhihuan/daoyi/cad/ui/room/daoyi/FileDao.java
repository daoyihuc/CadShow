package com.zhihuan.daoyi.cad.ui.room.daoyi;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.zhihuan.daoyi.cad.ui.adpterBean.FileBean;
import com.zhihuan.daoyi.cad.ui.room.entity.FileBeans;

import java.util.List;

/**
 * @author: daoyi(yanwen)
 * @Emaill: 1966287146
 * @params: “details”
 * @date :
 */
@Dao
public interface FileDao {
    @Insert
    void insert(FileBeans fileBean);

    @Delete
    void delete(FileBeans fileBeans);

    @Query("UPDATE FileBeans SET isFavorites= :f  WHERE path = :path")
    void update(int f,String path);

    @Query("select * from FileBeans") // 查询所有数据
    LiveData<List<FileBeans>> queryAll();

    @Query("select * from FileBeans where isFavorites= :favor") // 查询收藏数据
    LiveData<List<FileBeans>> queryF(int favor);

    @Query("select count(*) from FileBeans where path= :path ") // 查询当前路劲是否有收藏
    int queryFN(String path);

    @Query("select * from FileBeans where isRecent= :Recent") // 查询收藏数据
    LiveData<List<FileBeans>> queryR(int Recent);

    @Query("select * from FileBeans where isRecent= :Recent and path= :path") // 查询收藏数据
    List<FileBeans> queryRP(int Recent,String path);

}
