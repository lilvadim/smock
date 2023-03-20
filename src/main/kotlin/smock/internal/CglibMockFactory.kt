package smock.internal

import kotlin.reflect.KClass

class CglibMockFactory : MockFactory {
    override fun <T : Any> create(kClass: KClass<T>): T {
        TODO("Not yet implemented")
    }
}