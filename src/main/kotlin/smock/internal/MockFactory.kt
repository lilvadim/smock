package smock.internal

import smock.external.cglib.CglibMockFactory
import kotlin.reflect.KClass

interface MockFactory {
    fun <T: Any> create(kClass: KClass<T>): T

    val callValuesStorage: CallValuesStorage

    companion object DefaultFactory {
        operator fun invoke(callValuesStorage: CallValuesStorage): MockFactory = CglibMockFactory(callValuesStorage)
    }
}