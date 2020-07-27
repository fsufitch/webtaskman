import React from 'react';
import ReactDOM from 'react-dom';
import { CounterButton } from './CounterButton';

function App() {
    return <div>
        <p>Hello world! This component was rendered by React.</p>
        <p>
            Have a counter button: 
            <CounterButton />
        </p>
    </div>;
}

const wrapper = document.getElementById('app');

if (wrapper) {
    ReactDOM.render(<App />, wrapper);
} else {
    console.error('No wrapper element found');
}
