package name.sufitchi.webtaskman_agent.net

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import name.sufitchi.webtaskman_agent.common.config.Configuration
import name.sufitchi.webtaskman_agent.probe.SystemProber


class Reporter(private val configuration: Configuration): Runnable {
    private val thread = Thread(this, "$THREAD_BASE_NAME-${threadCount++}")
     val pollChannel = Channel<Long>(1)

    fun startThread() {
        thread.start()
    }

    override fun run() {
        runBlocking {
            while (true) {
                println("starting reporter loop")
                report(pollChannel.receive())
            }
        }
    }

    private fun report(pingID: Long) {
        println("reporter loop $pingID")
        println("would report to ${configuration.hub()} if I could")

        println(Json.encodeToString(SystemProber().probe()))
    }

    companion object {
        private var threadCount = 1
        private const val THREAD_BASE_NAME = "name.sufitchi.webtaskman_agent.net.Reporter.Thread"
    }
}