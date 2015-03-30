package com.wisedu.scc.love.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by JZ on 2015/3/30.
 */
public class MyView extends View {

    public float currentX = 40;
    public float currentY = 50;
    // 创建一个画笔对象
    Paint paint = new Paint();

    public MyView(Context context) {
        super(context);
    }

    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setColor(Color.RED);
        canvas.drawCircle(currentX, currentY, 20, paint);
        canvas.drawLine(10, 10, 100, 10, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 重新设置X、Y
        currentX = event.getX();
        currentY = event.getY();
        // 通知当前组件重绘自己
        invalidate();
        // 返回true表明该处理方法已经处理该事件
        return true;
    }

}
