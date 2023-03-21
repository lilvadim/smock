package smock.internal

import smock.api.annotations.SmockSpied
import smock.api.annotations.Smocked
import java.lang.reflect.Field
import kotlin.reflect.KClass

val apiAnnotations: List<KClass<out Annotation>> = listOf(
    Smocked::class,
    SmockSpied::class
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