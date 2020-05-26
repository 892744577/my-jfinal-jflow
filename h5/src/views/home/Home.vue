<template>
    <div class="home">
        <div class="banner">
            <div class="device" v-for="(item,index) in items" @click="onGeneralClick(item,item)">
                <div class="left">
                    <div> TENON &nbsp; </div>
                    <div></div>
                </div>
            </div>
        </div>
        <div class="buttonList"><div class="plus" @click="onPlusClick('/addDevice',null)"></div></div>
    </div>
</template>

<script>
    import {post} from "../../js/utils";

    export default {
        name: 'Home',
        components: {},
        data () {
            return {
                items: [],
            }
        },
        async created () {
            let viewer = window.navigator.userAgent.toLowerCase();
            if(viewer.match(/MicroMessenger/i) == 'micromessenger'){
                let ret = await post("/user/listGateways")
                //1、获取用户绑定的设备
                ret.devices.map((device,index) => {
                    //2、查询绑定的设备的详细信息（锁名称） ---待确认是否从保修卡取信息还是从devicesKv取，后端接口待确认
                    ret.guarantee.map((item,index)=>{
                        if(device.Id == item.Id  ){
                            this.items.push(Object.assign(device,{Info:JSON.parse(item.Info)}));
                        }
                    })
                    //3、判断设备是否已经绑定了网关
                    let link = device.GatewayId != '' ? '/product':'/productNoGateway';
                    return  Object.assign(device, {link:link});
                });

                //2、保修卡信息
                /*this.items = ret.guarantee.map((item,index)=>{
                 //2判断设备是否已经绑定网关
                 //合并网关id，得出设备列表

                 //根据设备列表，合并设备列表的keyValue
                 let device = ret.devices.filter((device)=>{
                 if(device.Id == item.Id ){
                 return true;
                 }
                 return false;
                 })

                 //3将设备信息作绑定
                 let link = device.length>0 ? '/product':'/productNoGateway';
                 return Object.assign(item,{Info:JSON.parse(item.Info)}, {link:link});
                 })*/

                //3管理员自定义设备信息,这种分组导致length失效
                /*let newRet = [] ;
                 ret.userDeviceKvs||[].map((item,index)=>{
                 let group = item.DevId||''.toString(); //按DevId分组
                 newRet[group]= newRet[group] || [];
                 newRet[group].push(item);
                 });
                 newRet.map((item,index)=>{
                 let userDevicesAttributes = { DevId: index }   //每组都有的参数
                 item.map(({ Key,Value},sequence)=> Object.assign(userDevicesAttributes,{ [Key]: Value}) ) //分组进行kv合并
                 //this.items.push(userDevicesAttributes);
                 });*/
            }
        },
        methods: {
            onGeneralClick: function ({DevId,link}, queryParams) {
                this.$router.push({
                    path: link,
                    query: queryParams
                });//其中login是你定义的一个路由模块
            },
            onPlusClick: function (link, queryParams) {
                this.$router.push({
                    path: link,
                    query: queryParams
                });//其中login是你定义的一个路由模块
            }
        }
    }
</script>

<style scoped>
    .home {
        height: 100vh;
        width: 100vw;
    }

</style>
