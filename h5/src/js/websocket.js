import { Toast  } from 'vant';
export class TenonWebsocket {
    static ws = null
    static wsUrl = ""
    static retryTimes = 0
    //static toasts = new Map();//方式二：条件判断
    static msgHandlerCallbackMap = new Map();
    static msgHandler({data}) {
        console.log("收到websocket信息：", data)

        //方式一：添加回调函数
        let {Cmd,SubCmd} = JSON.parse(data);
        let key = Cmd+SubCmd
        if(TenonWebsocket.msgHandlerCallbackMap.get(key)){
            (TenonWebsocket.msgHandlerCallbackMap.get(key))(data);
        }

        //方式二：条件判断
        /**let {Cmd,SubCmd} = JSON.parse(data);
        if(Cmd == 'CmdFromGateway' && SubCmd == 'devAddSuccess'){
            let toast1 = TenonWebsocket.toasts.get("toast1");
            toast1.clear();
            Toast.success("网关绑定成功");
        }**/
    }

    /**
     * 通过websocket发送数据，不对数据进行任何包装
     * @param msg
     */
    static sendData(msg) {
        if (TenonWebsocket.ws) {
            TenonWebsocket.ws.send(msg)

            /**
             * //方式二：条件判断
             if(Cmd == 'CmdFromGateway' && SubCmd == 'devAddSuccess'){
                let toast1 = Toast.loading({
                    duration: 0,
                    message: '网关绑定中...',
                    forbidClick: true
                });
                TenonWebsocket.toasts.set("toast1",toast1);
            }
             **/
        }
    }

    static reinit() {
        setTimeout(() => {
            TenonWebsocket.init(TenonWebsocket.wsUrl)
        }, 1000)
    }

    static init(wsUrl) {
        if (TenonWebsocket.retryTimes > 3) {
            console.log("websocket连接失败")
            return
        }
        TenonWebsocket.retryTimes += 1
        TenonWebsocket.wsUrl = wsUrl
        let _ws = new WebSocket(wsUrl)
        return new Promise((resolve, reject) => {
            _ws.onopen = (e) => {
                TenonWebsocket.ws = _ws
                _ws.onerror = TenonWebsocket.reinit
                _ws.onclose = TenonWebsocket.reinit
                _ws.onmessage = TenonWebsocket.msgHandler
                resolve()
            }
            _ws.onerror = (e) => {
                TenonWebsocket.reinit()
                console.log("ws on error!!!!!!!!!")
                reject(e)
            }
        })
    }
}
