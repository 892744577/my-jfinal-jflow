import Vue from "vue";
import Router from "vue-router";
import HelloWorld from "@/components/HelloWorld";

Vue.use(Router);

export default new Router({
    routes: [
        {
            path: "/",
            name: "HelloWorld",
            component: HelloWorld
        },
        {
            path: "/activityPage618",
            name: "activityPage618",
            component: () => import("@/views/ActivityPage/activityPage618")
        }
    ]
});
