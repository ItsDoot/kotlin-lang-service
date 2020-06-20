package pw.dotdash.kls.kodein

import com.google.inject.Binding
import com.google.inject.Injector
import com.google.inject.Key
import com.google.inject.TypeLiteral
import org.kodein.di.DI
import org.kodein.di.provider
import org.kodein.type.TypeToken
import org.kodein.type.typeToken

@Suppress("UNCHECKED_CAST")
internal fun <T : Any> DI.Builder.bind(type: TypeLiteral<out T>, tag: Any? = null, overrides: Boolean? = null): DI.Builder.TypeBinder<T> =
    Bind(typeToken(type.type) as TypeToken<out T>, tag, overrides)

internal fun DI.Builder.import(injector: Injector, allowOverride: Boolean = false) {
    val module = DI.Module("guice") {
        for ((key: Key<*>, binding: Binding<*>) in injector.allBindings) {
            bind<Any>(key.typeLiteral) with provider<Any, Any> { binding.provider.get() }
        }
    }
    import(module, allowOverride)
}