package name.sufitchi.webtaskman_agent

import name.sufitchi.webtaskman_agent.cli.CLI
import name.sufitchi.webtaskman_agent.common.config.Configuration
import name.sufitchi.webtaskman_agent.gui.GUI

class WebTaskManAgent {
    companion object {
        @JvmStatic
        fun main(vararg args: String) {
            Configuration.Mux(
                    Configuration.Environment(),
                    Configuration.Varargs.parse(*args),
            ).run {
                when {
                    cli() -> CLI(this).start()
                    else -> GUI.launch(*args)
                }
            }
        }
    }

}

