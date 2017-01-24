package ww.com.core.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.Selection;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;


public class SeePassEditText extends EditText {
    /**
     * 查看密码图片
     */
    private Drawable mSeePwdDrawable;

    private boolean seePwd = false;

    public SeePassEditText(Context context) {
        this(context, null);
    }

    public SeePassEditText(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.editTextStyle);
    }

    public SeePassEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }


    private void init() {
        mSeePwdDrawable = getCompoundDrawables()[2];
        if (mSeePwdDrawable == null) {
            throw new NullPointerException("You can add drawableRight attribute in XML");
        }
        mSeePwdDrawable.setBounds(0, 0, mSeePwdDrawable.getIntrinsicWidth(),
                mSeePwdDrawable.getIntrinsicHeight());
    }


    public void switchSeePwd() {
        seePwd = isSelected();
        if (seePwd) {
            seePwd = false;
            setTransformationMethod(PasswordTransformationMethod
                    .getInstance());
        } else {
            seePwd = true;
            setTransformationMethod(HideReturnsTransformationMethod
                    .getInstance());
        }
        setSelected(seePwd);
//        if (mSeePwdDrawable instanceof StateListDrawable) {
//            StateListDrawable sld = ((StateListDrawable) mSeePwdDrawable);
//            if (seePwd) {
//                sld.setState(new int[]{android.R.attr.state_selected});
//            } else {
//                sld.setState(new int[]{-android.R.attr.state_selected});
//            }
//            refreshDrawableState();
//        }


        Editable etext = getText();
        Selection.setSelection(etext, etext.length());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (getCompoundDrawables()[2] != null) {

                boolean touchable = event.getX() > (getWidth() - getTotalPaddingRight())
                        && (event.getX() < ((getWidth() - getPaddingRight())));
                if (touchable) {
                    switchSeePwd();
                }
            }
        }

        return super.onTouchEvent(event);
    }
}
