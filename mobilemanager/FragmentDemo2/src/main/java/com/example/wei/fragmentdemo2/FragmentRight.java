package com.example.wei.fragmentdemo2;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/7/29 0029.
 */
public class FragmentRight extends Fragment {

    private TextView tv_title;
    private ImageView iv_pic;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragmentright_layout, null);
        tv_title = (TextView) view.findViewById(R.id.tv_title);
        iv_pic = (ImageView) view.findViewById(R.id.iv_pic);
        return view;
    }
    public void setTitle(String s){
        tv_title.setText(s);
    }
    public void setPic(int resId){
        iv_pic.setImageResource(resId);
    }

}
