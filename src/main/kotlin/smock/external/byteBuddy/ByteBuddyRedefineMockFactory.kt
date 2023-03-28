package smock.external.byteBuddy

import net.bytebuddy.ByteBuddy
import net.bytebuddy.agent.ByteBuddyAgent
import net.bytebuddy.asm.Advice
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy
import net.bytebuddy.matcher.ElementMatchers.isMethod
import smock.internal.*
import kotlin.reflect.KClass

class ByteBuddyRedefineMockFactory(
    private val callValuesStorage: CallValuesStorage,
    private val byteBuddy: ByteBuddy = ByteBuddy()
) : MockFactory {

    override fun <T : Any> mock(kClass: KClass<T>): T {
        val newInstance = createInstance(kClass)

        MethodDispatcher.registerInterceptor(
            CallerIdentifier(objRef = newInstance, newInstance::class.java),
            MockInterceptor(callValuesStorage)
        )

        return newInstance
    }

    private fun <T : Any> createInstance(kClass: KClass<T>): T = byteBuddy.redefine(kClass.java)
        .visit(
            Advice.to(ByteBuddyMethodDispatcherAdapterJ::class.java)
                .on(isMethod())
        )
        .make()
        .load(kClass.java.classLoader, ClassReloadingStrategy.fromInstalledAgent())
        .loaded
        .getConstructor()
        .newInstance()

    override fun <T : Any> spy(kClass: KClass<T>): T {
        val newInstance = createInstance(kClass)

        MethodDispatcher.registerInterceptor(
            CallerIdentifier(objRef = newInstance, newInstance::class.java),
            SpyInterceptor(callValuesStorage)
        )

        return newInstance
    }

    companion object StaticAgentInitializer {
        init {
            ByteBuddyAgent.install()
        }
    }
}