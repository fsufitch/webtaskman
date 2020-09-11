package name.sufitchi.webtaskman_agent.probe.compat

import name.sufitchi.webtaskman_agent.probe.ProbeResult

class MacCompatibleProber : OSCompatibleProber {
    override suspend fun load(): ProbeResult.Load {
        TODO("Not yet implemented")
    }

    override suspend fun cpus(): Collection<ProbeResult.CPU> {
        TODO("Not yet implemented")
    }

    override suspend fun processes(): Collection<ProbeResult.Process> {
        TODO("Not yet implemented")
    }
}