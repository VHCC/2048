package com.game.Utils;


import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.game.Activity.MainFragment;
import com.game.Model.Card;
import com.game.R;

import java.util.ArrayList;
import java.util.List;


public class GameView extends LinearLayout {
    private final String TAG = getClass().getSimpleName() + "@" + Integer.toHexString(hashCode());

    private Context context;
    private MediaPlayer player;
    private Card[][] cardsMap = new Card[Config.LINES][Config.LINES];
    private List<Point> emptyPoints = new ArrayList<Point>();

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

            private float startX
                    ,
                    startY
                    ,
                    offsetX
                    ,
                    offsetY;

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
                                swipeLeft();
                            } else if (offsetX > 5) {
                                player.start();
                                swipeRight();
                            }
                        } else {
                            if (offsetY < -5) {
                                player.start();
                                swipeUp();
                            } else if (offsetY > 5) {
                                player.start();
                                swipeDown();
                            }
                        }

                        break;
                }
                return true;
            }
        });
    }

    //初始化卡片大小
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
//        Log.d(TAG, "onSizeChanged(), w= " + w + ", h= " + h);
        super.onSizeChanged(w, h, oldw, oldh);

        Config.CARD_WIDTH = (Math.min(w, h) - 10) / Config.LINES;

        initialLayout(Config.CARD_WIDTH, Config.CARD_WIDTH);

        startGame();
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

                cardsMap[x][y] = c;
            }
        }
    }

    //开始游戏（也是重新开始）
    public void startGame() {
        Log.i(TAG, "startGame()");
        MainFragment aty = MainFragment.getMainFragment();
        aty.clearScore();
        aty.showBestScore(aty.getBestScore());

        for (int y = 0; y < Config.LINES; y++) {
            for (int x = 0; x < Config.LINES; x++) {
                cardsMap[x][y].setNum(0);
                if (MainFragment.getMainFragment().getAnimLayer().getOldCardsMap()[x][y] != null) {
                    MainFragment.getMainFragment().getAnimLayer().recycleCard(MainFragment.getMainFragment().getAnimLayer().getOldCardsMap()[x][y]);
                }
            }
        }

        addRandomNum();
        addRandomNum();
    }

    //添加随机卡片
    private void addRandomNum() {
        Log.d(TAG, "addRandomNum()");
        emptyPoints.clear();
        for (int y = 0; y < Config.LINES; y++) {
            for (int x = 0; x < Config.LINES; x++) {
                if (cardsMap[x][y].getNum() <= 0) {
                    emptyPoints.add(new Point(x, y));
                }
            }
        }

        if (emptyPoints.size() > 0) {
//            Log.d(TAG, "- emptyPoints.size()= " + emptyPoints.size());
            Point p = emptyPoints.remove((int) (Math.random() * emptyPoints.size()));
            Log.w(TAG, "p= " + p);
            cardsMap[p.x][p.y].setNum(Math.random() > 0.1 ? 2 : 4);

            MainFragment.getMainFragment().getAnimLayer().createScaleTo1(cardsMap[p.x][p.y], p.x, p.y);
        }
    }

    //向左移动
    private void swipeLeft() {
//        Log.d(TAG, "swipeLeft()");

        boolean merge = false;

        for (int y = 0; y < Config.LINES; y++) {
            for (int x = 0; x < Config.LINES; x++) {

                for (int x1 = x + 1; x1 < Config.LINES; x1++) {
                    if (cardsMap[x1][y].getNum() > 0) {

                        if (cardsMap[x][y].getNum() <= 0) {

                            MainFragment
                                    .getMainFragment()
                                    .getAnimLayer()
                                    .createMoveAnim(cardsMap[x1][y],
                                            cardsMap[x][y], x1, x, y, y);

                            cardsMap[x][y].setNum(cardsMap[x1][y].getNum());
                            cardsMap[x1][y].setNum(0);

                            x--;
                            merge = true;

                        } else if (cardsMap[x][y].equals(cardsMap[x1][y])) {
                            MainFragment
                                    .getMainFragment()
                                    .getAnimLayer()
                                    .createMoveAnim(cardsMap[x1][y],
                                            cardsMap[x][y], x1, x, y, y);
                            cardsMap[x][y].setNum(cardsMap[x][y].getNum() * 2);
                            cardsMap[x1][y].setNum(0);

                            MainFragment.getMainFragment().addScore(
                                    cardsMap[x][y].getNum());
                            merge = true;
                        }

                        break;
                    }
                }
            }
        }

        if (merge) {
            addRandomNum();
            checkComplete();
        }
    }

    //向右移动
    private void swipeRight() {
//        Log.d(TAG, "swipeRight()");

        boolean merge = false;

        for (int y = 0; y < Config.LINES; y++) {
            for (int x = Config.LINES - 1; x >= 0; x--) {

                for (int x1 = x - 1; x1 >= 0; x1--) {
                    if (cardsMap[x1][y].getNum() > 0) {

                        if (cardsMap[x][y].getNum() <= 0) {
                            MainFragment
                                    .getMainFragment()
                                    .getAnimLayer()
                                    .createMoveAnim(cardsMap[x1][y],
                                            cardsMap[x][y], x1, x, y, y);
                            cardsMap[x][y].setNum(cardsMap[x1][y].getNum());
                            cardsMap[x1][y].setNum(0);

                            x++;
                            merge = true;
                        } else if (cardsMap[x][y].equals(cardsMap[x1][y])) {
                            MainFragment
                                    .getMainFragment()
                                    .getAnimLayer()
                                    .createMoveAnim(cardsMap[x1][y],
                                            cardsMap[x][y], x1, x, y, y);
                            cardsMap[x][y].setNum(cardsMap[x][y].getNum() * 2);
                            cardsMap[x1][y].setNum(0);
                            MainFragment.getMainFragment().addScore(
                                    cardsMap[x][y].getNum());
                            merge = true;
                        }

                        break;
                    }
                }
            }
        }

        if (merge) {
            addRandomNum();
            checkComplete();
        }
    }

    //向上移动
    private void swipeUp() {
//        Log.d(TAG, "swipeUp()");

        boolean merge = false;

        for (int x = 0; x < Config.LINES; x++) {
            for (int y = 0; y < Config.LINES; y++) {

                for (int y1 = y + 1; y1 < Config.LINES; y1++) {
                    if (cardsMap[x][y1].getNum() > 0) {

                        if (cardsMap[x][y].getNum() <= 0) {
                            MainFragment
                                    .getMainFragment()
                                    .getAnimLayer()
                                    .createMoveAnim(cardsMap[x][y1],
                                            cardsMap[x][y], x, x, y1, y);
                            cardsMap[x][y].setNum(cardsMap[x][y1].getNum());
                            cardsMap[x][y1].setNum(0);

                            y--;

                            merge = true;
                        } else if (cardsMap[x][y].equals(cardsMap[x][y1])) {
                            MainFragment
                                    .getMainFragment()
                                    .getAnimLayer()
                                    .createMoveAnim(cardsMap[x][y1],
                                            cardsMap[x][y], x, x, y1, y);
                            cardsMap[x][y].setNum(cardsMap[x][y].getNum() * 2);
                            cardsMap[x][y1].setNum(0);
                            MainFragment.getMainFragment().addScore(
                                    cardsMap[x][y].getNum());
                            merge = true;
                        }

                        break;

                    }
                }
            }
        }

        if (merge) {
            addRandomNum();
            checkComplete();
        }
    }

    //向下移动
    private void swipeDown() {
//        Log.d(TAG, "swipeDown()");

        boolean merge = false;

        for (int x = 0; x < Config.LINES; x++) {
            for (int y = Config.LINES - 1; y >= 0; y--) {

                for (int y1 = y - 1; y1 >= 0; y1--) {
                    if (cardsMap[x][y1].getNum() > 0) {

                        if (cardsMap[x][y].getNum() <= 0) {
                            MainFragment
                                    .getMainFragment()
                                    .getAnimLayer()
                                    .createMoveAnim(cardsMap[x][y1],
                                            cardsMap[x][y], x, x, y1, y);
                            cardsMap[x][y].setNum(cardsMap[x][y1].getNum());
                            cardsMap[x][y1].setNum(0);

                            y++;
                            merge = true;
                        } else if (cardsMap[x][y].equals(cardsMap[x][y1])) {
                            MainFragment
                                    .getMainFragment()
                                    .getAnimLayer()
                                    .createMoveAnim(cardsMap[x][y1],
                                            cardsMap[x][y], x, x, y1, y);
                            cardsMap[x][y].setNum(cardsMap[x][y].getNum() * 2);
                            cardsMap[x][y1].setNum(0);
                            MainFragment.getMainFragment().addScore(
                                    cardsMap[x][y].getNum());
                            merge = true;
                        }

                        break;
                    }
                }
            }
        }

//        DialogUtils.getAddChartDialog(context, MainFragment.getMainFragment().getScore());

        if (merge) {
            addRandomNum();
            checkComplete();
        }
    }

    //检查是否完成
    private void checkComplete() {

        boolean complete = true;

        ALL:
        for (int y = 0; y < Config.LINES; y++) {
            for (int x = 0; x < Config.LINES; x++) {
                if (cardsMap[x][y].getNum() == 0
                        || (x > 0 && cardsMap[x][y].equals(cardsMap[x - 1][y]))
                        || (x < Config.LINES - 1 && cardsMap[x][y]
                        .equals(cardsMap[x + 1][y]))
                        || (y > 0 && cardsMap[x][y].equals(cardsMap[x][y - 1]))
                        || (y < Config.LINES - 1 && cardsMap[x][y]
                        .equals(cardsMap[x][y + 1]))) {

                    complete = false;
                    break ALL;
                }
            }
        }

        if (complete) {
            DialogUtils.getAddChartDialog(context, MainFragment.getMainFragment().getScore());
        }
    }

}
