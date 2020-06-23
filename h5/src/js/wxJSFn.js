import wx from "weixin-js-sdk";
export default {
    share(obj) {
        // wx.onMenuShareAppMessage({
        //     title: obj.title || "", // 分享标题
        //     desc: obj.desc || "", // 分享描述
        //     link: obj.link, // 分享链接，该链接域名或路径必须与当前页面对应的公众号JS安全域名一致
        //     imgUrl: obj.imgUrl || "", // 分享图标
        //     type: "", // 分享类型,music、video或link，不填默认为link
        //     dataUrl: "", // 如果type是music或video，则要提供数据链接，默认为空
        //     success: function() {
        //         // 用户点击了分享后执行的回调函数
        //     }
        // });
        wx.updateAppMessageShareData({
            title: obj.title || "", // 分享标题
            desc: obj.desc || "", // 分享描述
            link: obj.link, // 分享链接，该链接域名或路径必须与当前页面对应的公众号JS安全域名一致
            imgUrl: obj.imgUrl || "", // 分享图标
            success: function() {
                // 设置成功
            }
        });

        // wx.onMenuShareTimeline({
        //     title: obj.title || "", // 分享标题
        //     link: obj.link, // 分享链接，该链接域名或路径必须与当前页面对应的公众号JS安全域名一致
        //     imgUrl: obj.imgUrl || "", // 分享图标
        //     success: function() {
        //         // 设置成功
        //     }
        // });
        wx.updateTimelineShareData({
            title: obj.title || "", // 分享标题
            link: obj.link, // 分享链接，该链接域名或路径必须与当前页面对应的公众号JS安全域名一致
            imgUrl: obj.imgUrl || "", // 分享图标
            success: function() {
                // 设置成功
            }
        });
    },
    openLocation(obj) {
        wx.openLocation({
            latitude: obj.latitude, // 纬度，浮点数，范围为90 ~ -90
            longitude: obj.longitude, // 经度，浮点数，范围为180 ~ -180。
            name: obj.name, // 位置名
            address: obj.address, // 地址详情说明
            scale: 26, // 地图缩放级别,整形值,范围从1~28。默认为最大
            infoUrl: "" // 在查看位置界面底部显示的超链接,可点击跳转
        });
    }
};
