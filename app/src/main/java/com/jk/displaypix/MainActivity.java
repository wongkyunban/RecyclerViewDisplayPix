package com.jk.displaypix;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private List<Integer> list = new ArrayList<>(4);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // 存入图片
        list.add(R.drawable.b1);
        list.add(R.drawable.b2);
        list.add(R.drawable.b3);
        list.add(R.drawable.b4);


        BannerAdapter adapter = new BannerAdapter(this, list);
        RecyclerView recyclerView = findViewById(R.id.recycler);
        // LinearLayoutManager 第二个参数表示布局方向，默认是垂直的
        // 这里轮播广告要用为LinearLayoutManager.HORIZONTAL，水平方向
        // 使用LinearLayoutManager会让图片滚动太快，我们继承LinearLayoutManager写一个子类重写它的滑动，让它不要太快
        // LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        SmoothLinearLayoutManager layoutManager = new SmoothLinearLayoutManager(this, SmoothLinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        // 由于广告是一页一页的划过去，所以我们还需要用SnapHelper的子类PagerSnapHelper。直接追加到上面的recyclerView.setAdapter(adapter) 后面。
        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);
        // recyclerView.scrollToPosition(list.size()*10)这句使RecyclerView一开始位于 list.size()*10 处，避免了一开始position为0不能前滑的尴尬
        recyclerView.scrollToPosition(list.size() * 10);
        // 自动轮播
        autoScroll(recyclerView, layoutManager);
        // 指示器  显示指示器上的红点需要得到当前展示的广告轮播图片的position。RecyclerView有个addOnScrollListener()方法，可以监听当前滑动状态
        indicator(recyclerView, layoutManager);
    }

    private void autoScroll(final RecyclerView recyclerView, final LinearLayoutManager layoutManager) {
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                // layoutManager.findFirstVisibleItemPosition() 表示得到当前RecyclerView第一个能看到的item的位置。
                // 由于广告是每次展示一张，所以得到的就是当前图片的position。
                // recyclerView.smoothScrollToPosition（int position）表示滑动到某个position。
                // 所以上面的代码就表示每过2秒滑动到下个position，以此来完成自动轮播。
                recyclerView.smoothScrollToPosition(layoutManager.findFirstVisibleItemPosition() + 1);
            }
        }, 2000, 2000, TimeUnit.MILLISECONDS);// 表示2秒后每过2秒运行一次run（）里的程序

    }

    private void indicator(final RecyclerView recyclerView, final LinearLayoutManager layoutManager) {
        final PixIndicator bannerIndicator = findViewById(R.id.indicator);
        bannerIndicator.setNumber(list.size());
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                // onScrollStateChangedd的 newState 参数有三种状态:
                // SCROLL_STATE_IDLE 静止状态
                // SCROLL_STATE_DRAGGING 拖拽状态
                // SCROLL_STATE_SETTLING手指离开后的惯性滚动状态
                // 当RecyclerView的状态为SCROLL_STATE_IDLE时得到当前图片的position，然后与图片列表取余就得到指示器红点的位置。

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    int i = layoutManager.findFirstVisibleItemPosition() % list.size();
                    //得到指示器红点的位置
                    bannerIndicator.setPosition(i);
                }
            }
        });

    }
}
