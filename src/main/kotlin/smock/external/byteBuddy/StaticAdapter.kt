package smock.external.byteBuddy

import net.bytebuddy.implementation.bind.annotation.*
import smock.internal.CallerIdentifier
import smock.internal.MethodDispatcher

object StaticAdapter {
    @JvmStatic
    @RuntimeType
    fun intercept(@Origin clazz: Class<*>?, @AllArguments args: Array<Any?>): Any? {
        val nameofCurrMethod = Throwable()
            .stackTrace[1]
            .methodName
        val types = args.map { a -> a?.javaClass }
        val method = clazz!!.getMethod(nameofCurrMethod, *types.toTypedArray())
        return MethodDispatcher.delegate(CallerIdentifier(objRef = clazz), obj = null, method = method, args = args, superMethod = null)
    }
}
