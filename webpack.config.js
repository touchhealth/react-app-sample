const webpack = require('webpack');
const path = require('path');
const fs = require('fs');
const HtmlWebpackPlugin = require('html-webpack-plugin')

module.exports = {
    entry: './src/main/js/index.js',
    output: {
        filename: 'bundle-[hash].js',
        path: path.join(__dirname, 'src/main/webapp/app'),
    },
    resolve: {
        modules: [ "node_modules" ]
    },
    devtool: "source-map",
    module: {
        rules: [{
                test: /\.js$/,
                exclude: /(node_modules|bower_components)/,
                use: [ 'babel-loader' ]
            },
            {
                test: /\.css$/,
                use: [ 'style-loader', 'css-loader' ]
            },
            {
                test: /\.(png|svg|jpg|gif)$/,
                use: [ 'file-loader' ]
            },
            {
                test: /\.(woff|woff2|eot|ttf|otf)$/,
                use: [ 'file-loader' ]
            }
        ]
    },
    optimization: {
        minimize: false,
        splitChunks: {
            chunks: 'all'
        }
    },
    plugins: [        
        new HtmlWebpackPlugin({
            title: "react-app-sample",
            meta: {
                viewport: 'width=device-width, initial-scale=1, shrink-to-fit=no'
            },
            template: path.join(__dirname, 'src/main/js/_template.html')
        })
    ] 
};