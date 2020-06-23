import Vue from "vue";
import Router from "vue-router";
// import HelloWorld from "@/components/HelloWorld";
import blank from "@/views/blank/blank";

Vue.use(Router);

export default new Router({
    routes: [
        {
            path: "/",
            name: "blank",
            component: blank
        },
        {
            path: "/activityPage7",
            name: "activityPage7",
            component: () => import("@/views/ActivityPage/activityPage7"),
            redirect: "/activityPage7/activityPage",
            children: [
                // {
                //     //助力页
                //     path: "supportPage",
                //     name: "act7support",
                //     component: () =>
                //         import("@/views/ActivityPage/components/supportPage"),
                //     meta: {
                //         activeTab: 0
                //     }
                // },
                // {
                //     //首页
                //     path: "index",
                //     name: "act7banner",
                //     component: () =>
                //         import("@/views/ActivityPage/components/bannerPage"),
                //     meta: {
                //         activeTab: 0,
                //         hideTabbar: true
                //     }
                // },
                {
                    //秒杀活动页
                    path: "activityPage",
                    name: "act7index",
                    component: () =>
                        import("@/views/ActivityPage/components/indexPage"),
                    meta: {
                        activeTab: 0
                    }
                },
                {
                    path: "shopListPage",
                    name: "act7shop",
                    component: () =>
                        import("@/views/ActivityPage/components/shopListPage"),
                    meta: {
                        activeTab: 2
                    }
                },
                // {
                //     //我的助力
                //     path: "supportIndex",
                //     name: "act7supportIndex",
                //     component: () =>
                //         import("@/views/ActivityPage/components/supportIndex"),
                //     meta: {
                //         activeTab: 0
                //     }
                // },
                {
                    path: "sharePage",
                    name: "act7share",
                    component: () =>
                        import("@/views/ActivityPage/components/sharePage"),
                    meta: {
                        activeTab: 3
                    }
                },
                {
                    path: "postPage",
                    name: "postPage",
                    component: () =>
                        import("@/views/ActivityPage/components/postPage"),
                    meta: {
                        activeTab: 3
                    }
                },
                {
                    //发起助力
                    path: "startSup",
                    name: "startSup",
                    component: () =>
                        import("@/views/ActivityPage/supPage/startSup"),
                    meta: {
                        activeTab: 0
                    }
                },
                {
                    //填写信息
                    path: "fillMsg",
                    name: "fillMsg",
                    component: () =>
                        import("@/views/ActivityPage/supPage/fillMsg"),
                    meta: {
                        activeTab: 0
                    }
                },
                {
                    //我的助力
                    path: "mySup",
                    name: "mySup",
                    component: () =>
                        import("@/views/ActivityPage/supPage/mySup"),
                    meta: {
                        activeTab: 0
                    }
                },
                {
                    //帮助好友助力页
                    path: "supFriend",
                    name: "supFriend",
                    component: () =>
                        import("@/views/ActivityPage/supPage/supFriend"),
                    meta: {
                        activeTab: 0
                    }
                },
                {
                    //帮助好友助力成功页
                    path: "supFriendSuc",
                    name: "supFriendSuc",
                    component: () =>
                        import("@/views/ActivityPage/supPage/supFriendSuc"),
                    meta: {
                        activeTab: 0
                    }
                },
            ]
        }
    ]
});
