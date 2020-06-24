// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from "vue";
import App from "./App";
import router from "./router";
import VueCookies from "vue-cookies";
import "@/common/main.scss";
import "./iconFont.css";

import { Lazyload } from "vant";

Vue.use(Lazyload);

// 注册时可以配置额外的选项
Vue.use(Lazyload, {
    lazyComponent: true
});

Vue.use(VueCookies);

import { store } from "./store/store";

Vue.config.productionTip = false;

router.beforeEach((to, form, next) => {
    if (to.meta.title) {
        document.title = to.meta.title;
    }
    next();
});

import eruda from "eruda";
eruda.init();

/* eslint-disable no-new */
new Vue({
    router,
    store,
    render: h => h(App)
}).$mount("#app");
