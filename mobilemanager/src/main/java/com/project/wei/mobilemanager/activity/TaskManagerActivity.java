package com.project.wei.mobilemanager.activity;

import android.app.ActivityManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.project.wei.mobilemanager.R;
import com.project.wei.mobilemanager.bean.TaskInfo;
import com.project.wei.mobilemanager.engine.TaskEngine;
import com.project.wei.mobilemanager.utils.MyAsyncTask;
import com.project.wei.mobilemanager.utils.TaskUtil;

import java.util.ArrayList;
import java.util.List;

public class TaskManagerActivity extends AppCompatActivity {
    private ListView lv_taskmanager_processes;
    private ProgressBar loading;
    private List<TaskInfo> list;
    //用户程序集合
    private List<TaskInfo> userappinfo;
    //系统程序的集合
    private List<TaskInfo> systemappinfo;
    private Myadapter myadapter;
    private TaskInfo taskInfo;

    private TextView tv_taskmanager_processes;
    private TextView tv_taskmanager_freeandtotalram;
    private int processCount;
    private boolean isShowSystem = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_manager);
        getSupportActionBar().hide();
        lv_taskmanager_processes = (ListView) findViewById(R.id.lv_taskmanager_processes);
        loading = (ProgressBar) findViewById(R.id.loading);


        tv_taskmanager_processes = (TextView) findViewById(R.id.tv_taskmanager_processes);
        tv_taskmanager_freeandtotalram = (TextView) findViewById(R.id.tv_taskmanager_freeandtotalram);

        // 设置显示数据
        // 获取相应的数据
        // 获取运行的进程个数
        processCount = TaskUtil.getProcessCount(getApplicationContext());
        tv_taskmanager_processes.setText("运行中进程:" + processCount + "个  ");
        // 获取剩余,总内存'
        long availableRam = TaskUtil.getAvailableRam(getApplicationContext());
        // 数据转化
        String availaRam = Formatter.formatFileSize(getApplicationContext(),
                availableRam);
        // 获取总内存
        // 根据不同的sdk版去调用不同的方法
        // 1.获取当前的sdk版本
        int sdk = android.os.Build.VERSION.SDK_INT;
        long totalRam;
        if (sdk >= 16) {
            totalRam = TaskUtil.getTotalRam(getApplicationContext());
        } else {
            totalRam = TaskUtil.getTotalRam();
        }
        // 数据转化
        String totRam = Formatter.formatFileSize(getApplicationContext(),
                totalRam);
        tv_taskmanager_freeandtotalram.setText("剩余/总内存:" + availaRam + "/"
                + totRam);

        //加载数据
        fillData();
        listviewItemClick();
    }
    /**
     * listview条目点击事件
     */
    private void listviewItemClick() {
        lv_taskmanager_processes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            //view : 条目的view对象
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //动态改变checkbox状态
                //1.屏蔽用户程序和系统程序(...个)弹出气泡
                if (position == 0 || position == userappinfo.size()+1) {
                    return;
                }
                //2.获取条目所对应的应用程序的信息
                //数据就要从userappinfo和systemappinfo中获取
                if (position <= userappinfo.size()) {
                    //用户程序
                    taskInfo = userappinfo.get(position-1);
                }else{
                    //系统程序
                    taskInfo = systemappinfo.get(position - userappinfo.size() - 2);
                }
                //3.根据之前保存的checkbox的状态设置点击之后的状态,原先选中,点击之后不选中
                if (taskInfo.isChecked()) {
                    taskInfo.setChecked(false);
                }else{
                    //如果是当前应用不能设置成true
                    if (!taskInfo.getPackageName().equals(getPackageName())) {
                        taskInfo.setChecked(true);
                    }
                }
                //4.更新界面
                //myadapter.notifyDataSetChanged();
                //只更新点击的条目
                /*.单独更新单个条目
                因为控件是保存在ViewHolder控件容器中,而容器又是在listview条目展示的时候通过view.settag和view对象进行绑定了,
                那我们就可以在条目点击事件进行使用处理,因为在条目点击事件中有view参数,这个参数代表就是点击条目的view对象,
                通过view.gettag获取到和view对象绑定的容器,从容器中获取控件,设置控件的状态了*/
                ViewHolder viewHolder = (ViewHolder) view.getTag();
                viewHolder.cb_itemtaskmanager_ischecked.setChecked(taskInfo.isChecked());
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
                    lv_taskmanager_processes.setAdapter(myadapter);
                }else{
                    myadapter.notifyDataSetChanged();
                }
                loading.setVisibility(View.INVISIBLE);
            }

            @Override
            public void doinBackGround() {
                list = TaskEngine.getTaskAllInfo(getApplicationContext());
                userappinfo = new ArrayList<TaskInfo>();
                systemappinfo = new ArrayList<TaskInfo>();
                for (TaskInfo taskinfo : list) {
                    //将数据分别存放到用户程序集合和系统程序集合中
                    if (taskinfo.isUser()) {
                        userappinfo.add(taskinfo);
                    }else{
                        systemappinfo.add(taskinfo);
                    }
                }
            }
        }.execute();
    }
    private class Myadapter extends BaseAdapter {

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            //list.size() = userappinfo.size()+systemappinfo.size()
            return isShowSystem==true ? userappinfo.size()+systemappinfo.size()+2 : userappinfo.size()+1;
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
                //添加用户程序(...个)textview
                TextView textView = new TextView(getApplicationContext());
                textView.setBackgroundColor(Color.GRAY);
                textView.setTextColor(Color.WHITE);
                textView.setText("用户进程("+userappinfo.size()+")");
                return textView;
            }else if(position == userappinfo.size()+1){
                //添加系统程序(....个)textview
                TextView textView = new TextView(getApplicationContext());
                textView.setBackgroundColor(Color.GRAY);
                textView.setTextColor(Color.WHITE);
                textView.setText("系统进程("+systemappinfo.size()+")");
                return textView;
            }

            View view;
            ViewHolder viewHolder;
            //复用的时候判断复用view对象的类型
            if (convertView != null && convertView instanceof RelativeLayout) {
                view = convertView;
                viewHolder = (ViewHolder) view.getTag();
            }else{
                view = View.inflate(getApplicationContext(), R.layout.item_taskmanager, null);
                viewHolder = new ViewHolder();
                viewHolder.iv_itemtaskmanager_icon = (ImageView) view.findViewById(R.id.iv_itemtaskmanager_icon);
                viewHolder.tv_itemtaskmanager_name = (TextView) view.findViewById(R.id.tv_itemtaskmanager_name);
                viewHolder.tv_itemtaskmanager_ram = (TextView) view.findViewById(R.id.tv_itemtaskmanager_ram);
                viewHolder.cb_itemtaskmanager_ischecked = (CheckBox) view.findViewById(R.id.cb_itemtaskmanager_ischecked);
                //将viewholer和view对象绑定
                view.setTag(viewHolder);
            }
            //1.获取应用程序的信息
            TaskInfo taskinfo = null;
            //数据就要从userappinfo和systemappinfo中获取
            if (position <= userappinfo.size()) {
                //用户程序
                taskinfo = userappinfo.get(position-1);
            }else{
                //系统程序
                taskinfo = systemappinfo.get(position - userappinfo.size() - 2);
            }

            //设置显示数据,null.方法    参数为null
            if (taskinfo.getIcon() == null) {
                viewHolder.iv_itemtaskmanager_icon.setImageResource(R.drawable.ic_default);
            }else{
                viewHolder.iv_itemtaskmanager_icon.setImageDrawable(taskinfo.getIcon());
            }
            //名称为空的可以设置为包名
            if (TextUtils.isEmpty(taskinfo.getName())) {
                viewHolder.tv_itemtaskmanager_name.setText(taskinfo.getPackageName());
            }else{
                viewHolder.tv_itemtaskmanager_name.setText(taskinfo.getName());
            }

            long ramSize = taskinfo.getRamSize();
            //数据转化
            String formatFileSize = Formatter.formatFileSize(getApplicationContext(), ramSize);
            viewHolder.tv_itemtaskmanager_ram.setText("内存占用:"+formatFileSize);

            //问题:checkbox的状态会跟着控件一起复用,
            // 解决:所以将状态保存到bean类中,根据bean中保存的状态在getview中进行动态设置

            //因为checkbox的状态会跟着一起复用,所以一般要动态修改的控件的状态,不会跟着去复用,而是将状态保存到bean对象,在每次复用使用控件的时候
            //根据每个条目对应的bean对象保存的状态,来设置控件显示的相应的状态
            if (taskinfo.isChecked()) {
                viewHolder.cb_itemtaskmanager_ischecked.setChecked(true);
            }else{
                viewHolder.cb_itemtaskmanager_ischecked.setChecked(false);
            }
            //判断如果是我们的应用程序,就把checkbox隐藏,不是的话显示,在getview中有if必须有else
            if (taskinfo.getPackageName().equals(getPackageName())) {
                viewHolder.cb_itemtaskmanager_ischecked.setVisibility(View.INVISIBLE);
            }else{
                viewHolder.cb_itemtaskmanager_ischecked.setVisibility(View.VISIBLE);
            }
            return view;
        }

    }

    static class ViewHolder{
        ImageView iv_itemtaskmanager_icon;
        TextView tv_itemtaskmanager_name,tv_itemtaskmanager_ram;
        CheckBox cb_itemtaskmanager_ischecked;
    }
    /**
     * 全选
     * @param v
     */
    public void all(View v){
        //用户进程
        for (int i = 0; i < userappinfo.size(); i++) {
            if (!userappinfo.get(i).getPackageName().equals(getPackageName())) {
                userappinfo.get(i).setChecked(true);
            }
        }
        //系统进程
        for (int i = 0; i < systemappinfo.size(); i++) {
            systemappinfo.get(i).setChecked(true);
        }
        //更新界面
        myadapter.notifyDataSetChanged();
    }
    /**
     * 取消
     * @param v
     */
    public void cancel(View v){
        //用户进程
        for (int i = 0; i < userappinfo.size(); i++) {
            userappinfo.get(i).setChecked(false);
        }
        //系统进程
        for (int i = 0; i < systemappinfo.size(); i++) {
            systemappinfo.get(i).setChecked(false);
        }
        //更新界面
        myadapter.notifyDataSetChanged();
    }
    /**
     * 清理
     * @param v
     */
    public void clear(View v){
        //1.获取进程的管理者
        ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        //保存杀死进程信息的集合
        List<TaskInfo> deleteTaskInfos = new ArrayList<TaskInfo>();

        for (int i = 0; i < userappinfo.size(); i++) {
            if (userappinfo.get(i).isChecked()) {
                //杀死进程
                //packageName : 进程的包名
                //杀死后台进程
                am.killBackgroundProcesses(userappinfo.get(i).getPackageName());
                deleteTaskInfos.add(userappinfo.get(i));//将杀死的进程信息保存的集合中
            }
        }
        //系统进程
        for (int i = 0; i < systemappinfo.size(); i++) {
            if (systemappinfo.get(i).isChecked()) {
                //杀死进程
                //packageName : 进程的包名
                //杀死后台进程
                am.killBackgroundProcesses(systemappinfo.get(i).getPackageName());
                deleteTaskInfos.add(systemappinfo.get(i));//将杀死的进程信息保存的集合中
            }
        }
        long memory=0;
        //遍历deleteTaskInfos,分别从userappinfo和systemappinfo中删除deleteTaskInfos中的数据
        for (TaskInfo taskInfo : deleteTaskInfos) {
            if (taskInfo.isUser()) {
                userappinfo.remove(taskInfo);
            }else{
                systemappinfo.remove(taskInfo);
            }
            memory+=taskInfo.getRamSize();
        }
        //数据转化
        String deletesize = Formatter.formatFileSize(getApplicationContext(), memory);
        Toast.makeText(getApplicationContext(), "共清理"+deleteTaskInfos.size()+"个进程,释放"+deletesize+"内存空间", Toast.LENGTH_SHORT).show();

        // 更改运行中的进程个数以及剩余总内存
        processCount = processCount - deleteTaskInfos.size();
        tv_taskmanager_processes.setText("运行中进程:" + processCount + "个  ");

        // 更改剩余总内存,重新获取剩余总内存
        // 获取剩余,总内存'
        long availableRam = TaskUtil.getAvailableRam(getApplicationContext());
        // 数据转化
        String availaRam = Formatter.formatFileSize(getApplicationContext(),
                availableRam);
        // 获取总内存
        // 根据不同的sdk版去调用不同的方法
        // 1.获取当前的sdk版本
        int sdk = android.os.Build.VERSION.SDK_INT;
        long totalRam;
        if (sdk >= 16) {
            totalRam = TaskUtil.getTotalRam(getApplicationContext());
        } else {
            totalRam = TaskUtil.getTotalRam();
        }
        // 数据转化
        String totRam = Formatter.formatFileSize(getApplicationContext(),
                totalRam);
        tv_taskmanager_freeandtotalram.setText("剩余/总内存:" + availaRam + "/"
                + totRam);

        //为下次清理进程做准备
        deleteTaskInfos.clear();
        deleteTaskInfos=null;
        //更新界面
        myadapter.notifyDataSetChanged();
    }
    /**
     * 设置
     * @param v
     */
    public void setting(View v){
        isShowSystem = !isShowSystem;
        myadapter.notifyDataSetChanged();
    }
}
