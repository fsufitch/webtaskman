package name.sufitchi.webtaskman_agent.cli

import name.sufitchi.webtaskman_agent.common.config.Configuration
import name.sufitchi.webtaskman_agent.net.Reporter
import name.sufitchi.webtaskman_agent.net.Ticker

class CLI(configuration: Configuration) {
    private val reporter = Reporter(configuration)
    private val ticker = Ticker(configuration.intervalMilliseconds()!!, reporter.pollChannel)

    fun start() {
        reporter.startThread()
        ticker.startThread()
    }
}