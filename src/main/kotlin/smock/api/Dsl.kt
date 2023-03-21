package smock.api

import smock.internal.SmockContext

inline fun <reified T> smock(): T {
    return SmockContext.dslDelegate.mock()
}

inline fun <reified R> every(mockedObjMethod: () -> R): EveryScope<R> {
    mockedObjMethod()
    return EveryScope()
}

class EveryScope<T> {
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

infix fun EveryScope<Unit>.just(runs: Runs) {
    SmockContext.dslDelegate.returns(Unit)
}

object Runs
typealias runs = Runs