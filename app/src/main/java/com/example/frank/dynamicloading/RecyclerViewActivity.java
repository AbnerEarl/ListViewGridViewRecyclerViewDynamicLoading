package com.example.frank.dynamicloading;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MyAdapter myAdapter;
    private static String TAG="RecyclerViewActivity";
    private int i=0;
    private int firstVisibleItemTag=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);

        recyclerView=(RecyclerView)findViewById(R.id.rv_test);
        //创建线性布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);//默认vertical,可以不写
        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));//这里用线性宫格显示类似于gridview
//        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, OrientationHelper.VERTICAL));//这里用线性宫格显示 类似于瀑布流

        myAdapter=new MyAdapter(this);
        for (int i=0;i<25;i++){
            myAdapter.list.add("初始数据"+i);
        }
        recyclerView.setAdapter(myAdapter);

        /*recyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(RecyclerViewActivity.this,"onclick事件点击",Toast.LENGTH_LONG).show();
            }
        });*/

        myAdapter.setOnItemClickListener(new OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int postion) {
                Toast.makeText(RecyclerViewActivity.this,"item事件短按点击："+postion,Toast.LENGTH_LONG).show();
            }
        });

        myAdapter.setOnItemLongClickListener(new OnRecyclerViewItemLongClickListener() {
            @Override
            public void onItemClick(View view, int postion) {
                Toast.makeText(RecyclerViewActivity.this,"item事件长按点击："+postion,Toast.LENGTH_LONG).show();

            }
        });


    }


    public class MyAdapter extends RecyclerView.Adapter<MyViewHolder>{
        private OnRecyclerViewItemClickListener clickListener;// 声明自定义的接口
        private OnRecyclerViewItemLongClickListener longClickListener;
        private Context mContext;
        private List<String> list=new ArrayList<>();

        public MyAdapter(Context mContext) {
            this.mContext = mContext;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            //获取列表中，每行的布局文件
            //mContext = parent.getContext();
            View view = LayoutInflater.from(mContext).inflate(R.layout.layout, parent, false);
            MyViewHolder holder = new MyViewHolder(view);           //
            return holder;

        }

        public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
            this.clickListener = listener;
        }
        public void setOnItemLongClickListener(OnRecyclerViewItemLongClickListener listener) {
            this.longClickListener = listener;
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (clickListener!=null){
                        clickListener.onItemClick(v,position);
                    }
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (longClickListener!=null){
                        longClickListener.onItemClick(v,position);
                    }
                    return true;
                }
            });
            holder.textView.setText(list.get(position));

            //滑动加载数据
            /*int top = recyclerView.getTop();
            if(position == getItemCount()-1){//已经到达列表的底部
                loadMoreData();
            }*/
        }

        @Override
        public int getItemCount() {
            return list.size();
        }


    }
    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, int postion);
    }
    public interface OnRecyclerViewItemLongClickListener {
        void onItemClick(View view, int postion);
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        public ImageView imageView;
        public TextView textView;

        public MyViewHolder(View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.textView);
        }


    }


    private void loadMoreData(){
        myAdapter.list.add("动态加载的数据"+i);
        i++;
        myAdapter.notifyDataSetChanged();
    }

}
