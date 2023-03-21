package smock.api.annotation

import smock.internal.SmockContext

@Target(AnnotationTarget.FIELD)
annotation class Smocked

@Target(AnnotationTarget.FIELD)
annotation class SmockSpied

fun smockAnnotated(thisInstance: Any) {
    SmockContext.dslDelegate.initAnnotated(thisInstance)
}