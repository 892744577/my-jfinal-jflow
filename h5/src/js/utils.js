import VueCookies from 'vue-cookies'
import {Base64} from "js-base64";
import axios from 'axios';
import qs from 'qs';
export const basePrefix = "/tmr"
export function getStorageObj(key) {
    let valStr = localStorage.getItem(key)
    if (!valStr) {
        return null
    }
    return JSON.parse(valStr)
}

export function setStorageObj(key, val) {
    localStorage.setItem(key, JSON.stringify(val))
}

export function getSessionStorageObj(key) {
    let valStr = sessionStorage.getItem(key)
    if (!valStr) {
        return null
    }
    return JSON.parse(valStr)
}

export function setSessionStorageObj(key, val) {
    sessionStorage.setItem(key, JSON.stringify(val))
}

export async function post(path, urlParams={}) {
    path = basePrefix + path
    Object.assign(urlParams,{ userId:getSessionStorageObj("userId")})
    let ret = await axios.post(path, qs.stringify(urlParams))
    if (ret.status !== 200) {
        throw Error("请求失败")
    }
    ret = ret.data
    if (ret.code !== '000000') {
        throw Error("请求失败：" + ret.desc)
    }
    return ret.data
}

export  function sleep(ms) {
    return new Promise((resolve)=>{
        setTimeout(()=>{
            resolve()
        },ms)
    })
}

export function getCookie(key) {
    let valStr = VueCookies.get(key)
    if (!valStr) {
        return null
    }
    //valStr = Base64.decode(valStr)
    return valStr
}
