// https://github.com/michael-ciniawsky/postcss-load-config

module.exports = {
    plugins: {
        "postcss-import": {},
        "postcss-url": {},
        // to edit target browsers: use "browserslist" field in package.json
        autoprefixer: {},
        "postcss-px-to-viewport": {
            unitToConvert: "px",
            viewportWidth: 1080,
            unitPrecision: 5,
            propList: ["*"],
            viewportUnit: "vw",
            fontViewportUnit: "vw",
            selectorBlackList: [],
            minPixelValue: 1,
            mediaQuery: false,
            replace: true,
            exclude: [/node_modules/],
            include: undefined,
            landscape: false,
            landscapeUnit: "vw",
            landscapeWidth: 568
        }
    }
};
