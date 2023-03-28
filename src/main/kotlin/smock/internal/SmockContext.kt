package smock.internal

object SmockContext {
    private val callValuesStorage = CallValuesStorage()
    private val staticMock = StaticMock()
    val dslDelegate = Smock(
        mockFactory = MockFactory(callValuesStorage),
        callValuesStorage = callValuesStorage,
        staticMock = staticMock
    )
}