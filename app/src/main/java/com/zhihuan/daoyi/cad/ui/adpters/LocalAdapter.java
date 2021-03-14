package com.zhihuan.daoyi.cad.ui.adpters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;

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

    public LocalAdapter(@NonNull FragmentManager fm,List<Fragment> li) {
        super(fm);

        if(list!=null&&list.size()<=0){
            list.addAll(li);
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
}
