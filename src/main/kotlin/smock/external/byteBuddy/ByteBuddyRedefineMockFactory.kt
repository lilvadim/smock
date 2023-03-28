package smock.external.byteBuddy

import net.bytebuddy.ByteBuddy
import net.bytebuddy.agent.ByteBuddyAgent
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy
import net.bytebuddy.implementation.MethodDelegation
import net.bytebuddy.matcher.ElementMatchers
import smock.internal.CallValuesStorage
import smock.internal.MockFactory
import java.lang.instrument.ClassDefinition
import java.lang.instrument.Instrumentation
import kotlin.reflect.KClass

class ByteBuddyRedefineMockFactory(
    private val callValuesStorage: CallValuesStorage,
    private val byteBuddy: ByteBuddy = ByteBuddy()
) : MockFactory {
    override fun <T : Any> mock(kClass: KClass<T>): T {
        val unloaded = byteBuddy.redefine(kClass.java)
            .method(ElementMatchers.any())
            .intercept(MethodDelegation.to(ByteBuddyMockInterceptor(callValuesStorage)))
            .make()

        val bytes = unloaded.bytes

        val newClass = unloaded
            .load(ClassLoader.getSystemClassLoader(), ClassLoadingStrategy.Default.CHILD_FIRST)
            .loaded

        instrumentation.redefineClasses(ClassDefinition(newClass, bytes))

        return kClass.java.getConstructor().newInstance()
    }

    override fun <T : Any> spy(kClass: KClass<T>): T {
        val unloaded = byteBuddy.redefine(kClass.java)
            .method(ElementMatchers.any())
            .intercept(MethodDelegation.to(ByteBuddySpyInterceptor(callValuesStorage)))
            .make()

        val bytes = unloaded.bytes

        val newClass = unloaded
            .load(ClassLoader.getSystemClassLoader(), ClassLoadingStrategy.Default.CHILD_FIRST)
            .loaded

        instrumentation.redefineClasses(ClassDefinition(newClass, bytes))

        return kClass.java.getConstructor().newInstance()
    }

    companion object StaticAgentInitializer {
        private val instrumentation: Instrumentation = ByteBuddyAgent.install()
    }
}