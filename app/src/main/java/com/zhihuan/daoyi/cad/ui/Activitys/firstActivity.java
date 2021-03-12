package com.zhihuan.daoyi.cad.ui.Activitys;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.next.easynavigation.constant.Anim;
import com.next.easynavigation.view.EasyNavigationBar;
import com.zhihuan.daoyi.cad.R;
import com.zhihuan.daoyi.cad.base.BaseActivity;
import com.zhihuan.daoyi.cad.databinding.ActivityFirstBinding;
import com.zhihuan.daoyi.cad.ui.Fragments.LocalDrawingsFragment;
import com.zhihuan.daoyi.cad.utils.MacUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: daoyi(yanwen)
 * @Emaill: 1966287146
 * @params: “details”
 * @date :
 */
public class firstActivity  extends BaseActivity<ActivityFirstBinding> {



    private List<Fragment> fragments = new ArrayList<>();//存放Fragment的集合
    private Activity mActivity;

    //底部导航栏的文字信息数据
    private String[] tabText = {"本地图纸", "快看云盘", "我的"};
    //未选中icon
    private int[] normalIcon = {R.drawable.description,
            R.drawable.wb_cloudy,
            R.drawable.person};
    //选中时icon
    private int[] selectIcon = {R.drawable.a_description,
            R.drawable.a_wb_cloudy,
            R.drawable.a_person,};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MacUtils.initWindow(this,0xffffffff,false,null,true);
        init();
    }

    @Override
    protected ActivityFirstBinding getViewBinding() {
        return ActivityFirstBinding.inflate(getLayoutInflater(),baseBinding.getRoot(),true);

    }

    private void init(){
        fragments.add( new LocalDrawingsFragment());
        fragments.add( new LocalDrawingsFragment());
        fragments.add( new LocalDrawingsFragment());
        viewBinding.navigationBar.titleItems(tabText)//添加文字
                .tabTextSize(10)//Tab文字大小
                .tabTextTop(2) //Tab文字距Tab图标的距离
                .normalTextColor(Color.parseColor("#222222"))//Tab未选中时字体颜色
                .selectTextColor(Color.parseColor("#555555"))//Tab选中时字体颜色
                .navigationHeight(50)//导航栏高度
                .lineHeight(1) //分割线高度  默认1px
                .lineColor(Color.parseColor("#EBEBEB"))//分割线的颜色
                .scaleType(ImageView.ScaleType.FIT_CENTER)  //同 ImageView的ScaleType

                .navigationBackground(Color.parseColor("#FFFFFF")) //导航栏背景色
                .onTabClickListener(new EasyNavigationBar.OnTabClickListener(){   //Tab点击事件  return true 页面不会切换
                    @Override
                    public boolean onTabClickEvent(View view, int position) {
                        return false;
                    }
                })
                .smoothScroll(false)//点击Tab  Viewpager切换是否有动画
                .canScroll(false)//Viewpager能否左右滑动
                .anim(Anim.BounceIn)//点击Tab时的动画ZoomIn BounceIn
                .normalIconItems(normalIcon)//未选中时icon
                .selectIconItems(selectIcon)//选中时icon
                .fragmentList(fragments)
                .fragmentManager(getSupportFragmentManager())
                .onTabClickListener(new EasyNavigationBar.OnTabClickListener() {
                    @Override
                    public boolean onTabClickEvent(View view, int position) {
                        Log.e("MainActivity","当前界面"+position);
                        switch (position){
                            case 0:

                                break;
                            case 1:

                                break;
                            case 2:
//                                MacUtils.setTranslucentStatus(mActivity);
                                break;
                            case 3:
                                break;
                        }
                        return false;
                    }
                })
                .build();
    }



}
