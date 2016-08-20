package com.project.wei.mobilemanager.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.project.wei.mobilemanager.R;
import com.project.wei.mobilemanager.fragment.CacheFragment;
import com.project.wei.mobilemanager.fragment.SDFragment;

public class ClearCacheActivity extends FragmentActivity {

    private CacheFragment cacheFragment;
    private SDFragment sdFragment;
    private FragmentManager supportFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clear_cache);

        //挂载fragment
        //1.获取fragment
        cacheFragment = new CacheFragment();
        sdFragment = new SDFragment();
        //2.加载fragment
        //2.1获取fragment的管理者
        supportFragmentManager = getSupportFragmentManager();
        //2.2开启事务
        FragmentTransaction beginTransaction = supportFragmentManager.beginTransaction();
        //2.3添加fragment操作
        //数据不会重新刷新
        //参数1:fragment要替换的控件的id
        //参数2:要替换fragment
        beginTransaction.add(R.id.fram_clearcache_fragment, cacheFragment);
        //界面展示以最后一个添加的为准
        beginTransaction.add(R.id.fram_clearcache_fragment, sdFragment);
        //隐藏fragment操作
        beginTransaction.hide(sdFragment);
        //也可以实现替换操作,数据会重新刷新
        //beginTransaction.replace(arg0, arg1)
        //2.4提交事务
        beginTransaction.commit();
    }
    //缓存清理
    public void cache(View v){
        //隐藏sdfragment,显示cacheFragment
        //1.获取事务
        FragmentTransaction beginTransaction = supportFragmentManager.beginTransaction();
        //2.隐藏显示操作
        beginTransaction.hide(sdFragment);//隐藏fragmnt
        beginTransaction.show(cacheFragment);//显示fragment操作
        //3.提交
        beginTransaction.commit();
    }
    //SD卡清理
    public void sd(View v){
        //隐藏cachefragment,显示sdfragment
        //1.获取事务
        FragmentTransaction beginTransaction = supportFragmentManager.beginTransaction();
        //2.隐藏显示操作
        beginTransaction.hide(cacheFragment);//隐藏fragmnt
        beginTransaction.show(sdFragment);//显示fragment操作
        //3.提交
        beginTransaction.commit();
    }

}
