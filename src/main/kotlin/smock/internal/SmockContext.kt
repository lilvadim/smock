package smock.internal

object SmockContext {
    private val callValuesStorage = CallValuesStorage()
    val dslDelegate = Smock(
        mockFactory = MockFactory(callValuesStorage),
        callValuesStorage = callValuesStorage
    )
}