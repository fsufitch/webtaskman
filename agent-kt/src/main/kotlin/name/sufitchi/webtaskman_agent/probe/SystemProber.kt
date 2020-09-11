package name.sufitchi.webtaskman_agent.probe

import kotlinx.coroutines.runBlocking
import name.sufitchi.webtaskman_agent.probe.compat.OSCompatibleProber

class SystemProber {
    private val osCompatibleProber = OSCompatibleProber.getCompatibleProber(System.getProperty("os.name").toLowerCase())

    fun probe(): ProbeResult = runBlocking {
        ProbeResult(
                osCompatibleProber.memory(),
                probeOS(),
                osCompatibleProber.cpus(),
                osCompatibleProber.load(),
                osCompatibleProber.processes(),
        )
    }

    private fun probeOS(): ProbeResult.OS {
        return ProbeResult.OS(
                System.getProperty("os.name"),
                System.getProperty("os.arch"),
                System.getProperty("os.version"),
        )
    }
}