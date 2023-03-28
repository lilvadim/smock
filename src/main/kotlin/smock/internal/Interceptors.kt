package smock.internal

import smock.exception.SmockException

abstract class AbstractInterceptor(
    protected val callValuesStorage: CallValuesStorage
) {
    protected fun reportNoAnswerRegistered(callData: CallData) {
        throw SmockException("No answer registered for ${callData.obj?.javaClass?.typeName}#${callData.method?.name}")
    }
}