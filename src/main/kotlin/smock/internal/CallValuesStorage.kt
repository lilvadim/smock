package smock.internal

import java.lang.reflect.Method

class CallValuesStorage {
    class CallData(
        val obj: Any?,
        val method: Method?,
        val args: List<Any>
    ) {
        override fun hashCode(): Int {
            var result = method?.hashCode() ?: 0
            result = 31 * result + args.hashCode()
            return result
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as CallData

            if (method != other.method) return false
            if (args != other.args) return false

            return true
        }
    }

    var lastCall: CallData? = null

    private val returnValues: MutableMap<CallData, Any?> = mutableMapOf()

    private val answerFunctions: MutableMap<CallData, () -> Any?> = mutableMapOf()

    private val throwables: MutableMap<CallData, Throwable> = mutableMapOf()

    fun returnValue(callData: CallData) = returnValues[callData]

    fun answersFunction(callData: CallData) = answerFunctions[callData]

    fun throwable(callData: CallData) = throwables[callData]

    fun registerReturnValueForLastCall(returnValue: Any?) {
        if (lastCall != null) {
            returnValues[lastCall!!] = returnValue
        }
    }

    fun registerAnswerFunctionForLastCall(answer: () -> Any?) {
        if (lastCall != null) {
            answerFunctions[lastCall!!] = answer
        }
    }

    fun registerThrowableForLastCall(throwable: Throwable) {
        if (lastCall != null) {
            throwables[lastCall!!] = throwable
        }
    }
}