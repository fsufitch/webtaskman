package name.sufitchi.webtaskman_agent

import name.sufitchi.webtaskman_agent.cli.CLI
import name.sufitchi.webtaskman_agent.gui.GUI

class WebTaskManAgent {
    companion object {
        @JvmStatic
        fun main(vararg args: String) {
            CLI(*args).run {
                when {
                    help() -> printHelp()
                    gui() -> GUI.launch(*args)
                    else -> start()
                }
            }
        }
    }

}

