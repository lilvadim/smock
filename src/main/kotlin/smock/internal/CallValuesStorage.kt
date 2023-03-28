package smock.internal

import smock.exception.NoLastCallException

class CallValuesStorage {

    private var isCallRecording = false

    var lastCall: CallData? = null

    private val returnValues: MutableMap<CallData, Any?> = mutableMapOf()

    private val answerFunctions: MutableMap<CallData, () -> Any?> = mutableMapOf()

    private val throwables: MutableMap<CallData, Throwable> = mutableMapOf()

    fun callback(callData: CallData): (() -> Any?)? {
        val returnValue = returnValues[callData]
        val answerFunction = answerFunctions[callData]
        val throwable = throwables[callData]

        when {
            (returnValue != null) -> return { returnValue }
            (answerFunction != null) -> return answerFunction
            (throwable != null) -> return { throw throwable }
            else -> return null
        }
    }

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

    fun startRecordingCall() {
        isCallRecording = true
    }

    fun stopRecordingCall() {
        isCallRecording = false
    }

    fun isRecordingCall() = isCallRecording
}