package com.zhihuan.daoyi.cad.ui.Activitys;

import android.Manifest;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.jaeger.library.StatusBarUtil;
import com.next.easynavigation.constant.Anim;
import com.next.easynavigation.view.EasyNavigationBar;
import com.zhihuan.daoyi.cad.R;
import com.zhihuan.daoyi.cad.base.BaseActivity;
import com.zhihuan.daoyi.cad.databinding.ActivityFirstBinding;
import com.zhihuan.daoyi.cad.databinding.MyFragmentBinding;
import com.zhihuan.daoyi.cad.ui.Fragments.LocalDrawingsFragment;
import com.zhihuan.daoyi.cad.ui.Fragments.MyFragment;
import com.zhihuan.daoyi.cad.ui.Fragments.ShopFragment;
import com.zhihuan.daoyi.cad.ui.Fragments.WebFragment;
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
    int count=0;

    //底部导航栏的文字信息数据
    private String[] tabText = {"本地图纸", "快看云盘","素材商店", "我的"};
    //未选中icon
    private int[] normalIcon = {R.drawable.description,
            R.drawable.wb_cloudy,
            R.drawable.shopping,
            R.drawable.person};
    //选中时icon
    private int[] selectIcon = {R.drawable.a_description,
            R.drawable.a_wb_cloudy,
            R.drawable.a_shopping,
            R.drawable.a_person,};
    private String[] permission;
    {
        permission=new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        MacUtils.initWindow(this,0xffffffff,false,null,true);
        MacUtils.setStatusText(firstActivity.this,true);
        StatusBarUtil.setTranslucentForImageView(firstActivity.this,1,baseBinding.maxBox);
    }

    @Override
    protected ActivityFirstBinding getViewBinding() {
        return ActivityFirstBinding.inflate(getLayoutInflater(),baseBinding.maxBox,true);

    }

    @Override
    protected void initData() {

    }

    protected void init(){
        baseBinding.title.setVisibility(View.VISIBLE);
        fragments.add( new LocalDrawingsFragment());
        fragments.add( new WebFragment());
        fragments.add( new ShopFragment());
        fragments.add( new MyFragment());
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
                        count=position;
                        switch (position){
                            case 0:
                                MacUtils.ClearRootView(firstActivity.this);
                                MacUtils.clearStatus(firstActivity.this);
                                baseBinding.title.setBackgroundColor(0xffffffff);
                                MacUtils.setStatusText(firstActivity.this,true);
//                                MacUtils.initWindow(firstActivity.this,0xffffffff,false,null,true);
                                baseBinding.title.setVisibility(View.VISIBLE);
                                break;
                            case 1:
                                baseBinding.title.setVisibility(View.VISIBLE);
                                MacUtils.ClearRootView(firstActivity.this);
                                MacUtils.clearStatus(firstActivity.this);
                                baseBinding.title.setBackgroundColor(0xffffffff);
                                MacUtils.setStatusText(firstActivity.this,true);
//                                MacUtils.initWindow(firstActivity.this,0xffffffff,false,null,true);
                                break;
                            case 2:
                                baseBinding.title.setVisibility(View.VISIBLE);
                                MacUtils.ClearRootView(firstActivity.this);
                                MacUtils.clearStatus(firstActivity.this);
                                baseBinding.title.setBackgroundColor(0xffffffff);
                                MacUtils.setStatusText(firstActivity.this,true);
//                                MacUtils.setTranslucentStatus(mActivity);
                                break;
                            case 3:
                                MacUtils.ClearRootView(firstActivity.this);
                                MacUtils.clearStatus(firstActivity.this);
                                baseBinding.title.setVisibility(View.VISIBLE);
                                baseBinding.title.setLeftDrawable(null);
                                baseBinding.title.setBackground(firstActivity.this.getResources().getDrawable(R.drawable.my_back_g));
                                StatusBarUtil.setTranslucentForImageView(firstActivity.this,1,baseBinding.maxBox);
                                break;
                        }
                        return false;
                    }
                })
                .build();
        getPermissions(permission);
    }

    @Override
    protected void onResume() {
        super.onResume();
        switch (count){
            case 0:
                MacUtils.ClearRootView(firstActivity.this);
                MacUtils.clearStatus(firstActivity.this);
                baseBinding.title.setBackgroundColor(0xffffffff);
                MacUtils.setStatusText(firstActivity.this,true);
//                                MacUtils.initWindow(firstActivity.this,0xffffffff,false,null,true);
                baseBinding.title.setVisibility(View.VISIBLE);
                break;
            case 1:
                baseBinding.title.setVisibility(View.VISIBLE);
                MacUtils.ClearRootView(firstActivity.this);
                MacUtils.clearStatus(firstActivity.this);
                baseBinding.title.setBackgroundColor(0xffffffff);
                MacUtils.setStatusText(firstActivity.this,true);
//                                MacUtils.initWindow(firstActivity.this,0xffffffff,false,null,true);
                break;
            case 2:
                baseBinding.title.setVisibility(View.VISIBLE);
                MacUtils.ClearRootView(firstActivity.this);
                MacUtils.clearStatus(firstActivity.this);
                baseBinding.title.setBackgroundColor(0xffffffff);
                MacUtils.setStatusText(firstActivity.this,true);
                break;
            case 3:
                MacUtils.ClearRootView(firstActivity.this);
                MacUtils.clearStatus(firstActivity.this);
                baseBinding.title.setVisibility(View.VISIBLE);
                baseBinding.title.setLeftDrawable(null);
                baseBinding.title.setBackground(firstActivity.this.getResources().getDrawable(R.drawable.my_back_g));
                StatusBarUtil.setTranslucentForImageView(firstActivity.this,1,baseBinding.maxBox);
                break;
        }
    }
}
