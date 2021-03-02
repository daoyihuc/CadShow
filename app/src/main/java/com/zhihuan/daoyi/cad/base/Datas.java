package com.zhihuan.daoyi.cad.base;

/**
 * @author: daoyi(yanwen)
 * @Emaill: 1966287146
 * @params: “数据基类”
 * @date :
 */
public  class Datas {

    protected  int x; // 初始化坐标x
    protected  int y; // 初始化坐标y

    public   float[] srcM = new float[8]; // 源数据

    public Datas(int w,int h) {
        this.x = w;
        this.y = h;
    }
    public void setLeftTopx(float a){
        this.srcM[0] = a;
    }
    public void setLeftTopy(float a){
        this.srcM[1] = a;
    }

    public void setRightTopx(float a){
        this.srcM[2] = a;
    }
    public void setRightTopy(float a){
        this.srcM[3] = a;
    }

    public void setRightBottomx(float a){
        this.srcM[4] = a;
    }
    public void setRightBottomy(float a){
        this.srcM[5] = a;
    }

    public void setLeftBottomx(float a){
        this.srcM[6] = a;
    }
    public void setLeftBottomy(float a){
        this.srcM[7] = a;
    }

    public float[] getSrcM() {
        return srcM;
    }

    public void setSrcM(float[] srcM) {
        this.srcM = srcM;
    }
}
