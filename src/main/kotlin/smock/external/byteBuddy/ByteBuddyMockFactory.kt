package smock.external.byteBuddy

import net.bytebuddy.ByteBuddy
import net.bytebuddy.implementation.MethodDelegation
import net.bytebuddy.matcher.ElementMatchers.any
import smock.exception.SmockException
import smock.internal.CallValuesStorage
import smock.internal.MockFactory
import kotlin.reflect.KClass

class ByteBuddyMockFactory(
    private val callValuesStorage: CallValuesStorage,
    private val byteBuddy: ByteBuddy = ByteBuddy()
) : MockFactory {
    override fun <T : Any> mock(kClass: KClass<T>): T {
        if (!kClass.isFinal) {
            return byteBuddy.subclass(kClass.java)
                .method(any())
                .intercept(MethodDelegation.to(ByteBuddyMockInterceptor(callValuesStorage)))
                .make()
                .load(kClass.java.classLoader)
                .loaded
                .getConstructor()
                .newInstance()
        } else {
            throw SmockException("Not implemented for final")
        }
    }

    override fun <T : Any> spy(kClass: KClass<T>): T {
        if (!kClass.isFinal) {
            return byteBuddy.subclass(kClass.java)
                .method(any())
                .intercept(MethodDelegation.to(ByteBuddySpyInterceptor(callValuesStorage)))
                .make()
                .load(kClass.java.classLoader)
                .loaded
                .getConstructor()
                .newInstance()
        } else {
            throw SmockException("Not implemented for final")
        }
    }

}