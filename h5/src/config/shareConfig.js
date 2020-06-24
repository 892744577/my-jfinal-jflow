import qs from "qs";
export const shareConfig = {
    //助力
    title: "亚太天能九周年 四川巡礼",
    desc: "帮我助力0.01元购乳胶枕！我教你怎么领！"
};

export const shareConfig2 = {
    //活动
    title: "亚太天能九周年 四川巡礼",
    desc: ""
};

export const shareRouter = {
    supRouter: "#/activityPage7/supFriend", //分享助力
    actRouter: "#/activityPage7/activityPage" //分享活动页
};

export const createShareUrl = function(params, routePath, shareRouter) {
    // aid:1 pid海报id shareId分享id gid助力商品id
    let url1 = location.href.split("?")[0];
    url1.replace(routePath, "");
    let url = url1 + "?" + qs.stringify(params) + shareRouter;
    return url;
};
