package name.sufitchi.webtaskman_agent.probe.compat

import name.sufitchi.webtaskman_agent.probe.ProbeResult

interface OSCompatibleProber {
    suspend fun memory(): ProbeResult.Memory
    suspend fun load(): ProbeResult.Load
    suspend fun cpus(): Collection<ProbeResult.CPU>
    suspend fun processes(): Collection<ProbeResult.Process>

    companion object {
        fun getCompatibleProber(osName: String): OSCompatibleProber {
            return when {
                osName.contains("win") -> WindowsCompatibleProber()
                osName.contains("mac") -> MacCompatibleProber()
                osName.contains("nix") || osName.contains("nux") || osName.contains("aix") -> UnixCompatibleProber()
                osName.contains("sunos") -> SolarisCompatibleProber()
                else -> UnknownStubCompatibleProber()
            }

        }
    }
}