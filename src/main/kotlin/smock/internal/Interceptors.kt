package smock.internal

import smock.exception.SmockException
import java.lang.reflect.Method

interface Interceptor {
    fun intercept(
        obj: Any?,
        method: Method,
        args: Array<Any?>,
        superMethod: (() -> Any?)?,
    ): Any?
}

abstract class AbstractInterceptor(
    protected val callValuesStorage: CallValuesStorage
) : Interceptor {
    protected fun reportNoAnswerRegistered(callData: CallData) {
        throw SmockException("No answer registered for ${callData.obj?.javaClass?.typeName}#${callData.method?.name}")
    }
}

class SpyInterceptor(callValuesStorage: CallValuesStorage) : AbstractInterceptor(callValuesStorage) {
    override fun intercept(
        obj: Any?,
        method: Method,
        args: Array<Any?>,
        superMethod: (() -> Any?)?,
    ): Any? {
        val callData = CallData(obj, method, args.toList())
        callValuesStorage.lastCall = callData
        val callback = callValuesStorage.callback(callData) ?: { superMethod?.invoke() }
        return callback()
    }
}

class MockInterceptor(callValuesStorage: CallValuesStorage) : AbstractInterceptor(callValuesStorage) {
    override fun intercept(
        obj: Any?,
        method: Method,
        args: Array<Any?>,
        superMethod: (() -> Any?)?
    ): Any? {
        val callData = CallData(obj, method, args.toList())
        callValuesStorage.lastCall = callData
        val callback = callValuesStorage.callback(callData) ?: if (!callValuesStorage.isRecordingCall()) {
            { reportNoAnswerRegistered(callData) }
        } else {
            { null }
        }
        return callback()
    }
}