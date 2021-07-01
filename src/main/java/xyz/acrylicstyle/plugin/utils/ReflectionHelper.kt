package xyz.acrylicstyle.plugin.utils

import java.lang.reflect.Field
import java.lang.reflect.Method

object ReflectionHelper {
    private fun <T> findField(clazz: Class<out T>, fieldName: String): Field? {
        return try {
            val field = clazz.getDeclaredField(fieldName)
            field.isAccessible = true
            field
        } catch (e: NoSuchFieldException) {
            null
        }
    }

    fun <T> getField(clazz: Class<out T>, instance: T?, fieldName: String): Any {
        val field = findField(clazz, fieldName) ?: throw NoSuchFieldException()
        field.isAccessible = true
        return field[instance]
    }

    fun <T> setField(clazz: Class<out T>, instance: T?, fieldName: String, value: Any?) {
        val field = findField(clazz, fieldName) ?: throw NoSuchFieldException()
        field.isAccessible = true
        field[instance] = value
    }

    private fun <T> findMethod(clazz: Class<out T?>, methodName: String, vararg args: Class<*>?): Method? {
        return try {
            clazz.getDeclaredMethod(methodName, *args).apply { isAccessible = true }
        } catch (e: NoSuchMethodException) {
            null
        }
    }

    fun <T> invokeMethod(clazz: Class<out T?>, instance: T?, methodName: String, vararg args: Any): Any? {
        val classes: MutableList<Class<*>> = ArrayList()
        for (arg in args) classes.add(arg.javaClass)
        val method: Method = findMethod(clazz, methodName, *classes.toTypedArray())
            ?: throw NoSuchMethodException()
        return method.invoke(instance, *args)
    }
}