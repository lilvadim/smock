package smock.internal

import smock.external.byteBuddy.dynamicProxy.ByteBuddySubclassMockFactory
import kotlin.reflect.KClass

interface MockFactory {
    fun <T : Any> mock(kClass: KClass<T>): T

    fun <T : Any> spy(kClass: KClass<T>): T

    companion object DefaultFactory {
        operator fun invoke(callValuesStorage: CallValuesStorage): MockFactory =
            ByteBuddySubclassMockFactory(callValuesStorage)
    }
}