import request from "@/js/request";
import qs from "qs";

export function startSup(data) {
    return request({
        url: "/port/activity/savePortActivityAssist",
        method: "post",
        data: qs.stringify(data)
    });
}

export function supFriend(data) {
    return request({
        url: "/port/activity/savePortActivityHelper",
        method: "post",
        data: qs.stringify(data)
    });
}

export function getSupInfo(data) {
    return request({
        url: "/port/activity/getHelperListByAssistId",
        method: "post",
        data: qs.stringify(data)
    });
}

export function updateShareAndSup(data) {
    return request({
        url: "/port/activity/updatePortActivityShare",
        method: "post",
        data: qs.stringify(data)
    });
}

export function getUserSupId(data) {
    return request({
        url: "/port/activity/getAssistByWxOpenId",
        method: "post",
        data: qs.stringify(data)
    });
}

export function getSupNum(data) {
    return request({
        url: "/port/activity/getAssistCount",
        method: "post",
        data: qs.stringify(data)
    });
}
