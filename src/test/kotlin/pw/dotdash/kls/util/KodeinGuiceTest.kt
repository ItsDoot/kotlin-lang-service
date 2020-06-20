package pw.dotdash.kls.util

import com.google.inject.AbstractModule
import com.google.inject.Guice
import com.google.inject.Provider
import com.google.inject.TypeLiteral
import org.kodein.di.DI
import org.kodein.di.instance
import pw.dotdash.kls.kodein.import
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals

class KodeinGuiceTest {

    private val module = object : AbstractModule() {
        override fun configure() {
            // Basic types
            bind(String::class.java).toInstance("Hello world!")
            bind(Int::class.java).toInstance(5)
            bind(Boolean::class.java).toInstance(true)
            bind(Double::class.java).toInstance(10.0)

            // Complex types
            bind(object : TypeLiteral<List<String>>() {}).toProvider(Provider { listOf("lets", "go") })
            bind(object : TypeLiteral<Optional<List<Set<Int>>>>() {}).toProvider(Provider { Optional.of(listOf(setOf(1), setOf(2), setOf(3))) })
        }
    }

    @Test
    fun `check basic types`() {
        val kodein = DI {
            import(Guice.createInjector(module))
        }

        val string by kodein.instance<String>()
        val int by kodein.instance<Int>()
        val boolean by kodein.instance<Boolean>()
        val double by kodein.instance<Double>()

        assertEquals(string, "Hello world!")
        assertEquals(int, 5)
        assertEquals(boolean, true)
        assertEquals(double, 10.0)
    }

    @Test
    fun `check complex types`() {
        val kodein = DI {
            import(Guice.createInjector(module))
        }

        val list by kodein.instance<List<String>>()
        val optional by kodein.instance<Optional<List<Set<Int>>>>()

        assertEquals(list, listOf("lets", "go"))
        assertEquals(optional, Optional.of(listOf(setOf(1), setOf(2), setOf(3))))
    }
}