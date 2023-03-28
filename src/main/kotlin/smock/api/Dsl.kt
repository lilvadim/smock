package smock.api

import smock.internal.SmockContext
import kotlin.reflect.KClass

inline fun <reified T> smock(): T {
    return SmockContext.dslDelegate.mock()
}

inline fun <reified T> spy(): T {
    return SmockContext.dslDelegate.spy()
}

inline fun <reified R> every(mockedObjMethod: EveryScope.() -> R): BehaviorScope<R> {
    SmockContext.dslDelegate.startRecordingCall()
    EveryScope().mockedObjMethod()
    SmockContext.dslDelegate.stopRecordingCall()
    return BehaviorScope()
}

class EveryScope

class BehaviorScope<T> {
    infix fun returns(value: T) {
        SmockContext.dslDelegate.returns(value)
    }

    infix fun answers(answer: () -> T) {
        SmockContext.dslDelegate.answers(answer)
    }

    infix fun throws(throwable: Throwable) {
        SmockContext.dslDelegate.throws(throwable)
    }
}

infix fun BehaviorScope<Unit>.just(runs: Runs) {
    SmockContext.dslDelegate.returns(Unit)
}

object Runs
typealias runs = Runs

fun smockStatic(classToMock: KClass<*>) {
    SmockContext.dslDelegate.mockStatic(classToMock)
}

fun unmockStatic(classToUnmock: KClass<*>) {
    SmockContext.dslDelegate.unmockStatic(classToUnmock)
}