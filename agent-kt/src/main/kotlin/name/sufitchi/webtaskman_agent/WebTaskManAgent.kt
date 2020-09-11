package name.sufitchi.webtaskman_agent

class WebTaskManAgent {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            println("hello world")

            ProcessHandle.allProcesses()
                    .forEach {
                        println("${it.pid()} - ${it.info().user().orElse("<nil>")} - ${it.info().command().orElse("<nil>")} - ${it.info().commandLine().orElse("<nil>")}")
                    }
        }    
    }
}