package com.leenanxi.android.open.feed.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.*;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ImageView;
import com.leenanxi.android.open.feed.R;

import java.util.StringTokenizer;

public class TextCircleImageView extends ImageView {
    private static final ScaleType SCALE_TYPE = ScaleType.CENTER_CROP;
    private final Paint mImagePaint = new Paint();
    private final Paint mBackgroundPaint = new Paint();
    private final Paint mBorderPaint = new Paint();
    private final TextPaint mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
    private Bitmap mImage;
    private int mBorderWidth;
    private int mBorderColor;
    private int mTextMargin;
    private int mTextColor;
    private int mBackgroundColor;
    private String mText;

    public TextCircleImageView(Context context) {
        this(context, null);
    }

    public TextCircleImageView(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.TextCircleImageViewStyle);
    }

    public TextCircleImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        super.setScaleType(SCALE_TYPE);
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.TextCircleImageView, defStyle, 0);
        if (attributes.getBoolean(R.styleable.TextCircleImageView_border, true)) {
            mBorderWidth = attributes.getDimensionPixelOffset(R.styleable.TextCircleImageView_border_width, 0);
            int defaultTextMarginSize = (int) (16 * getContext().getResources().getDisplayMetrics().density);
            mTextMargin = attributes.getDimensionPixelOffset(R.styleable.TextCircleImageView_text_margin, defaultTextMarginSize);
            mBorderColor = attributes.getColor(R.styleable.TextCircleImageView_border_color, Color.TRANSPARENT);
            mText = attributes.getString(R.styleable.TextCircleImageView_text);
            mBackgroundColor = attributes.getColor(R.styleable.TextCircleImageView_background_color, Color.TRANSPARENT);
            mTextColor = attributes.getColor(R.styleable.TextCircleImageView_text_color, Color.WHITE);
        }
    }

    public static String getInitialByText(String text) {
        StringBuffer buffer = new StringBuffer();
        if (!TextUtils.isEmpty(text)) {
            char firstChar = text.charAt(0);
            if ((firstChar >= 65 && firstChar <= 90) || (firstChar >= 97 && firstChar <= 122)) {
                StringTokenizer t = new StringTokenizer(text, " ");
                if (t.countTokens() > 0) {
                    String firstWord = t.nextToken();
                    if (firstWord.length() > 0) {
                        buffer.append(firstWord.substring(0, 1));
                    }
                    if (t.countTokens() >= 1) {
                        String lastWord = t.nextToken();
                        if (lastWord.length() > 0) {
                            buffer.append(lastWord.substring(0, 1));
                        }
                    }
                }
            } else {
                text = text.replaceAll(" ", "");
                if (text.length() <= 2) {
                    return text;
                }
                buffer.append(text.substring(text.length() - 2, text.length()));
            }
        }
        String result = buffer.toString();
        return result.toUpperCase();
    }

    public void setBorderWidth(int width) {
        this.mBorderWidth = width;
        this.requestLayout();
        this.invalidate();
    }

    public void setTextMargin(int textMargin) {
        this.mTextMargin = textMargin;
        this.requestLayout();
        this.invalidate();
    }

    public void setText(String text) {
        this.mText = text;
        this.requestLayout();
        this.invalidate();
    }

    public void setBorderColor(int borderColor) {
        if (mBorderPaint != null)
            mBorderPaint.setColor(borderColor);
        this.invalidate();
    }

    public void setBackgroundColor(int bgColor) {
        if (mBackgroundPaint != null)
            mBackgroundPaint.setColor(bgColor);
        this.requestLayout();
        this.invalidate();
    }

    public void setTextColor(int textColor) {
        this.mTextColor = textColor;
        this.requestLayout();
        this.invalidate();
    }

    @Override
    public ScaleType getScaleType() {
        return SCALE_TYPE;
    }

    @Override
    public void setScaleType(ScaleType scaleType) {
        if (scaleType != SCALE_TYPE) {
            throw new IllegalArgumentException(String.format("ScaleType %s not supported.",
                    scaleType));
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (getWidth() == 0 && getHeight() == 0) {
            return;
        }
        mImage = getBitmapFromDrawable(getDrawable());
        if (mImage == null) {
            return;
        }
        RectF drawableRect = new RectF();
        drawableRect.set(getPaddingLeft(), getPaddingTop(), getWidth() - getPaddingRight(),
                getHeight() - getPaddingBottom());
        if (mBorderWidth > 0) {
            if (mBorderColor != Color.TRANSPARENT) {
                float radius = Math.min((drawableRect.height()) / 2f, (drawableRect.width()) / 2f);
                mBorderPaint.setColor(mBorderColor);
                canvas.drawCircle(drawableRect.centerX(), drawableRect.centerY(), radius, mBorderPaint);
            }
            drawableRect.set(getPaddingLeft() + mBorderWidth, getPaddingTop() + mBorderWidth, getWidth() - getPaddingRight() - mBorderWidth,
                    getHeight() - getPaddingBottom() - mBorderWidth);
        }
        float drawableRectHeight = drawableRect.height();
        float drawableRectWidth = drawableRect.width();
        if (mBackgroundColor != Color.TRANSPARENT) {
            float radius = Math.min((drawableRectHeight) / 2f, (drawableRectWidth) / 2f);
            mBackgroundPaint.setColor(mBackgroundColor);
            canvas.drawCircle(drawableRect.centerX(), drawableRect.centerY(), radius, mBackgroundPaint);
        }
        if (mImage != null) {
            float imageWidth = mImage.getWidth();
            float imageHeight = mImage.getHeight();
            Matrix shaderMatrix = new Matrix();
            float scale;
            float dx = 0;
            float dy = 0;
            if (imageWidth * drawableRectHeight > drawableRectWidth * imageHeight) {
                scale = drawableRect.height() / imageHeight;
                dx = (drawableRectWidth - imageWidth * scale) / 2f;
            } else {
                scale = drawableRectWidth / imageWidth;
                dy = (drawableRectHeight - imageHeight * scale) / 2f;
            }
            shaderMatrix.setScale(scale, scale);
            shaderMatrix.postTranslate(Math.round(dx + drawableRect.left),
                    Math.round(dy + drawableRect.top));
            BitmapShader shader = new BitmapShader(mImage, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            shader.setLocalMatrix(shaderMatrix);
            mImagePaint.setShader(shader);
            float radius = Math.min((drawableRectHeight) / 2f, (drawableRectWidth) / 2f);
            canvas.drawCircle(drawableRect.centerX(), drawableRect.centerY(), radius, mImagePaint);
            return;
        }
        if (!TextUtils.isEmpty(mText)) {
            String viewText = getInitialByText(mText);
            mTextPaint.setColor(mTextColor);
            Rect textBound = new Rect();
            mTextPaint.setTextSize(getTextSizeToFit(drawableRectWidth - mTextMargin, drawableRectHeight - mTextMargin, viewText, mTextPaint));
            mTextPaint.getTextBounds(viewText, 0, viewText.length(), textBound);
            canvas.drawText(viewText, drawableRect.centerX() - textBound.exactCenterX(), drawableRect.centerY() - textBound.exactCenterY(), mTextPaint);
        }
    }

    public Bitmap getBitmapFromDrawable(Drawable drawable) {
        if (drawable == null) {
            return null;
        } else if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    private float getTextSizeToFit(float maxWidth, float maxHeight, String viewText, TextPaint textPaint) {
        float textSize = textPaint.getTextSize();
        int length = viewText.length();
        if (length == 0)
            return textSize;
        if (length == 1) {
            maxWidth = maxWidth - (maxWidth / 4);
        }
        Rect textBound = new Rect();
        textPaint.getTextBounds(viewText, 0, viewText.length(), textBound);
        float width = textBound.width();
        float height = textBound.height();
        float adjustX = maxWidth / width;
        float adjustY = maxHeight / height;
        textSize = textSize * (adjustY < adjustX ? adjustY : adjustX);
        return textSize;
    }
}
