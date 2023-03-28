package smock.external.byteBuddy

import net.bytebuddy.ByteBuddy
import net.bytebuddy.implementation.MethodDelegation
import net.bytebuddy.matcher.ElementMatchers.any
import smock.internal.*
import kotlin.reflect.KClass

class ByteBuddySubclassMockFactory(
    private val callValuesStorage: CallValuesStorage,
    private val byteBuddy: ByteBuddy = ByteBuddy()
) : MockFactory {
    override fun <T : Any> mock(kClass: KClass<T>): T {
        val newInstance = createInstance(kClass)

        MethodDispatcher.registerInterceptor(
            CallerIdentifier(objRef = newInstance),
            MockInterceptor(callValuesStorage)
        )

        return newInstance
    }

    private fun <T : Any> createInstance(kClass: KClass<T>): T = byteBuddy.subclass(kClass.java)
        .method(any())
        .intercept(MethodDelegation.to(ByteBuddyMethodDispatcherAdapter::class.java))
        .make()
        .load(kClass.java.classLoader)
        .loaded
        .getConstructor()
        .newInstance()

    override fun <T : Any> spy(kClass: KClass<T>): T {
        val newInstance = createInstance(kClass)

        MethodDispatcher.registerInterceptor(
            CallerIdentifier(objRef = newInstance),
            SpyInterceptor(callValuesStorage)
        )

        return newInstance
    }
}