package com.game.CustomView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import com.game.Config.Config;
import com.game.Model.Card;

public class AnimLayer extends FrameLayout {
    private final String TAG = getClass().getSimpleName() + "@" + Integer.toHexString(hashCode());


    public AnimLayer(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public AnimLayer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AnimLayer(Context context) {
        super(context);
    }

    //获取要拷贝的动画的信息 以用来拷贝
    public Card getTempCard(int num) {
        final Card c = new Card(getContext());
        addView(c);
        c.setVisibility(View.VISIBLE);
        c.setNum(num);
        return c;
    }
}
