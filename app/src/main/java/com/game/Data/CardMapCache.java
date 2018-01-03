package com.game.Data;

import com.game.Config.Config;
import com.game.Model.Card;

public class CardMapCache {
    private final String TAG = getClass().getSimpleName() + "@" + Integer.toHexString(hashCode());

    public static CardMapCache mCarMapCache;

    /* Constructor */
    public CardMapCache() {}

    public static CardMapCache getInstance() {
        if (mCarMapCache == null) {
            mCarMapCache = new CardMapCache();
        }
        return mCarMapCache;
    }

    /* old cards */
    private Card[][] oldCardsMap = new Card[Config.LINES][Config.LINES];

    public synchronized Card[][] getOldCardsMap() {
        return oldCardsMap;
    }

    /* real cards */
    private Card[][] cardsMap = new Card[Config.LINES][Config.LINES];

    public synchronized Card[][] getCardsMap() {
        return cardsMap;
    }
}
