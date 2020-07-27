import { app, ipcMain, BrowserWindow, Menu, Tray } from 'electron';
import * as path from 'path';

import KittenIcon from './kitten-small.png';

import { runMonitor } from './monitor';

const KittenPath = path.join(APP_DIR, KittenIcon);

const createWindow = () => {
    const mainWindow = new BrowserWindow({
        width: 1000,
        height: 800,
        center: true,
        webPreferences: {
            nodeIntegration: true,
        },
        icon: KittenPath,
    });

    mainWindow.loadFile(path.join(APP_DIR, 'index.html'));
    //mainWindow.setMenu(null);

    return mainWindow;
};

const createTray = (
    onShow: () => void,
    onQuit: () => void,
) => {
    const tray = new Tray(KittenPath);
    const contextMenu = Menu.buildFromTemplate([
        { label: 'webtaskman-agent v0.0.1', type: 'normal', enabled: false },
        { label: 'Show app window', type: 'normal', click: onShow },
        { label: 'Quit', type: 'normal', click: onQuit },
    ]);
    tray.setToolTip('This is my application.');
    tray.setContextMenu(contextMenu);

    return tray;
};

app.on('ready', () => {
    let quitting = false;
    app.on('activate', function () {
        // On macOS it's common to re-create a window in the app when the
        // dock icon is clicked and there are no other windows open.
        console.log('activate');
        if (BrowserWindow.getAllWindows().length === 0) {
            createWindow();
        } else {
            app.show();
        }
    });
    app.on('before-quit', () => {
        console.log('before-quit');
        quitting = true;
    });

    const mainWindow  = createWindow();

    mainWindow.on('close', (e: Event) => {
        console.log(`close; quitting? ${quitting}`);
        if (!quitting) {
            e.preventDefault();
            mainWindow.hide();    
        }
    });

    const tray = createTray(
        () => { 
            console.log('show from menu');
            mainWindow.show(); 
            mainWindow.focus(); 
        },
        () => {
            console.log('quit from menu');
            app.quit(); 
        },
    );

    tray.on('click', () => {
        console.log('tray click');
    });

    ipcMain.once('renderer-ready', event => {
        console.log('received renderer-ready');
        runMonitor(2, data => {
            event.reply('monitor-update', JSON.stringify(data));
            console.log(data);
        });    
    });
});
    
    
// // Quit when all windows are closed, except on macOS. There, it's common
// // for applications and their menu bar to stay active until the user quits
// // explicitly with Cmd + Q.
// app.on('window-all-closed', () => {
//     if (process.platform !== 'darwin') {
//         app.quit();
//     }
// });    
