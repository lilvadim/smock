package smock.internal

import smock.api.annotation.Smock
import smock.api.annotation.Spy
import java.lang.reflect.Field
import kotlin.reflect.KClass

val apiAnnotations: List<KClass<out Annotation>> = listOf(
    Smock::class,
    Spy::class
)

class AnnotationFilter(
    private val apiAnnotations: Collection<KClass<out Annotation>>
) {
    fun annotatedFields(instance: Any): List<Field> {
        return instance.javaClass.fields.filter { field ->
            apiAnnotations.any { field.isAnnotationPresent(it.java) }
        }
    }
}