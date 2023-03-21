package smock.external.cglib

import net.sf.cglib.proxy.MethodInterceptor
import net.sf.cglib.proxy.MethodProxy
import smock.internal.CallValuesStorage
import java.lang.reflect.Method

class CglibSmockInterceptor(
    private val callValuesStorage: CallValuesStorage
) : MethodInterceptor {
    override fun intercept(obj: Any?, method: Method?, args: Array<out Any>?, proxy: MethodProxy?): Any? {
        val callData = CallValuesStorage.CallData(obj, method, args?.toList() ?: emptyList())
        callValuesStorage.lastCall = callData

        val returnValue = callValuesStorage.returnValue(callData)
        val answerFunction = callValuesStorage.answersFunction(callData)
        val throwable = callValuesStorage.throwable(callData)

        return returnValue ?: answerFunction?.invoke() ?: throw throwable ?: return null
    }
}