package com.game.Helper;

import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import com.game.Activity.Fragment.MainFragment;
import com.game.Config.Config;
import com.game.Data.CardMapCache;
import com.game.Model.Card;

public class AnimationHelper {
    private final String TAG = getClass().getSimpleName() + "@" + Integer.toHexString(hashCode());

    public static AnimationHelper mAnimationHelper;

    public AnimationHelper() {

    }

    public static AnimationHelper getInstance() {
        if (mAnimationHelper == null) {
            mAnimationHelper = new AnimationHelper();
        }
        return mAnimationHelper;
    }

    //回收卡片
    public synchronized void recycleCard(Card c) {
        //隐藏卡片
        c.setVisibility(View.INVISIBLE);

        //设置卡片动画为null
        c.setAnimation(null);
    }

    //新出现卡片的扩散动画
    public void createScaleTo1(Card targetCard, final int fromX, final int fromY) {
//		Log.d(TAG, "createScaleTo1(), targetCard= " + targetCard.getNum());

        //获取一张卡片
        final Card c = MainFragment.getInstance().getAnimLayer().getTempCard(targetCard.getNum());
        //设置左侧外边距  右侧外边距
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(Config.CARD_WIDTH, Config.CARD_WIDTH);
        lp.leftMargin = fromX * Config.CARD_WIDTH;
        lp.topMargin = fromY * Config.CARD_WIDTH;

        //应用设置
        c.setLayoutParams(lp);

        //扩散动画实体

//        fromX：起始X坐标上的伸缩尺寸。
//
//        toX：结束X坐标上的伸缩尺寸。
//
//        fromY：起始Y坐标上的伸缩尺寸。
//
//        toY：结束Y坐标上的伸缩尺寸。
//
//        pivotXType：X轴的伸缩模式，可以取值为ABSOLUTE、RELATIVE_TO_SELF、RELATIVE_TO_PARENT。
//
//        pivotXValue：X坐标的伸缩值。
//
//        pivotYType：Y轴的伸缩模式，可以取值为ABSOLUTE、RELATIVE_TO_SELF、RELATIVE_TO_PARENT。
//
//        pivotYValue：Y坐标的伸缩值。
        ScaleAnimation sa = new ScaleAnimation(0.1f, 1, 0.1f, 1,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,0.5f);
        //设置动画演示速度
        sa.setDuration(300);

        //卡片动画执行之后回收动画
        c.setAnimation(null);

        //设置动画监听器
        sa.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                if (CardMapCache.getInstance().getOldCardsMap()[fromX][fromY] != null) {
                    Log.d(TAG, ":recycleCard() oldCardsMap, fromX= " + fromX + ", fromY= " + fromY);
                    AnimationHelper.getInstance().recycleCard(CardMapCache.getInstance().getOldCardsMap()[fromX][fromY]);
                }
                CardMapCache.getInstance().getOldCardsMap()[fromX][fromY] = c;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }


            @Override
            public void onAnimationEnd(Animation animation) {
            }
        });

        //然后开始执行动画
        c.getLabel().startAnimation(sa);
        c.startAnimation(sa);
    }

    /* ----------------------- Animation ----------------------- */
    //创建移动动画
    public void createMoveAnim(final Card moveCard, final Card toCard, final int fromX, final int toX, final int fromY, final int toY) {
//		Log.d(TAG, "createMoveAnim()");

        //获取一张卡片
        final Card c = MainFragment.getInstance().getAnimLayer().getTempCard(moveCard.getNum());

        //设置左侧外边距  右侧外边距
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(Config.CARD_WIDTH, Config.CARD_WIDTH);
        lp.leftMargin = fromX * Config.CARD_WIDTH;
        lp.topMargin = fromY * Config.CARD_WIDTH;

        //应用设置
        c.setLayoutParams(lp);

        //如果卡片是0  将卡片隐藏
        if (toCard.getNum() <= 0) {
            toCard.getLabel().setVisibility(View.INVISIBLE);
        }

        //创建一个动画实体
        TranslateAnimation ta = new TranslateAnimation(0, Config.CARD_WIDTH
                * (toX - fromX), 0, Config.CARD_WIDTH * (toY - fromY));

        //设置动画播放速度
        ta.setDuration(100);

        //设置动画监听器
        ta.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                if (CardMapCache.getInstance().getOldCardsMap()[fromX][fromY] != null) {
                    Log.d(TAG, ":recycleCard() oldCardsMap, fromX= " + fromX + ", fromY= " + fromY);
                    AnimationHelper.getInstance().recycleCard(CardMapCache.getInstance().getOldCardsMap()[fromX][fromY]);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //获取一张卡片
                final Card cTo = MainFragment.getInstance().getAnimLayer().getTempCard(toCard.getNum());

                //设置左侧外边距  右侧外边距
                FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(Config.CARD_WIDTH, Config.CARD_WIDTH);
                lp.leftMargin = toX * Config.CARD_WIDTH;
                lp.topMargin = toY * Config.CARD_WIDTH;

                //应用设置
                cTo.setLayoutParams(lp);

                cTo.getLabel().setVisibility(View.VISIBLE);
                cTo.setVisibility(View.VISIBLE);
                if (CardMapCache.getInstance().getOldCardsMap()[toX][toY] != null) {
                    AnimationHelper.getInstance().recycleCard(CardMapCache.getInstance().getOldCardsMap()[toX][toY]);
                }
                CardMapCache.getInstance().getOldCardsMap()[toX][toY] = cTo;
                Log.d(TAG, ":add() oldCardsMap, toX= " + toX + ", toY= " + toY);
                AnimationHelper.getInstance().recycleCard(c);
            }
        });
        c.startAnimation(ta);
    }
}
