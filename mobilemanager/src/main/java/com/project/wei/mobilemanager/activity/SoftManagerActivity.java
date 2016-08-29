package com.project.wei.mobilemanager.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Formatter;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.project.wei.mobilemanager.R;
import com.project.wei.mobilemanager.bean.AppInfo;
import com.project.wei.mobilemanager.dao.WatchDogDao;
import com.project.wei.mobilemanager.engine.AppEngine;
import com.project.wei.mobilemanager.utils.DensityUtil;
import com.project.wei.mobilemanager.utils.FreeSpaceUtil;
import com.project.wei.mobilemanager.utils.MyAsyncTask;

import java.util.ArrayList;
import java.util.List;


public class SoftManagerActivity extends AppCompatActivity implements View.OnClickListener{
    private ListView lv_softmanager_application;
    private ProgressBar loading;
    private List<AppInfo> list;
    //存放用户程序
    private List<AppInfo> userappinfo;
    //存放系统程序
    private List<AppInfo> systemappinfo;
    private TextView tv_softmanager_userorsystem;
    private AppInfo appInfo;
    private PopupWindow popupWindow;
    private Myadapter myadapter;
    private TextView tv_softmanager_rom;
    private TextView tv_softmanager_sd;
    private WatchDogDao watchDogDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soft_manager);
        getSupportActionBar().hide();

        watchDogDao = new WatchDogDao(this);
        //初始化控件
        lv_softmanager_application = (ListView) findViewById(R.id.lv_softmanager_application);
        loading = (ProgressBar) findViewById(R.id.loading);
        tv_softmanager_userorsystem = (TextView) findViewById(R.id.tv_softmanager_userorsystem);

        tv_softmanager_rom = (TextView) findViewById(R.id.tv_softmanager_rom);
        tv_softmanager_sd = (TextView) findViewById(R.id.tv_softmanager_sd);
        //获取可用内存,获取都是kb
        long availSpaceSDCard = FreeSpaceUtil.getAvailSpaceSDCard();
        long availSpaceROM = FreeSpaceUtil.getAvailSpaceROM();
        //数据转化
        String sdsize = Formatter.formatFileSize(getApplicationContext(), availSpaceSDCard);
        String romsize = Formatter.formatFileSize(getApplicationContext(), availSpaceROM);
        //设置显示
        tv_softmanager_sd.setText("SD卡可用:"+sdsize);
        tv_softmanager_rom.setText("内存可用:"+romsize);

        //加载数据
        fillData();
        listviewOnscroll();
        listviewItemClick();
        listviewItemLongClick();
    }

    private void listviewItemLongClick() {
        lv_softmanager_application.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //屏蔽这两个item
                if (position == 0 || position == userappinfo.size() + 1) {
                    return true;
                }
                //获取数据
                if (position <= userappinfo.size()) {
                    appInfo = userappinfo.get(position - 1);
                } else {
                    appInfo = systemappinfo.get(position - userappinfo.size() - 2);
                }
                ViewHolder viewHolder = (ViewHolder) view.getTag();
                //判断应用有没有加锁，有的话解锁，没有的话加锁
                if (watchDogDao.queryLockApp(appInfo.getPackagName())) {
                    //解锁
                    watchDogDao.deleteLockApp(appInfo.getPackagName());
                    viewHolder.iv_itemsoftmanager_islock.setImageResource(R.drawable.unlock);
                } else {
                    //加锁
                    //如果是当前应用程序，就不要加锁了
                    if (!appInfo.getPackagName().equals(getPackageName())) {
                        watchDogDao.addLockApp(appInfo.getPackagName());
                        viewHolder.iv_itemsoftmanager_islock.setImageResource(R.drawable.lock);
                    }
                }

                return true;
            }
        });
    }

    /**
     * 条目点击事件
     */
    private void listviewItemClick() {
        lv_softmanager_application.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            //view : 条目的view对象
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //弹出气泡
                //1.屏蔽用户程序和系统程序(...个)弹出气泡
                if (position == 0 || position == userappinfo.size()+1) {
                    return;
                }
                //2.获取条目所对应的应用程序的信息
                //数据就要从userappinfo和systemappinfo中获取
                if (position <= userappinfo.size()) {
                    //用户程序
                    appInfo = userappinfo.get(position-1);
                }else{
                    //系统程序
                    appInfo = systemappinfo.get(position - userappinfo.size() - 2);
                }
                //5.弹出新的气泡之前,删除旧 的气泡
                hidePopuwindow();
                //3.弹出气泡
				/*TextView contentView = new TextView(getApplicationContext());
				contentView.setText("我是popuwindow的textview控件");
				contentView.setBackgroundColor(Color.RED);*/
                View contentView = View.inflate(getApplicationContext(), R.layout.popu_window, null);

                //初始化控件
                LinearLayout ll_popuwindow_uninstall = (LinearLayout) contentView.findViewById(R.id.ll_popuwindow_uninstall);
                LinearLayout ll_popuwindow_start = (LinearLayout) contentView.findViewById(R.id.ll_popuwindow_start);
                LinearLayout ll_popuwindow_share = (LinearLayout) contentView.findViewById(R.id.ll_popuwindow_share);
                LinearLayout ll_popuwindow_detail = (LinearLayout) contentView.findViewById(R.id.ll_popuwindow_detail);
                //给控件设置点击事件
                ll_popuwindow_uninstall.setOnClickListener(SoftManagerActivity.this);
                ll_popuwindow_start.setOnClickListener(SoftManagerActivity.this);
                ll_popuwindow_share.setOnClickListener(SoftManagerActivity.this);
                ll_popuwindow_detail.setOnClickListener(SoftManagerActivity.this);

                //contentView : 显示view对象
                //width,height : view宽高
                popupWindow = new PopupWindow(contentView, WindowManager.LayoutParams.WRAP_CONTENT,
                        WindowManager.LayoutParams.WRAP_CONTENT);
                //动画要想执行,执行的控件必须有背景,动画都是基于背景来进行一些计算,
                // 没有背景动画是无法执行,popuwindow默认是没有设置背景
                popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));//设置透明的背景
                //4.获取条目的位置,让气泡显示在相应的条目
                int[] location = new int[2];//保存x和y坐标的数组
                view.getLocationInWindow(location);//获取条目x和y的坐标,同时保存到int[]
                //获取x和y的坐标
                int x = location[0];
                int y = location[1];
                //parent : 要挂载在那个控件上
                //gravity,x,y : 控制popuwindow显示的位置
                popupWindow.showAtLocation( parent , Gravity.LEFT | Gravity.TOP,
                        x+ DensityUtil.dip2px(getApplicationContext(),65), y);

                //6.设置动画
                //缩放动画
                //前四个 :　控制控件由没有变到有   动画 0:没有    1:整个控件
                //后四个:控制控件是按照自身还是父控件进行变化
                //RELATIVE_TO_SELF : 以自身变化
                //RELATIVE_TO_PARENT : 以父控件变化
                ScaleAnimation scaleAnimation = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0.5f);
                scaleAnimation.setDuration(500);

                //渐变动画
                AlphaAnimation alphaAnimation = new AlphaAnimation(0.2f, 1.0f);//由半透明变成不透明
                alphaAnimation.setDuration(500);

                //组合动画
                //shareInterpolator : 是否使用相同的动画插补器  true:共享    false:各自使用各自的
                AnimationSet animationSet = new AnimationSet(true);
                //添加动画
                animationSet.addAnimation(scaleAnimation);
                animationSet.addAnimation(alphaAnimation);
                //执行动画  popupWindow无法执行动画
                contentView.startAnimation(animationSet);

            }

        });
    }
    /**
     * 隐藏气泡
     */
    private void hidePopuwindow() {
        if (popupWindow != null) {
            popupWindow.dismiss();//隐藏气泡
            popupWindow = null;
        }
    }
    /**
     * listview滑动监听事件
     */
    private void listviewOnscroll() {
        lv_softmanager_application.setOnScrollListener(new AbsListView.OnScrollListener() {
            //滑动状态改变的时候调用
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // TODO Auto-generated method stub

            }
            //滑动的时候调用
            //view : listview
            //firstVisibleItem : 界面第一个显示条目
            //visibleItemCount : 显示条目总个数
            //totalItemCount : 条目的总个数
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                //隐藏气泡
                hidePopuwindow();
                //为null的原因:listview在初始化的时候就会调用onScroll方法
                if (userappinfo != null && systemappinfo != null) {
                    if (firstVisibleItem >= userappinfo.size()+1) {
                        tv_softmanager_userorsystem.setText("系统程序("+systemappinfo.size()+")");
                    }else{
                        tv_softmanager_userorsystem.setText("用户程序("+userappinfo.size()+")");
                    }
                }
            }
        });
    }
    /**
     * 加载数据
     */
    private void fillData() {
        new MyAsyncTask() {

            @Override
            public void preTask() {
                loading.setVisibility(View.VISIBLE);
            }

            @Override
            public void postTask() {
                if (myadapter == null) {
                    myadapter = new Myadapter();
                    lv_softmanager_application.setAdapter(myadapter);
                } else {
                    myadapter.notifyDataSetChanged();
                }
                loading.setVisibility(View.INVISIBLE);
            }

            @Override
            public void doinBackGround() {
                list = AppEngine.getAppInfos(getApplicationContext());
                userappinfo  = new ArrayList<AppInfo>();
                systemappinfo  = new ArrayList<AppInfo>();
                //将数据分别存放到用户程序集合和系统程序集合中
                for (AppInfo appinfo : list) {
                    if (appinfo.isUser()) {
                        userappinfo.add(appinfo);
                    } else {
                        systemappinfo.add(appinfo);
                    }
                }
            }
        }.execute();
    }

    private class Myadapter extends BaseAdapter {

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return list.size()+2;
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (position == 0) {
                TextView textView = new TextView(getApplicationContext());
                textView.setText("用户程序（"+userappinfo.size()+")个");
                textView.setTextSize(15);
                textView.setTextColor(Color.BLUE);
                return textView;
            }
            if (position == userappinfo.size() + 1) {
                TextView textView = new TextView(getApplicationContext());
                textView.setText("系统程序（"+systemappinfo.size()+")个");
                textView.setTextSize(15);
                textView.setTextColor(Color.BLUE);
                return textView;
            }
            View view;
            ViewHolder viewHolder;
            //复用的时候判断复用view对象的类型,因为有可能是TextView
            if (convertView != null && convertView instanceof RelativeLayout) {
                view = convertView;
                viewHolder = (ViewHolder) view.getTag();
            }else{
                view = View.inflate(getApplicationContext(), R.layout.item_softmanager, null);
                viewHolder = new ViewHolder();
                viewHolder.iv_itemsoftmanage_icon = (ImageView) view.findViewById(R.id.iv_itemsoftmanage_icon);
                viewHolder.tv_softmanager_name = (TextView) view.findViewById(R.id.tv_softmanager_name);
                viewHolder.tv_softmanager_issd = (TextView) view.findViewById(R.id.tv_softmanager_issd);
                viewHolder.tv_softmanager_version = (TextView) view.findViewById(R.id.tv_softmanager_version);
                viewHolder.iv_itemsoftmanager_islock = (ImageView) view.findViewById(R.id.iv_itemsoftmanager_islock);
                //将view和viewholder对象绑定
                view.setTag(viewHolder);
            }

            //1.获取应用程序的信息
            AppInfo appInfo ;
            //数据就要从userappinfo和systemappinfo中获取
            if (position <= userappinfo.size()) {
                //用户程序
                appInfo = userappinfo.get(position-1);
            } else {
                //系统程序
                appInfo = systemappinfo.get(position-userappinfo.size()-2);
            }
            //设置显示数据
            viewHolder.iv_itemsoftmanage_icon.setImageDrawable(appInfo.getIcon());
            viewHolder.tv_softmanager_name.setText(appInfo.getName());
            boolean sd = appInfo.isSD();
            if (sd) {
                viewHolder.tv_softmanager_issd.setText("SD卡");
            }else{
                viewHolder.tv_softmanager_issd.setText("手机内存");
            }
            viewHolder.tv_softmanager_version.setText(appInfo.getVersionName());
            //判断应用程序是加锁还是解锁
            if (watchDogDao.queryLockApp(appInfo.getPackagName())) {
                //加锁
                viewHolder.iv_itemsoftmanager_islock.setImageResource(R.drawable.lock);
            } else {
                //解锁
                viewHolder.iv_itemsoftmanager_islock.setImageResource(R.drawable.unlock);
            }
            return view;
        }

    }

    static class ViewHolder{
        ImageView iv_itemsoftmanage_icon,iv_itemsoftmanager_islock;
        TextView tv_softmanager_name,tv_softmanager_issd,tv_softmanager_version;
    }
    @Override
    public void onClick(View v) {
         //判断点击是按个按钮
        //getId() : 获取点击按钮的id
        switch (v.getId()) {
            case R.id.ll_popuwindow_uninstall:
                System.out.println("卸载");
                uninstall();
                break;
            case R.id.ll_popuwindow_start:
                System.out.println("启动");
                start();
                break;
            case R.id.ll_popuwindow_share:
                System.out.println("分享");
                share();
                break;
            case R.id.ll_popuwindow_detail:
                System.out.println("详情");
                detail();
                break;
        }
        hidePopuwindow();
    }

    private void uninstall() {
        /**
         * <intent-filter>
         <action android:name="android.intent.action.VIEW" />
         <action android:name="android.intent.action.DELETE" />
         <category android:name="android.intent.category.DEFAULT" />
         <data android:scheme="package" />
         </intent-filter>
         */
        //判断是否是系统程序
        if (appInfo.isUser()) {
            //判断是否是我们自己的应用,是不能卸载
            if (!appInfo.getPackagName().equals(getPackageName())) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.DELETE");
                intent.addCategory("android.intent.category.DEFAULT");
                //////////////////////////////////////////////////////////////////////////////
                intent.setData(Uri.parse("package:"+appInfo.getPackagName()));//tel:110
                startActivityForResult(intent,0);
            }else{
                Toast.makeText(getApplicationContext(), "文明社会,杜绝自杀", Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(getApplicationContext(), "要想卸载系统程序,请先root", Toast.LENGTH_LONG).show();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        fillData();
    }

    private void start() {
        PackageManager packageManager = getPackageManager();
        //获取应用程序的启动意图
        Intent intent = packageManager.getLaunchIntentForPackage(appInfo.getPackagName());
        if (intent != null) {
            startActivity(intent);
        } else {
            Toast.makeText(getApplicationContext(),"核心程序，无法启动！",Toast.LENGTH_LONG).show();
        }
    }

    private void share() {
        /**
         *  Intent
         {
         act=android.intent.action.SEND
         typ=text/plain
         flg=0x3000000
         cmp=com.android.mms/.ui.ComposeMessageActivity (has extras)   intent中包含信息
         } from pid 228
         */
        Intent intent = new Intent();
        intent.setAction("android.intent.action.SEND");
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, "发现一个很牛x软件:"+appInfo.getName()+"," +
                "下载地址:www.baidu.com,自己去搜");
        startActivity(intent);
    }

    private void detail() {
        /**
         *  Intent
         {
         act=android.settings.APPLICATION_DETAILS_SETTINGS    action
         dat=package:com.example.android.apis   data
         cmp=com.android.settings/.applications.InstalledAppDetails
         } from pid 228
         */
        Intent intent = new Intent();
        intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.setData(Uri.parse("package:"+appInfo.getPackagName()));
        startActivity(intent);
    }

}
