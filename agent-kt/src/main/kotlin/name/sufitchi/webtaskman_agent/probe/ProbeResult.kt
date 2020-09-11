package name.sufitchi.webtaskman_agent.probe

import kotlinx.serialization.Serializable

@Serializable
data class ProbeResult(
        val memory: Memory,
        val os: OS,
        val cpus: Collection<CPU>,
        val load: Load?,
        val processes: Collection<Process>,
) {
    @Serializable
    data class Load(
            val minute1: Double,
            val minute5: Double,
            val minute15: Double,
    )

    @Serializable
    data class Memory(
            val total: Long,
            val used: Long,
            val free: Long,
            val shared: Long,
            val buffers: Long,
            val cache: Long,
            val available: Long,
            val swapTotal: Long,
            val swapUsed: Long,
            val swapFree: Long,
    )

    @Serializable
    data class CPU(
            val id: Int,
            val modelName: String?,
            val mhz: Double?,
            val percentIdle: Double?,
    )

    @Serializable
    data class Process(
            val pid: Long,
            val parentPID: Long?,
            val name: String?,
            val path: String?,
            val cmdline: String?,
            val owner: String?,
            val cpuUsagePercent: Double?,
            val memoryBytes: Long?,
            val diskReadKBPS: Double?,
            val diskWriteKBPS: Double?,
            val numThreads: Int?,
    )

    @Serializable
    data class OS(
            val name: String,
            val arch: String,
            val version: String,
    )
}