package com.zhihuan.daoyi.cad.utils;

import android.graphics.Matrix;

/**
 * @author: daoyi(yanwen)
 * @Emaill: 1966287146
 * @params: “details”
 * @date :
 */
public class MatrixUtils {


    /**
     * 存储Matrix矩阵的9个值
     * Matrix是一个3 x 3的矩阵
     */
    private static float[] matrixValues = new float[9];


    /**
     * 获取Matrix在平移缩放之后的X坐标
     * @param x 缩放操作前的X坐标
     * @param matrix 变化的Matrix矩阵
     */
    public static float  getMatrixScaleX(float x, Matrix matrix) {
        matrix.getValues(matrixValues);
        float mscale_x = matrixValues[0];
        float mtrans_x = matrixValues[2];
        if(mtrans_x>0){
            return x * (Math.abs(mscale_x) - 1 );
        }else{
            return x * (Math.abs(mscale_x) + 1) ;
        }

    }

    /**
     * 获取Matrix在平移缩放之后的X坐标
     * @param y 缩放操作前的Y坐标
     * @param matrix 变化的Matrix矩阵
     */
    public static float  getMatrixScaleY(float y, Matrix matrix) {
        matrix.getValues(matrixValues);
        float mscale_y = matrixValues[4];
        float mtrans_y = matrixValues[5];
        if(mtrans_y>0){
            return y * (Math.abs(mscale_y) - 1);
        }else{
            return y * (Math.abs(mscale_y) + 1 );
        }

    }
}
