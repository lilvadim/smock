package smock.internal

import smock.api.annotation.SmockSpied
import smock.api.annotation.Smocked

class Smock(
    val mockFactory: MockFactory,
    private val callValuesStorage: CallValuesStorage
) {
    fun initAnnotated(instance: Any) {
        val fields = AnnotationFilter(apiAnnotations).annotatedFields(instance)
        for (field in fields) {
            field.isAccessible = true
            when {
                field.isAnnotationPresent(Smocked::class.java) ->
                    field.set(
                        instance,
                        mockFactory.mock(field.type.kotlin)
                    )

                field.isAnnotationPresent(SmockSpied::class.java) ->
                    field.set(
                        instance,
                        mockFactory.spy(field.type.kotlin)
                    )
            }
        }
    }

    inline fun <reified T : Any> mock(): T {
        return mockFactory.mock(T::class)
    }

    inline fun <reified T : Any> spy(): T {
        return mockFactory.spy(T::class)
    }

    fun returns(returnValue: Any?) {
        callValuesStorage.registerReturnValueForLastCall(returnValue)
    }

    fun answers(callback: () -> Any?) {
        callValuesStorage.registerAnswerFunctionForLastCall(callback)
    }

    fun throws(throwable: Throwable) {
        callValuesStorage.registerThrowableForLastCall(throwable)
    }
}