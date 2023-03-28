package smock.internal

class CallerIdentifier(
    val objRef: Any? = null,
    val objClass: Class<*>? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CallerIdentifier

        if (objRef === other.objRef) return true
        if (objClass != other.objClass) return false

        return true
    }

    override fun hashCode(): Int {
        return objClass?.hashCode() ?: 0
    }
}
