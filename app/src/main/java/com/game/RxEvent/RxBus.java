package com.game.RxEvent;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;

/**
 * Created by Ichen on 2017/7/11.
 */
public class RxBus {
    private final String TAG = getClass().getSimpleName() + "@" + Integer.toHexString(hashCode());

    private static volatile RxBus mInstance;
    private SerializedSubject<Object, Object> mSubject;

    public RxBus() {
        mSubject = PublishSubject.create().toSerialized();
    }

    public static RxBus getInstance() {
        if (mInstance == null) {
            synchronized (RxBus.class) {
                mInstance = new RxBus();
            }
        }
        return mInstance;
    }

    /**
     * A standard subscribe methods.
     *
     * @param type
     * @param observer
     * @param <T>
     * @return
     */
    public <T> Subscription doSubscribe(Class<T> type, JswObserver<T> observer) {
        /**
         * Operating Task before OnNext Task, when Observer had been subscribed.
         */
        Observable.fromCallable(observer);

        return toObservable(type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    /**
     * Sent Event
     *
     * @param o
     */
    public void send(Object o) {
        mSubject.onNext(o);
    }

    private <T> Observable<T> toObservable(final Class<T> type) {
        return mSubject.ofType(type);
    }

    /**
     * To Check Whether Observers exist in Observable or not.
     *
     * @return
     */
    public boolean hasObservers() {
        return mSubject.hasObservers();
    }

}
