package pw.dotdash.kls

import org.kodein.di.DI
import org.kodein.di.instance
import org.spongepowered.plugin.jvm.Plugin
import java.nio.file.Path

@Plugin("my-plugin-with-kodein")
class KodeinPlugin(private val kodein: DI) {
    val configDir by kodein.instance<Path>()

    fun onInit() {

    }
}