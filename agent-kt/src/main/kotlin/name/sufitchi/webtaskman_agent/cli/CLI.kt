package name.sufitchi.webtaskman_agent.cli

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import name.sufitchi.webtaskman_agent.probe.SystemProber
import org.apache.commons.cli.*
import kotlin.system.exitProcess

class CLI(vararg args: String) {
    private val usage = "webtaskman-agent [-b] [-h]"
    private val options = Options().run {
        addOption(Option.builder("b").longOpt("batch").hasArg(false).desc("enable batch (cli) mode").build())
        addOption(Option.builder("h").longOpt("help").hasArg(false).desc("print this message and exit").build())
    }
    private val cmd = try {
        DefaultParser().parse(options, args)!!
    } catch (e: ParseException) {
        println("Exception parsing CLI: ${e.message}\n")
        printHelp()
        exitProcess(1)
    }

    fun gui() = !cmd.hasOption('b')
    fun help() = cmd.hasOption('h')

    fun printHelp() = HelpFormatter().printHelp(usage, options)

    fun start() {
        println("Starting in CLI mode")

        val result = Json.encodeToString(SystemProber().probe())
        println(result)
    }
}