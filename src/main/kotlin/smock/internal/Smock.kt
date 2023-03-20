package smock.internal

class Smock(
    val mockFactory: MockFactory
) {
    inline fun <reified T : Any> mock(): T {
        return mockFactory.create(T::class)
    }

    inline fun <reified T> every() {}
}