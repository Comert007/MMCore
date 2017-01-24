package ww.com.core.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import ww.com.core.R;
import ww.com.core.ScreenUtil;

@SuppressWarnings("ResourceType")
public class TranslateTabBar extends RelativeLayout implements OnClickListener,
        AnimationListener {

    private static final int MATCH_PARENT = -1;

    private Context mContext;
    private int btnCount = 4;
    private int buttonWidth = 0;
    private int buttonHeight = LayoutParams.MATCH_PARENT;
    private int translateLineWidth = MATCH_PARENT;
    private int translateLineHeight = 10;
    private int dividLineWidth = 0;
    private int dividLineHeight = 64;
    private int currentTranslateLineIndex = 0;

    private int textSize = 40;

    private int splitLineColor = Color.parseColor("#ff1930");
    private int translateLineColor = Color.parseColor("#0b86ee");
    private int bottomLineColor = Color.parseColor("#f5f5f9");

    private LinearLayout buttonLayout;
    private Button[] tabButton;
    private String[] stringArr;
    private View translateLineView;

    private ColorStateList btnColorState = null;

    private OnTabChangeListener onChangelistener;
    private int viewWidth;

    public TranslateTabBar(Context context) {
        this(context, null);
    }

    public TranslateTabBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        getResourceForXml(attrs);
        initView();
//        initLine();
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        setCurrentIndex(v.getId());
    }

    private void initView() {
        buttonLayout = new LinearLayout(mContext);
        buttonLayout.setGravity(Gravity.CENTER_VERTICAL);
        LayoutParams p = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        p.addRule(RelativeLayout.CENTER_VERTICAL);
        this.addView(buttonLayout, p);

        this.setGravity(Gravity.CENTER_VERTICAL);

        buttonLayout.post(new Runnable() {
            @Override
            public void run() {
                setText(stringArr);
                initLine();
            }
        });
    }

    private void initLine() {
        if (MATCH_PARENT == translateLineWidth) {
            translateLineWidth = getWidth() / getCount();
        }
        LayoutParams p2 = new LayoutParams(translateLineWidth,
                translateLineHeight);
        p2.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        p2.leftMargin = getTranslateLineOffsetX(0);
        View tempView = createTranslateLine();
        this.addView(tempView, p2);

        LayoutParams p3 = new LayoutParams(LayoutParams.MATCH_PARENT, 2);
        p3.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        // this.addView(createBottomLine(), p3);

//        buttonWidth = ScreenUtil.getScalePxValue(buttonWidth);
//        translateLineWidth = ScreenUtil.getScalePxValue(translateLineWidth);

        setCurrentIndex(0);

//        ScreenUtil.scale(this);
    }

    private void getResourceForXml(AttributeSet attrs) {
        if (attrs == null)
            return;
        TypedArray array = mContext.getTheme().obtainStyledAttributes(attrs,
                R.styleable.TranslateTabBar, 0, 0);

        int textResId = array.getResourceId(R.styleable.TranslateTabBar_text,
                -1);
        translateLineWidth = array.getLayoutDimension(
                R.styleable.TranslateTabBar_lineWidth, translateLineWidth);
        if (MATCH_PARENT != translateLineWidth) {
            translateLineWidth = ScreenUtil.getScalePxValue(translateLineWidth);
        }
        btnColorState = array.getColorStateList(R.styleable.TranslateTabBar_textColor);
        splitLineColor = array.getColor(R.styleable.TranslateTabBar_spliteLineColor, splitLineColor);
        translateLineColor = array.getColor(R.styleable.TranslateTabBar_translateLineColor,
                translateLineColor);
        bottomLineColor = array.getColor(R.styleable.TranslateTabBar_bottomLineColor,
                bottomLineColor);
        textSize = array.getDimensionPixelSize(R.styleable.TranslateTabBar_textSize, textSize);
        translateLineHeight = array.getDimensionPixelOffset(R.styleable.TranslateTabBar_translateLineHeight,
                translateLineHeight);
        translateLineHeight = ScreenUtil.getScalePxValue(translateLineHeight);
        stringArr = getResources().getStringArray(textResId);
        array.recycle();
    }

    private void setText(String[] str) {
        buttonLayout.removeAllViews();
        btnCount = str.length;
        tabButton = new Button[btnCount];
        buttonWidth = btnCount <= 1 ? getWidth()
                : (getWidth() - dividLineWidth * (btnCount - 1)) / btnCount;
//        buttonWidth = ScreenUtil.getScalePxValue(buttonWidth);
        for (int i = 0; i < btnCount; i++) {
            LayoutParams params = new LayoutParams(buttonWidth, buttonHeight);
            buttonLayout.addView(createButton(str[i], i), params);
            if (btnCount > 1 && i < (btnCount - 1)) {
                LayoutParams params2 = new LayoutParams(dividLineWidth,
//                        (int) (dividLineHeight / ScreenUtil.getScale())
                        dividLineHeight
                );
                buttonLayout.addView(createLine(), params2);
            }
        }
    }

    private Button createButton(String str, int index) {
        Button v = new Button(mContext);
        if (Build.VERSION.SDK_INT >= 16) {
            v.setBackground(null);
        } else {
            v.setBackgroundDrawable(null);
        }
        v.setOnClickListener(this);
        v.setId(index);
        v.setText(String.format(str, "0"));
        v.setTextSize(TypedValue.COMPLEX_UNIT_PX, ScreenUtil.getScalePxValue(textSize));
        v.setTextColor(btnColorState);
        tabButton[index] = v;
        return v;
    }

    private View createLine() {
        View v = new View(mContext);
//        v.setBackgroundResource(splitLineColor);
        v.setBackgroundColor(splitLineColor);

        return v;
    }

    private View createTranslateLine() {
        View v = new View(mContext);
//        v.setBackgroundResource(translateLineColor);
        v.setBackgroundColor(translateLineColor);
        translateLineView = v;
        return v;
    }

    private View createBottomLine() {
        View v = new View(mContext);
//        v.setBackgroundResource(bottomLineColor);
        v.setBackgroundColor(bottomLineColor);
        return v;
    }


    /**
     * @param index 0 - ...
     * @return
     */
    private int getTranslateLineOffsetX(int index) {

        int offset = (buttonWidth - translateLineWidth) / 2 + buttonWidth
                * index;
        return offset;
    }

    private TranslateAnimation translateLine(int index) {
        int fromX = getTranslateLineOffsetX(currentTranslateLineIndex)
                - getTranslateLineOffsetX(0);
        int toX = getTranslateLineOffsetX(index) - getTranslateLineOffsetX(0);
        TranslateAnimation anim = new TranslateAnimation(fromX, toX, 0, 0);
        anim.setDuration(250);
        anim.setFillAfter(true);
        anim.setAnimationListener(this);

        currentTranslateLineIndex = index;

        return anim;
    }

    @Override
    public void onAnimationStart(Animation animation) {
        for (Button btn : tabButton) {
            btn.setClickable(false);
        }
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        for (Button btn : tabButton) {
            btn.setClickable(true);
        }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    public void setText(int index, String str) {
        tabButton[index].setText(String.format(stringArr[index], str));
    }

    public void setOnTabChangeListener(OnTabChangeListener _li) {
        onChangelistener = _li;
    }

    public int getCount() {
        return btnCount;
    }

    public void setCurrentIndex(int index) {
        if (index != currentTranslateLineIndex && onChangelistener != null) {
            onChangelistener.onChange(index);
        }
        if (translateLineView == null)
            return;

        translateLineView.startAnimation(translateLine(index));
        for (Button btn : tabButton) {
            btn.setSelected(false);
        }
        tabButton[index].setSelected(true);
        tabButton[index].setClickable(false);
    }

    public interface OnTabChangeListener {
        void onChange(int index);
    }
}
