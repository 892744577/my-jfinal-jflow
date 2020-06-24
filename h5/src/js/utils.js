import VueCookies from "vue-cookies";
import { Base64 } from "js-base64";
import axios from "axios";
import qs from "qs";
import { Toast } from "vant";
export const basePrefix = "/tmr";
const baseUrl = process.env.VUE_APP_BASE_API;
export function getStorageObj(key) {
    let valStr = localStorage.getItem(key);
    if (!valStr) {
        return null;
    }
    return JSON.parse(valStr);
}

export function setStorageObj(key, val) {
    localStorage.setItem(key, JSON.stringify(val));
}

export function getSessionStorageObj(key) {
    let valStr = sessionStorage.getItem(key);
    if (!valStr) {
        return null;
    }
    return JSON.parse(valStr);
}

export function setSessionStorageObj(key, val) {
    sessionStorage.setItem(key, JSON.stringify(val));
}

export async function post(path, urlParams = {}) {
    // path = basePrefix + path
    // Object.assign(urlParams, { userId: getSessionStorageObj("userId") });
    axios.defaults.baseURL = baseUrl;
    let ret = await axios.post(path, qs.stringify(urlParams));
    if (ret.status !== 200) {
        throw Error("请求失败");
    }
    ret = ret.data;
    if (ret.code !== "000000") {
        // Toast.fail({
        //     message: ret.msg,
        //     duration: 1.5 * 1000
        // });
        // throw Error("请求失败：" + ret.desc);
        return null;
    }
    return ret.data;
}

export function sleep(ms) {
    return new Promise(resolve => {
        setTimeout(() => {
            resolve();
        }, ms);
    });
}

export function getCookie(key) {
    let valStr = VueCookies.get(key);
    if (!valStr) {
        return null;
    }
    //valStr = Base64.decode(valStr)
    return valStr;
}

export const getDeviceInfo = function() {
    let obj = {
        clientWidth: 375,
        clientHeight: 667,
        dpr: 2
    };

    try {
        obj.clientWidth = document.documentElement.clientWidth;
        obj.clientHeight = document.documentElement.clientHeight;
        obj.dpr = window.devicePixelRatio;
    } catch (error) {}

    return obj;
};

export const formatAvatarList = function(arr) {
    let avList = [];
    arr.forEach((e, i) => {
        if (e.helperInfo) {
            let obj = JSON.parse(e.helperInfo);
            avList.push(obj);
        }
    });
    return avList;
};
