package name.sufitchi.webtaskman_agent.probe.compat

import name.sufitchi.webtaskman_agent.probe.ProbeResult

interface OSCompatibleProber {
    suspend fun load(): ProbeResult.Load
    suspend fun cpus(): Collection<ProbeResult.CPU>
    suspend fun processes(): Collection<ProbeResult.Process>

    companion object {
        fun getCompatibleProber(): OSCompatibleProber {
            val name = System.getProperty("os.name")
            return when {
                name.contains("win") -> WindowsCompatibleProber()
                name.contains("mac") -> MacCompatibleProber()
                name.contains("nix") || name.contains("nux") || name.contains("aix") -> UnixCompatibleProber()
                name.contains("sunos") -> SolarisCompatibleProber()
                else -> UnknownStubCompatibleProber()
            }

        }
    }
}