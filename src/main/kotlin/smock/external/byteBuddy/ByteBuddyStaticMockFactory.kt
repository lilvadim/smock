package smock.external.byteBuddy

import Source
import net.bytebuddy.ByteBuddy
import net.bytebuddy.agent.ByteBuddyAgent
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy
import net.bytebuddy.implementation.MethodDelegation
import net.bytebuddy.matcher.ElementMatchers
import smock.internal.*
import kotlin.reflect.KClass

class ByteBuddyStaticMockFactory(
    private val callValuesStorage: CallValuesStorage,
) : StaticMock {
    private val byteBuddy = ByteBuddy()
    override fun mock(kClass: KClass<*>) {

        MethodDispatcher.registerInterceptor(
            CallerIdentifier(objRef = kClass.java, kClass.java),
            MockInterceptor(callValuesStorage)
        )

        byteBuddy
            .redefine(kClass.java)
            .method(ElementMatchers.isStatic())
            .intercept(MethodDelegation.to(StaticAdapter::class.java))
            .make()
            .load(Source::class.java.classLoader, ClassReloadingStrategy.fromInstalledAgent())
    }

    override fun unmock(kClass: KClass<*>) {
        TODO("Not yet implemented")
    }

    companion object StaticAgentInitializer {
        init {
            ByteBuddyAgent.install()
        }
    }

}