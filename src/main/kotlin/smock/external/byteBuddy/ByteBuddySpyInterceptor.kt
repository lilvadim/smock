package smock.external.byteBuddy

import net.bytebuddy.implementation.bind.annotation.*
import smock.internal.AbstractInterceptor
import smock.internal.CallData
import smock.internal.CallValuesStorage
import java.lang.reflect.Method
import java.util.concurrent.Callable

class ByteBuddySpyInterceptor(
    callValuesStorage: CallValuesStorage
) : AbstractInterceptor(callValuesStorage), ByteBuddyInterceptor {
    @RuntimeType
    override fun intercept(
        @This obj: Any,
        @Origin method: Method,
        @AllArguments args: Array<Any?>,
        @SuperCall superMethod: Callable<Any?>,
    ): Any? {
        val callData = CallData(obj, method, args.toList())
        callValuesStorage.lastCall = callData
        return callValuesStorage.registeredAction(callData)?.invoke() ?: superMethod.call()
    }
}