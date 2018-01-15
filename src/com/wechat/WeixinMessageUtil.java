package com.wechat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.sword.lang.HttpUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jeeplus.modules.goodsorder.entity.GoodsOrder;


public class WeixinMessageUtil {
	private static Logger logger = Logger.getLogger(WeixinMessageUtil.class);
	private String appid;
	private String appsecrete;
	private String departid;
	private String accesstoken;
	private long tokentime;
	private long expirein;
	private String toUserOpenId;
	private String templateId;
	private JSONObject datajson;
	private String msgBody;
	private String url = "";
	private String isRequest = "0"; //0,未请求微信服务器获取access_token;1,请求微信服务器获取access_token.
	
	/**
	 * 构造函数
	 * @param appid
	 * @param appsecrete
	 * @param departid
	 * @param accesstoken
	 * @param tokentime
	 * @param expirein
	 * @param toUserOpenId
	 * @param templateId
	 * @param datajson
	 */
	public WeixinMessageUtil(String appid,String appsecrete,String accesstoken,
			long tokentime, long expirein, String toUserOpenId,
			String templateId, JSONObject datajson,String url) {
		this.appid = appid;
		this.appsecrete = appsecrete;
		this.accesstoken = accesstoken;
		this.tokentime = tokentime;
		this.expirein = expirein;
		this.toUserOpenId = toUserOpenId;
		this.templateId = templateId;
		this.datajson = datajson;
		this.url = url;
	}
	
	/**
	 * 构造函数
	 * @param appid
	 * @param appsecrete
	 * @param accesstoken
	 * @param tokentime
	 * @param expirein
	 */
	public WeixinMessageUtil(String appid,String appsecrete, String accesstoken,
			long tokentime, long expirein) {
		this.appid = appid;
		this.appsecrete = appsecrete;
		this.accesstoken = accesstoken;
		this.tokentime = tokentime;
		this.expirein = expirein;
	}
	
	/**
	 * 获取access_token
	 * @return
	 */
	public String getAccessToken(){
		if(!isValid()){
			request();
		}
		return this.accesstoken;
	}
	
	/**
     * access_token是否合法
     * @return
     */
	public boolean isValid() {
		if (StringUtils.isBlank(this.accesstoken)) {
			return false;
		}
	    if (this.expirein <= 0L) {
	      return false;
	    }
	    if (isExpire())
	      return false;
	    return true;
	}
	
	/**
	 * access_token是否过期  
	 * @return
	 */
    private boolean isExpire()
    {
	    Date currentDate = new Date();
	    long currentTime = currentDate.getTime();
	    long expiresTime = this.expirein * 1000L - 600000; //延迟10分钟

	    if (this.tokentime*1000L + expiresTime > currentTime)
	      return false;
	    return true;
    }
    
    /**
	 * 请求access_token
	 * @return
	 */
	private boolean request(){
		String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + this.appid + "&secret=" + this.appsecrete;
	    String result = HttpUtils.get(url);
	    if (StringUtils.isBlank(result))
	      return false;
	    if (!parseData(result)) {
	      return false;
	    }
	    logger.info("token获取成功");
	    System.out.println("wx:请求access_token返回:"+result);
		this.isRequest = "1";
		return true;
	}
	
	/**
	 * 解析access_token请求返回数据
	 */
	private boolean parseData(String data){
	    JSONObject jsonObject = JSONObject.parseObject(data);
	    String tokenName = "access_token";
	    String expiresInName = "expires_in";
	    try {
	      String token = jsonObject.get(tokenName).toString();
	      if (StringUtils.isBlank(token)) {
	        logger.error("token获取失败,获取结果" + data);
	        return false;
	      }
	      this.accesstoken = token;
	      //this.tokentime = new Date().getTime();
	      this.tokentime = System.currentTimeMillis()/1000L;
	      String expiresIn = jsonObject.get(expiresInName).toString();
	      if (StringUtils.isBlank(expiresIn)) {
	        logger.error("token获取失败,获取结果" + expiresIn);
	        return false;
	      }
	      this.expirein = Long.valueOf(expiresIn).longValue();
	    }
	    catch (Exception e) {
	      logger.error("token 结果解析失败，token参数名称: " + tokenName + 
	        "有效期参数名称:" + expiresInName + 
	        "token请求结果:" + data);
	      e.printStackTrace();
	      return false;
	    }
	    return true;
    }
	
	/**
	 * 发送[消费]模板消息
	 * @return
	 */
	public Map<String,Object> sendText() throws Exception {
		//获取access_token
		getAccessToken();
		
		//发送模板消息
		try {
			send();
		} catch (Exception e) {
			throw e;
		}
		
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("isRequest", this.isRequest);
		map.put("accesstoken", this.accesstoken);
		map.put("expirein", this.expirein);
		map.put("tokentime", this.tokentime);
		
		return map;
	}
	
	/**
	 * 发送[消费]模板消息
	 * @return
	 * @throws Exception
	 */
	public void send(){
		String rtn = null;
		JSONObject jsonMsg = new JSONObject();
		jsonMsg.put("touser", this.toUserOpenId);
		jsonMsg.put("template_id", this.templateId);
		jsonMsg.put("url", this.url);
		jsonMsg.put("topcolor", "#FF0000");
		jsonMsg.put("data", this.datajson);		

	    this.msgBody = jsonMsg.toJSONString();
	    
	    rtn = sendTemplateMsg();
	    JSONObject jo = JSON.parseObject(rtn);
		String errcode = jo.get("errcode").toString();
		String errmsg = jo.get("errmsg").toString();
		//String msgid = jo.get("msgid").toString();
		if("0".equals(errcode)){
			logger.info("微信推送模板消息成功,errcode:"+errcode+",errmsg:"+errmsg);
			System.out.println("wx:推送模板消息成功,errcode:"+errcode+",errmsg:"+errmsg);
		}else{
			logger.info("微信推送模板消息失败,errcode:"+errcode+",errmsg:"+errmsg);
			System.out.println("wx:推送模板消息失败,errcode:"+errcode+",errmsg:"+errmsg);
		}
	}
	
	/**
	 * 发送微信模板消息[微信接口]
	 * @return
	 */
	private String sendTemplateMsg(){
		String rtn=null;
		String accessToken = this.accesstoken;
		if (StringUtils.isBlank(this.toUserOpenId)) {
	      return rtn;
	    }
	    if (StringUtils.isBlank(accessToken)) {
	      logger.error("发送失败，无法得到accessToken");
	      return rtn;
	    }
	    if (StringUtils.isNotBlank(accessToken)) {
	        String url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=" + accessToken;
	        rtn = HttpUtils.post(url, this.msgBody);
	    }
	    return rtn;
	}
	
	/**
	 * 创建微信菜单带菜单数据[微信接口]
	 * @param menuBody
	 * @return
	 * @throws Exception
	 */
	public String createMenuWithBody(String menuBody) throws Exception {
		//获取access_token
		getAccessToken();
		
		//组装菜单数据并发送请求
		String postUrl = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token="+this.accesstoken;
		String rtn = HttpUtils.post(postUrl, menuBody);
		System.out.println("wx:创建微信菜单返回："+rtn);
		
		JSONObject jo = JSON.parseObject(rtn);
		jo.put("isRequest", this.isRequest);
		jo.put("accesstoken", this.accesstoken);
		jo.put("expirein", this.expirein);
		jo.put("tokentime", this.tokentime);
		return jo.toJSONString();
	}
	
	
	/**
	 * 查询微信菜单[微信接口]
	 * @return
	 * @throws Exception
	 */
	public String getMenu() throws Exception {
		String menulist = "";
		
		//获取access_token
		getAccessToken();
		
		//发送请求
		String url = "https://api.weixin.qq.com/cgi-bin/menu/get?access_token="+this.accesstoken;
		menulist = HttpUtils.get(url);
		
		JSONObject jo = new JSONObject();
		jo.put("menulist", menulist);
		jo.put("isRequest", this.isRequest);
		jo.put("accesstoken", this.accesstoken);
		jo.put("expirein", this.expirein);
		jo.put("tokentime", this.tokentime);
		
		return jo.toJSONString();
	}

	/**
	 * 删除微信菜单[微信接口]
	 * @return
	 */
	public String deleteMenu() {
		//获取access_token
		getAccessToken();
		
		//发送请求
		String url = "https://api.weixin.qq.com/cgi-bin/menu/delete?access_token="+this.accesstoken;
		String rtn = HttpUtils.get(url);
		JSONObject jo = JSON.parseObject(rtn);
		jo.put("isRequest", this.isRequest);
		jo.put("accesstoken", this.accesstoken);
		jo.put("expirein", this.expirein);
		jo.put("tokentime", this.tokentime);
		return jo.toJSONString();
	}

	/**
	 * 获取媒体素材数量
	 * @return
	 */
	public Map<String,Object> getMediaCount() {
		 Map<String,Object> map = new HashMap<String, Object>();
		
		//获取access_token
		getAccessToken();
		
		//发送请求
		String url = "https://api.weixin.qq.com/cgi-bin/material/get_materialcount?access_token="+this.accesstoken;
		String rtn = HttpUtils.get(url);
		System.out.println("wx:获取媒体素材数量返回："+rtn);
		
		map.put("rtn", rtn);
		map.put("isRequest", this.isRequest);
		map.put("accesstoken", this.accesstoken);
		map.put("expirein", this.expirein);
		map.put("tokentime", this.tokentime);
		
		return map;
	}

	/**
	 * 获取媒体素材列表
	 * @param type
	 * @param offset
	 * @param count
	 * @return
	 */
	public Map<String,Object> getMediaList(String type,int offset,int count) throws Exception {
		Map<String,Object> map = new HashMap<String, Object>();
		
		//获取access_token
		getAccessToken();
		
		//组装菜单数据并发送请求
		String postUrl = "https://api.weixin.qq.com/cgi-bin/material/batchget_material?access_token="+this.accesstoken;
		String rtn = HttpUtils.post(postUrl, "{\"type\":\""+type+"\",\"offset\":"+offset+",\"count\":"+count+"}");
		rtn = new String(rtn.getBytes("ISO8859-1"),"UTF-8");
		System.out.println("wx:获取媒体素材列表返回:"+rtn);
		
		map.put("rtn", rtn);
		map.put("isRequest", this.isRequest);
		map.put("accesstoken", this.accesstoken);
		map.put("expirein", this.expirein);
		map.put("tokentime", this.tokentime);
		
		return map;
	}

}
