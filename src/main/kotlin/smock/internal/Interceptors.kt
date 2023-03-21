package smock.internal

abstract class AbstractInterceptor(
    protected val callValuesStorage: CallValuesStorage
) {
    fun CallValuesStorage.registeredAction(callData: CallData): (() -> Any?)? {
        val returnValue = returnValue(callData)
        val answerFunction = answersFunction(callData)
        val throwable = throwable(callData)

        when {
            (returnValue != null) -> return { returnValue }
            (answerFunction != null) -> return answerFunction
            (throwable != null) -> return { throw throwable }
            else -> return null
        }
    }
}