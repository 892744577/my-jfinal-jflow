const path = require("path");
const CompressionWebpackPlugin = require("compression-webpack-plugin");
// const UglifyjsWebpackPlugin = require("uglifyjs-webpack-plugin");
const TerserPlugin = require("terser-webpack-plugin");
// var LodashModuleReplacementPlugin = require("lodash-webpack-plugin");
const { BundleAnalyzerPlugin } = require("webpack-bundle-analyzer");
const port = process.env.port || process.env.npm_config_port || 8081;
const cdnDomian = "http://app.aptenon.com/crm/dist/"; // cdn域名，如果有cdn修改成对应的cdn
const name = "亚太天能"; // page title
const IS_PRODUCTION = process.env.NODE_ENV === "production";
const cdn = {
    css: [],
    js: [
        "https://cdn.bootcss.com/vue/2.6.10/vue.min.js",
        // "https://cdn.bootcss.com/vue-router/3.0.3/vue-router.min.js",
        "https://cdn.bootcss.com/vuex/3.1.0/vuex.min.js",
        "https://cdn.bootcss.com/axios/0.19.0-beta.1/axios.min.js",
        "https://cdn.bootcss.com/js-cookie/2.2.1/js.cookie.min.js"
    ]
};

const externals = {
    vue: "Vue",
    // "vue-router": "VueRouter",
    vuex: "Vuex",
    axios: "axios",
    "js-cookie": "Cookies"
};

function resolve(dir) {
    return path.join(__dirname, dir);
}

module.exports = {
    publicPath: IS_PRODUCTION ? cdnDomian : "./",
    indexPath: "activity.html",
    outputDir: "dist",
    assetsDir: ".",
    lintOnSave: false,
    productionSourceMap: false,
    transpileDependencies: ["vant", "fuse", "fuse.js"],
    devServer: {
        port: port,
        open: true,
        overlay: {
            warnings: false,
            errors: true
        },
        proxy: {
            // change xxx-api/login => mock/login
            // detail: https://cli.vuejs.org/config/#devserver-proxy

            "/crm": {
                // target: "http://192.168.110.43:8220",
                target: "http://app.aptenon.com",
                pathRewrite: {
                    "/crm": "/crm"
                },
                ws: true,
                changeOrigin: true,
                cookieDomainRewrite: { "*": "localhost" },
                cookiePathRewrite: {
                    "*": "localhost"
                }
            }
        }
    },
    configureWebpack: {
        // provide the app's title in webpack's name field, so that
        // it can be accessed in index.html to inject the correct title.
        name: name,
        resolve: {
            alias: {
                "@": resolve("src"), // 主目录
                views: resolve("src/views"), // 页面
                components: resolve("src/components"), // 组件
                api: resolve("src/api"), // 接口
                utils: resolve("src/utils"), // 通用功能
                assets: resolve("src/assets"), // 静态资源
                style: resolve("src/style") // 通用样式
            }
        }
    },
    chainWebpack(config) {
        // config.plugins.delete("preload"); // TODO: need test
        // config.plugins.delete("prefetch"); // TODO: need test
        // config.entry('main').add('babel-polyfill')
        // set svg-sprite-loader
        config.module
            .rule("svg")
            .exclude.add(resolve("src/icons"))
            .end();
        config.module
            .rule("icons")
            .test(/\.svg$/)
            .include.add(resolve("src/icons"))
            .end()
            .use("svg-sprite-loader")
            .loader("svg-sprite-loader")
            .options({
                symbolId: "icon-[name]"
            })
            .end();

        // set preserveWhitespace
        config.module
            .rule("vue")
            .use("vue-loader")
            .loader("vue-loader")
            .tap(options => {
                options.compilerOptions.preserveWhitespace = true;
                return options;
            })
            .end();

        config
            // https://webpack.js.org/configuration/devtool/#development
            .when(process.env.NODE_ENV === "development", config =>
                config.devtool("cheap-source-map")
            );

        config.when(process.env.NODE_ENV !== "development", config => {
            // config
            //     .plugin("ScriptExtHtmlWebpackPlugin")
            //     .after("html")
            //     .use("script-ext-html-webpack-plugin", [
            //         {
            //             // `runtime` must same as runtimeChunk name. default is `runtime`
            //             inline: /runtime\..*\.js$/
            //         }
            //     ])
            //     .end();
            config.optimization.splitChunks({
                chunks: "all",
                cacheGroups: {
                    libs: {
                        name: "chunk-libs",
                        test: /[\\/]node_modules[\\/]/,
                        priority: 10,
                        chunks: "initial" // only package third parties that are initially dependent
                    },
                    commons: {
                        name: "chunk-commons",
                        test: resolve("src/components"), // can customize your rules
                        minChunks: 3, //  minimum common number
                        priority: 5,
                        reuseExistingChunk: true
                    }
                }
            });
            config.optimization.runtimeChunk("single");
        });
        if (IS_PRODUCTION) {
            // config.plugin("analyzer").use(BundleAnalyzerPlugin);
            // config.plugin("lodash-webpack-plugin").use(LodashModuleReplacementPlugin);
            // config.plugin("html").tap(args => {
            //   args[0].cdn = cdn;
            //   return args;
            // });
            // config.externals(externals);
            config.plugin("html").tap(args => {
                args[0].minify.minifyCSS = true; // 压缩html中的css
                return args;
            });
            // gzip需要nginx进行配合
            config
                .plugin("compression")
                .use(CompressionWebpackPlugin)
                .tap(() => [
                    {
                        test: /\.js$|\.html$|\.css/, // 匹配文件名
                        threshold: 10240, // 超过10k进行压缩
                        deleteOriginalAssets: false // 是否删除源文件
                    }
                ]);
        }
    },
    css: {
        // 是否使用css分离插件 ExtractTextPlugin
        extract: !!IS_PRODUCTION,
        // 开启 CSS source maps?
        sourceMap: false,
        // css预设器配置项
        // 启用 CSS modules for all css / pre-processor files.
        loaderOptions: {}
    }
};
