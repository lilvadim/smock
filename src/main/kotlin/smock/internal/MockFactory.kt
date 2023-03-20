package smock.internal

import kotlin.reflect.KClass

interface MockFactory {
    fun <T: Any> create(kClass: KClass<T>): T

    companion object DefaultFactory {
        operator fun invoke(): MockFactory = CglibMockFactory()
    }
}