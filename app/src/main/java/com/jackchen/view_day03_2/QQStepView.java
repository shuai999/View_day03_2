package com.jackchen.view_day03_2;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Email: 2185134304@qq.com
 * Created by JackChen 2018/3/18 8:10
 * Version 1.0
 * Params:
 * Description:   仿QQ运动步数
*/

public class QQStepView extends View {

    // 外圆弧颜色
    private int mOurterColor = Color.RED ;
    // 内圆弧颜色
    private int mInnerColor = Color.BLUE ;
    // 圆弧宽度
    private int mBorderWidth = 20 ; //20px
    // 文字大小
    private int mStepTextSize ;
    // 文字颜色
    private int mStepTextColor ;

    // 外圆弧画笔、内圆弧画笔、文字画笔
    private Paint mOuterPaint , mInnerPaint , mTextPaint ;

    // 总共步数
    private int mStepMax = 0 ;
    // 当前步数
    private int mCurrentStep = 0 ;


    public QQStepView(Context context) {
        this(context,null);
    }

    public QQStepView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public QQStepView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        // 1. 创建attrs.xml文件，编写自定义属性
        // 2. 在布局中使用
        // 3. 在自定义View中获取自定义属性
        // 4. 测量onMeasure()
        // 5. 绘制固定圆弧、变化圆弧、文字

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.QQStepView);
        mOurterColor = array.getColor(R.styleable.QQStepView_outerColor , mOurterColor) ;
        mInnerColor = array.getColor(R.styleable.QQStepView_innerColor , mInnerColor) ;
        mBorderWidth = array.getDimensionPixelSize(R.styleable.QQStepView_borderWidth , mBorderWidth) ;
        mStepTextSize = array.getDimensionPixelSize(R.styleable.QQStepView_stepTextSize , mStepTextSize) ;
        mStepTextColor = array.getColor(R.styleable.QQStepView_stepTextColor , mStepTextColor) ;
        array.recycle();


        // 外圆弧画笔
        mOuterPaint = new Paint() ;
        mOuterPaint.setAntiAlias(true);
        mOuterPaint.setStrokeWidth(mBorderWidth);
        mOuterPaint.setColor(mOurterColor);
        mOuterPaint.setStrokeCap(Paint.Cap.ROUND);
        mOuterPaint.setStyle(Paint.Style.STROKE); //画笔空心

        // 内圆弧画笔
        mInnerPaint = new Paint() ;
        mInnerPaint.setAntiAlias(true);
        mInnerPaint.setStrokeWidth(mBorderWidth);
        mInnerPaint.setColor(mInnerColor);
        mInnerPaint.setStrokeCap(Paint.Cap.ROUND);
        mInnerPaint.setStyle(Paint.Style.STROKE); //画笔空心

        // 文字画笔
        mTextPaint = new Paint() ;
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(mStepTextColor);
        mTextPaint.setTextSize(mStepTextSize);
    }



    // 画外圆弧、内圆弧、文字
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 调用者可能会在布局文件中给宽高设置 wrap_content
        // 这里确保：如果宽高不一致，则取最小值，确保是一个正方形

        int width = MeasureSpec.getSize(widthMeasureSpec) ;
        int height = MeasureSpec.getSize(heightMeasureSpec) ;
        setMeasuredDimension(width>height?height:width , width>height?height:width);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 画外圆弧
        RectF rectF = new RectF(mBorderWidth/2,mBorderWidth/2
                ,getWidth()-mBorderWidth/2,getHeight()-mBorderWidth/2);
        canvas.drawArc(rectF , 135 , 270 , false , mOuterPaint);
        if (mStepMax ==0) return;


        // 画内圆弧  肯定不能写死，使用百分比，让使用者从外边传递
        float sweepAngle = (float) mCurrentStep/mStepMax ;
        canvas.drawArc(rectF , 135 , sweepAngle*270 , false , mInnerPaint);


        // 画文字
        String stepText = mCurrentStep+"" ;
        Rect rect = new Rect() ;
        mTextPaint.getTextBounds(stepText,0,stepText.length(),rect);

        int dx = getWidth()/2 - rect.width()/2 ;

        // 基线baseLine
        Paint.FontMetricsInt fontMetrics = mTextPaint.getFontMetricsInt();
        int dy = (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom ;
        int baseLine = getHeight()/2 + dy ;
        canvas.drawText(stepText , dx , baseLine , mTextPaint);
    }


    /**
     * 最大步数
     * 在这里添加 synchronized，目的就是防止多线程操作
     */
    public synchronized void setStepMax(int stepMax){
        this.mStepMax = stepMax ;
    }


    /**
     * 当前步数
     * 在这里添加 synchronized，目的就是防止多线程操作
     */
    public synchronized void setCurrentStep(int currentStep){
        this.mCurrentStep = currentStep ;
        // 根据效果可以看到，圆弧和文字一直在变，所以就知道，是不断的重新绘制
        invalidate();
    }
}
