package com.game.RxEvent;

import rx.Observer;

import java.util.concurrent.Callable;

/**
 * Created by Ichen on 2017/7/11.
 */
public interface JswObserver<T> extends Observer<T>, Callable {

}

