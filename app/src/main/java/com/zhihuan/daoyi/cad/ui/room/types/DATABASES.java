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
    UPDATE(2,"更新"),
    QUERY(3,"查询"),
    QUERYF(4,"收藏"),
    QUERYR(5,"最近打开"),
    QUERYFN(6,"当前名字是否被收藏"),
    QUERYRP(7,"当前文件是都被打开过");

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
