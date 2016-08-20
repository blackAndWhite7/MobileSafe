package com.project.wei.mobilemanager.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.wei.mobilemanager.R;

/**
 * Created by Administrator on 2016/8/20 0020.
 */
public class SDFragment extends Fragment{
    //初始化操作
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
    }
    //设置fragment的布局
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //参数1:布局文件
        //参数2:容器
        //参数3:自动挂载  ,一律false
        View view= inflater.inflate(R.layout.fragment_sd, container, false);
        return view;
    }
    //设置填充显示数据
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
    }

}
