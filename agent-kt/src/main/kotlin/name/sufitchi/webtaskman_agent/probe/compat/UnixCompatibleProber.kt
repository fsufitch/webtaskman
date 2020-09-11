package name.sufitchi.webtaskman_agent.probe.compat

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runInterruptible
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import name.sufitchi.webtaskman_agent.probe.ProbeResult
import java.io.FileReader
import java.util.*
import java.util.stream.Stream
import kotlin.streams.toList

class UnixCompatibleProber : OSCompatibleProber {
    override suspend fun memory(): ProbeResult.Memory =
            free().run {
                ProbeResult.Memory(
                        total = total,
                        used = used,
                        free = free,
                        shared = shared,
                        buffers = buffers,
                        cache = cache,
                        available = available,
                        swapTotal = swapTotal,
                        swapUsed = swapUsed,
                        swapFree = swapFree,
                )
            }

    override suspend fun load(): ProbeResult.Load =
            uptime().run { ProbeResult.Load(minute1, minute5, minute15) }


    override suspend fun cpus(): Collection<ProbeResult.CPU> {
        val cpuMap = hashMapOf<Int, CPUInfo>()
        listOf(
                parseProcCPUInfo(),
                mpstatAll(),
        )
                .flatMap { a -> a.toList() }
                .forEach { cpuMap[it.id] = if (!cpuMap.containsKey(it.id)) it else cpuMap[it.id]!!.merge(it) }

        return cpuMap.values.filter { it.id >= 0 }.map {
            ProbeResult.CPU(it.id, it.modelName, it.mhz, it.percentIdle)
        }
    }


    override suspend fun processes(): Collection<ProbeResult.Process> {
        val procMap = hashMapOf<Long, ProcInfo>()
        listOf<Stream<ProcInfo>>(
                javaProcInfo(),
                pidstatD(),
                pidstatR(),
                pidstatV(),
                pidstatAll(),
        )
                .flatMap { it.toList() }
                .forEach { procMap[it.pid] = if (!procMap.containsKey(it.pid)) it else procMap[it.pid]!!.merge(it) }

        return procMap.values.filter { it.pid > 0 }.map {
            ProbeResult.Process(
                    it.pid, it.parentPID, it.name, it.path, it.cmdline, it.owner,
                    it.cpuUsagePercent, it.memoryBytes, it.diskReadKBPS, it.diskWriteKBPS, it.numThreads)
        }


    }

    private suspend fun free() = withTimeout(5000) {
        runInterruptible(Dispatchers.IO) { ProcessBuilder("free", "-bw").start().apply { waitFor() } }
    }
            .inputStream.bufferedReader().readLines().let { lines ->
                Pair(
                        lines[1].split(Regex("\\s+")).map { it.trim() },
                        lines[2].split(Regex("\\s+")).map { it.trim() },
                )
            }
            .let { (first, second) ->
                object {
                    val total = first[1].toLong()
                    val used = first[2].toLong()
                    val free = first[3].toLong()
                    val shared = first[4].toLong()
                    val buffers = first[5].toLong()
                    val cache = first[6].toLong()
                    val available = first[7].toLong()
                    val swapTotal = second[1].toLong()
                    val swapUsed = second[2].toLong()
                    val swapFree = second[3].toLong()
                }
            }


    private suspend fun uptime() = withTimeout(5000) {
        runInterruptible(Dispatchers.IO) { ProcessBuilder("uptime").start().apply { waitFor() } }
    }
            .inputStream.bufferedReader().readText().trim()
            .split("load average:")
            .let { parts ->
                parts[1].split(",")
                        .map { it.trim().toDouble() }
                        .let {
                            object {
                                val minute1 = it[0]
                                val minute5 = it[1]
                                val minute15 = it[2]
                            }
                        }
            }

    private suspend fun parseProcCPUInfo(): Stream<CPUInfo> = withContext(Dispatchers.IO) {
        runInterruptible { FileReader("/proc/cpuinfo").let { f -> f.buffered().readText().also { f.close() } } }
    }
            .split("\n\n").map {
                it.split("\n")
                        .map { line -> line.split(":", limit = 2) + listOf("", "") }
                        .map { pair -> Pair(pair[0].trim(), pair[1].trim()) }
                        .let { pairList -> mapOf(*pairList.toTypedArray()) }
            }.map {
                CPUInfo(
                        id = it["processor"]?.toInt() ?: -1,
                        modelName = it["model name"],
                        mhz = it["cpu MHz"]?.toDouble() ?: -1.0,
                )
            }.stream()

    private suspend fun mpstatAll(): Stream<CPUInfo> = withTimeout(5000) {
        runInterruptible(Dispatchers.IO) { ProcessBuilder("mpstat", "-P", "ALL").start().apply { waitFor() } }
    }
            .inputStream.bufferedReader().lines().skip(3)
            .map {
                it.split(Regex("\\s+"), 14)
                        .map { sub -> sub.trim() }
                        .filter { sub -> sub.isNotEmpty() }
            }
            .filter { it.size == 13 }
            .map {
                try {
                    CPUInfo(
                            id = it[2].toInt(),
                            percentIdle = it[12].toDouble(),
                    )
                } catch (e: Exception) {
                    CPUInfo(-1)
                }
            }


    private fun javaProcInfo() = ProcessHandle.allProcesses()
            .map { proc ->
                ProcInfo(
                        proc.pid(),
                        parentPID = proc.parent().unwrap()?.pid(),
                        path = proc.info().command().unwrap(),
                        cmdline = proc.info().commandLine().unwrap(),
                        owner = proc.info().user().unwrap(),
                )
            }

    private suspend fun pidstatD(): Stream<ProcInfo> = runAndParsePidstat(listOf("-d"), 9)
            .map {
                try {
                    ProcInfo(
                            pid = it[3].toLong(),
                            diskReadKBPS = it[4].toDouble().let { x -> if (x > 0.0) x else 0.0 },
                            diskWriteKBPS = it[5].toDouble().let { x -> if (x > 0.0) x else 0.0 },
                            name = it[8].trim(),
                    )
                } catch (e: Exception) {
                    ProcInfo(0)
                }
            }

    private suspend fun pidstatR(): Stream<ProcInfo> = runAndParsePidstat(listOf("-r"), 10)
            .map {
                try {
                    ProcInfo(
                            pid = it[3].toLong(),
                            uid = it[2].toLong(),
                            memoryBytes = it[7].toLong() * 1000,
                            name = it[9].trim(),
                    )
                } catch (e: Exception) {
                    ProcInfo(0)
                }
            }

    private suspend fun pidstatAll(): Stream<ProcInfo> = runAndParsePidstat(listOf(), 11)
            .map {
                try {
                    ProcInfo(
                            pid = it[3].toLong(),
                            uid = it[2].toLong(),
                            cpuUsagePercent = it[8].toDouble(),
                            //[9] = cpu id
                            name = it[10].trim(),
                    )
                } catch (e: Exception) {
                    ProcInfo(0)
                }
            }

    private suspend fun pidstatV(): Stream<ProcInfo> = runAndParsePidstat(listOf("-v"), 7)
            .map {
                try {
                    ProcInfo(
                            pid = it[3].toLong(),
                            uid = it[2].toLong(),
                            numThreads = it[4].toInt(),
                            name = it[6].trim(),
                    )
                } catch (e: Exception) {
                    ProcInfo(0)
                }
            }

    private suspend fun runAndParsePidstat(args: List<String>, columns: Int) = withTimeout(5000) {
        runInterruptible(Dispatchers.IO) { ProcessBuilder("pidstat", *args.toTypedArray()).start().apply { waitFor() } }
    }
            .inputStream.bufferedReader().lines().skip(3)
            .map {
                it.split(Regex("\\s+"), columns)
                        .map { sub -> sub.trim() }
                        .filter { sub -> sub.isNotEmpty() }
            }
            .filter { it.size == columns }


    private data class ProcInfo(
            val pid: Long,
            val uid: Long? = null,
            val parentPID: Long? = null,
            val name: String? = null,
            val path: String? = null,
            val cmdline: String? = null,
            val owner: String? = null,
            val cpuUsagePercent: Double? = null,
            val memoryBytes: Long? = null,
            val diskReadKBPS: Double? = null,
            val diskWriteKBPS: Double? = null,
            val numThreads: Int? = null,
    ) {
        fun merge(other: ProcInfo): ProcInfo = if (pid == other.pid) ProcInfo(
                pid,
                uid = other.uid ?: uid,
                parentPID = other.parentPID ?: parentPID,
                name = other.name ?: name,
                path = other.path ?: path,
                cmdline = other.cmdline ?: cmdline,
                owner = other.owner ?: owner,
                cpuUsagePercent = other.cpuUsagePercent ?: cpuUsagePercent,
                memoryBytes = other.memoryBytes ?: memoryBytes,
                diskReadKBPS = other.diskReadKBPS ?: diskReadKBPS,
                diskWriteKBPS = other.diskWriteKBPS ?: diskWriteKBPS,
                numThreads = other.numThreads ?: numThreads,
        ) else throw Exception("tried to merge mismatched pids")
    }

    private data class CPUInfo(
            val id: Int,
            val modelName: String? = null,
            val mhz: Double? = null,
            val percentIdle: Double? = null,
    ) {
        fun merge(other: CPUInfo): CPUInfo = if (id == other.id) CPUInfo(
                id,
                modelName = other.modelName ?: modelName,
                mhz = other.mhz ?: mhz,
                percentIdle = other.percentIdle ?: percentIdle,
        ) else throw Exception("tried to merge mismatched ids")
    }

    private fun <T> Optional<T>.unwrap(): T? = orElse(null)
}