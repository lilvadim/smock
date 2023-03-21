package smock.internal

class Smock(
    val mockFactory: MockFactory,
    private val callValuesStorage: CallValuesStorage = mockFactory.callValuesStorage
) {
    inline fun <reified T : Any> mock(): T {
        return mockFactory.create(T::class)
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