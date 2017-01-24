package ww.com.core.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.NumberKeyListener;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import ww.com.core.R;
import ww.com.core.ScreenUtil;

/**
 * 格子密码输入框
 * Created by fighter on 2016/5/19.
 */
public class GridPasswordView extends LinearLayout implements View.OnClickListener,
        TextWatcher {
    private static final String DEFAULT_TRANSFORMATION = "●";
    private static final int MAX_PASS_LEN = 6;

    private EditText editPwdInput;
    private TextView[] txtViPwdList;

    private int storkColor = Color.GRAY;  // 边框|分割线颜色
    private int backgroundColor = Color.WHITE; // 背景颜色
    private int roundRadius = 0;
    private int stockWidth = 2;
    private int txtColor = Color.BLACK;
    private int textSize = ScreenUtil.getScalePxValue(64);
    private String passType = DEFAULT_TRANSFORMATION;

    private OnPassChangeListener passChangeListener;

    public GridPasswordView(Context context, AttributeSet attrs) {
        super(context, attrs);

        initAttrs(attrs);
        initViews();
    }

    @Override
    public void afterTextChanged(Editable s) {
        String pass = getText().trim();
        if (pass.length() > txtViPwdList.length) {
            pass = pass.substring(0, txtViPwdList.length);
        }
        for (int i = 0; i < pass.length(); i++) {
            txtViPwdList[i].setText(passType);
        }

        for (int i = txtViPwdList.length; i > pass.length(); i--) {
            txtViPwdList[i - 1].setText(null);
        }
        if (passChangeListener != null) {
            passChangeListener.onChange(pass);
        }
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void onClick(View v) {
        regFocus();
    }

    public void clear() {
        editPwdInput.setText("");
        for (int i = 0; i < txtViPwdList.length; i++) {
            txtViPwdList[i].setText(null);
        }
        regFocus();
    }

    public String getText() {
        return editPwdInput.getText().toString().trim();
    }

    public void regFocus() {
        editPwdInput.setFocusable(true);
        editPwdInput.setFocusableInTouchMode(true);
        editPwdInput.requestFocus();
        editPwdInput.setSelection(editPwdInput.getText().length());

        InputMethodManager im = (InputMethodManager) getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        im.showSoftInput(editPwdInput, InputMethodManager.SHOW_IMPLICIT);
    }

    public void setOnPassChangeListener(OnPassChangeListener _listener) {
        passChangeListener = _listener;
    }

    private void initAttrs(AttributeSet _attrs) {
        TypedArray a = getContext().getTheme().obtainStyledAttributes(_attrs
                , R.styleable.GridPasswordView, 0, 0);
        try {
            passType = a.getString(R.styleable.GridPasswordView_pass_type);
            if (TextUtils.isEmpty(passType)) {
                passType = DEFAULT_TRANSFORMATION;
            }
            txtColor = a.getColor(R.styleable.GridPasswordView_text_color, txtColor);
//            backgroundColor = a.getColor(R.styleable.GridPasswordView_vi_bg_color, backgroundColor);
            storkColor = a.getColor(R.styleable.GridPasswordView_stork_color, storkColor);
            roundRadius = a.getDimensionPixelSize(R.styleable.GridPasswordView_radius, roundRadius);
            roundRadius = ScreenUtil.getScalePxValue(roundRadius);
            stockWidth = a.getDimensionPixelSize(R.styleable.GridPasswordView_stork_with, stockWidth);
            stockWidth = ScreenUtil.getScalePxValue(stockWidth);
        } finally {
            a.recycle();
        }
    }

    private void initViews() {
        setOrientation(HORIZONTAL);
        setOnClickListener(this);

        editPwdInput = new EditText(getContext());
        LayoutParams params =
                new LayoutParams(1, 1);
        editPwdInput.setLayoutParams(params);
        editPwdInput.setFilters(new InputFilter[]{new InputFilter.LengthFilter(MAX_PASS_LEN)});
        editPwdInput.addTextChangedListener(this);
        editPwdInput.setKeyListener(new NumberKeyListener() {
            @Override
            protected char[] getAcceptedChars() {
                return new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
            }

            @Override
            public int getInputType() {
                return EditorInfo.TYPE_CLASS_NUMBER;
            }
        });

        addView(editPwdInput);

        txtViPwdList = new TextView[MAX_PASS_LEN];
        for (int i = 0; i < txtViPwdList.length; i++) {
            txtViPwdList[i] = createPassTextView();

            addView(txtViPwdList[i]);
            if (i < txtViPwdList.length - 1) {
                addView(createSplitLineView());
            }
        }

        Drawable drawable = createShapeDrawable();
        if (Build.VERSION.SDK_INT >= 16)
            setBackground(drawable);
        else {
            setBackgroundDrawable(drawable);
        }
    }

    private TextView createPassTextView() {
        TextView txtVi = new TextView(getContext());
        LayoutParams params =
                new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        params.weight = 1;
        txtVi.setTextColor(txtColor);
        txtVi.setLayoutParams(params);
        txtVi.setGravity(Gravity.CENTER);
        txtVi.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        return txtVi;
    }

    private View createSplitLineView() {
        View vi = new View(getContext());
        LayoutParams params =
                new LayoutParams(2, ViewGroup.LayoutParams.MATCH_PARENT);
        vi.setLayoutParams(params);
        vi.setBackgroundColor(storkColor);
        return vi;
    }

    private Drawable createShapeDrawable() {
        GradientDrawable drawable = new GradientDrawable();//创建drawable

        drawable.setColor(backgroundColor);
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setCornerRadius(roundRadius);
        drawable.setStroke(stockWidth, storkColor);

        return drawable;
    }

    public interface OnPassChangeListener {
        void onChange(String pass);
    }
}
