// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import VueCookies from 'vue-cookies'

Vue.use(VueCookies);

import {store} from "./store/store";

Vue.config.productionTip = false;

router.beforeEach((to,form,next)=>{
    if(to.meta.title){
        document.title = to.meta.title;
    }
    next();
});

/* eslint-disable no-new */
new Vue({
    el: '#app',
    router,
    store,
    components: {App},
    template: '<App/>'
});
