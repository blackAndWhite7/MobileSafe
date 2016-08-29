package com.example.wei.fragmentdemo2;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/7/29 0029.
 */
public class FragmentLeft extends Fragment {
    FragmentRight fragmentRight;

    ArrayList<String> title = new ArrayList<String>();
    public void addTitle(){
        for(int i=1;i<=30;i++){
            String s = "新闻"+i;
            title.add(s);
        }
    }
    ArrayList<Integer> pic = new ArrayList<Integer>();
    public void addPic(){
        for(int i=1;i<=30;i++){
            if(i%2==0) {
                Integer integer = new Integer(R.drawable.a1);
                pic.add(integer);
            }else {
                Integer integer = new Integer(R.drawable.a2);
                pic.add(integer);
            }
        }
    }
    public void setFragmentRight(FragmentRight fragmentRight){
        this.fragmentRight = fragmentRight;
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragmentleft_layout, null);
        ListView lv_left = (ListView) view.findViewById(R.id.lv_left);
        lv_left.setAdapter(new MyAdapter());

//     初始化源数据
        addTitle();
        addPic();

        lv_left.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                fragmentRight.setTitle(title.get(position));
                fragmentRight.setPic(pic.get(position));
            }
        });
        return  view;
    }
    class  MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return title.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(getActivity(), R.layout.item_list, null);
            ImageView iv_item = (ImageView) view.findViewById(R.id.iv_item);
            TextView tv_item = (TextView) view.findViewById(R.id.tv_item);
            iv_item.setImageResource(R.drawable.c);
            tv_item.setText("新闻"+(position+1));
            return view;
        }
    }
}
