package smock.external.cglib

import net.sf.cglib.proxy.Enhancer
import smock.internal.CallValuesStorage
import smock.internal.MockFactory
import kotlin.reflect.KClass

class CglibMockFactory(
    private val callValuesStorage: CallValuesStorage
) : MockFactory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> mock(kClass: KClass<T>): T {
        return Enhancer().apply {
            setSuperclass(kClass.java)
            setCallback(CglibMockInterceptor(callValuesStorage))
        }.create() as T
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> spy(kClass: KClass<T>): T {
        return Enhancer().apply {
            setSuperclass(kClass.java)
            setCallback(CglibSpyInterceptor(callValuesStorage))
        }.create() as T
    }
}