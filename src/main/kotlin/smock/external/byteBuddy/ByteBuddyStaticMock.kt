package smock.external.byteBuddy

import net.bytebuddy.ByteBuddy
import net.bytebuddy.agent.ByteBuddyAgent
import net.bytebuddy.dynamic.DynamicType
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy
import net.bytebuddy.implementation.MethodDelegation
import net.bytebuddy.matcher.ElementMatchers
import smock.internal.*
import kotlin.reflect.KClass

class ByteBuddyStaticMock(
    private val callValuesStorage: CallValuesStorage,
) : StaticMock {
    private val byteBuddy = ByteBuddy()
    private val classMap = hashMapOf<KClass<*>, DynamicType.Unloaded<out Any>?>()
    override fun mock(kClass: KClass<*>) {

        MethodDispatcher.registerInterceptor(
            CallerIdentifier(objRef = kClass.java, kClass.java),
            MockInterceptor(callValuesStorage)
        )
        if (!classMap.containsKey(kClass)) {
            classMap[kClass] = byteBuddy.redefine(kClass.java).make()
        }

        byteBuddy
            .redefine(kClass.java)
            .method(ElementMatchers.isStatic())
            .intercept(MethodDelegation.to(StaticAdapter::class.java))
            .make()
            .load(kClass.java.classLoader, ClassReloadingStrategy.fromInstalledAgent())
    }

    override fun unmock(kClass: KClass<*>) {
        if (classMap.containsKey(kClass)) {
            classMap[kClass]?.load(kClass.java.classLoader, ClassReloadingStrategy.fromInstalledAgent())
        }
    }

    companion object StaticAgentInitializer {
        init {
            ByteBuddyAgent.install()
        }
    }

}