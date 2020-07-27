/* eslint-disable */
const ForkTsCheckerWebpackPlugin = require('fork-ts-checker-webpack-plugin');
const HtmlWebPackPlugin = require('html-webpack-plugin');
const TsconfigPathsPlugin = require('tsconfig-paths-webpack-plugin');
const UglifyJsPlugin = require('uglifyjs-webpack-plugin');
const MiniCssExtractPlugin = require('mini-css-extract-plugin');
const DefinePlugin = require('webpack').DefinePlugin;
const Dotenv = require('dotenv-webpack');

const path = require('path');

const BUILD_DIR = path.join(__dirname, 'build');

let babelLoader = { loader: 'babel-loader' };

let tsLoader = {
    loader: 'ts-loader',
    options: {
        transpileOnly: true,
    },
}

let htmlLoader = { loader: 'html-loader' };
let sassLoader = { loader: 'sass-loader', options: { sourceMap: true } };
let cssLoader = {
    loader: 'css-loader', options: {
        sourceMap: true, modules: {
            localIdentName: '[path][name]__[local]--[hash:base64:5]',
        }
    }
};
let cssModulesTypescriptLoader = { loader: 'css-modules-typescript-loader' };
let miniCssExtractLoader = { loader: MiniCssExtractPlugin.loader };
let styleLoader = { loader: 'style-loader' };
let fileLoader = { loader: 'file-loader', options: { name: '[name]--[contenthash].[ext]' } };
let urlLoader = {
    loader: 'url-loader', options: {
        limit: 8192,
        fallback: fileLoader,
    }
};

const ELECTRON_MAIN = {
    target: 'electron-main',
    devtool: "none",
    entry: {
        main: './main/main.ts',
    },
    output: {
        path: BUILD_DIR,
        filename: "./[name].bundle.js",
    },
    resolve: {
        // Add `.ts` and `.tsx` as a resolvable extension.
        extensions: [".ts", ".tsx", ".js"],
        plugins: [
            new TsconfigPathsPlugin(),
        ],
    },
    module: {
        rules: [
            { test: /\.tsx?$/i, use: [babelLoader, tsLoader] },
            { test: /\.html$/i, use: [htmlLoader] },
            { test: /\.s[ac]ss/i, use: [miniCssExtractLoader, cssModulesTypescriptLoader, cssLoader, sassLoader] },
            { test: /\.(png|jpe?g|gif)$/i, use: [urlLoader] },
        ]
    },
    plugins: [
        new MiniCssExtractPlugin(),
        new ForkTsCheckerWebpackPlugin(),
        new DefinePlugin({
            APP_DIR: JSON.stringify(BUILD_DIR),
        }),
        new Dotenv({ systemvars: true }),
    ],
    optimization: {
        minimizer: [
            new UglifyJsPlugin({
                cache: true,
                sourceMap: true,
            }),
        ],
        splitChunks: {
            chunks: 'all',
        },
    },
};

const ELECTRON_RENDERER = {
    target: 'electron-renderer',
    devtool: "inline-source-map",
    entry: {
        renderer: './renderer/app.tsx',
    },
    output: {
        path: BUILD_DIR,
        filename: "./[name].bundle.js",
    },
    resolve: {
        // Add `.ts` and `.tsx` as a resolvable extension.
        extensions: [".ts", ".tsx", ".js"],
        plugins: [
            new TsconfigPathsPlugin(),
        ],
    },
    module: {
        rules: [
            { test: /\.tsx?$/i, use: [babelLoader, tsLoader] },
            { test: /\.html$/i, use: [htmlLoader] },
            { test: /\.s[ac]ss/i, use: [miniCssExtractLoader, cssModulesTypescriptLoader, cssLoader, sassLoader] },
            { test: /\.(png|jpe?g|gif)$/i, use: [urlLoader] },
        ]
    },
    plugins: [
        new MiniCssExtractPlugin(),
        new ForkTsCheckerWebpackPlugin(),
        new HtmlWebPackPlugin({
            template: "./renderer/index.html",
            filename: "./index.html",
        }),
        new DefinePlugin({
            APP_DIR: JSON.stringify(BUILD_DIR),
        }),
        new Dotenv({ systemvars: true }),
    ],
    optimization: {
        minimizer: [
            new UglifyJsPlugin({
                cache: true,
                sourceMap: true,
            }),
        ],
        splitChunks: {
            chunks: 'all',
        },
    },
}

module.exports = [ELECTRON_RENDERER, ELECTRON_MAIN];