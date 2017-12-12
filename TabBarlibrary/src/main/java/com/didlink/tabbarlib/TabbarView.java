package com.didlink.tabbarlib;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

public class TabbarView extends View {

    private Context mContext;
    private Bitmap mIconNormal;
    private Bitmap mIconSelected;
    private String mText;
    private int mTextColorNormal = 0xFF999999;
    private int mTextColorSelected = 0xFF46C01B;
    private int mTextSize = 12;
    private int mPadding = 5;

    private float mAlpha;
    private Paint mSelectedPaint = new Paint();
    private Rect mIconAvailableRect = new Rect();
    private Rect mIconDrawRect = new Rect();
    private Paint mTextPaint;
    private Rect mTextBound;
    private Paint.FontMetricsInt mFmi;

    private boolean isShowRemove;
    private boolean isShowPoint;
    private int mBadgeNumber;
    private int mBadgeBackgroundColor = 0xFFFF0000;

    public TabbarView(Context context) {
        this(context, null);
        mContext = context;
    }

    public TabbarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        mContext = context;
    }

    public TabbarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        mTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, mTextSize, getResources().getDisplayMetrics());
        mPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mPadding, getResources().getDisplayMetrics());

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TabbarView);
        BitmapDrawable iconNormal = (BitmapDrawable) a.getDrawable(R.styleable.TabbarView_tabIconNormal);
        if (iconNormal != null) {
            mIconNormal = iconNormal.getBitmap();
        }
        BitmapDrawable iconSelected = (BitmapDrawable) a.getDrawable(R.styleable.TabbarView_tabIconSelected);
        if (iconSelected != null) {
            mIconSelected = iconSelected.getBitmap();
        }

        if (null != mIconNormal) {
            mIconSelected = null == mIconSelected ? mIconNormal : mIconSelected;
        } else {
            mIconNormal = null == mIconSelected ? mIconNormal : mIconSelected;
        }

        mText = a.getString(R.styleable.TabbarView_tabText);
        mTextSize = a.getDimensionPixelSize(R.styleable.TabbarView_tabTextSize, mTextSize);
        mTextColorNormal = a.getColor(R.styleable.TabbarView_textColorNormal, mTextColorNormal);
        mTextColorSelected = a.getColor(R.styleable.TabbarView_textColorSelected, mTextColorSelected);
        mBadgeBackgroundColor = a.getColor(R.styleable.TabbarView_badgeBackgroundColor, mBadgeBackgroundColor);
        a.recycle();
        initText();
    }


    private void initText() {
        if (mText != null) {
            mTextBound = new Rect();
            mTextPaint = new Paint();
            mTextPaint.setTextSize(mTextSize);
            mTextPaint.setAntiAlias(true);
            mTextPaint.setDither(true);
            mTextPaint.getTextBounds(mText, 0, mText.length(), mTextBound);
            mFmi = mTextPaint.getFontMetricsInt();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mText == null && (mIconNormal == null || mIconSelected == null)) {
            throw new IllegalArgumentException("must be tabText or tabIconSelected、tabIconNormal, or all");
        }

        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();
        int measuredWidth = getMeasuredWidth();
        int measuredHeight = getMeasuredHeight();


        int availableWidth = measuredWidth - paddingLeft - paddingRight;
        int availableHeight = measuredHeight - paddingTop - paddingBottom;
        if (mText != null && mIconNormal != null) {
            availableHeight -= (mTextBound.height() + mPadding);

            mIconAvailableRect.set(paddingLeft, paddingTop, paddingLeft + availableWidth, paddingTop + availableHeight);

            int textLeft = paddingLeft + (availableWidth - mTextBound.width()) / 2;
            int textTop = mIconAvailableRect.bottom + mPadding;
            mTextBound.set(textLeft, textTop, textLeft + mTextBound.width(), textTop + mTextBound.height());
        } else if (mText == null) {

            mIconAvailableRect.set(paddingLeft, paddingTop, paddingLeft + availableWidth, paddingTop + availableHeight);
        } else if (mIconNormal == null) {

            int textLeft = paddingLeft + (availableWidth - mTextBound.width()) / 2;
            int textTop = paddingTop + (availableHeight - mTextBound.height()) / 2;
            mTextBound.set(textLeft, textTop, textLeft + mTextBound.width(), textTop + mTextBound.height());
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int alpha = (int) Math.ceil(mAlpha * 255);
        if (mIconNormal != null && mIconSelected != null) {
            Rect drawRect = availableToDrawRect(mIconAvailableRect, mIconNormal);
            mSelectedPaint.reset();
            mSelectedPaint.setAntiAlias(true);
            mSelectedPaint.setFilterBitmap(true);
            mSelectedPaint.setAlpha(255 - alpha);
            canvas.drawBitmap(mIconNormal, null, drawRect, mSelectedPaint);
            mSelectedPaint.reset();
            mSelectedPaint.setAntiAlias(true);
            mSelectedPaint.setFilterBitmap(true);
            mSelectedPaint.setAlpha(alpha); 

            canvas.drawBitmap(mIconSelected, null, drawRect, mSelectedPaint);
        }
        if (mText != null) {

            mTextPaint.setColor(mTextColorNormal);
            mTextPaint.setAlpha(255 - alpha);

            canvas.drawText(mText, mTextBound.left, mTextBound.bottom - mFmi.bottom / 2, mTextPaint);

            mTextPaint.setColor(mTextColorSelected);
            mTextPaint.setAlpha(alpha);
            canvas.drawText(mText, mTextBound.left, mTextBound.bottom - mFmi.bottom / 2, mTextPaint);
        }


        if (!isShowRemove) {
            drawBadge(canvas);
        }
    }


    private void drawBadge(Canvas canvas) {
        int i = getMeasuredWidth() / 14;
        int j = getMeasuredHeight() / 9;
        i = i >= j ? j : i;
        if (mBadgeNumber > 0) {
            Paint backgroundPaint = new Paint();
            backgroundPaint.setColor(mBadgeBackgroundColor);
            backgroundPaint.setAntiAlias(true);
            String number = mBadgeNumber > 99 ? "99+" : String.valueOf(mBadgeNumber);
            float textSize = i / 1.5f == 0 ? 5 : i / 1.5f;
            int width;
            int hight = (int) dp2px(mContext, i);
            Bitmap bitmap;
            if (number.length() == 1) {
                width = (int) dp2px(mContext, i);
                bitmap = Bitmap.createBitmap(width, hight, Bitmap.Config.ARGB_8888);
            } else if (number.length() == 2) {
                width = (int) dp2px(mContext, i + 5);
                bitmap = Bitmap.createBitmap(width, hight, Bitmap.Config.ARGB_8888);
            } else {
                width = (int) dp2px(mContext, i + 8);
                bitmap = Bitmap.createBitmap(width, width, Bitmap.Config.ARGB_8888);
            }
            Canvas canvasMessages = new Canvas(bitmap);
            RectF messageRectF = new RectF(0, 0, width, hight);
            canvasMessages.drawRoundRect(messageRectF, 50, 50, backgroundPaint);
            Paint numberPaint = new Paint();
            numberPaint.setColor(Color.WHITE);
            numberPaint.setTextSize(dp2px(mContext, textSize));
            numberPaint.setAntiAlias(true);
            numberPaint.setTextAlign(Paint.Align.CENTER);
            numberPaint.setTypeface(Typeface.DEFAULT_BOLD);
            Paint.FontMetrics fontMetrics = numberPaint.getFontMetrics();
            float x = width / 2f;
            float y = hight / 2f - fontMetrics.descent + (fontMetrics.descent - fontMetrics.ascent) / 2;
            canvasMessages.drawText(number, x, y, numberPaint);
            float left = getMeasuredWidth() / 10 * 6f;
            float top = dp2px(mContext, 5);
            canvas.drawBitmap(bitmap, left, top, null);
            bitmap.recycle();
        } else if (mBadgeNumber == 0) {

        } else {
            if (isShowPoint) {
                Paint paint = new Paint();
                paint.setColor(mBadgeBackgroundColor);
                paint.setAntiAlias(true);
                float left = getMeasuredWidth() / 10 * 6f;
                float top = dp2px(getContext(), 5);
                i = i > 10 ? 10 : i;
                float width = dp2px(getContext(), i);
                RectF messageRectF = new RectF(left, top, left + width, top + width);
                canvas.drawOval(messageRectF, paint);
            }
        }
    }

    public void showPoint() {
        isShowRemove = false;
        mBadgeNumber = -1;
        isShowPoint = true;
        invalidate();
    }

    public void showNumber(int badgeNum) {
        isShowRemove = false;
        isShowPoint = false;
        mBadgeNumber = badgeNum;
        if (badgeNum > 0) {
            invalidate();
        } else {
            isShowRemove = true;
            invalidate();
        }
    }

    public void removeShow() {
        mBadgeNumber = 0;
        isShowPoint = false;
        isShowRemove = true;
        invalidate();
    }

    public int getBadgeNumber() {
        return mBadgeNumber;
    }

    public boolean isShowPoint() {
        return isShowPoint;
    }

    private Rect availableToDrawRect(Rect availableRect, Bitmap bitmap) {
        float dx = 0, dy = 0;
        float wRatio = availableRect.width() * 1.0f / bitmap.getWidth();
        float hRatio = availableRect.height() * 1.0f / bitmap.getHeight();
        if (wRatio > hRatio) {
            dx = (availableRect.width() - hRatio * bitmap.getWidth()) / 2;
        } else {
            dy = (availableRect.height() - wRatio * bitmap.getHeight()) / 2;
        }
        int left = (int) (availableRect.left + dx + 0.5f);
        int top = (int) (availableRect.top + dy + 0.5f);
        int right = (int) (availableRect.right - dx + 0.5f);
        int bottom = (int) (availableRect.bottom - dy + 0.5f);
        mIconDrawRect.set(left, top, right, bottom);
        return mIconDrawRect;
    }


    public void setIconAlpha(float alpha) {
        if (alpha < 0 || alpha > 1) {
            throw new IllegalArgumentException("alpha 0.0 - 1.0");
        }
        mAlpha = alpha;
        invalidateView();
    }


    private void invalidateView() {
        if (Looper.getMainLooper() == Looper.myLooper()) {
            invalidate();
        } else {
            postInvalidate();
        }
    }

    private float dp2px(Context context, float dipValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale);
    }
}
