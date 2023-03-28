package smock.api.annotation

import smock.internal.SmockContext

@Target(AnnotationTarget.FIELD)
annotation class Smock

@Target(AnnotationTarget.FIELD)
annotation class Spy

fun smock(thisInstance: Any) {
    SmockContext.dslDelegate.initAnnotated(thisInstance)
}