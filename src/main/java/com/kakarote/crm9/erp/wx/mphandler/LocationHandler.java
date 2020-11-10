package com.kakarote.crm9.erp.wx.mphandler;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.kakarote.crm9.erp.wx.mpbuilder.TextBuilder;
import com.kakarote.crm9.erp.wx.util.BaiduMapUtils;
import com.kakarote.crm9.erp.wx.util.DateUtil;
import com.kakarote.crm9.erp.wx.util.MyComparator;
import com.kakarote.crm9.erp.wxcms.entity.WxcmsAccountQrcodeFans;
import com.kakarote.crm9.erp.wxcms.entity.WxcmsAccountShop;
import com.kakarote.crm9.erp.wxcms.entity.WxcmsAccountShopQrcode;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

import static me.chanjar.weixin.common.api.WxConsts.XmlMsgType;

/**
 * @author Binary Wang(https://github.com/binarywang)
 */
public class LocationHandler extends AbstractHandler {

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage,
                                    Map<String, Object> context, WxMpService wxMpService,
                                    WxSessionManager sessionManager) {
        if (wxMessage.getMsgType().equals(XmlMsgType.LOCATION)) {
            //TODO 接收处理用户发送的地理位置消息
            try {
                String content = "感谢反馈，您的的地理位置已收到！";
                return new TextBuilder().build(content, wxMessage, null);
            } catch (Exception e) {
                this.logger.error("位置消息接收处理失败", e);
                return null;
            }
        }

        //上报地理位置事件
        this.logger.info("上报地理位置，纬度 : {}，经度 : {}，精度 : {}",
            wxMessage.getLatitude(), wxMessage.getLongitude(), String.valueOf(wxMessage.getPrecision()));

        //add by wangkaida
        logger.debug("进入上报地理位置事件:"+wxMessage.toString());
        String toUserName = wxMessage.getToUser(); //开发者微信号
        String fromUserName = wxMessage.getFromUser(); //发送方帐号（一个OpenID）
        String createTime = DateUtil.timestampToDateStr(String.valueOf(wxMessage.getCreateTime())); //消息创建时间 （整型）
        String msgType = wxMessage.getMsgType(); //消息类型，event
        String event = wxMessage.getEvent(); //事件类型，LOCATION
        String latitude = String.valueOf(wxMessage.getLatitude()); //地理位置纬度
        String longitude = String.valueOf(wxMessage.getLongitude()); //地理位置经度
        String precision = String.valueOf(wxMessage.getPrecision()); //地理位置精度

        //亚太天能公众号
        if ("gh_9594312e8ff1".equals(toUserName)) {
            logger.debug(fromUserName+"进入亚太天能公众号,上报地理位置");
            String formatted_address = "";
            String country = "";
            String province = "";
            String city = "";
            String district = "";
            String street = "";
            String provinceStr = "";
            String cityStr = "";
            String districtStr = "";
            if (latitude != null & longitude != null) {
                JSONObject jsonObject = BaiduMapUtils.changeLocationToBaidu(latitude, longitude);
                int status = jsonObject.getIntValue("status");
                if (status == 0) {
                    JSONArray jsonArray = jsonObject.getJSONArray("result");
                    JSONObject jsonObj = (JSONObject)jsonArray.get(0);
                    String x = jsonObj.getString("x");
                    String y = jsonObj.getString("y");
                    //根据百度地图坐标获取详细地址信息
                    JSONObject jsonLocation = BaiduMapUtils.getLocationInfo(x, y);
                    int locationStatus = jsonLocation.getIntValue("status");
                    if (locationStatus == 0) {
                        JSONObject jObj = jsonLocation.getJSONObject("result");
                        formatted_address = jObj.getString("formatted_address");
                        JSONObject jsonObjDetail = jObj.getJSONObject("addressComponent");
                        country = jsonObjDetail.getString("country");
                        provinceStr = jsonObjDetail.getString("province");
                        if (provinceStr.indexOf("省") > 0) {
                            province = provinceStr.substring(0, provinceStr.lastIndexOf("省"));
                        } else if (provinceStr.indexOf("市") > 0) {
                            province = provinceStr.substring(0, provinceStr.lastIndexOf("市"));
                        } else if (provinceStr.indexOf("自治区") > 0) {
                            if (provinceStr.indexOf("壮族自治区") > 0) {
                                province = provinceStr.substring(0, provinceStr.lastIndexOf("壮族自治区"));
                            } else if (provinceStr.indexOf("回族自治区") > 0) {
                                province = provinceStr.substring(0, provinceStr.lastIndexOf("回族自治区"));
                            } else if (provinceStr.indexOf("维吾尔自治区") > 0) {
                                province = provinceStr.substring(0, provinceStr.lastIndexOf("维吾尔自治区"));
                            } else {
                                province = provinceStr.substring(0, provinceStr.lastIndexOf("自治区"));
                            }
                        } else {
                            province = provinceStr;
                        }
                        cityStr = jsonObjDetail.getString("city");
                        if (cityStr.indexOf("市") > 0) {
                            city = cityStr.substring(0, cityStr.lastIndexOf("市"));
                        } else if (cityStr.indexOf("自治州") > 0) {
                            city = cityStr.substring(0, cityStr.lastIndexOf("自治州"));
                        } else if (cityStr.indexOf("盟") > 0) {
                            city = cityStr.substring(0, cityStr.lastIndexOf("盟"));
                        } else {
                            city = cityStr;
                        }
                        districtStr = jsonObjDetail.getString("district");
                        if (districtStr.indexOf("区") > 0) {
                            district = districtStr.substring(0, districtStr.lastIndexOf("区"));
                        } else if (districtStr.indexOf("县") > 0) {
                            district = districtStr.substring(0, districtStr.lastIndexOf("县"));
                        } else if (districtStr.indexOf("市") > 0) {
                            district = districtStr.substring(0, districtStr.lastIndexOf("市"));
                        } else if (districtStr.indexOf("旗") > 0) {
                            district = districtStr.substring(0, districtStr.lastIndexOf("旗"));
                        } else {
                            district = districtStr;
                        }
                        street = jsonObjDetail.getString("street");
                        logger.debug("formatted_address:"+formatted_address+"country:"+country+"province:"+province+"city:"+city+"district:"+district+"street:"+street);
                    }else {
                        //调用通过经度纬度获取详细地址信息失败
                        logger.debug("调用通过经度纬度获取详细地址信息失败");
                        return null;
                    }
                }else {
                    //调用把微信获取到的经纬度转变为百度的经纬度失败
                    logger.debug("调用把微信获取到的经纬度转变为百度的经纬度失败");
                    return null;
                }
            }

            logger.debug("toUserName:"+toUserName+","+"fromUserName:"+fromUserName+","+"createTime:"+createTime+","+"msgType:"+msgType+","+"event:"+event+","+"latitude:"+latitude+","+"longitude:"+longitude+","+"precision:"+precision);
            //已经通过扫码关注店铺店铺的用户不进行地理位置划分
            WxcmsAccountQrcodeFans wxcmsAccountQrcodeFansDb = WxcmsAccountQrcodeFans.dao.findFirst(Db.getSql("admin.wxcmsAccountQrcodeFans.getFansByFromUserName")
                    ,fromUserName);
            if (wxcmsAccountQrcodeFansDb != null) {
                logger.debug("用户"+fromUserName+"已经通过二维码参数划分给店铺,店铺二维码参数:"+wxcmsAccountQrcodeFansDb.getEventKey());
            }else {
                    //用户未划分给店铺,根据区域进行店铺划分
                    //根据区域把粉丝划分给对应区域粉丝最多的店铺
                    //先判断省市区的店铺匹配情况
                    List<WxcmsAccountShop> shopDistrictList = null;
                    List<WxcmsAccountShop> shopCityList = null;
                    List<WxcmsAccountShop> shopProvinceList = null;
                    if (StringUtils.isNotBlank(district)) {
                        shopDistrictList = WxcmsAccountShop.dao.find(Db.getSqlPara("admin.wxcmsAccountShop.getAccountShopByAddress", Kv.by("province",province).set("city",city).set("district",district)));
                    }
                    Long shopId = 0L;
                    if (shopDistrictList.size() > 0) {
                        Map<Long, Integer> treeMap = new TreeMap<>();// 用TreeMap储存
                        //区下面有店铺
                        if (shopDistrictList.size() == 1) {
                            //区下面只有一个店铺
                            shopId = shopDistrictList.get(0).getId();
                        } else {
                            //区下面有多个店铺
                            for (WxcmsAccountShop accountShop : shopDistrictList) {
                                logger.debug("区下面有店铺:"+accountShop.getId());
                                //计算该店铺旗下的粉丝数量
                                List<WxcmsAccountQrcodeFans> qrcodeFansList = WxcmsAccountQrcodeFans.dao.find(Db.getSql("admin.wxcmsAccountQrcodeFans.getFansByShopId")
                                        ,accountShop.getId());
                                int fansCount = qrcodeFansList.size();
                                treeMap.put(accountShop.getId(), fansCount);
                            }
                            //找到粉丝数目最多的店铺
                            shopId = getMaxFansShop(treeMap);
                        }
                        logger.debug("粉丝数目最多的店铺Id:"+shopId);
                        //把粉丝划分到对应区域的店铺
                        //查询对应店铺的二维码参数
                        //把信息保存到qrcode_fans表,进行划分
                        WxcmsAccountShopQrcode wxcmsAccountShopQrcode = WxcmsAccountShopQrcode.dao.findFirst(Db.getSql("admin.wxcmsAccountShopQrcode.getQrcodeParamByShopId")
                                ,shopId);
                        saveToQrcodeFans(fromUserName,wxcmsAccountShopQrcode.getQrcodeParam(),toUserName);
                    } else {
                        //区下面无店铺
                        shopCityList = WxcmsAccountShop.dao.find(Db.getSqlPara("admin.wxcmsAccountShop.getAccountShopByAddress", Kv.by("province",province).set("city",city)));
                        if (shopCityList.size() > 0) {
                            Map<Long, Integer> treeMap = new TreeMap<>();// 用TreeMap储存
                            //市下面有店铺
                            if (shopCityList.size() == 1) {
                                //市下面只有一个店铺
                                shopId = shopCityList.get(0).getId();
                            } else {
                                //市下面有多个店铺
                                for (WxcmsAccountShop accountShop : shopCityList) {
                                    logger.debug("市下面有店铺:"+accountShop.getId());
                                    //计算该店铺旗下的粉丝数量
                                    List<WxcmsAccountQrcodeFans> qrcodeFansList = WxcmsAccountQrcodeFans.dao.find(Db.getSql("admin.wxcmsAccountQrcodeFans.getFansByShopId")
                                            ,accountShop.getId());
                                    int fansCount = qrcodeFansList.size();
                                    treeMap.put(accountShop.getId(), fansCount);
                                }
                                //找到粉丝数目最多的店铺
                                shopId = getMaxFansShop(treeMap);
                            }
                            logger.debug("粉丝数目最多的店铺Id:"+shopId);
                            //把粉丝划分到对应区域的店铺
                            //查询对应店铺的二维码参数
                            //把信息保存到qrcode_fans表,进行划分
                            WxcmsAccountShopQrcode wxcmsAccountShopQrcode = WxcmsAccountShopQrcode.dao.findFirst(Db.getSql("admin.wxcmsAccountShopQrcode.getQrcodeParamByShopId")
                                    ,shopId);
                            saveToQrcodeFans(fromUserName,wxcmsAccountShopQrcode.getQrcodeParam(),toUserName);
                        }else {
                            //市下面无店铺
                            shopProvinceList = WxcmsAccountShop.dao.find(Db.getSqlPara("admin.wxcmsAccountShop.getAccountShopByAddress", Kv.by("province",province)));
                            if (shopProvinceList.size() > 0) {
                                Map<Long, Integer> treeMap = new TreeMap<>();// 用TreeMap储存
                                //省下面有店铺
                                if (shopProvinceList.size() == 1) {
                                    //省下面只有一个店铺
                                    shopId = shopProvinceList.get(0).getId();
                                } else {
                                    //省下面有多个店铺
                                    for (WxcmsAccountShop accountShop : shopProvinceList) {
                                        logger.debug("省下面有店铺:"+accountShop.getId());
                                        //计算该店铺旗下的粉丝数量
                                        List<WxcmsAccountQrcodeFans> qrcodeFansList = WxcmsAccountQrcodeFans.dao.find(Db.getSql("admin.wxcmsAccountQrcodeFans.getFansByShopId")
                                                ,accountShop.getId());
                                        int fansCount = qrcodeFansList.size();
                                        treeMap.put(accountShop.getId(), fansCount);
                                    }
                                    //找到粉丝数目最多的店铺
                                    shopId = getMaxFansShop(treeMap);
                                }
                                logger.debug("粉丝数目最多的店铺Id:"+shopId);
                                //把粉丝划分到对应区域的店铺
                                //查询对应店铺的二维码参数
                                //把信息保存到qrcode_fans表,进行划分
                                WxcmsAccountShopQrcode wxcmsAccountShopQrcode = WxcmsAccountShopQrcode.dao.findFirst(Db.getSql("admin.wxcmsAccountShopQrcode.getQrcodeParamByShopId")
                                        ,shopId);
                                saveToQrcodeFans(fromUserName,wxcmsAccountShopQrcode.getQrcodeParam(),toUserName);
                            }
                        }
                    }
            }
        }

        return null;
    }

    /*
     * @Description //保存到qrcode_fans表
     * @Author wangkaida
     * @Date 11:58 2020/11/9
     * @Param [fromUserName, eventKey, toUserName]
     * @return boolean
     **/
    private boolean saveToQrcodeFans(String fromUserName,String eventKey,String toUserName){
        WxcmsAccountQrcodeFans wxcmsAccountQrcodeFansDb = WxcmsAccountQrcodeFans.dao.findFirst(Db.getSql("admin.wxcmsAccountQrcodeFans.getFansByFromUserName")
                ,fromUserName);

        WxcmsAccountQrcodeFans wxcmsAccountQrcodeFans = WxcmsAccountQrcodeFans.dao.findFirst(Db.getSql("admin.wxcmsAccountQrcodeFans.getFansByQrcodeParam")
                ,fromUserName,eventKey);

        if (wxcmsAccountQrcodeFansDb != null) {
            if (wxcmsAccountQrcodeFans != null) {
                logger.debug("用户"+fromUserName+"已经关注过亚太天能公众号,参数:"+eventKey);
            }else {
                //更新到表qrcode_fans
                if (StringUtils.isNotBlank(eventKey)) {
                    WxcmsAccountQrcodeFans wxcmsAccountQrcodeFansUpdate = new WxcmsAccountQrcodeFans();
                    wxcmsAccountQrcodeFansUpdate.setId(wxcmsAccountQrcodeFansDb.getId());
                    wxcmsAccountQrcodeFansUpdate.setEventKey(eventKey);
                    wxcmsAccountQrcodeFansUpdate.update();

                    //进行代理商的新增关注粉丝数量信息推送
//                                        String tmpResult = sendTemplateMessageToAgent(agentId);
//                                        logger.debug("进行代理商的新增关注粉丝数量信息推送结果:"+tmpResult);
                }
            }
        }else {
            //保存到表qrcode_fans
            if (StringUtils.isNotBlank(eventKey)) {
                WxcmsAccountQrcodeFans wxcmsAccountQrcodeFansSave = new WxcmsAccountQrcodeFans();
                wxcmsAccountQrcodeFansSave.setFromuserName(fromUserName);
                wxcmsAccountQrcodeFansSave.setEventKey(eventKey);
                wxcmsAccountQrcodeFansSave.setTouserName(toUserName);
                wxcmsAccountQrcodeFansSave.setCreateTime(new Date());
                Boolean flag = wxcmsAccountQrcodeFansSave.save();

                //进行代理商的新增关注粉丝数量信息推送
//                                    String tmpResult = sendTemplateMessageToAgent(agentId);
//                                    logger.debug("进行代理商的新增关注粉丝数量信息推送结果:"+tmpResult);
            }
        }
        return true;
    }

    /**
     * MethodName: getMaxFansShop
     * @Description: 获取粉丝数目最多的店铺Id
     * @author wangkaida
     * @date 2020年11月10日
     */
    public Long getMaxFansShop(Map<Long, Integer> treeMap) {
        // 1：把map转换成entryset，再转换成保存Entry对象的list。
        List<Map.Entry<Long, Integer>> entrys = new ArrayList<>(treeMap.entrySet());
        // 2：调用Collections.sort(list,comparator)方法把Entry-list排序
        Collections.sort(entrys, new MyComparator());
        return entrys.get(0).getKey();
    }

}
