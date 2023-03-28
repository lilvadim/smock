package smock.internal

import java.lang.reflect.Method
import java.util.logging.Logger

object MethodDispatcher {
    private val interceptorsStorage: MutableMap<CallerIdentifier, Interceptor> = mutableMapOf()

    @JvmStatic
    fun delegate(
        callerIdentifier: CallerIdentifier,
        obj: Any?,
        method: Method,
        args: Array<Any?>,
        superMethod: (() -> Any?)?,
    ): Any? {
        val interceptor = interceptorsStorage[callerIdentifier]
        if (interceptor != null) {
            Logger.getGlobal().info("Interceptor found")
            return interceptor.intercept(obj, method, args, superMethod)
        } else {
            throw IllegalStateException("No interceptor found for $method")
        }
    }

    fun registerInterceptor(callerIdentifier: CallerIdentifier, interceptor: Interceptor) {
        interceptorsStorage[callerIdentifier] = interceptor
    }
}