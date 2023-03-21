package smock.external.byteBuddy

import net.bytebuddy.implementation.bind.annotation.*
import java.lang.reflect.Method
import java.util.concurrent.Callable

interface ByteBuddyInterceptor {
    @RuntimeType
    fun intercept(
        @This obj: Any,
        @Origin method: Method,
        @AllArguments args: Array<Any?>,
        @SuperCall superMethod: Callable<Any?>,
    ): Any?
}