package name.sufitchi.webtaskman_agent.probe.compat

import name.sufitchi.webtaskman_agent.probe.ProbeResult

class UnknownStubCompatibleProber : OSCompatibleProber {
    override suspend fun load(): ProbeResult.Load {
        throw NotImplementedError("OS is unknown")
    }

    override suspend fun cpus(): Collection<ProbeResult.CPU> {
        throw NotImplementedError("OS is unknown")
    }

    override suspend fun processes(): Collection<ProbeResult.Process> {
        throw NotImplementedError("OS is unknown")
    }
}