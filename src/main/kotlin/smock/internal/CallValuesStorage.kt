package smock.internal

import smock.exception.NoLastCallException

class CallValuesStorage {

    var lastCall: CallData? = null

    private val returnValues: MutableMap<CallData, Any?> = mutableMapOf()

    private val answerFunctions: MutableMap<CallData, () -> Any?> = mutableMapOf()

    private val throwables: MutableMap<CallData, Throwable> = mutableMapOf()

    fun returnValue(callData: CallData) = returnValues[callData]

    fun answersFunction(callData: CallData) = answerFunctions[callData]

    fun throwable(callData: CallData) = throwables[callData]

    @Throws(NoLastCallException::class)
    fun registerReturnValueForLastCall(returnValue: Any?) {
        if (lastCall != null) {
            returnValues[lastCall!!] = returnValue
        } else {
            reportNoLastCall()
        }
    }

    @Throws(NoLastCallException::class)
    fun registerAnswerFunctionForLastCall(answer: () -> Any?) {
        if (lastCall != null) {
            answerFunctions[lastCall!!] = answer
        } else {
            reportNoLastCall()
        }
    }

    @Throws(NoLastCallException::class)
    fun registerThrowableForLastCall(throwable: Throwable) {
        if (lastCall != null) {
            throwables[lastCall!!] = throwable
        } else {
            reportNoLastCall()
        }
    }

    private fun reportNoLastCall() {
        throw NoLastCallException("No calls of mocked object methods were registered")
    }
}