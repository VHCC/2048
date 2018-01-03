package com.game.Helper;

import android.content.SharedPreferences;
import android.graphics.Point;
import android.util.Log;
import com.game.Activity.MainActivity;
import com.game.Config.Config;
import com.game.Data.CardMapCache;
import com.game.RxEvent.PageEvent;
import com.game.RxEvent.RxBus;

import java.util.ArrayList;
import java.util.List;

import static com.game.Config.Constants.SP_KEY_BEST_SCORE;

public class GameHelper {
    private final String TAG = getClass().getSimpleName() + "@" + Integer.toHexString(hashCode());

    public static GameHelper mGameHelper;

    /* Data */
    private int score = 0;
    private List<Point> emptyPoints = new ArrayList<Point>();

    public GameHelper() {
    }

    public static GameHelper getInstance() {
        if (mGameHelper == null) {
            mGameHelper = new GameHelper();
        }
        return mGameHelper;
    }

    //开始游戏（也是重新开始）
    public void startGame() {
        Log.i(TAG, "startGame()");
        GameHelper.getInstance().resetScore();

        for (int y = 0; y < Config.LINES; y++) {
            for (int x = 0; x < Config.LINES; x++) {
                CardMapCache.getInstance().getCardsMap()[x][y].setNum(0);
                if (CardMapCache.getInstance().getOldCardsMap()[x][y] != null) {
                    AnimationHelper.getInstance().recycleCard(CardMapCache.getInstance().getOldCardsMap()[x][y]);
                }
            }
        }

        addRandomNum();
        addRandomNum();
    }

    //添加随机卡片
    public synchronized void addRandomNum() {
        Log.d(TAG, "addRandomNum()");
        emptyPoints.clear();
        for (int y = 0; y < Config.LINES; y++) {
            for (int x = 0; x < Config.LINES; x++) {
                if (CardMapCache.getInstance().getCardsMap()[x][y].getNum() <= 0) {
                    emptyPoints.add(new Point(x, y));
                }
            }
        }

        if (emptyPoints.size() > 0) {
//            Log.d(TAG, "- emptyPoints.size()= " + emptyPoints.size());
            Point p = emptyPoints.remove((int) (Math.random() * emptyPoints.size()));
            Log.w(TAG, "p= " + p);
            CardMapCache.getInstance().getCardsMap()[p.x][p.y].setNum(Math.random() > 0.1 ? 2 : 4);

            AnimationHelper.getInstance().createScaleTo1(CardMapCache.getInstance().getCardsMap()[p.x][p.y], p.x, p.y);
        }
    }

    /* Feature */
    //获取最高分
    public int getBestScoreHistory() {
        return MainActivity.getInstance().getPreferences(MainActivity.getInstance().MODE_PRIVATE).getInt(SP_KEY_BEST_SCORE, 0);
    }

    public void saveBestScore(int s) {

        // 获取  偏好编辑器
        SharedPreferences.Editor e = MainActivity.getInstance().getPreferences(MainActivity.getInstance().MODE_PRIVATE).edit();

        //往编辑器中放东西
        e.putInt(SP_KEY_BEST_SCORE, s);

        //提交
        e.commit();
    }

    public void resetScore() {
        Log.d(TAG, "resetScore(), old= " + score);
        score = 0;
        updateScore(score);
    }

    public void updateScore(int s) {
//        Log.d(TAG, "updateScore(), s= " + s);
        score += s;
//        Log.d(TAG, "updateScore(), score= " + score);
        saveBestScore(getBestScoreCompare());
        RxBus.getInstance().send(PageEvent.MainFragmentEvent.UPDATE_SCORE);
    }

    public int getBestScoreCompare() {
        return Math.max(score, getBestScoreHistory());
    }

    public int getScore() {
//        Log.d(TAG, "getScore(), score= " + score);
        return score;
    }
}
