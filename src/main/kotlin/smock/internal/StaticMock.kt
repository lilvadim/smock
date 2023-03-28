package smock.internal

import kotlin.reflect.KClass

interface StaticMock {
    fun mock(kClass: KClass<*>)

    fun unmock(kClass: KClass<*>)
}