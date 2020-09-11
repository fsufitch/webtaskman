package name.sufitchi.webtaskman_agent.probe.compat

import name.sufitchi.webtaskman_agent.probe.ProbeResult

class UnknownStubCompatibleProber : OSCompatibleProber {
    companion object {
        val NOT_IMPLEMENTED = NotImplementedError("OS is unknown")
    }
    override suspend fun memory() = throw NOT_IMPLEMENTED
    override suspend fun load() = throw NOT_IMPLEMENTED
    override suspend fun cpus() = throw NOT_IMPLEMENTED
    override suspend fun processes() = throw NOT_IMPLEMENTED
}