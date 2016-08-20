package com.project.wei.mobilemanager.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.project.wei.mobilemanager.R;
import com.project.wei.mobilemanager.bean.BlackNumInfo;
import com.project.wei.mobilemanager.dao.BlackNumDao;
import com.project.wei.mobilemanager.utils.MyAsyncTask;

import java.util.List;

public class CallSmsSafeActivity extends AppCompatActivity {

    private ListView lv_callsmssafe_blacknum;
    private List<BlackNumInfo> list;
    private ProgressBar pb_contactslist_plan;
    private BlackNumDao blackNumDao;
    private MyAdapter myAdapter;
    private AlertDialog dialog;
    private final int maxnum = 20;
    private int startindex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_sms_safe);
        getSupportActionBar().hide();
        lv_callsmssafe_blacknum = (ListView) findViewById(R.id.lv_callsmssafe_blacknum);
        pb_contactslist_plan = (ProgressBar) findViewById(R.id.pb_contactslist_plan);
        //listview滑动监听事件
        lv_callsmssafe_blacknum.setOnScrollListener(new AbsListView.OnScrollListener() {
            //当滑动状态改变的时候调用的方法
            //view : listview
            //scrollState : 滑动状态
            //SCROLL_STATE_IDLE : 空闲的状态
            //SCROLL_STATE_TOUCH_SCROLL : 缓慢滑动的状态
            //SCROLL_STATE_FLING : 快速滑动
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                //当listview静止的时候判断界面显示的最后一个条目是否是查询数据的最后一个条目,
                // 是加载下一波数据,不是用户进行其他操作
                if (scrollState == SCROLL_STATE_IDLE) {
                    //获取界面显示最后一个条目,返回的时候条目的位置
                    int lastVisiblePosition = lv_callsmssafe_blacknum.getLastVisiblePosition();
                    //判断是否是查询数据的最后一个数据
                    if (lastVisiblePosition == list.size() - 1) {
                        //加载下一波数据
                        //更新查询的起始位置
                        startindex+=maxnum;
                        fillData();
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
        fillData();
    }

    private void fillData() {
        /*BlackNumOpenHelper openHelper = new BlackNumOpenHelper(this,"blacknum.db",null,1);
        SQLiteDatabase database = openHelper.getReadableDatabase();*/
        blackNumDao = new BlackNumDao(getApplicationContext());
        new MyAsyncTask() {
            @Override
            public void preTask() {
                pb_contactslist_plan.setVisibility(View.VISIBLE);
            }

            @Override
            public void doinBackGround() {
                if (list == null) {
                    list = blackNumDao.queryPartBlackNum(maxnum, startindex);
                } else {
                    //addAll : 将一个集合整合到另一个集合
                    list.addAll(blackNumDao.queryPartBlackNum(maxnum,startindex));
                }
            }

            @Override
            public void postTask() {
                if (myAdapter == null) {
                    myAdapter = new MyAdapter();
                    lv_callsmssafe_blacknum.setAdapter(myAdapter);
                } else {
                    myAdapter.notifyDataSetChanged();
                }
                pb_contactslist_plan.setVisibility(View.INVISIBLE);
            }
        }.execute();


    }

    class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return list.size();
        }

        //获取条目对应的数据
        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        //获取条目的id
        @Override
        public long getItemId(int position) {
            return position;
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            View view;
            ViewHolder holder;
            if (convertView == null) {
                view = View.inflate(getApplicationContext(), R.layout.item_callsmssafe_blacknum, null);
                //创建控件的容器
                holder = new ViewHolder();
                holder.tv_itemblacknum_number = (TextView) view.findViewById(R.id.tv_itemblacknum_number);
                holder.tv_itemblacknum_mode = (TextView) view.findViewById(R.id.tv_itemblacknum_mode);
                holder.iv_itemblacknum_delete = (ImageView) view.findViewById(R.id.iv_itemblacknum_delete);
                //将容器和view对象绑定在一起
                view.setTag(holder);
            } else {
                view = convertView;
                //从view对象中得到控件的容器
                holder = (ViewHolder) view.getTag();
            }
          /*  TextView tv_itemblacknum_number = (TextView) view.findViewById(R.id.tv_itemblacknum_number);
            TextView tv_itemblacknum_mode = (TextView) view.findViewById(R.id.tv_itemblacknum_mode);
            ImageView iv_itemblacknum_delete = (ImageView) view.findViewById(R.id.iv_itemblacknum_delete);*/

            final BlackNumInfo blackNumInfo = list.get(position);
            holder.tv_itemblacknum_number.setText(blackNumInfo.getBlacknum());
            int mode = blackNumInfo.getMode();
            switch (mode) {
                case 0:
                    holder.tv_itemblacknum_mode.setText("拦截电话");
                    break;
                case 1:
                    holder.tv_itemblacknum_mode.setText("拦截短信");
                    break;
                case 2:
                    holder.tv_itemblacknum_mode.setText("全部拦截");
                    break;
            }
            holder.iv_itemblacknum_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(CallSmsSafeActivity.this);
                    builder.setMessage("你确定要删除"+blackNumInfo.getBlacknum()+"\n这个号码吗？");
                    builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //删除黑名单操作
                            //1.删除数据库中的黑名单号码
                            blackNumDao.deleteBlackNum(blackNumInfo.getBlacknum());
                            //2.删除界面中已经显示黑名单号码
                            //2.1从存放有所有数据的list集合中删除相应的数据
                            list.remove(position);
                            //2.2更新界面
                            myAdapter.notifyDataSetChanged();
                            //3.隐藏对话框
                            dialog.dismiss();
                        }
                    });
                    builder.setNegativeButton("no",null);
                    builder.show();
                }
            });

            return view;
        }
    }
      //存放控件的容器
      class ViewHolder {
          TextView tv_itemblacknum_number;
          TextView tv_itemblacknum_mode;
          ImageView iv_itemblacknum_delete;
      }

    public void addBlackNum(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(CallSmsSafeActivity.this);
        View inflate = View.inflate(getApplicationContext(), R.layout.dialog_addblacknum, null);
        final EditText et_blacknum_mumber = (EditText) inflate.findViewById(R.id.et_blacknum_mumber);
        final RadioGroup rg_modes = (RadioGroup) inflate.findViewById(R.id.rg_modes);
        Button btn_confirm = (Button) inflate.findViewById(R.id.btn_confirm);
        Button btn_cancle = (Button) inflate.findViewById(R.id.btn_cancle);

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //1.获取输入的黑名单号码
                String number = et_blacknum_mumber.getText().toString().trim();
                //2.判断获取的内容是否为空
                if (TextUtils.isEmpty(number)) {
                    Toast.makeText(getApplicationContext(),"请输入号码！",Toast.LENGTH_LONG).show();
                    return;
                }
                //3.获取拦截模式
                int mode = -1;
                int checkedRadioButtonId = rg_modes.getCheckedRadioButtonId();
                switch (checkedRadioButtonId) {
                            case R.id.rb_mode_tel:
                                mode = BlackNumDao.MODE_CALL;
                                break;
                            case R.id.rb_mode_sms:
                                mode = BlackNumDao.MODE_SMS;
                                break;
                            case R.id.rb_mode_all:
                                mode = BlackNumDao.MODE_ALL;
                                break;
                        }
                //4.添加黑名单
                //1.添加到数据库
                blackNumDao.addBlackNum(number,mode);
                //2.添加到界面显示
                //2.1添加到list集合中
                //参数1要添加到位置,参数2:添加的数据
                list.add(0,new BlackNumInfo(number,mode));
                //2.2更新界面
                myAdapter.notifyDataSetChanged();
                //隐藏对话框
                dialog.dismiss();

            }
        });
        btn_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        builder.setView(inflate);
        dialog = builder.create();
        dialog.show();
    }
}
