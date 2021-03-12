package com.zhihuan.daoyi.cad.ui.room.types;

/**
 * @author: daoyi(yanwen)
 * @Emaill: 1966287146
 * @params: “details”
 * @date :
 */
public enum DATABASES {

    INSERT(0,"添加"),
    DELETE(1,"删除"),
    UPDATE(2,"添加"),
    QUERY(3,"添加");

    private int type;
    private String name;

    private DATABASES(int a,String b){
        this.type=a;
        this.name=b;
    }


    /**
     * 根据传入的state返回相应的enum值
     * @param state
     * @return
     */
    public static DATABASES stateOf(int state) {
        for (DATABASES stateEnum : values()) {
            if (stateEnum.getType() == state) {
                return stateEnum;
            }
        }
        return null;
    }


    public int getType() {
        return type;
    }


    public String getName() {
        return name;
    }

}
