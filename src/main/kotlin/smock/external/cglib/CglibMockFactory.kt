package smock.external.cglib

import net.sf.cglib.proxy.Enhancer
import smock.internal.CallValuesStorage
import smock.internal.MockFactory
import kotlin.reflect.KClass

class CglibMockFactory(
    override val callValuesStorage: CallValuesStorage
) : MockFactory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> create(kClass: KClass<T>): T {
        val enhancer = Enhancer().apply {
            setSuperclass(kClass.java)
            setCallback(CglibSmockInterceptor(callValuesStorage))
        }
        return enhancer.create() as T
    }
}