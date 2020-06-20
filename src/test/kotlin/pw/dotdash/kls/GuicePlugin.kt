package pw.dotdash.kls

import org.spongepowered.plugin.jvm.Plugin
import java.nio.file.Path
import javax.inject.Inject

@Plugin("my-plugin-with-guice")
class GuicePlugin @Inject constructor(private val configDir: Path) {

    fun onInit() {

    }
}