package smock.api

inline fun <reified T> smock(): T {
    TODO()
}

inline fun <reified R> every(mockedObjMethod: () -> R): EveryScope<R> {
    TODO()
}

inline fun every(mockedObjMethod: () -> Unit): EveryScope<Unit> {
    TODO()
}

class EveryScope<T> {
    infix fun returns(value: T) {
        TODO()
    }
}

class EveryUnitScope {
    companion object {
        val runs: Unit = Unit
    }

    infix fun just(runs: Unit) {}
}
