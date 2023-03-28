package smock.external.byteBuddy.dynamicProxy

import net.bytebuddy.implementation.bind.annotation.*
import smock.internal.AbstractInterceptor
import smock.internal.CallData
import smock.internal.CallValuesStorage
import java.lang.reflect.Method
import java.util.concurrent.Callable

class ByteBuddySpyInterceptor(
    callValuesStorage: CallValuesStorage
) : AbstractInterceptor(callValuesStorage) {

    @RuntimeType
    fun intercept(
        @This obj: Any,
        @Origin method: Method,
        @AllArguments args: Array<Any?>,
        @SuperCall(nullIfImpossible = true) superMethod: Callable<Any?>?,
        @DefaultCall(nullIfImpossible = true) defaultMethod: Callable<Any?>?
    ): Any? {
        val callData = CallData(obj, method, args.toList())
        callValuesStorage.lastCall = callData
        val callback = callValuesStorage.callback(callData) ?: { (defaultMethod ?: superMethod)?.call() }
        return callback()
    }
}