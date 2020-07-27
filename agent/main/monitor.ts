import {cpu, mem, proc} from 'node-os-utils';

export interface MonitorData {
    cpu: {
        count: number;
        averageUsage: number;
    };

    memory: {
        total: number;
        used: number;
        free: number;
    };

    proc: {
        count: string | number;
        zombie: string | number;
    }
}

export const runMonitor = (intrevalSeconds: number, callback: (data: MonitorData) => void) => {
    setInterval(async () => {
        Promise.all([
            cpu.usage(intrevalSeconds*5),
            mem.free(),
            mem.used(),
            proc.totalProcesses(),
            proc.zombieProcesses(),
        ]).then(([cpuAverageUsage, memFree, memUsed, procTotal, procZombie]) => {
            callback({
                cpu: {
                    count: cpu.count(),
                    averageUsage: cpuAverageUsage,
                },
                memory: {
                    total: memFree.totalMemMb,
                    free: memFree.freeMemMb,
                    used: memUsed.usedMemMb,
                },
                proc: {
                    count: procTotal,
                    zombie: procZombie,
                },
            });
        });
        
    }, intrevalSeconds*1000);
};