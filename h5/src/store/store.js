import Vuex from "vuex";
import Vue from "vue";
import tabBar from "./tabBar";
import activity from "./activity";
import {
    getStorageObj,
    setStorageObj,
    getSessionStorageObj,
    setSessionStorageObj,
    getDeviceInfo
} from "../js/utils";

/* eslint-disable */

Vue.use(Vuex);
export const store = new Vuex.Store({
    state: {
        jsapiTicket: {},
        userId: getSessionStorageObj("userId"),
        subscribe: getSessionStorageObj("subscribe"),
        shareParams: getSessionStorageObj("shareParams"),
        activityMsg: getSessionStorageObj("activityMsg"),
        loading: false,
        device: getDeviceInfo(),
        userInfo:getSessionStorageObj("userInfo"),
        inited: getSessionStorageObj("inited")
    },
    mutations: {
        setJsapiTicket(store, ticket) {
            store.jsapiTicket = ticket;
        },
        setUserId(store, userId) {
            store.userId = userId;
            setSessionStorageObj("userId", userId);
        },
        setUserInfo(store, userInfo) {
            store.userInfo = userInfo;
            store.userId = userInfo.openId;
            setSessionStorageObj("userId", userInfo.openId);
            setSessionStorageObj("userInfo", userInfo);
        },
        setSubscribe(store, subscribe) {
            store.subscribe = subscribe;
            setSessionStorageObj("subscribe", subscribe);
        },
        setShareParams(store, params) {
            store.shareParams = params;
            setSessionStorageObj("shareParams", params);
        },
        setActivityMsg(store, payload) {
            store.activityMsg = payload;
            setSessionStorageObj("activityMsg", payload);
        },
        showLoading(state) {
            state.loading = true;
        },
        hideLoading(state) {
            state.loading = false;
        },
        setInit(state){
            state.inited = true
            setSessionStorageObj("inited", true);
        }
    },
    modules: {
        tabBar,
        activity
    }
});
