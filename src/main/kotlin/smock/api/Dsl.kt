package smock.api

import java.lang.Exception

inline fun <reified T> smock(): T {
    TODO()
}

inline fun <reified R> every(mockedObjMethod: () -> R): EveryScope<R> {
    TODO()
}

class EveryScope<T> {
    infix fun returns(value: T) {
        TODO()
    }

    infix fun answers(answer: () -> T) {
        TODO()
    }

    infix fun throws(exception: Exception) {
        TODO()
    }
}

infix fun EveryScope<Unit>.just(runs: Runs) {
    TODO()
}

object Runs
typealias runs = Runs