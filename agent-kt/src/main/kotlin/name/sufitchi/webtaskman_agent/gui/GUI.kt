package name.sufitchi.webtaskman_agent.gui

import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.scene.layout.StackPane
import javafx.stage.Stage

class GUI : Application() {
    companion object {
        fun launch(vararg args: String) = launch(GUI::class.java, *args)
    }

    override fun start(stage: Stage) {
        //Platform.setImplicitExit(false)

        val javaVersion = System.getProperty("java.version")
        val javafxVersion = System.getProperty("javafx.version")
        val l = Label("Hello, JavaFX $javafxVersion, running on Java $javaVersion.")
        val scene = Scene(StackPane(l), 640.0, 480.0)
        stage.scene = scene
        stage.show()

        //TrayIcon(stage).start()
    }
}