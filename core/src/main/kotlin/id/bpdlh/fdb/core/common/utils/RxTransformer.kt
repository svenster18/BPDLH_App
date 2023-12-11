package id.bpdlh.fdb.core.common.utils

import io.reactivex.CompletableTransformer
import io.reactivex.FlowableTransformer
import io.reactivex.ObservableTransformer
import io.reactivex.SingleTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


fun <T> singleIo(): SingleTransformer<T, T> {
    return SingleTransformer { upstream ->
        upstream.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}

fun <T> flowableIo(): FlowableTransformer<T, T> {
    return FlowableTransformer { upstream ->
        upstream.subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
    }
}

fun completeIo(): CompletableTransformer {
    return CompletableTransformer { upstream ->
        upstream.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    }
}

fun <T> observableIo(): ObservableTransformer<T, T> {
    return ObservableTransformer {
        it.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}