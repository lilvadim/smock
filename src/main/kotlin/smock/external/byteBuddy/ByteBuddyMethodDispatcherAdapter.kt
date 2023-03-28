package smock.external.byteBuddy

import net.bytebuddy.implementation.bind.annotation.*
import smock.internal.CallerIdentifier
import smock.internal.MethodDispatcher
import java.lang.reflect.Method
import java.util.concurrent.Callable

object ByteBuddyMethodDispatcherAdapter {

    @JvmStatic
    @RuntimeType
    fun interceptDynamic(
        @This obj: Any?,
        @Origin method: Method,
        @AllArguments args: Array<Any?>,
        @SuperCall(nullIfImpossible = true) superMethod: Callable<Any?>?,
    ): Any? {
        val superMethodCasted = { superMethod?.call() }
        return MethodDispatcher.delegate(CallerIdentifier(objRef = obj), obj, method, args, superMethodCasted)
    }
}