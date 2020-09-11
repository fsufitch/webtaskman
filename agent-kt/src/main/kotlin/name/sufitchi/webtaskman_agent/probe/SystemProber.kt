package name.sufitchi.webtaskman_agent.probe

import kotlinx.coroutines.runBlocking
import name.sufitchi.webtaskman_agent.probe.compat.OSCompatibleProber

class SystemProber {

    fun probe(): ProbeResult = runBlocking {
        ProbeResult(
                probeMemory(),
                probeOS(),
                osCompatibleProber.cpus(),
                osCompatibleProber.load(),
                osCompatibleProber.processes(),
        )
    }

    private val runtime = Runtime.getRuntime()
    private val sysProps = System.getProperties()
    private val osCompatibleProber = OSCompatibleProber.getCompatibleProber()

    private fun probeMemory(): ProbeResult.Memory {
        val total = runtime.totalMemory()
        val free = runtime.freeMemory()
        val used = total - free
        return ProbeResult.Memory(total, used, free)
    }

    private fun probeOS(): ProbeResult.OS {
        return ProbeResult.OS(
                sysProps["os.name"].toString(),
                sysProps["os.arch"].toString(),
                sysProps["os.version"].toString(),
        )
    }
}