package com.zhihuan.daoyi.cad.ui.room.entity;

import androidx.room.Ignore;

/**
 * @author: daoyi(yanwen)
 * @Emaill: 1966287146
 * @params: “details”
 * @date :
 */
public class BaseF {
    @Ignore
    private  int code;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
