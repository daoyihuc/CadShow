package com.zhihuan.daoyi.cad.ui.adpters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;

import com.zhihuan.daoyi.cad.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: daoyi(yanwen)
 * @Emaill: 1966287146
 * @params: “details”
 * @date :
 */
public class LocalAdapter extends FragmentStatePagerAdapter {

    List<Fragment> list=new ArrayList<>();
    String[] stringArray;
    public LocalAdapter(@NonNull FragmentManager fm,List<Fragment> li,String[] titles) {
        super(fm);

        if(list!=null&&list.size()<=0){
            list.addAll(li);
            this.stringArray = titles;
        }
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return stringArray[position];
    }
}
