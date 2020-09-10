import { ProcessDetails } from 'process-list';

export interface MonitorData {
    cpu: {
        count: number;
        averageUsage: number;
    };

    memory: {
        total: number;  // bytes
        used: number;  // bytes
        free: number;  // bytes
    };

    proc: {
        count: string | number;
        zombie: string | number;
        procs: ProcessDetails[];
    }
}