package pw.dotdash.kls

import com.google.inject.Injector
import org.kodein.di.DI
import org.spongepowered.plugin.PluginCandidate
import org.spongepowered.plugin.PluginEnvironment
import org.spongepowered.plugin.PluginKeys
import org.spongepowered.plugin.jvm.JVMPluginLanguageService
import pw.dotdash.kls.kodein.UseKodein
import pw.dotdash.kls.kodein.import
import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KType
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.typeOf

class KotlinLanguageService : JVMPluginLanguageService() {

    override fun getName(): String = "kotlin-lang-service"

    override fun createPluginInstance(environment: PluginEnvironment, candidate: PluginCandidate, targetClassLoader: ClassLoader): Any {
        val mainClass: String = candidate.metadata.mainClass
        val pluginClass: Class<*> = Class.forName(mainClass, true, targetClassLoader)

        val parentInjector: Injector? = environment.blackboard.get(PluginKeys.PARENT_INJECTOR).orElse(null)

        if (pluginClass.isAnnotationPresent(UseKodein::class.java)) {
            // Plugin wants to use Kodein instead of Guice
            val kClass: KClass<out Any> = pluginClass.kotlin

            // Create our "injector".
            val kodein = DI {
                if (parentInjector != null) {
                    import(parentInjector)
                }
            }

            val singleton: Any? = kClass.objectInstance
            if (singleton != null) {
                // Plugin is using a kotlin `object`.

                // Save it here so that the plugin can access it later with `kodein<MyPlugin>()`.
                kodeins[pluginClass] = kodein

                // Now just return the singleton instance.
                return singleton
            } else {
                // Plugin is using a normal class

                val constr: KFunction<Any>? = kClass.primaryConstructor
                if (constr?.parameters?.singleOrNull() == kTypeKodein) {
                    // Found a way to inject our Kodein instance.
                    return constr.call(kodein)
                } else {
                    // Uhhhhh, how else are we gonna create the instance if they want Kodein???
                    // TODO: throw exception
                    return kClass.createInstance()
                }
            }
        } else {
            // Otherwise use Guice
            return parentInjector?.getInstance(pluginClass)
            // No parent injector to use, just create the class instead
                ?: pluginClass.newInstance()
        }
    }

    @OptIn(ExperimentalStdlibApi::class)
    private val kTypeKodein: KType = typeOf<DI>()

    companion object {
        internal val kodeins = ConcurrentHashMap<Class<*>, DI>()
    }
}

fun kodein(pluginClass: Class<*>): DI =
    requireNotNull(KotlinLanguageService.kodeins[pluginClass]?.di) { "This class was not loaded as a singleton through KotlinLanguageService" }

inline fun <reified T> kodein(): DI =
    kodein(T::class.java)