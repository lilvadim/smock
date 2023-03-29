package smock.internal

import smock.external.byteBuddy.ByteBuddyStaticMockFactory
import kotlin.reflect.KClass

interface StaticMock {
    fun mock(kClass: KClass<*>)

    fun unmock(kClass: KClass<*>)

    companion object DefaultFactory {
        operator fun invoke(callValuesStorage: CallValuesStorage): StaticMock =
            ByteBuddyStaticMockFactory(callValuesStorage)
    }
}
