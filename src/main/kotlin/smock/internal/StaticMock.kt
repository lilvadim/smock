package smock.internal

import smock.external.byteBuddy.ByteBuddyStaticMock
import kotlin.reflect.KClass

interface StaticMock {
    fun mock(kClass: KClass<*>)

    fun unmock(kClass: KClass<*>)

    companion object DefaultFactory {
        operator fun invoke(callValuesStorage: CallValuesStorage): StaticMock =
            ByteBuddyStaticMock(callValuesStorage)
    }
}
