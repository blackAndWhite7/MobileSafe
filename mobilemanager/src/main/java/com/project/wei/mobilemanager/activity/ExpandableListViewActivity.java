package com.project.wei.mobilemanager.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.project.wei.mobilemanager.R;

public class ExpandableListViewActivity extends AppCompatActivity {

    private ExpandableListView exlitview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expandable_list_view);
        getSupportActionBar().hide();
        exlitview = (ExpandableListView) findViewById(R.id.exlitview);
        exlitview.setAdapter(new MyAdapter());
    }
    private class MyAdapter extends BaseExpandableListAdapter {
        //获取组的个数
        @Override
        public int getGroupCount() {
            // TODO Auto-generated method stub
            return 8;
        }
        //获取每组孩子的个数
        @Override
        public int getChildrenCount(int groupPosition) {
            // TODO Auto-generated method stub
            return groupPosition+1;
        }
        //设置组对应的相应的信息
        @Override
        public Object getGroup(int groupPosition) {
            // TODO Auto-generated method stub
            return null;
        }
        //设置每组孩子对应的信息
        //groupPosition ：组的位置
        //childPosition : 孩子的位置
        @Override
        public Object getChild(int groupPosition, int childPosition) {
            // TODO Auto-generated method stub
            return null;
        }
        //获取组的id
        @Override
        public long getGroupId(int groupPosition) {
            // TODO Auto-generated method stub
            return 0;
        }
        //获取每组孩子的id
        @Override
        public long getChildId(int groupPosition, int childPosition) {
            // TODO Auto-generated method stub
            return 0;
        }
        //获取id是否稳定,有没有设置id,有设置,返回true,没有设置返回false
        @Override
        public boolean hasStableIds() {
            // TODO Auto-generated method stub
            return false;
        }
        //设置组的样式
        @Override
        public View getGroupView(int groupPosition, boolean isExpanded,
                                 View convertView, ViewGroup parent) {
            TextView textView = new TextView(getApplicationContext());
            textView.setText("         我是第"+groupPosition+"组");
            textView.setTextSize(40);
            textView.setTextColor(Color.RED);
            return textView;
        }
        //设置每组孩子样式
        @Override
        public View getChildView(int groupPosition, int childPosition,
                                 boolean isLastChild, View convertView, ViewGroup parent) {

            TextView textView = new TextView(getApplicationContext());
            textView.setTextSize(30);
            textView.setText("我是第"+groupPosition+"组的第"+childPosition+"个孩子");
            textView.setTextColor(Color.GREEN);
            return textView;
        }
        //设置孩子是否能够被点击,true:可以   false:不可以
        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            // TODO Auto-generated method stub
            return false;
        }

    }
}
