package smock.external.byteBuddy

import net.bytebuddy.implementation.bind.annotation.AllArguments
import net.bytebuddy.implementation.bind.annotation.Origin
import net.bytebuddy.implementation.bind.annotation.RuntimeType
import net.bytebuddy.implementation.bind.annotation.This
import smock.internal.AbstractInterceptor
import smock.internal.CallData
import smock.internal.CallValuesStorage
import java.lang.reflect.Method

class ByteBuddyMockInterceptor(callValuesStorage: CallValuesStorage) : AbstractInterceptor(callValuesStorage) {

    @RuntimeType
    fun intercept(
        @This obj: Any,
        @Origin method: Method,
        @AllArguments args: Array<Any?>
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