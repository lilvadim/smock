package smock.internal

class Smock(
    val mockFactory: MockFactory,
    private val callValuesStorage: CallValuesStorage
) {
    inline fun <reified T : Any> mock(): T {
        return mockFactory.mock(T::class)
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