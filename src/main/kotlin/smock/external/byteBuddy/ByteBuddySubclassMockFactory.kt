package smock.external.byteBuddy

import net.bytebuddy.ByteBuddy
import net.bytebuddy.implementation.MethodDelegation
import net.bytebuddy.matcher.ElementMatchers.any
import smock.internal.CallValuesStorage
import smock.internal.MockFactory
import kotlin.reflect.KClass

class ByteBuddySubclassMockFactory(
    private val callValuesStorage: CallValuesStorage,
    private val byteBuddy: ByteBuddy = ByteBuddy()
) : MockFactory {
    override fun <T : Any> mock(kClass: KClass<T>): T {
        return byteBuddy.subclass(kClass.java)
            .method(any())
            .intercept(MethodDelegation.to(ByteBuddyMockInterceptor(callValuesStorage)))
            .make()
            .load(kClass.java.classLoader)
            .loaded
            .getConstructor()
            .newInstance()
    }

    override fun <T : Any> spy(kClass: KClass<T>): T {
        return byteBuddy.subclass(kClass.java)
            .method(any())
            .intercept(MethodDelegation.to(ByteBuddySpyInterceptor(callValuesStorage)))
            .make()
            .load(kClass.java.classLoader)
            .loaded
            .getConstructor()
            .newInstance()
    }
}