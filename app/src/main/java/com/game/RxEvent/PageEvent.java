package com.game.RxEvent;

/**
 * Created by user on 2017/7/11.
 */
public class PageEvent {
    public enum MainFragmentEvent {
        UPDATE_SCORE,
        START_GAME
    }

    public enum GameViewEvent {
        ADD_RANDOM_NUMBER,
        CHECK_COMPLETE,
        REMOVE_SUBSCRIPT
    }
}
