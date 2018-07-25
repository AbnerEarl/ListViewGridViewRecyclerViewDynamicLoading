package com.example.frank.listviewloading;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private MyAdapter myAdapter;
    private static String TAG="MainActivity";
    private int i=0;
    private int firstVisibleItemTag=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView=(ListView)findViewById(R.id.lv_loading);
        myAdapter=new MyAdapter(this);
        for (int i=0;i<15;i++){
            myAdapter.list.add("初始数据"+i);
        }

        listView.setAdapter(myAdapter);


        /*listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (myAdapter.list.size()<10)
                for (;i<10;i++)
                    myAdapter.list.add("测试"+i);
                i=0;
                myAdapter.notifyDataSetChanged();
                return true;
            }
        });*/

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case SCROLL_STATE_IDLE://停止滑动
                        Log.w(TAG, "停止滑动");
                        break;
                    case SCROLL_STATE_TOUCH_SCROLL://正在滑动
                        Log.w(TAG, "正在滑动");
                        break;
                    case SCROLL_STATE_FLING://滑动ListView离开后，由于惯性继续滑动
                        Log.w(TAG, "滑动ListView离开后，由于惯性继续滑动");
                        break;
                }

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
             /*
             ListView滑动时，一直回调该方法
             firstVisibleItem：当前能看到的第一个子项的ID（从0开始）
             visibleItemCount：当前能看到的子项的总数，包含未完整显示的子项，比如只显示了一小部分的子项
             totalItemCount：所有子项的总数
            */
                Log.e(TAG, "当前能看到的第一个子项的ID（从0开始）= " + firstVisibleItem);
                Log.e(TAG, "当前能看到的子项的总数 = " + visibleItemCount);
                Log.e(TAG, "所有子项的总数 = " + totalItemCount);

                //用于底部加载更多数据的判断逻辑,在这个地方调用自己的方法请求网络数据，一次性请求10条或者15条等
                if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount > 0) {
                    Log.e(TAG, "滑动到ListView的最后一个子项");
                    myAdapter.list.add("动态加载的数据"+i);
                    i++;
                    myAdapter.notifyDataSetChanged();
                }

                //判断ListView的滑动方向
                if (firstVisibleItemTag == firstVisibleItem) {
                    Log.e(TAG, "未发生滑动");
                } else if (firstVisibleItemTag > firstVisibleItem) {
                    Log.e(TAG, "发生下滑");
                } else {
                    Log.e(TAG, "发生上滑");
                }
               firstVisibleItemTag = firstVisibleItem;


            }
        });
    }


    private class MyAdapter extends BaseAdapter{

        private LayoutInflater layoutInflater;
        private List<String> list=new ArrayList<>();

        public MyAdapter(Context context){
            this.layoutInflater=LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if (convertView==null){
                convertView=layoutInflater.inflate(R.layout.layout,null);
                holder=new ViewHolder();
                holder.title=(TextView)convertView.findViewById(R.id.textView);
                convertView.setTag(holder);
            }
            else {
                holder=(ViewHolder)convertView.getTag();
            }


            holder.title.setText(list.get(position));

            return convertView;
        }
    }

    public final class ViewHolder{
        public TextView title;
    }
}
