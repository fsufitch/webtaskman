package name.sufitchi.webtaskman_agent.net

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class Ticker(private val initInterval: Long, private val out: Channel<Long>) : Runnable {
    private val thread = Thread(::run, "$THREAD_BASE_NAME-${threadCount++}")
    private var expectTickID: Long = 1
    private val updateIntervalChannel = Channel<Long>(1)

    fun startThread() {
        thread.start()
    }

    suspend fun updateInterval(interval: Long) = updateIntervalChannel.send(interval)

    override fun run(): Unit = let { poller ->
        runBlocking {
            println("running ticker main thread")
            launch {
                println("starting update interval loop")
                while (true) {
                    val newInterval = updateIntervalChannel.receive()
                    println("received updated interval $newInterval")
                    expectTickID++
                    poller.tickLoop(newInterval)
                }
            }
            launch {
                tickLoop(initInterval)
            }
        }
    }

    private suspend fun tickLoop(interval: Long) {
        println("launching tick loop")
        var tickID = expectTickID
        while (tickID == expectTickID) {
            println("tick")
            out.send(tickID)
            tickID++
            expectTickID = tickID
            delay(interval)
        }
        println("retiring tick loop")
    }

    companion object {
        private var threadCount = 1
        private const val THREAD_BASE_NAME = "name.sufitchi.webtaskman_agent.net.Poller.Thread"
    }
}