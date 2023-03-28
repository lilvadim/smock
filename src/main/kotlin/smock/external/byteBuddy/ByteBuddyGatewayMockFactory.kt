package smock.external.byteBuddy

import net.bytebuddy.ByteBuddy
import smock.internal.CallValuesStorage
import smock.internal.MockFactory
import kotlin.reflect.KClass

class ByteBuddyGatewayMockFactory(
    val callValuesStorage: CallValuesStorage
) : MockFactory {
    private val byteBuddy = ByteBuddy()
    private val subclassMockFactory = ByteBuddySubclassMockFactory(callValuesStorage, byteBuddy)
    private val redefineMockFactory = ByteBuddyRedefineMockFactory(callValuesStorage, byteBuddy)

    override fun <T : Any> mock(kClass: KClass<T>): T {
        return if (kClass.isFinal) {
            redefineMockFactory.mock(kClass)
        } else {
            subclassMockFactory.mock(kClass)
        }
    }

    override fun <T : Any> spy(kClass: KClass<T>): T {
        return if (kClass.isFinal) {
            redefineMockFactory.spy(kClass)
        } else {
            subclassMockFactory.spy(kClass)
        }
    }
}