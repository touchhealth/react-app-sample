var merge = require('webpack-merge');
var baseConfig = require('./webpack.config.js');
var path = require('path');

module.exports = merge(baseConfig, {
    devServer: {
        contentBase: path.join(__dirname, "src/main/webapp"),
        compress: true,
        port: 9000,
        proxy: {
          "/api": {
            target: "http://localhost:8080"
          }
        },
        publicPath: "/app/"
    }
});