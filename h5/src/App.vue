<template>
    <div id="app">
        <router-view/>
    </div>
</template>

<script>
    import wx from "weixin-js-sdk"
    import {basePrefix, getCookie, getStorageObj} from "./js/utils";
    import {mapMutations, mapState} from "vuex";
    import {TenonWebsocket} from "./js/websocket";
    import qs from 'qs';
    import { post } from "./js/utils";
    import { Dialog } from 'vant';

    export default {
        name: 'App',
        components: {
            Dialog
        },
        async created() {
            //外部浏览器跳过
            let viewer = window.navigator.userAgent.toLowerCase();
            if(viewer.match(/MicroMessenger/i) == 'micromessenger'){
                //微信浏览器
                let jsApiTicket = await post("/mp/getJsapiConfig",{url:encodeURI(window.location.href)});

                //1、判断是否没有授权，没授权则重定向授权
                this.redirectOrNot(jsApiTicket);
                //2、若是通过用户，则获取用户信息
                const resQuery = qs.parse(window.location.href.split("?")[1].split("#")[0])
                let user = await post("/mp/oauth2getAccessToken",{code: resQuery.code})
                this.loadUser(user.openId)
                //3、url添加调试代码
                this.debug()
                //4、添加业务
                await this.doBusiness(resQuery,jsApiTicket)
            }
        },
        computed: {
            ...mapState(["userId","subscribe"])
        },
        methods: {
            ...mapMutations(["setJsapiTicket", "setUserId","setSubscribe"]),
            redirectOrNot(jsApiTicket) {
                const baseUrl = window.location.href.split("#")[0];
                const resQuery = qs.parse(window.location.href.split("?")[1].split("#")[0])
                //重定向服务器
                if( typeof(resQuery) == "undefined" || typeof(resQuery.code) == "undefined" || resQuery.code == null){

                    console.log("重定向")
                    const redirectUrl = encodeURIComponent(baseUrl);
                    const dst = `https://open.weixin.qq.com/connect/oauth2/authorize?appid=${jsApiTicket.appId}&redirect_uri=${redirectUrl}&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect`
                    window.location.href = dst
                }
            },
            loadUser(openId){
                this.setUserId(openId)
            },
            debug: function (){
                let {debug} = qs.parse(window.location.href.split("#")[0].split("?")[1])
                if(debug){
                    localStorage.clear()
                    sessionStorage.clear()
                }
            },
            doBusiness: async function (resQuery,jsApiTicket){
                let shareNew = null;
                let params={};
                if(typeof(resQuery.pid)!='undefined' && resQuery.pid !=null) {
                    let hb = await post("/port/activity/getActByPbId", {pbId: resQuery.pid});
                    if (hb.pbSourceOpenid != this.userId) {
                        //非海报所有者打开了活动连接,通过海报生成本次分享
                        shareNew = await post("/port/activity/activityShare", {
                            shareOpenId: hb.pbSourceOpenid,
                            toShareOpenId: this.userId,
                            pbId: resQuery.pid
                        })
                        Object.assign(params,{shareId :shareNew.id})
                    }else{
                        Object.assign(params,{pbId: resQuery.pid})
                    }
                }else if(typeof(resQuery.shareId)!='undefined' && resQuery.shareId !=null) {
                    let share = await post("/port/activity/getActByShareId", {shareId: resQuery.shareId});
                    if (share.srToShareOpenid != this.userId) {
                        //非分享人打开了活动连接,通过海报生成本次分享
                        shareNew = await post("/port/activity/activityShare", {
                            shareOpenId: share.srToShareOpenid ,
                            toShareOpenId: this.userId,
                            pbId: share.pbId
                        })
                        Object.assign(params,{shareId :shareNew.id})
                    }else{
                        Object.assign(params,{shareId: resQuery.shareId})
                    }
                }else{
                    console.info("缺少参数海报id或分享id")
                   return
                }
                //分享
                wx.config({
                    debug: false,
                    beta: true,
                    appId: jsApiTicket.appId,
                    timestamp: jsApiTicket.timestamp,
                    nonceStr: jsApiTicket.nonceStr,
                    signature: jsApiTicket.signature,
                    jsApiList: [
                        'checkJsApi',
                        'updateAppMessageShareData',
                        'onMenuShareAppMessage'
                    ]
                })
                wx.ready(function () {   //需在用户可能点击分享按钮前就先调用
                    console.info(params)
                    wx.updateAppMessageShareData({
                        title: '活动1', // 分享标题
                        desc: '活动1', // 分享描述
                        link: window.location.href.split("?")[0]+ "?" + qs.stringify(params), // 分享链接，该链接域名或路径必须与当前页面对应的公众号JS安全域名一致
                        imgUrl: '', // 分享图标
                        success: function () {
                            // 设置成功
                        }
                    })
                    wx.onMenuShareAppMessage({
                        title: '活动2', // 分享标题
                        desc: '活动2', // 分享描述
                        link: window.location.href.split("?")[0] + "?" + qs.stringify(params), // 分享链接，该链接域名或路径必须与当前页面对应的公众号JS安全域名一致
                        imgUrl: '', // 分享图标
                        type: '', // 分享类型,music、video或link，不填默认为link
                        dataUrl: '', // 如果type是music或video，则要提供数据链接，默认为空
                        success: function () {
                            // 用户点击了分享后执行的回调函数
                        }
                    })
                });

            }
        }
    }
</script>

<style>
    #app {
        font-family: 'Avenir', Helvetica, Arial, sans-serif;
        -webkit-font-smoothing: antialiased;
        -moz-osx-font-smoothing: grayscale;
        text-align: center;
        color: #2c3e50;
    }
</style>
