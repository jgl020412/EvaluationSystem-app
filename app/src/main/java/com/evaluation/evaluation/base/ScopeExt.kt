package com.evaluation.evaluation.base

import android.util.Log
import com.evaluation.evaluation.model.model.BaseModel
import com.evaluation.evaluation.util.MLog
import kotlinx.coroutines.*

/**
 * create by Rui on 2022/8/30
 * desc: 协程http 请求扩展
 */


/**
 * 当前协程未指定调度线程，恢复挂起的数据仍在当前线程中
 */
fun <T> CoroutineScope.http(
    request: (suspend () -> BaseModel<T>),
    response: (T?) -> Unit,
    error: (String) -> Unit = {},
    showToast: Boolean = true

): Job {
    return this.launch {
        try {
            val result = request()
            if (result.status == 200) {
                response(result.data)
            } else {
                showToast(showToast, result.msg)
                error(result.msg)
            }
        } catch (e: Exception) {
            showToast(showToast, e.message)
            error(e.message ?: "异常")
        }

    }
}


/**
 * 当前协程指定调度线程，恢复挂起的数据仍在当前指定的线程中
 */
fun <T> CoroutineScope.http2(
    request: (suspend () -> BaseModel<T>),
    response: (T?) -> Unit,
    dispatcher: CoroutineDispatcher = Dispatchers.Main,
    error: (String) -> Unit = {},
    showToast: Boolean = true

): Job {
    return this.launch(dispatcher) {
        try {
            val result = request()
            if (result.status == 200) {
                response(result.data)
            } else {
                showToast(showToast, result.msg)
                error(result.msg)
            }
        } catch (e: Exception) {
            showToast(showToast, e.message)
            error(e.message ?: "异常")
        }

    }
}

private const val TAG = "ScopeExt"

private fun showToast(isShow: Boolean, msg: String?) {
    MLog.e(TAG, "showToast: isShow:$isShow   msg:$msg")
}