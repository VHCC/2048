package com.game.Helper;

import com.game.Activity.Fragment.MainFragment;
import com.game.Data.CardMapCache;
import com.game.RxEvent.PageEvent;
import com.game.RxEvent.RxBus;
import com.game.Config.Config;

public class GameViewGestureHelper {
    private final String TAG = getClass().getSimpleName() + "@" + Integer.toHexString(hashCode());

    public static GameViewGestureHelper mGameViewGestureHelper;

    public static GameViewGestureHelper getInstance() {
        if (mGameViewGestureHelper == null) {
            mGameViewGestureHelper = new GameViewGestureHelper();
        }
        return mGameViewGestureHelper;
    }

    //向左移动
    public synchronized void swipeLeft() {

        boolean merge = false;

        for (int y = 0; y < Config.LINES; y++) {
            for (int x = 0; x < Config.LINES; x++) {
                for (int x1 = x + 1; x1 < Config.LINES; x1++) {

                    if (CardMapCache.getInstance().getCardsMap()[x1][y].getNum() > 0) {

                        if (CardMapCache.getInstance().getCardsMap()[x][y].getNum() <= 0) {

                            AnimationHelper
                                    .getInstance()
                                    .createMoveAnim(CardMapCache.getInstance().getCardsMap()[x1][y],
                                            CardMapCache.getInstance().getCardsMap()[x][y], x1, x, y, y);

                            CardMapCache.getInstance().getCardsMap()[x][y].setNum(CardMapCache.getInstance().getCardsMap()[x1][y].getNum());
                            CardMapCache.getInstance().getCardsMap()[x1][y].setNum(0);

                            x--;
                            merge = true;

                        } else if (CardMapCache.getInstance().getCardsMap()[x][y].equals(CardMapCache.getInstance().getCardsMap()[x1][y])) {
                            AnimationHelper
                                    .getInstance()
                                    .createMoveAnim(CardMapCache.getInstance().getCardsMap()[x1][y],
                                            CardMapCache.getInstance().getCardsMap()[x][y], x1, x, y, y);
                            CardMapCache.getInstance().getCardsMap()[x][y].setNum(CardMapCache.getInstance().getCardsMap()[x][y].getNum() * 2);
                            CardMapCache.getInstance().getCardsMap()[x1][y].setNum(0);

                            GameHelper.getInstance().updateScore(
                                    CardMapCache.getInstance().getCardsMap()[x][y].getNum());
                            merge = true;
                        }
                        break;
                    }
                }
            }
        }

        if (merge) {
            RxBus.getInstance().send(PageEvent.GameViewEvent.ADD_RANDOM_NUMBER);
            RxBus.getInstance().send(PageEvent.GameViewEvent.CHECK_COMPLETE);
        }
    }

    //向右移动
    public synchronized void swipeRight() {

        boolean merge = false;

        for (int y = 0; y < Config.LINES; y++) {
            for (int x = Config.LINES - 1; x >= 0; x--) {

                for (int x1 = x - 1; x1 >= 0; x1--) {
                    if (CardMapCache.getInstance().getCardsMap()[x1][y].getNum() > 0) {

                        if (CardMapCache.getInstance().getCardsMap()[x][y].getNum() <= 0) {
                            AnimationHelper
                                    .getInstance()
                                    .createMoveAnim(CardMapCache.getInstance().getCardsMap()[x1][y],
                                            CardMapCache.getInstance().getCardsMap()[x][y], x1, x, y, y);
                            CardMapCache.getInstance().getCardsMap()[x][y].setNum(CardMapCache.getInstance().getCardsMap()[x1][y].getNum());
                            CardMapCache.getInstance().getCardsMap()[x1][y].setNum(0);

                            x++;
                            merge = true;
                        } else if (CardMapCache.getInstance().getCardsMap()[x][y].equals(CardMapCache.getInstance().getCardsMap()[x1][y])) {
                            AnimationHelper
                                    .getInstance()
                                    .createMoveAnim(CardMapCache.getInstance().getCardsMap()[x1][y],
                                            CardMapCache.getInstance().getCardsMap()[x][y], x1, x, y, y);
                            CardMapCache.getInstance().getCardsMap()[x][y].setNum(CardMapCache.getInstance().getCardsMap()[x][y].getNum() * 2);
                            CardMapCache.getInstance().getCardsMap()[x1][y].setNum(0);
                            GameHelper.getInstance().updateScore(
                                    CardMapCache.getInstance().getCardsMap()[x][y].getNum());
                            merge = true;
                        }

                        break;
                    }
                }
            }
        }

        if (merge) {
            RxBus.getInstance().send(PageEvent.GameViewEvent.ADD_RANDOM_NUMBER);
            RxBus.getInstance().send(PageEvent.GameViewEvent.CHECK_COMPLETE);
        }
    }

    //向上移动
    public synchronized void swipeUp() {

        boolean merge = false;

        for (int x = 0; x < Config.LINES; x++) {
            for (int y = 0; y < Config.LINES; y++) {

                for (int y1 = y + 1; y1 < Config.LINES; y1++) {
                    if (CardMapCache.getInstance().getCardsMap()[x][y1].getNum() > 0) {

                        if (CardMapCache.getInstance().getCardsMap()[x][y].getNum() <= 0) {
                            AnimationHelper
                                    .getInstance()
                                    .createMoveAnim(CardMapCache.getInstance().getCardsMap()[x][y1],
                                            CardMapCache.getInstance().getCardsMap()[x][y], x, x, y1, y);
                            CardMapCache.getInstance().getCardsMap()[x][y].setNum(CardMapCache.getInstance().getCardsMap()[x][y1].getNum());
                            CardMapCache.getInstance().getCardsMap()[x][y1].setNum(0);

                            y--;

                            merge = true;
                        } else if (CardMapCache.getInstance().getCardsMap()[x][y].equals(CardMapCache.getInstance().getCardsMap()[x][y1])) {
                            AnimationHelper
                                    .getInstance()
                                    .createMoveAnim(CardMapCache.getInstance().getCardsMap()[x][y1],
                                            CardMapCache.getInstance().getCardsMap()[x][y], x, x, y1, y);
                            CardMapCache.getInstance().getCardsMap()[x][y].setNum(CardMapCache.getInstance().getCardsMap()[x][y].getNum() * 2);
                            CardMapCache.getInstance().getCardsMap()[x][y1].setNum(0);
                            GameHelper.getInstance().updateScore(
                                    CardMapCache.getInstance().getCardsMap()[x][y].getNum());
                            merge = true;
                        }

                        break;

                    }
                }
            }
        }

        if (merge) {
            RxBus.getInstance().send(PageEvent.GameViewEvent.ADD_RANDOM_NUMBER);
            RxBus.getInstance().send(PageEvent.GameViewEvent.CHECK_COMPLETE);
        }
    }

    //向下移动
    public synchronized void swipeDown() {

        boolean merge = false;

        for (int x = 0; x < Config.LINES; x++) {
            for (int y = Config.LINES - 1; y >= 0; y--) {

                for (int y1 = y - 1; y1 >= 0; y1--) {
                    if (CardMapCache.getInstance().getCardsMap()[x][y1].getNum() > 0) {

                        if (CardMapCache.getInstance().getCardsMap()[x][y].getNum() <= 0) {
                            AnimationHelper
                                    .getInstance()
                                    .createMoveAnim(CardMapCache.getInstance().getCardsMap()[x][y1],
                                            CardMapCache.getInstance().getCardsMap()[x][y], x, x, y1, y);
                            CardMapCache.getInstance().getCardsMap()[x][y].setNum(CardMapCache.getInstance().getCardsMap()[x][y1].getNum());
                            CardMapCache.getInstance().getCardsMap()[x][y1].setNum(0);

                            y++;
                            merge = true;
                        } else if (CardMapCache.getInstance().getCardsMap()[x][y].equals(CardMapCache.getInstance().getCardsMap()[x][y1])) {
                            AnimationHelper
                                    .getInstance()
                                    .createMoveAnim(CardMapCache.getInstance().getCardsMap()[x][y1],
                                            CardMapCache.getInstance().getCardsMap()[x][y], x, x, y1, y);
                            CardMapCache.getInstance().getCardsMap()[x][y].setNum(CardMapCache.getInstance().getCardsMap()[x][y].getNum() * 2);
                            CardMapCache.getInstance().getCardsMap()[x][y1].setNum(0);
                            GameHelper.getInstance().updateScore(
                                    CardMapCache.getInstance().getCardsMap()[x][y].getNum());
                            merge = true;
                        }
                        break;
                    }
                }
            }
        }
//        DialogUtils.getAddChartDialog(context, MainFragment.getMainFragment().getScore());

        if (merge) {
            RxBus.getInstance().send(PageEvent.GameViewEvent.ADD_RANDOM_NUMBER);
            RxBus.getInstance().send(PageEvent.GameViewEvent.CHECK_COMPLETE);
        }
    }

}
