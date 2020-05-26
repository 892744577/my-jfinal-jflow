import Vuex from "vuex";
import Vue from "vue";
import tabBar from "./tabBar";
import {getStorageObj, setStorageObj,getSessionStorageObj,setSessionStorageObj} from "../js/utils";

/* eslint-disable */

Vue.use(Vuex)
export const store = new Vuex.Store({
    state: {
        jsapiTicket: {},
        userId: getSessionStorageObj("userId"),
        subscribe: getSessionStorageObj("subscribe")

    },
    mutations: {
        setJsapiTicket(store, ticket) {
            store.jsapiTicket = ticket
        },
        setUserId(store, userId) {
            store.userId = userId
            setSessionStorageObj("userId", userId)
        },
        setSubscribe(store, subscribe) {
            store.subscribe = subscribe
            setSessionStorageObj("subscribe", subscribe)
        },
    },
    modules: {
        tabBar
    }
})
