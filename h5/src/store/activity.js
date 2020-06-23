import {
    startSup,
    supFriend,
    getSupInfo,
    updateShareAndSup,
    getUserSupId,
    getSupNum
} from "@/api/activity";
import { shops } from "@/js/shops";

export default {
    state: {
        supCount: {
            //统计信息
            involvedCount: 0,
            browseCount: 0,
            assistCount: 0,
            successCount: 0
        },
        avList: [], //好友助力头像
        avListMy: [], //我的助力头像
        supInfo: {}, //好友助力信息
        supInfoMy: {}, //我的助力信息
        isReady: false, //活动页信息加载
        shopsShow: [],
        shopsOrigin: shops,
        tabbarShow: true
    },
    mutations: {
        setSupCount(state, payload) {
            state.supCount = payload;
        },
        setAvList(state, payload) {
            state.avList = payload;
        },
        setAvListMy(state, payload) {
            state.avListMy = payload;
        },
        setSupInfo(state, payload) {
            state.supInfo = payload;
        },
        setSupInfoMy(state, payload) {
            state.supInfoMy = payload;
        },
        pageReady(state) {
            state.isReady = true;
        },
        setShopsShow(state, payload) {
            state.shopsShow = payload;
        },
        setTabbarShow(state, payload) {
            state.tabbarShow = payload;
        }
    },
    actions: {
        startSup({}, payload) {
            //发起助力
            return new Promise((resolve, reject) => {
                startSup(payload)
                    .then(res => {
                        resolve(res);
                    })
                    .catch(err => {
                        console.log(err);
                    });
            });
        },
        supFriend({}, payload) {
            //帮忙助力
            return new Promise((resolve, reject) => {
                supFriend(payload)
                    .then(res => {
                        resolve(res);
                    })
                    .catch(err => {
                        console.log(err);
                    });
            });
        },
        getSupInfo({}, payload) {
            //获取助力信息
            return new Promise((resolve, reject) => {
                getSupInfo(payload)
                    .then(res => {
                        resolve(res);
                    })
                    .catch(err => {
                        console.log(err);
                    });
            });
        },
        updateShareAndSup({}, payload) {
            //管理shareid和助力id
            return new Promise((resolve, reject) => {
                updateShareAndSup(payload)
                    .then(res => {
                        resolve(res);
                    })
                    .catch(err => {
                        console.log(err);
                    });
            });
        },
        getUserSupId({}, payload) {
            //获取当前人的助力id
            return new Promise((resolve, reject) => {
                getUserSupId(payload)
                    .then(res => {
                        resolve(res);
                    })
                    .catch(err => {
                        console.log(err);
                    });
            });
        },
        getSupNum({ commit }, payload) {
            //获取助力数据
            return new Promise((resolve, reject) => {
                getSupNum(payload)
                    .then(res => {
                        if (res.code == "000000") {
                            commit("setSupCount", res.data);
                        }
                        resolve(res);
                    })
                    .catch(err => {
                        console.log(err);
                    });
            });
        }
    },
    namespaced: true
};
