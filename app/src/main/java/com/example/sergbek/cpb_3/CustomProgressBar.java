package com.example.sergbek.cpb_3;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public final class CustomProgressBar extends View {

    private Line mLeftLine;
    private Line mTopLine;
    private Line mRightLine;
    private Line mBottomLine;
    private Line mFirstDiagonal;
    private Line mSecondDiagonal;

    private Rect mLeftTopRect;
    private Rect mRightTopRect;
    private Rect mLeftBottomRect;
    private Rect mRightBottomRect;
    private Rect mCentralRect;

    private int mSideRect;
    private int mHalfRect;
    private int mColorRect;

    private Paint mPCornerSquares;
    private Paint mPCenterSquare;

    private Point mRightBottomPoint;
    private Point mLeftBottomPoint;
    private Point mLeftTopPoint;
    private Point mRightTopPoint;

    private AnimatorSet mSet;

    private static final int DESIRED_WIDTH = 285;
    private static final int DESIRED_HEIGHT = 285;

    public CustomProgressBar(Context context) {
        super(context);
        init();
    }

    public CustomProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mSet = new AnimatorSet();

        mLeftTopRect = new Rect();
        mRightTopRect = new Rect();
        mLeftBottomRect = new Rect();
        mRightBottomRect = new Rect();
        mCentralRect = new Rect();

        mLeftLine = new Line();
        mTopLine = new Line();
        mRightLine = new Line();
        mBottomLine = new Line();
        mFirstDiagonal = new Line();
        mSecondDiagonal = new Line();

        mPCornerSquares = new Paint();
        mPCenterSquare = new Paint();

        mColorRect = 0xFFB6DE32;
        mPCornerSquares.setColor(mColorRect);
        mPCenterSquare.setColor(mColorRect);

        mRightBottomPoint = new Point();
        mLeftBottomPoint = new Point();
        mLeftTopPoint = new Point();
        mRightTopPoint = new Point();

        mSet.playSequentially(
                ObjectAnimator.ofInt(this, "alpha", 255, 0,
                        255, 0, 255, 0, 255, 0, 255, 0),

                ObjectAnimator.ofInt(this, "progress", 0, 100),

                ObjectAnimator.ofInt(this, "alphaCorner", 255, 0,
                        255, 0, 255, 0, 255, 0, 255),

                ObjectAnimator.ofInt(this, "progress", 100, 0),

                ObjectAnimator.ofInt(this, "alpha", 180)
        );

        mSet.getChildAnimations().get(0).setDuration(2 * 1000);
        mSet.getChildAnimations().get(1).setDuration(4 * 1000);
        mSet.getChildAnimations().get(2).setDuration(2 * 1000);
        mSet.getChildAnimations().get(3).setDuration(4 * 1000);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int width;
        int height;

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        //Measure Width
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
            mSideRect = width;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            width = Math.min(DESIRED_WIDTH, widthSize);
        } else {
            width = DESIRED_WIDTH;
        }

        mSideRect = width;

        //Measure Height
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            height = Math.min(DESIRED_HEIGHT, heightSize);
        } else {
            height = DESIRED_HEIGHT;
        }

        if (height < width)
            mSideRect = height;

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {

        int www = mSideRect / 16;
        mLeftTopRect.set(0, 0, www, www);
        mRightTopRect.set(mSideRect - www, 0, mSideRect, www);
        mLeftBottomRect.set(0, mSideRect - www, www, mSideRect);
        mRightBottomRect.set(mSideRect - www, mSideRect - www, mSideRect, mSideRect);

        int percent = mSideRect * 20 / 100;
        mCentralRect.set(percent, percent, mSideRect - percent, mSideRect - percent);

        mHalfRect = mSideRect / 32;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        mLeftLine.set(mLeftTopRect.centerX(), mLeftTopRect.centerY(),
                mLeftBottomRect.centerX(), mLeftBottomRect.centerY());
        mTopLine.set(mLeftTopRect.centerX(), mLeftTopRect.centerY(),
                mRightTopRect.centerX(), mRightTopRect.centerY());
        mRightLine.set(mRightTopRect.centerX(), mRightTopRect.centerY(),
                mRightBottomRect.centerX(), mRightBottomRect.centerY());
        mBottomLine.set(mRightBottomRect.centerX(), mRightBottomRect.centerY(),
                mLeftBottomRect.centerX(), mLeftBottomRect.centerY());
        mFirstDiagonal.set(mLeftTopRect.centerX(), mLeftTopRect.centerY(),
                mRightBottomRect.centerX(), mRightBottomRect.centerY());
        mSecondDiagonal.set(mRightTopRect.centerX(), mRightTopRect.centerY(),
                mLeftBottomRect.centerX(), mLeftBottomRect.centerY());

        mLeftLine.onDraw(canvas);
        mTopLine.onDraw(canvas);
        mRightLine.onDraw(canvas);
        mBottomLine.onDraw(canvas);
        mFirstDiagonal.onDraw(canvas);
        mSecondDiagonal.onDraw(canvas);

        canvas.drawRect(mLeftTopRect, mPCornerSquares);
        canvas.drawRect(mRightTopRect, mPCornerSquares);
        canvas.drawRect(mLeftBottomRect, mPCornerSquares);
        canvas.drawRect(mRightBottomRect, mPCornerSquares);
        canvas.drawRect(mCentralRect, mPCenterSquare);
    }

    public void setProgress(int progress) {

        calculationPoint(progress);

        movePoint(mRightBottomRect, mRightBottomPoint);

        movePoint(mLeftBottomRect, mLeftBottomPoint);

        movePoint(mLeftTopRect, mLeftTopPoint);

        movePoint(mRightTopRect, mRightTopPoint);

        invalidate();
    }

    private void calculationPoint(int progress) {
        int countRB;
        int countLB;
        int countLT;
        int countRT;

        if (progress <= 12)
            countRB = ((mSideRect / 2 ) * progress) / 12;
        else
            countRB = ((mSideRect / 2 - mHalfRect) * (progress - 12)) / 12;

        if (progress < 12)
            countLB = 0;
        else if (progress >= 12 && progress <= 24)
            countLB = ((mSideRect / 2 - mHalfRect) * (progress - 12)) / 12;
        else
            countLB = ((mSideRect / 2) * (progress - 24)) / 12;


        if (progress < 24)
            countLT = 0;
        else if (progress >= 24 && progress <= 36)
            countLT = ((mSideRect / 2 - mHalfRect-mHalfRect) * (progress - 24)) / 12;
        else
            countLT = ((mSideRect / 2 - mHalfRect) * (progress - 36)) / 12;


        if (progress < 36)
            countRT = 0;
        else if (progress >= 36 && progress <= 48)
            countRT = ((mSideRect / 2 - mHalfRect) * (progress - 36)) / 12;
        else
            countRT = ((mSideRect / 2 - mHalfRect) * (progress - 48)) / 12;


        if (progress <= 12)
            mRightBottomPoint.set(mSideRect - mHalfRect, mSideRect - countRB - mHalfRect);
        if (progress <= 24 && progress > 12)
            mRightBottomPoint.set(mSideRect - countRB - mHalfRect, mSideRect / 2 - mHalfRect);

        if (progress <= 24)
            mLeftBottomPoint.set(countLB + mHalfRect, mSideRect - mHalfRect);
        if (progress <= 36 && progress > 24)
            mLeftBottomPoint.set(mSideRect / 2, mSideRect - (countLB) - mHalfRect);

        if (progress <= 36)
            mLeftTopPoint.set(mHalfRect, countLT + mHalfRect);
        if (progress <= 48 && progress > 36)
            mLeftTopPoint.set(countLT + mHalfRect, (mSideRect / 2) - mHalfRect);

        if (progress <= 48)
            mRightTopPoint.set(mSideRect - countRT - mHalfRect, mHalfRect);
        if (progress <= 60 && progress > 48)
            mRightTopPoint.set(mSideRect / 2, countRT);
    }

    private void movePoint(Rect rect, Point point) {
        rect.set(point.x - mHalfRect, point.y - mHalfRect, point.x + mHalfRect, point.y + mHalfRect);
    }

    public void setAlpha(int alpha) {

        mPCenterSquare.setAlpha(alpha);

        invalidate();
    }

    public void setAlphaCorner(int alpha) {

        mPCornerSquares.setAlpha(alpha);

        invalidate();
    }

    public void start(){
        mSet.addListener(new AnimatorAdapter(mSet));
        mSet.start();

    }

    public void stop(){
        mSet.removeAllListeners();
        mSet.end();
        mSet.cancel();
    }

    public void setColorRect(int colorRect) {
        mColorRect = colorRect;
    }
}
