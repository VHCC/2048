package com.game.CustomView;


import android.content.Context;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import com.game.Config.Config;
import com.game.Data.CardMapCache;
import com.game.Helper.AnimationHelper;
import com.game.Helper.GameHelper;
import com.game.Helper.GameViewGestureHelper;
import com.game.Model.Card;
import com.game.R;
import com.game.RxEvent.JswObserver;
import com.game.RxEvent.PageEvent;
import com.game.RxEvent.RxBus;
import com.game.Utils.DialogUtils;
import rx.Subscription;

import java.util.ArrayList;
import java.util.List;


public class GameView extends LinearLayout {
    private final String TAG = getClass().getSimpleName() + "@" + Integer.toHexString(hashCode());

    private Context context;
    private MediaPlayer player;

    /**
     * RxBus
     */
    private ObserverCall mObserverCall = new ObserverCall();
    private Subscription mSubscription;


    public GameView(Context context) {
        super(context);
        this.context = context;
        player = MediaPlayer.create(context, R.raw.move);
        initGameView();
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        player = MediaPlayer.create(context, R.raw.move);
        initGameView();
    }


    //初始化Gameview
    private void initGameView() {
        Log.d(TAG, "initGameView()");
        setOrientation(LinearLayout.VERTICAL);
        setBackgroundColor(0xffbbada0);
        setOnTouchListener(new OnTouchListener() {

            private float startX, startY, offsetX, offsetY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = event.getX();
                        startY = event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        offsetX = event.getX() - startX;
                        offsetY = event.getY() - startY;

                        if (Math.abs(offsetX) > Math.abs(offsetY)) {
                            if (offsetX < -5) {
                                player.start();
                                GameViewGestureHelper.getInstance().swipeLeft();
                            } else if (offsetX > 5) {
                                player.start();
                                GameViewGestureHelper.getInstance().swipeRight();
                            }
                        } else {
                            if (offsetY < -5) {
                                player.start();
                                GameViewGestureHelper.getInstance().swipeUp();
                            } else if (offsetY > 5) {
                                player.start();
                                GameViewGestureHelper.getInstance().swipeDown();
                            }
                        }
                        break;
                }
                return true;
            }
        });

        mSubscription = RxBus.getInstance().doSubscribe(PageEvent.GameViewEvent.class, mObserverCall);

    }

    //初始化卡片大小
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        Log.d(TAG, "onSizeChanged(), w= " + w + ", h= " + h);
        super.onSizeChanged(w, h, oldw, oldh);

        Config.CARD_WIDTH = (Math.min(w, h) - 10) / Config.LINES;

        initialLayout(Config.CARD_WIDTH, Config.CARD_WIDTH);

    }


    //添加卡片
    private void initialLayout(int cardWidth, int cardHeight) {
        Log.d(TAG, "initialLayout(), cardWidth= " + cardWidth + ", cardHeight= " + cardHeight);
        Card c;

        LinearLayout line;
        LayoutParams lineLp;

        for (int y = 0; y < Config.LINES; y++) {
            line = new LinearLayout(getContext());
            lineLp = new LayoutParams(-1, cardHeight);
            addView(line, lineLp);

            for (int x = 0; x < Config.LINES; x++) {
                c = new Card(getContext());
                line.addView(c, cardWidth, cardHeight);
                CardMapCache.getInstance().getCardsMap()[x][y] = c;
            }
        }

        RxBus.getInstance().send(PageEvent.MainFragmentEvent.START_GAME);
    }


    //检查是否完成
    private void checkComplete() {

        boolean complete = true;

        ALL:
        for (int y = 0; y < Config.LINES; y++) {
            for (int x = 0; x < Config.LINES; x++) {
                if (CardMapCache.getInstance().getCardsMap()[x][y].getNum() == 0
                        || (x > 0 && CardMapCache.getInstance().getCardsMap()[x][y].equals(CardMapCache.getInstance().getCardsMap()[x - 1][y]))
                        || (x < Config.LINES - 1 && CardMapCache.getInstance().getCardsMap()[x][y]
                        .equals(CardMapCache.getInstance().getCardsMap()[x + 1][y]))
                        || (y > 0 && CardMapCache.getInstance().getCardsMap()[x][y].equals(CardMapCache.getInstance().getCardsMap()[x][y - 1]))
                        || (y < Config.LINES - 1 && CardMapCache.getInstance().getCardsMap()[x][y]
                        .equals(CardMapCache.getInstance().getCardsMap()[x][y + 1]))) {

                    complete = false;
                    break ALL;
                }
            }
        }

        if (complete) {
            DialogUtils.getAddChartDialog(context, GameHelper.getInstance().getScore());
        }
    }

    /**
     * RxBus Observer
     */
    private class ObserverCall implements JswObserver<PageEvent.GameViewEvent> {

        @Override
        public Object call() throws Exception {
            return null;
        }

        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onNext(PageEvent.GameViewEvent eventEnum) {
            Log.d(TAG, "onNext= [" + eventEnum + "]");
            switch (eventEnum) {
                case ADD_RANDOM_NUMBER: {
                    GameHelper.getInstance().addRandomNum();
                }
                break;
                case CHECK_COMPLETE: {
                    checkComplete();
                }
                break;
                case REMOVE_SUBSCRIPT: {
                    mSubscription.unsubscribe();
                }
            }
        }
    }

}
