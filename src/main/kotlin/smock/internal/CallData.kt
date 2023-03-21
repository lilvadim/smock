package smock.internal

import java.lang.reflect.Method

class CallData(
    val obj: Any?,
    val method: Method?,
    val args: List<Any?>
) {
    override fun hashCode(): Int {
        var result = method?.hashCode() ?: 0
        result = 31 * result + args.hashCode()
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CallData

        /**
         * Check obj property only using === operator !!!
         * to avoid obj.equals(...) call
         */
        if (obj !== other.obj) return false
        if (method != other.method) return false
        if (args != other.args) return false

        return true
    }
}