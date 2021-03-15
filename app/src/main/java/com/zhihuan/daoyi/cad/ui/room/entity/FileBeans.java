package com.zhihuan.daoyi.cad.ui.room.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

/**
 * @author: daoyi(yanwen)
 * @Emaill: 1966287146
 * @params: “details”
 * @date :
 */
@Entity
public class FileBeans extends BaseF{
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String name; // 名字
    public int isFavorites; // 是否收藏 0: No 1:yes
    public String time; // 时间
    public int type; // 文件夹or文件 0: 文件夹 1: 文件
    public String path; // 路劲
    public int p_type;// 图片类型 0: png 1:jpg
    public int isRecent;// 是否最近打开 1: 最近打开

    @Ignore
    public int getId() {
        return id;
    }
    @Ignore
    public void setId(int id) {
        this.id = id;
    }
    @Ignore
    public String getName() {
        return name;
    }
    @Ignore
    public void setName(String name) {
        this.name = name;
    }
    @Ignore
    public int getIsFavorites() {
        return isFavorites;
    }
    @Ignore
    public void setIsFavorites(int isFavorites) {
        this.isFavorites = isFavorites;
    }
    @Ignore
    public String getTime() {
        return time;
    }
    @Ignore
    public void setTime(String time) {
        this.time = time;
    }
    @Ignore
    public int getType() {
        return type;
    }
    @Ignore
    public void setType(int type) {
        this.type = type;
    }
    @Ignore
    public String getPath() {
        return path;
    }
    @Ignore
    public void setPath(String path) {
        this.path = path;
    }
    @Ignore
    public int getP_type() {
        return p_type;
    }
    @Ignore
    public void setP_type(int p_type) {
        this.p_type = p_type;
    }
    @Ignore
    public int getIsRecent() {
        return isRecent;
    }
    @Ignore
    public void setIsRecent(int isRecent) {
        this.isRecent = isRecent;
    }

    @Override
    public String toString() {
        return "FileBeans{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", isFavorites=" + isFavorites +
                ", time='" + time + '\'' +
                ", type=" + type +
                ", path='" + path + '\'' +
                ", p_type=" + p_type +
                ", isRecent=" + isRecent +
                '}';
    }
}
