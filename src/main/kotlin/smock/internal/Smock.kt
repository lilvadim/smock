package smock.internal

import smock.api.annotation.Smock
import smock.api.annotation.Spy
import kotlin.reflect.KClass

class Smock(
    val mockFactory: MockFactory,
    val staticMock: StaticMock,
    private val callValuesStorage: CallValuesStorage
) {
    fun initAnnotated(instance: Any) {
        val fields = AnnotationFilter(apiAnnotations).annotatedFields(instance)
        for (field in fields) {
            field.isAccessible = true
            when {
                field.isAnnotationPresent(Smock::class.java) ->
                    field.set(
                        instance,
                        mockFactory.mock(field.type.kotlin)
                    )

                field.isAnnotationPresent(Spy::class.java) ->
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

    fun startRecordingCall() {
        callValuesStorage.startRecordingCall()
    }

    fun stopRecordingCall() {
        callValuesStorage.stopRecordingCall()
    }

    fun mockStatic(classToMock: KClass<*>) {
        staticMock.mock(classToMock)
    }

    fun unmockStatic(classToUnmock: KClass<*>) {
        staticMock.unmock(classToUnmock)
    }
}