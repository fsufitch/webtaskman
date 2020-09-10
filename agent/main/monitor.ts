import {cpu, mem, proc} from 'node-os-utils';
import { snapshot } from 'process-list';
import { MonitorData } from '@webtaskman/common/types';

export const runMonitor = (intrevalSeconds: number, callback: (data: MonitorData) => void) => {
    setInterval(async () => {
        Promise.all([
            cpu.usage(intrevalSeconds*5),
            mem.free(),
            mem.used(),
            proc.totalProcesses(),
            proc.zombieProcesses(),
            snapshot(),
            
        ]).then(([cpuAverageUsage, memFree, memUsed, procTotal, procZombie, procs]) => {
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
                    procs,
                },
            });
        });
        
    }, intrevalSeconds*1000);
};