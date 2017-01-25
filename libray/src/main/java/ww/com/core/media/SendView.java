package ww.com.core.media;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import ww.com.core.R;
import ww.com.core.ScreenUtil;


/**
 * Created by feng on 2017/1/25.
 *
 */

public class SendView extends RelativeLayout {

    public RelativeLayout backLayout,selectLayout;
    public SendView(Context context) {
        super(context);
        init(context);
    }

    public SendView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SendView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    private void init(Context context){
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,180);
        setLayoutParams(params);
        RelativeLayout layout = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.item_send,null,false);
        layout.setLayoutParams(params);
        backLayout = (RelativeLayout) layout.findViewById(R.id.return_layout);
        selectLayout = (RelativeLayout) layout.findViewById(R.id.select_layout);
        ScreenUtil.scale(layout);
        addView(layout);
        setVisibility(GONE);
    }

    public void startAnim(){
        setVisibility(VISIBLE);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(
                ObjectAnimator.ofFloat(backLayout,"translationX",0,-260),
                ObjectAnimator.ofFloat(selectLayout,"translationX",0,260)
        );
        set.setDuration(250).start();
    }

    public void stopAnim(){
        AnimatorSet set = new AnimatorSet();
        set.playTogether(
                ObjectAnimator.ofFloat(backLayout,"translationX",-260,0),
                ObjectAnimator.ofFloat(selectLayout,"translationX",260,0)
        );
        set.setDuration(250).start();
        setVisibility(GONE);
    }

}
