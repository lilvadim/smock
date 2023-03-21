package smock.internal

object SmockContext {
    val dslDelegate = Smock(
        MockFactory(callValuesStorage = CallValuesStorage())
    )
}