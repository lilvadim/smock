package smock.external.byteBuddy

import net.bytebuddy.ByteBuddy
import net.bytebuddy.agent.ByteBuddyAgent
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy
import net.bytebuddy.implementation.MethodDelegation
import net.bytebuddy.matcher.ElementMatchers
import smock.internal.CallValuesStorage
import smock.internal.MockFactory
import kotlin.reflect.KClass

class ByteBuddyRedefineMockFactory(
    private val callValuesStorage: CallValuesStorage,
    private val byteBuddy: ByteBuddy = ByteBuddy()
) : MockFactory {
    override fun <T : Any> mock(kClass: KClass<T>): T {
        return byteBuddy.redefine(kClass.java)
            .method(ElementMatchers.any())
            .intercept(MethodDelegation.to(ByteBuddyMockInterceptor(callValuesStorage)))
            .make()
            .load(ClassLoader.getSystemClassLoader(), ClassLoadingStrategy.Default.INJECTION)
            .loaded
            .getConstructor()
            .newInstance()
    }

    override fun <T : Any> spy(kClass: KClass<T>): T {
        TODO("Not yet implemented")
        return byteBuddy.redefine(kClass.java)
            .method(ElementMatchers.any())
            .intercept(MethodDelegation.to(ByteBuddySpyInterceptor(callValuesStorage)))
            .make()
            .load(kClass.java.classLoader)
            .loaded
            .getConstructor()
            .newInstance()
    }

    companion object StaticAgentInitializer {
        init {
            ByteBuddyAgent.install()
        }
    }
}