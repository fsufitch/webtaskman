package name.sufitchi.webtaskman_agent.gui

import dorkbox.systemTray.SystemTray
import javafx.stage.Stage
import javax.swing.JMenuItem
import javax.swing.JSeparator

class TrayIcon(private val stage: Stage) {
    private val icon = ClassLoader.getSystemResource("kitten-small.png")

    private fun showStage() {
        stage.show()
        stage.toFront()
    }

    fun start() {
        println("wat")
        SystemTray.DEBUG = true
        val tray = SystemTray.get()
        tray.setImage(icon)

        println("ok")
        tray.menu.add(JMenuItem("entry1"))
        tray.menu.add(JSeparator())
        tray.menu.add(JMenuItem("entry 2"))

        showStage()
        println("ok")
    }

//    fun start() {
//
//
//        // JavaFX does not support tray icons yet, so we need to use AWT
//        // From: https://gist.github.com/jonyfs/b279b5e052c3b6893a092fed79aa7fbe
//
//        Toolkit.getDefaultToolkit()
//        if (!SystemTray.isSupported()) {
//            println("No system tray support, application exiting.")
//            // TODO: switch to a popup for ease of use?
//            Platform.exit()
//        }
//
//        val tray = SystemTray.getSystemTray()
//        val trayIcon = TrayIcon(ImageIO.read(ClassLoader.getSystemResource("kitten-small.png")))
//        println(ImageIO.read(ClassLoader.getSystemResource("kitten-small.png")))
//
//        trayIcon.addActionListener { Platform.runLater { showStage() } }
//
//        val openItem = MenuItem("hello, world")
//        openItem.addActionListener { Platform.runLater { showStage() } }
//
//        val defaultFont = Font.decode(null)
//        val boldFont = defaultFont.deriveFont(Font.BOLD)
//        openItem.font = boldFont
//
//        val exitItem = MenuItem("Exit")
//        exitItem.addActionListener {
//            Platform.exit()
//            tray.remove(trayIcon)
//        }
//
//        val popup = PopupMenu()
//        popup.add(openItem)
//        popup.addSeparator()
//        popup.add(exitItem)
//        trayIcon.popupMenu = popup
//
//        SwingUtilities.invokeLater {
//            trayIcon.displayMessage(
//                    "hello",
//                    "The time is now " + SimpleDateFormat.getTimeInstance().format(Date()),
//                    TrayIcon.MessageType.INFO
//            )
//        }
//
//        tray.add(trayIcon)
//    }
}