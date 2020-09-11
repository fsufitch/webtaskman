package name.sufitchi.webtaskman_agent

import javafx.application.Application
import javafx.application.Platform
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.scene.layout.StackPane
import javafx.stage.Stage

class WebTaskManAgent {
    companion object {
        @JvmStatic
        fun main(vararg args: String) {
            println("hello world")

            ProcessHandle.allProcesses()
                    .forEach {
                        println("${it.pid()} - ${it.info().user().orElse("<nil>")} - ${it.info().command().orElse("<nil>")} - ${it.info().commandLine().orElse("<nil>")}")
                    }

            Application.launch(JFXApplication::class.java, *args)
        }
    }

    class JFXApplication : Application() {
        override fun start(stage: Stage) {
            Platform.setImplicitExit(false)

            val javaVersion = System.getProperty("java.version")
            val javafxVersion = System.getProperty("javafx.version")
            val l = Label("Hello, JavaFX $javafxVersion, running on Java $javaVersion.")
            val scene = Scene(StackPane(l), 640.0, 480.0)
            stage.scene = scene
            stage.show()

            TrayIcon(stage).start()
        }
    }
}

