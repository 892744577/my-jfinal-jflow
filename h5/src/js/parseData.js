//解析16进制字符串
export  function parse(data) {
    let dataMap = new Map()
    let hexDecimal = [];
    let decimalSystem = [];
    let binarySystem = [];
    let binaryString = "";
    for(let i = 0;i<data.length;i= i+2){
        let dataTemp = data[i] + data[i+1]
        hexDecimal.push(dataTemp); //字符串转16进制
        decimalSystem.push(Number.parseInt('0x'+ dataTemp)); //16进制转10进制
        let binary = (Array(8).join(0) + Number.parseInt('0x'+ dataTemp).toString(2)).slice(-8); //16进制转2进制，补0并倒序
        binarySystem.push(binary)//二进制
        binaryString = binary + binaryString //二进制字符串，右高左低
    }
    return {hexDecimal:hexDecimal,decimalSystem:decimalSystem,binarySystem:binarySystem,binaryString:binaryString}
}

//解析20主命令的01子命令
export  function parse2001(data) {
    let dataMap = new Map()
    let hexDecimal = [];
    let decimalSystem = [];
    let binarySystem = [];
    let binaryString = "";
    let userList = [];
    for(let i = 0;i<data.length;i=i+2){
        let dataTemp = data[i] + data[i+1];
        hexDecimal.push(dataTemp); //字符串转16进制
        decimalSystem.push(Number.parseInt('0x'+ dataTemp)); //16进制转10进制
        let binary = (Array(8).join(0) + Number.parseInt('0x'+ dataTemp).toString(2)).slice(-8); //16进制转2进制，补0
        binarySystem.push(binary)//2进制(8位，从右往左读)
        if(i>0){ //计算出用户列表,暂时不考虑用户超出区块一的情况
            binaryString = binaryString + binary.split('').reverse().join('') //倒序
        }
    }

    //计算有用户的比的id
    let index = 1
    for(let item of binaryString){
        if(item == '1'){
            userList.push({id:index})
        }
        index++
    }
    dataMap.set("hexDecimal",hexDecimal);
    dataMap.set("decimalSystem",decimalSystem);
    dataMap.set("binarySystem",binarySystem);
    dataMap.set("userList",userList);
    return dataMap;
}

//解析20主命令的02子命令
export  function parse2002(data) {
    let dataMap = new Map()
    let hexDecimal = [];
    let decimalSystem = [];
    let binarySystem = [];
    for(let i = 0;i<data.length;i= (i == 2 || i == 6 ? i+4 : i+2)){
        let dataTemp = (i == 2 || i == 6 ? data[i] + data[i+1] + data[i+2] + data[i+3] : data[i] + data[i+1] ) ;
        hexDecimal.push(dataTemp); //字符串转16进制
        decimalSystem.push(Number.parseInt('0x'+ dataTemp)); //16进制转10进制
        let binary = (Array(8).join(0) + Number.parseInt('0x'+ dataTemp).toString(2)).slice(-8); //16进制转2进制，补0并倒序
        binarySystem.push(binary)//二进制
    }
    return {hexDecimal:hexDecimal,decimalSystem:decimalSystem,binarySystem:binarySystem}
}

//解析21主命令01子命令
export  function parse21(data) {
    let change = new Map()
    change.set("log_xh",Number.parseInt('0x'+ data.substr(4,4))) //日志序号
    if(data.substr(0,4) == '2101' || data.substr(0,4) == '2102' || data.substr(0,4) == '2104'){ //门锁用户增删改
        let index_year = Number.parseInt('0x'+ data.substr(16,4))
        let index_month = Number.parseInt('0x'+ data.substr(20,2))
        let index_date = Number.parseInt('0x'+ data.substr(22,2))
        let index_hour = Number.parseInt('0x'+ data.substr(24,2))
        let index_minute = Number.parseInt('0x'+ data.substr(26,2))
        let index_second = Number.parseInt('0x'+ data.substr(28,2))

        change.set("log_user_id",Number.parseInt('0x'+ data.substr(8,4))) //用户id
        change.set("log_user_type",Number.parseInt('0x'+ data.substr(12,2))) //用户类型
        //钥匙类型，开门是16进制，8位二进制
        change.set("log_key_type",data.substr(0,4) == '2104' ? Number.parseInt('0x'+ data.substr(14,2)) : (Array(8).join(0) + Number.parseInt('0x'+ data.substr(14,2)).toString(2)).slice(-8))
        change.set("log_time",new Date(index_year,index_month-1,index_date,index_hour,index_minute,index_second))

    }else if(data.substr(0,4) == '2103'){ //修改密码
        let index_year = Number.parseInt('0x'+ data.substr(12,4))
        let index_month = Number.parseInt('0x'+ data.substr(16,2))
        let index_date = Number.parseInt('0x'+ data.substr(18,2))
        let index_hour = Number.parseInt('0x'+ data.substr(20,2))
        let index_minute = Number.parseInt('0x'+ data.substr(22,2))
        let index_second = Number.parseInt('0x'+ data.substr(24,2))

        change.set("log_user_id",Number.parseInt('0x'+ data.substr(8,4)) ) //用户id
        change.set("log_time",new Date(index_year,index_month-1,index_date,index_hour,index_minute,index_second))

    }else if(data.substr(0,4) == '2105'){ //门锁报警
        let index_year = Number.parseInt('0x'+ data.substr(10,4))
        let index_month = Number.parseInt('0x'+ data.substr(14,2))
        let index_date = Number.parseInt('0x'+ data.substr(16,2))
        let index_hour = Number.parseInt('0x'+ data.substr(18,2))
        let index_minute = Number.parseInt('0x'+ data.substr(20,2))
        let index_second = Number.parseInt('0x'+ data.substr(22,2))

        change.set("log_warn_type",Number.parseInt('0x'+ data.substr(8,2))) //报警原因
        change.set("log_time",new Date(index_year,index_month-1,index_date,index_hour,index_minute,index_second))

    }else if(data.substr(0,4) == '2106'){ //门锁重置
        let index_year = Number.parseInt('0x'+ data.substr(8,4))
        let index_month = Number.parseInt('0x'+ data.substr(12,2))
        let index_date = Number.parseInt('0x'+ data.substr(14,2))
        let index_hour = Number.parseInt('0x'+ data.substr(16,2))
        let index_minute = Number.parseInt('0x'+ data.substr(18,2))
        let index_second = Number.parseInt('0x'+ data.substr(20,2))
        change.set("log_time",new Date(index_year,index_month-1,index_date,index_hour,index_minute,index_second))
    }
    return change;
}
