package name.sufitchi.webtaskman_agent.common.config

import org.apache.commons.cli.*
import java.net.URL
import kotlin.system.exitProcess

interface Configuration {
    fun cli(): Boolean?
    fun hub(): URL?
    fun intervalMilliseconds(): Long?

    class Environment(private val envmap: Map<String, String> = System.getenv()!!): Configuration {
        override fun cli() = envmap.getOrDefault("CLI", null)?.toBoolean()
        override fun hub() = envmap.getOrDefault("WEBTASKMAN_HUB", null)?.let { URL(it) }
        override fun intervalMilliseconds() = envmap.getOrDefault("WEBTASKMAN_INTERVAL", null)?.toLong()
    }

    class Varargs( private val cmd: CommandLine): Configuration {
        override fun cli() = if (cmd.hasOption('b')) true else null
        override fun hub() = cmd.getOptionValue('u')?.let { URL(it) }
        override fun intervalMilliseconds() = cmd.getOptionValue('i')?.toLong()

        companion object {
            private const val usage = "webtaskman-agent [-b] [-h]"
            private val options = Options().run {
                addOption(Option.builder("b").longOpt("batch").hasArg(false).desc("enable batch (cli) mode").build())
                addOption(Option.builder("u").longOpt("url").hasArg(true).desc("URL to the Webtaskman Hub").build())
                addOption(Option.builder("i").longOpt("interval").hasArg(true).desc("interval to send info at (in milliseconds)").build())
                addOption(Option.builder("h").longOpt("help").hasArg(false).desc("print this message and exit").build())
            }

            fun parse(vararg args: String) = try {
                DefaultParser().parse(options, args)!!.also {
                    if (it.hasOption('h')) {
                        printHelp()
                        exitProcess(0)
                    }
                }
            } catch (e: ParseException) {
                println("Exception parsing CLI: ${e.message}\n")
                printHelp()
                exitProcess(1)
            }.let { Varargs(it) }

            private fun printHelp() = HelpFormatter().printHelp(usage, options)
        }
    }

    class Mux(private vararg val configs: Configuration): Configuration {
        override fun cli() = configs.fold(null as Boolean?) { acc, c -> acc ?: c.cli()  } ?: false
        override fun hub() = configs.fold(null as URL?) {acc, c -> acc ?: c.hub() }
        override fun intervalMilliseconds() = configs.fold(null as Long?) {acc, c -> acc ?: c.intervalMilliseconds()} ?: 2500
    }
}