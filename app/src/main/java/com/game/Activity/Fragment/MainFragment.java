package com.game.Activity.Fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.game.Helper.GameHelper;
import com.game.R;
import com.game.RxEvent.JswObserver;
import com.game.RxEvent.PageEvent;
import com.game.RxEvent.RxBus;
import com.game.CustomView.AnimLayer;
import com.game.CustomView.GameView;
import rx.Subscription;

public class MainFragment extends Fragment {
    private final String TAG = getClass().getSimpleName() + "@" + Integer.toHexString(hashCode());

    /* Static Object */
    public static MainFragment mainFragment;

    /* View */
    private TextView tvScore, tvBestScore;
    private LinearLayout rootLayout;
    private GameView gameView;
    private AnimLayer animLayer;

    /**
     * RxBus
     */
    private ObserverCall mObserverCall = new ObserverCall();
    private Subscription mSubscription;


    /* Constructor */
    public MainFragment() {
    }

    public static MainFragment getInstance() {
        if (mainFragment == null) {
            mainFragment = new MainFragment();
        }
        return mainFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //首先将布局放进来 因为是fragment  所以特殊一点  这么放
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        //然后获取一个容器  放中间的gameview  因为开源库的原因更改成fragment
        rootLayout = (LinearLayout) rootView.findViewById(R.id.rootLayout);

        //设置颜色
        rootLayout.setBackgroundColor(0xfffaf8ef);

        //初始化控件
        tvScore = (TextView) rootView.findViewById(R.id.tvScore);
        tvBestScore = (TextView) rootView.findViewById(R.id.tvBestScore);

        gameView = (GameView) rootView.findViewById(R.id.gameView);

        animLayer = (AnimLayer) rootView.findViewById(R.id.animLayer);

        mSubscription = RxBus.getInstance().doSubscribe(PageEvent.MainFragmentEvent.class, mObserverCall);

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mSubscription.unsubscribe();
        RxBus.getInstance().send(PageEvent.GameViewEvent.REMOVE_SUBSCRIPT);
    }


    private void showScore(int s) {
        tvScore.setText(s + "");
    }

    private void showBestScore(int s) {
        tvBestScore.setText(s + "");
    }

    public AnimLayer getAnimLayer() {
        return animLayer;
    }

    /**
     * RxBus Observer
     */
    private class ObserverCall implements JswObserver<PageEvent.MainFragmentEvent> {

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
        public void onNext(PageEvent.MainFragmentEvent eventEnum) {
            Log.d(TAG, "onNext= [" + eventEnum + "]");
            switch (eventEnum) {
                case UPDATE_SCORE: {
                    showScore(GameHelper.getInstance().getScore());
                    showBestScore(GameHelper.getInstance().getBestScoreHistory());
                }
                break;
                case START_GAME: {
                    GameHelper.getInstance().startGame();
                }
            }
        }
    }

}
