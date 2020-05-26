<template>
    <div class="hello">
        helloxxxxxxxxxxx
        openId = {{userId}}
        <label>
            <textarea rows="10" style="width: 90vw" v-model="sendViaMqtt"></textarea>
        </label>
        <button type="button" @click="sendMqttData">发送mqtt</button>
    </div>
</template>

<script>

    import {mapState} from "vuex";
    import {post, sleep} from "../js/utils";
    import {TenonWebsocket} from "../js/websocket";

    export default {
        name: 'HelloWorld',
        computed: {
            ...mapState(["userId"]),
        },
        async created() {
            console.log("helllllllllllllllllllllllllo")
            console.log("userId = ", this.userId)
        },
        data() {
            return {
                sendViaMqtt: "",
                msg: 'Welcome to Your Vue.js App22222222'
            }
        },

        methods: {
            sendMqttData() {
                if (!this.sendViaMqtt) {
                    alert("待发送的数据为空")
                    return
                }
                TenonWebsocket.sendData(this.sendViaMqtt)
            },
        }

    }
</script>

<style scoped>

</style>
