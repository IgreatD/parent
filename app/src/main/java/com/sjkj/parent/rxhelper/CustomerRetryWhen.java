package com.sjkj.parent.rxhelper;

import android.util.Log;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;

/**
 * @author by dingl on 2017/11/1.
 * @desc CustomerRetryWhen
 */

public class CustomerRetryWhen implements Func1<Observable<? extends Throwable>, Observable<?>> {

    @Override
    public Observable<?> call(Observable<? extends Throwable> observable) {
        return observable.zipWith(Observable.range(1, 4), (Func2<Throwable, Integer, Object>) CombineClass::new).flatMap((Func1<Object, Observable<?>>) o -> {
            CombineClass combineClass = (CombineClass) o;
            if (combineClass.throwable instanceof ConnectException
                    || combineClass.throwable instanceof SocketTimeoutException
                    || combineClass.throwable instanceof TimeoutException
                    || combineClass.integer < 4) {
                Log.d("CustomerRetryWhen", "重试");
                return Observable.timer(5, TimeUnit.SECONDS);
            }
            return Observable.error(combineClass.throwable);
        });
    }

    public class CombineClass {
        Throwable throwable;
        Integer integer;

        CombineClass(Throwable throwable, Integer integer) {
            this.throwable = throwable;
            this.integer = integer;
        }
    }
}
