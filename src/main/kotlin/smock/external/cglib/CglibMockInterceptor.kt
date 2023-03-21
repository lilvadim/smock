package smock.external.cglib

import net.sf.cglib.proxy.MethodInterceptor
import net.sf.cglib.proxy.MethodProxy
import smock.internal.AbstractInterceptor
import smock.internal.CallData
import smock.internal.CallValuesStorage
import java.lang.reflect.Method

class CglibMockInterceptor(
    callValuesStorage: CallValuesStorage
) : AbstractInterceptor(callValuesStorage), MethodInterceptor {
    override fun intercept(obj: Any, method: Method?, args: Array<out Any>?, proxy: MethodProxy?): Any? {
        val callData = CallData(obj, method, args?.toList() ?: emptyList())
        callValuesStorage.lastCall = callData
        return callValuesStorage.registeredAction(callData)?.invoke()
    }
}