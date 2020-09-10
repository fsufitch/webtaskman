
declare module 'process-list' {
    interface ProcessDetails {
        /** process pid */
        pid?: number,
    
        /** parent process pid */
        ppid?: number,
    
        /** process name (title) */
        name?: string,
    
        /** full path to the process binary file */
        path?: string,
    
        /** threads per process */
        threads?: number,
    
        /** the owner of the process */
        owner?: string,
    
        /** an os-specific process priority */
        priority?: number,
    
        /** full command line of the process */
        cmdline?: string,
    
        /** the process start date / time */
        starttime?: Date,
    
        /** virtual memory size in bytes used by process */
        vmem?: string,
    
        /** physical memory size in bytes used by process */
        pmem?: string,
    
        /** cpu usage by process in percent */
        cpu?: number,
    
        /** amount of time in ms that this process has been scheduled in user mode */
        utime?: string,
    
        /** amount of time that in ms this process has been scheduled in kernel mode */
        stime?: string,
    }
    
    /** List of allowed fields. */
    const allowedFields: (keyof ProcessDetails)[];

    /** Returns the list of the launched processes. */
    function snapshot(): Promise<ProcessDetails[]>;
}
