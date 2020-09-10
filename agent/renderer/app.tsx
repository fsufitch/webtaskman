import React, { useState, useEffect } from 'react';
import ReactDOM from 'react-dom';
import {ipcRenderer } from 'electron';

import { CounterButton } from './CounterButton';
import { MonitorData } from '@webtaskman/common/types';

function App() {
    const [monitorData, setMonitorData] = useState<MonitorData | undefined>();

    useEffect(() => {
        console.log('effect run');
        ipcRenderer.send('renderer-ready');
        ipcRenderer.on('monitor-update', (e, data) => {
            const parsedData = JSON.parse(data) as MonitorData;
            console.log(`received ${e} ${parsedData}`);
            setMonitorData(parsedData);
        });    
    }, []);

    return <div>
        <p>Hello world! This component was rendered by React.</p>
        <p>
            Have a counter button: 
            <CounterButton />
        </p>
        <p>
            {`${monitorData ? JSON.stringify(monitorData) : 'no data yet'}`}
        </p>
    </div>;
}

const wrapper = document.getElementById('app');

if (wrapper) {
    ReactDOM.render(<App />, wrapper);
} else {
    console.error('No wrapper element found');
}
