/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.sys.service;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.shiro.session.Session;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.security.Digests;
import com.jeeplus.common.security.shiro.session.SessionDAO;
import com.jeeplus.common.service.BaseService;
import com.jeeplus.common.service.ServiceException;
import com.jeeplus.common.utils.CacheUtils;
import com.jeeplus.common.utils.Encodes;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.dmcardorder.entity.DmCardOrder;
import com.jeeplus.modules.goods.dao.GoodsDao;
import com.jeeplus.modules.goods.entity.Goods;
import com.jeeplus.modules.goodsorder.entity.GoodsOrder;
import com.jeeplus.modules.goodsorder.entity.GoodsOrderDetail;
import com.jeeplus.modules.member.dao.MemberDao;
import com.jeeplus.modules.member.entity.Member;
import com.jeeplus.modules.sys.dao.MenuDao;
import com.jeeplus.modules.sys.dao.RoleDao;
import com.jeeplus.modules.sys.dao.UserDao;
import com.jeeplus.modules.sys.entity.Menu;
import com.jeeplus.modules.sys.entity.Office;
import com.jeeplus.modules.sys.entity.Role;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.security.SystemAuthorizingRealm;
import com.jeeplus.modules.sys.utils.LogUtils;
import com.jeeplus.modules.sys.utils.UserUtils;
import com.jeeplus.modules.sysaccesstoken.dao.SysaccesstokenDao;
import com.jeeplus.modules.sysaccesstoken.entity.Sysaccesstoken;
import com.jeeplus.modules.sysweixin.dao.SysweixinDao;
import com.jeeplus.modules.sysweixin.entity.Sysweixin;
import com.jeeplus.modules.sysweixintemplate.dao.SysweixintemplateDao;
import com.jeeplus.modules.sysweixintemplate.entity.Sysweixintemplate;
import com.jeeplus.modules.tuangou.dao.TuangouDao;
import com.jeeplus.modules.tuangou.entity.Tuangou;
import com.jeeplus.modules.tuangouorder.dao.TuangouOrderDao;
import com.jeeplus.modules.tuangouorder.entity.TuangouOrder;
import com.jeeplus.modules.yongjintixian.entity.Yjtx;
import com.job.thread.WxNickImgUtils;
import com.wechat.WeixinMessageUtil;

/**
 * 系统管理，安全相关实体的管理类,包括用户、角色、菜单.
 * @author jeeplus
 * @version 2013-12-05
 */
@Service
@Transactional(readOnly = true)
public class SystemService extends BaseService implements InitializingBean {
	
	public static final String HASH_ALGORITHM = "SHA-1";
	public static final int HASH_INTERATIONS = 1024;
	public static final int SALT_SIZE = 8;
	
	@Autowired
	private UserDao userDao;
	@Autowired
	private RoleDao roleDao;
	@Autowired
	private MenuDao menuDao;
	@Autowired
	private SessionDAO sessionDao;
	@Autowired
	private SystemAuthorizingRealm systemRealm;
	
	@Autowired
	private SysweixintemplateDao sysweixintemplateDao;
	
	@Autowired
	private SysaccesstokenDao sysaccesstokenDao;
	
	@Autowired
	private SysweixinDao sysweixinDao;
	
	@Autowired
	private MemberDao memberDao;
	
	@Autowired
	private TuangouDao tuangouDao;
	
	@Autowired
	private TuangouOrderDao tuangouOrderDao;
	
	@Autowired
	private GoodsDao goodsDao;
	
	public SessionDAO getSessionDao() {
		return sessionDao;
	}


	//-- User Service --//
	
	/**
	 * 获取用户
	 * @param id
	 * @return
	 */
	public User getUser(String id) {
		return UserUtils.get(id);
	}

	/**
	 * 根据登录名获取用户
	 * @param loginName
	 * @return
	 */
	public User getUserByLoginName(String loginName) {
		return UserUtils.getByLoginName(loginName);
	}
	
	public Page<User> findUser(Page<User> page, User user) {
		// 生成数据权限过滤条件（dsf为dataScopeFilter的简写，在xml中使用 ${sqlMap.dsf}调用权限SQL）
		user.getSqlMap().put("dsf", dataScopeFilter(user.getCurrentUser(), "o", "a"));
		// 设置分页参数
		user.setPage(page);
		// 执行分页查询
		page.setList(userDao.findList(user));
		return page;
	}
	
	/**
	 * 无分页查询人员列表
	 * @param user
	 * @return
	 */
	public List<User> findUser(User user){
		// 生成数据权限过滤条件（dsf为dataScopeFilter的简写，在xml中使用 ${sqlMap.dsf}调用权限SQL）
		user.getSqlMap().put("dsf", dataScopeFilter(user.getCurrentUser(), "o", "a"));
		List<User> list = userDao.findList(user);
		return list;
	}

	/**
	 * 通过部门ID获取用户列表，仅返回用户id和name（树查询用户时用）
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<User> findUserByOfficeId(String officeId) {
		List<User> list = (List<User>)CacheUtils.get(UserUtils.USER_CACHE, UserUtils.USER_CACHE_LIST_BY_OFFICE_ID_ + officeId);
		if (list == null){
			User user = new User();
			user.setOffice(new Office(officeId));
			list = userDao.findUserByOfficeId(user);
			CacheUtils.put(UserUtils.USER_CACHE, UserUtils.USER_CACHE_LIST_BY_OFFICE_ID_ + officeId, list);
		}
		return list;
	}
	
	@Transactional(readOnly = false)
	public void saveUser(User user) {
		if (StringUtils.isBlank(user.getId())){
			user.preInsert();
			userDao.insert(user);
		}else{
			// 清除原用户机构用户缓存
			User oldUser = userDao.get(user.getId());
			if (oldUser.getOffice() != null && oldUser.getOffice().getId() != null){
				CacheUtils.remove(UserUtils.USER_CACHE, UserUtils.USER_CACHE_LIST_BY_OFFICE_ID_ + oldUser.getOffice().getId());
			}
			// 更新用户数据
			user.preUpdate();
			userDao.update(user);
		}
		if (StringUtils.isNotBlank(user.getId())){
			// 更新用户与角色关联
			userDao.deleteUserRole(user);
			if (user.getRoleList() != null && user.getRoleList().size() > 0){
				userDao.insertUserRole(user);
			}else{
				throw new ServiceException(user.getLoginName() + "没有设置角色！");
			}
			// 清除用户缓存
			UserUtils.clearCache(user);
//			// 清除权限缓存
//			systemRealm.clearAllCachedAuthorizationInfo();
		}
	}
	
	@Transactional(readOnly = false)
	public void updateUserInfo(User user) {
		user.preUpdate();
		userDao.updateUserInfo(user);
		// 清除用户缓存
		UserUtils.clearCache(user);
//		// 清除权限缓存
//		systemRealm.clearAllCachedAuthorizationInfo();
	}
	
	@Transactional(readOnly = false)
	public void deleteUser(User user) {
		userDao.delete(user);
		// 清除用户缓存
		UserUtils.clearCache(user);
//		// 清除权限缓存
//		systemRealm.clearAllCachedAuthorizationInfo();
	}
	
	@Transactional(readOnly = false)
	public void updatePasswordById(String id, String loginName, String newPassword) {
		User user = new User(id);
		user.setPassword(entryptPassword(newPassword));
		userDao.updatePasswordById(user);
		// 清除用户缓存
		user.setLoginName(loginName);
		UserUtils.clearCache(user);
//		// 清除权限缓存
//		systemRealm.clearAllCachedAuthorizationInfo();
	}
	
	@Transactional(readOnly = false)
	public void updateUserLoginInfo(User user) {
		// 保存上次登录信息
		user.setOldLoginIp(user.getLoginIp());
		user.setOldLoginDate(user.getLoginDate());
		// 更新本次登录信息
		user.setLoginIp(UserUtils.getSession().getHost());
		user.setLoginDate(new Date());
		userDao.updateLoginInfo(user);
	}
	
	/**
	 * 生成安全的密码，生成随机的16位salt并经过1024次 sha-1 hash
	 */
	public static String entryptPassword(String plainPassword) {
		byte[] salt = Digests.generateSalt(SALT_SIZE);
		byte[] hashPassword = Digests.sha1(plainPassword.getBytes(), salt, HASH_INTERATIONS);
		return Encodes.encodeHex(salt)+Encodes.encodeHex(hashPassword);
	}
	
	/**
	 * 验证密码
	 * @param plainPassword 明文密码
	 * @param password 密文密码
	 * @return 验证成功返回true
	 */
	public static boolean validatePassword(String plainPassword, String password) {
		byte[] salt = Encodes.decodeHex(password.substring(0,16));
		byte[] hashPassword = Digests.sha1(plainPassword.getBytes(), salt, HASH_INTERATIONS);
		return password.equals(Encodes.encodeHex(salt)+Encodes.encodeHex(hashPassword));
	}
	
	/**
	 * 获得活动会话
	 * @return
	 */
	public Collection<Session> getActiveSessions(){
		return sessionDao.getActiveSessions(false);
	}
	
	//-- Role Service --//
	
	public Role getRole(String id) {
		return roleDao.get(id);
	}

	public Role getRoleByName(String name) {
		Role r = new Role();
		r.setName(name);
		return roleDao.getByName(r);
	}
	
	public Role getRoleByEnname(String enname) {
		Role r = new Role();
		r.setEnname(enname);
		return roleDao.getByEnname(r);
	}
	
	public List<Role> findRole(Role role){
		return roleDao.findList(role);
	}
	
	public List<Role> findAllRole(){
		return UserUtils.getRoleList();
	}
	
	public List<Role> findCompanyRole(){
		return UserUtils.getCompanyLayerRoleList();
	}
	
	@Transactional(readOnly = false)
	public void saveRole(Role role) {
		if (StringUtils.isBlank(role.getId())){
			role.preInsert();
			roleDao.insert(role);
		}else{
			role.preUpdate();
			roleDao.update(role);
		}
		
		// 更新角色与菜单关联
		roleDao.deleteRoleMenu(role);
		if (role.getMenuList().size() > 0){
			roleDao.insertRoleMenu(role);
		}
		
		// 清除用户角色缓存
		UserUtils.removeCache(UserUtils.CACHE_ROLE_LIST);
		
		//清除用户的菜单缓存
		UserUtils.removeCache(UserUtils.CACHE_MENU_LIST);
	}

	@Transactional(readOnly = false)
	public void deleteRole(Role role) {
		roleDao.delete(role);
		// 清除用户角色缓存
		UserUtils.removeCache(UserUtils.CACHE_ROLE_LIST);
	}
	
	@Transactional(readOnly = false)
	public Boolean outUserInRole(Role role, User user) {
		List<Role> roles = user.getRoleList();
		for (Role e : roles){
			if (e.getId().equals(role.getId())){
				roles.remove(e);
				saveUser(user);
				return true;
			}
		}
		return false;
	}
	
	@Transactional(readOnly = false)
	public User assignUserToRole(Role role, User user) {
		if (user == null){
			return null;
		}
		List<String> roleIds = user.getRoleIdList();
		if (roleIds.contains(role.getId())) {
			return null;
		}
		user.getRoleList().add(role);
		saveUser(user);
		return user;
	}

	//-- Menu Service --//
	
	public Menu getMenu(String id) {
		return menuDao.get(id);
	}

	public List<Menu> findAllMenu(){
		return UserUtils.getAllMenuList();
	}
	
	@Transactional(readOnly = false)
	public void saveMenu(Menu menu) {
		
		// 获取父节点实体
		menu.setParent(this.getMenu(menu.getParent().getId()));
		
		// 获取修改前的parentIds，用于更新子节点的parentIds
		String oldParentIds = menu.getParentIds(); 
		
		// 设置新的父节点串
		menu.setParentIds(menu.getParent().getParentIds()+menu.getParent().getId()+",");

		// 保存或更新实体
		if (StringUtils.isBlank(menu.getId())){
			menu.preInsert();
			menuDao.insert(menu);
		}else{
			menu.preUpdate();
			menuDao.update(menu);
		}
		
		// 更新子节点 parentIds
		Menu m = new Menu();
		m.setParentIds("%,"+menu.getId()+",%");
		List<Menu> list = menuDao.findByParentIdsLike(m);
		for (Menu e : list){
			e.setParentIds(e.getParentIds().replace(oldParentIds, menu.getParentIds()));
			menuDao.updateParentIds(e);
		}
		// 清除用户菜单缓存
		UserUtils.removeCache(UserUtils.CACHE_MENU_LIST);
//		// 清除权限缓存
//		systemRealm.clearAllCachedAuthorizationInfo();
		// 清除日志相关缓存
		CacheUtils.remove(LogUtils.CACHE_MENU_NAME_PATH_MAP);
	}

	@Transactional(readOnly = false)
	public void updateMenuSort(Menu menu) {
		menuDao.updateSort(menu);
		// 清除用户菜单缓存
		UserUtils.removeCache(UserUtils.CACHE_MENU_LIST);
//		// 清除权限缓存
//		systemRealm.clearAllCachedAuthorizationInfo();
		// 清除日志相关缓存
		CacheUtils.remove(LogUtils.CACHE_MENU_NAME_PATH_MAP);
	}

	@Transactional(readOnly = false)
	public void deleteMenu(Menu menu) {
		menuDao.delete(menu);
		// 清除用户菜单缓存
		UserUtils.removeCache(UserUtils.CACHE_MENU_LIST);
//		// 清除权限缓存
//		systemRealm.clearAllCachedAuthorizationInfo();
		// 清除日志相关缓存
		CacheUtils.remove(LogUtils.CACHE_MENU_NAME_PATH_MAP);
	}
	
	/**
	 * 获取Key加载信息
	 */
	public static boolean printKeyLoadMessage(){
		StringBuilder sb = new StringBuilder();
		sb.append("\r\n======================================================================\r\n");
		//sb.append("\r\n    欢迎使用 "+Global.getConfig("productName")+"  - Powered By http://www.jeeplus.org\r\n");
		sb.append("\r\n    欢迎使用后台管理系统   - Developed by ZhaoLiangDong - Powered By zhaoliangdong \r\n");
		sb.append("\r\n======================================================================\r\n");
		System.out.println(sb.toString());
		return true;
	}


	@Override
	public void afterPropertiesSet() throws Exception {
		
	}
	
	//------------------------------------------------微信公众号 发送模板消息   BEGIN----------------------------------------------------------------
	/**
	 * 1. 菜品订单发货时，推送模板消息 ，模板编号：OPENTM202375201
	 * 模板内容
	 * {{first.DATA}}
	 * 快递单号：{{keyword1.DATA}}
	 * 快递名称：{{keyword2.DATA}}
	 * {{remark.DATA}}
	 * @param order
	 */
	@Transactional(readOnly = false)
	public void sendFahuo_goodsOrder(GoodsOrder order) throws Exception {
		//1.获取消息模板ID
		Sysweixintemplate tmp = new Sysweixintemplate();
		tmp.setBh("OPENTM202375201");
		tmp = this.sysweixintemplateDao.getSystemTemplateByBh(tmp);
		
		//2.获取会员openid
		Member member = memberDao.get(order.getMemberid());
		String toUserOpenId = member.getWxopenid();
		
		//只有当消息模板开启，并且会员接收微信消息标识开启时，才能接收消息 
		if(!"1".equals(tmp.getIson()) || !"1".equals(member.getIsWechat())){
			return;
		}
		
		//3.组织推送消息体
		JSONObject datajson = new JSONObject();
		
		JSONObject first = new JSONObject();
		first.put("value", "您好，有机汇选菜订单已经发货。");
		first.put("color", "#173177");
		datajson.put("first", first);
		
		JSONObject keyword1 = new JSONObject();
		keyword1.put("value", order.getWldh());
		keyword1.put("color", "#173177");
		datajson.put("keyword1", keyword1);
		
		JSONObject keyword2 = new JSONObject();
		keyword2.put("value", order.getKdgs());
		keyword2.put("color", "#173177");
		datajson.put("keyword2", keyword2);
		
		JSONObject remark = new JSONObject();
		remark.put("value", "订单号："+order.getDdh()+"，如有疑问，请咨询400-007-0011。");
		remark.put("color", "#173177");
		datajson.put("remark",remark);
		
		//4.创建消息推送对象
		List<Sysaccesstoken> tokenlist = sysaccesstokenDao.findAllList();
		List<Sysweixin> wxlist = sysweixinDao.findAllList();
		Sysweixin wx = wxlist.get(0);
		
		String accesstoken = null;
		String tokentime = "0";
		String expiresin = "0";
		if(tokenlist.size()>0){
			Sysaccesstoken token = tokenlist.get(0);
			accesstoken = token.getAccesstoken();
			tokentime = token.getTokentime()==null ? "0" : token.getTokentime();
			expiresin = token.getExpiresin()==null ? "0" : String.valueOf(token.getExpiresin());
		}
		
		WeixinMessageUtil tms = new WeixinMessageUtil(wx.getWeixinappid(),wx.getWeixinappsecret(),
				accesstoken, Long.parseLong(tokentime), Long.parseLong(expiresin), toUserOpenId, 
				tmp.getTemplateid(), datajson, Global.getConfig("wxurl")+"/Member/Myorder");
		
		//5.推送消息
		Map<String,Object> rtnmap = tms.sendText();
	    if(rtnmap!=null){ //是否更新access_token
	    	updateAccessToken(rtnmap);
	    }
	}
	
	
	/**
	 * 2. 套餐卡订单发货时，推送模板消息 ，模板编号：OPENTM202375201
	 * 模板内容
	 * {{first.DATA}}
	 * 快递单号：{{keyword1.DATA}}
	 * 快递名称：{{keyword2.DATA}}
	 * {{remark.DATA}}
	 * @param order
	 */
	@Transactional(readOnly = false)
	public void sendFahuo_cardOrder(DmCardOrder order) throws Exception {
		//1.获取消息模板ID
		Sysweixintemplate tmp = new Sysweixintemplate();
		tmp.setBh("OPENTM202375201");
		tmp = this.sysweixintemplateDao.getSystemTemplateByBh(tmp);
		
		//2.获取会员openid
		Member member = memberDao.get(order.getMember().getId());
		String toUserOpenId = member.getWxopenid();
		
		//只有当消息模板开启，并且会员接收微信消息标识开启时，才能接收消息 
		if(!"1".equals(tmp.getIson()) || !"1".equals(member.getIsWechat())){
			return;
		}
		
		//3.组织推送消息体
		JSONObject datajson = new JSONObject();
		
		JSONObject first = new JSONObject();
		first.put("value", "您好，有机汇套餐卡订单已经发货。");
		first.put("color", "#173177");
		datajson.put("first", first);
		
		JSONObject keyword1 = new JSONObject();
		keyword1.put("value", order.getWldh());
		keyword1.put("color", "#173177");
		datajson.put("keyword1", keyword1);
		
		JSONObject keyword2 = new JSONObject();
		keyword2.put("value", order.getKdgs());
		keyword2.put("color", "#173177");
		datajson.put("keyword2", keyword2);
		
		JSONObject remark = new JSONObject();
		remark.put("value", "订单号："+order.getDdh()+"，如有疑问，请咨询400-007-0011。");
		remark.put("color", "#173177");
		datajson.put("remark",remark);
		
		//4.创建消息推送对象
		List<Sysaccesstoken> tokenlist = sysaccesstokenDao.findAllList();
		List<Sysweixin> wxlist = sysweixinDao.findAllList();
		Sysweixin wx = wxlist.get(0);
		
		String accesstoken = null;
		String tokentime = "0";
		String expiresin = "0";
		if(tokenlist.size()>0){
			Sysaccesstoken token = tokenlist.get(0);
			accesstoken = token.getAccesstoken();
			tokentime = token.getTokentime()==null ? "0" : token.getTokentime();
			expiresin = token.getExpiresin()==null ? "0" : String.valueOf(token.getExpiresin());
		}
		
		WeixinMessageUtil tms = new WeixinMessageUtil(wx.getWeixinappid(),wx.getWeixinappsecret(),
				accesstoken, Long.parseLong(tokentime), Long.parseLong(expiresin), toUserOpenId, 
				tmp.getTemplateid(), datajson, Global.getConfig("wxurl")+"/Member/Myorder");
		
		//5.推送消息
		Map<String,Object> rtnmap = tms.sendText();
	    if(rtnmap!=null){ //是否更新access_token
	    	updateAccessToken(rtnmap);
	    }
	}
	
	/**
	 * 3. 团购订单发货时，推送模板消息 ，模板编号：OPENTM202375201
	 * 模板内容
	 * {{first.DATA}}
	 * 快递单号：{{keyword1.DATA}}
	 * 快递名称：{{keyword2.DATA}}
	 * {{remark.DATA}}
	 * @param order
	 */
	@Transactional(readOnly = false)
	public void sendFahuo_tuangouOrder(TuangouOrder order) throws Exception {
		
		//1.获取消息模板ID
		Sysweixintemplate tmp = new Sysweixintemplate();
		tmp.setBh("OPENTM202375201");
		tmp = this.sysweixintemplateDao.getSystemTemplateByBh(tmp);
		
		//2.获取会员openid
		Member member = memberDao.get(order.getMemberid());
		String toUserOpenId = member.getWxopenid();
		
		//只有当消息模板开启，并且会员接收微信消息标识开启时，才能接收消息 
		if(!"1".equals(tmp.getIson()) || !"1".equals(member.getIsWechat())){
			return;
		}		
		
		//3.组织推送消息体
		JSONObject datajson = new JSONObject();
		
		JSONObject first = new JSONObject();
		first.put("value", "您好，有机汇团购订单已经发货。");
		first.put("color", "#173177");
		datajson.put("first", first);
		
		JSONObject keyword1 = new JSONObject();
		keyword1.put("value", order.getWldh());
		keyword1.put("color", "#173177");
		datajson.put("keyword1", keyword1);
		
		JSONObject keyword2 = new JSONObject();
		keyword2.put("value", order.getKdgs());
		keyword2.put("color", "#173177");
		datajson.put("keyword2", keyword2);
		
		JSONObject remark = new JSONObject();
		remark.put("value", "订单号："+order.getDdh()+"，如有疑问，请咨询400-007-0011。");
		remark.put("color", "#173177");
		datajson.put("remark",remark);
		
		//4.创建消息推送对象
		List<Sysaccesstoken> tokenlist = sysaccesstokenDao.findAllList();
		List<Sysweixin> wxlist = sysweixinDao.findAllList();
		Sysweixin wx = wxlist.get(0);
		
		String accesstoken = null;
		String tokentime = "0";
		String expiresin = "0";
		if(tokenlist.size()>0){
			Sysaccesstoken token = tokenlist.get(0);
			accesstoken = token.getAccesstoken();
			tokentime = token.getTokentime()==null ? "0" : token.getTokentime();
			expiresin = token.getExpiresin()==null ? "0" : String.valueOf(token.getExpiresin());
		}
		
		WeixinMessageUtil tms = new WeixinMessageUtil(wx.getWeixinappid(),wx.getWeixinappsecret(),
				accesstoken, Long.parseLong(tokentime), Long.parseLong(expiresin), toUserOpenId, 
				tmp.getTemplateid(), datajson, Global.getConfig("wxurl")+"/Member/Myorder");
		
		//5.推送消息
		Map<String,Object> rtnmap = tms.sendText();
	    if(rtnmap!=null){ //是否更新access_token
	    	updateAccessToken(rtnmap);
	    }
	}
	
	/**
	 * 4. 菜品订单取消时，推送模板消息 ，模板编号：OPENTM200763001
	 * 模板内容
	 *  {{first.DATA}}
		下单时间：{{keyword1.DATA}}
		订单金额：{{keyword2.DATA}}
		订单详情：{{keyword3.DATA}}
		订单状态：{{keyword4.DATA}}
		取消原因：{{keyword5.DATA}}
		{{remark.DATA}}
	 * @param order
	 */
	@Transactional(readOnly = false)
	public void sendCancel_goodsOrder(GoodsOrder order) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//订单详情
		List<GoodsOrderDetail> lists = order.getGoodsOrderDetailList();
		StringBuffer sb = new StringBuffer();
		for(GoodsOrderDetail each : lists){
			sb.append(each.getGoodsname()+each.getGoodsguige()+"("+each.getNum()+"份) ");
		}
		
		//1.获取消息模板ID
		Sysweixintemplate tmp = new Sysweixintemplate();
		tmp.setBh("OPENTM200763001");
		tmp = this.sysweixintemplateDao.getSystemTemplateByBh(tmp);
		
		//2.获取会员openid
		Member member = memberDao.get(order.getMemberid());
		String toUserOpenId = member.getWxopenid();
		
		//只有当消息模板开启，并且会员接收微信消息标识开启时，才能接收消息 
		if(!"1".equals(tmp.getIson()) || !"1".equals(member.getIsWechat())){
			return;
		}		
		
		//3.组织推送消息体
		JSONObject datajson = new JSONObject();
		
		JSONObject first = new JSONObject();
		first.put("value", "您好，有机汇选菜订单已取消。");
		first.put("color", "#173177");
		datajson.put("first", first);
		
		JSONObject keyword1 = new JSONObject();
		keyword1.put("value", sdf.format(order.getCreateDate()));
		keyword1.put("color", "#173177");
		datajson.put("keyword1", keyword1);
		
		JSONObject keyword2 = new JSONObject();
		keyword2.put("value", "(使用次数1次)");
		keyword2.put("color", "#173177");
		datajson.put("keyword2", keyword2);
		
		JSONObject keyword3 = new JSONObject();
		keyword3.put("value", sb.toString());
		keyword3.put("color", "#173177");
		datajson.put("keyword3", keyword3);
		
		JSONObject keyword4 = new JSONObject();
		keyword4.put("value", "已发货");
		keyword4.put("color", "#173177");
		datajson.put("keyword4", keyword4);
		
		JSONObject keyword5 = new JSONObject();
		keyword5.put("value", order.getZuofeireason());
		keyword5.put("color", "#173177");
		datajson.put("keyword5", keyword5);
		
		JSONObject remark = new JSONObject();
		remark.put("value", "订单号："+order.getDdh()+"，如有疑问，请咨询400-007-0011。");
		remark.put("color", "#173177");
		datajson.put("remark",remark);
		
		//4.创建消息推送对象
		List<Sysaccesstoken> tokenlist = sysaccesstokenDao.findAllList();
		List<Sysweixin> wxlist = sysweixinDao.findAllList();
		Sysweixin wx = wxlist.get(0);
		
		String accesstoken = null;
		String tokentime = "0";
		String expiresin = "0";
		if(tokenlist.size()>0){
			Sysaccesstoken token = tokenlist.get(0);
			accesstoken = token.getAccesstoken();
			tokentime = token.getTokentime()==null ? "0" : token.getTokentime();
			expiresin = token.getExpiresin()==null ? "0" : String.valueOf(token.getExpiresin());
		}
		
		WeixinMessageUtil tms = new WeixinMessageUtil(wx.getWeixinappid(),wx.getWeixinappsecret(),
				accesstoken, Long.parseLong(tokentime), Long.parseLong(expiresin), toUserOpenId, 
				tmp.getTemplateid(), datajson, Global.getConfig("wxurl")+"/Member/Myorder");
		
		//5.推送消息
		Map<String,Object> rtnmap = tms.sendText();
	    if(rtnmap!=null){ //是否更新access_token
	    	updateAccessToken(rtnmap);
	    }
	}
	
	/**
	 * 5. 团购订单取消时，推送模板消息 ，模板编号：OPENTM200763001
	 * 模板内容
	 *  {{first.DATA}}
		下单时间：{{keyword1.DATA}}
		订单金额：{{keyword2.DATA}}
		订单详情：{{keyword3.DATA}}
		订单状态：{{keyword4.DATA}}
		取消原因：{{keyword5.DATA}}
		{{remark.DATA}}
	 * @param order
	 */
	@Transactional(readOnly = false)
	public void sendCancel_tuangouOrder(TuangouOrder order) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//订单详情
		StringBuffer sb = new StringBuffer();
		Tuangou tg = tuangouDao.get(order.getTuangou());
		Goods goods = goodsDao.get(tg.getGoods());
		sb.append(goods.getName()).append("("+order.getNum()+"份)");
		
		String tkje = "";
		if("1".equals(order.getSftk())){
			tkje = "退款金额：¥"+order.getTkje()+"，";
		}
		
		//1.获取消息模板ID
		Sysweixintemplate tmp = new Sysweixintemplate();
		tmp.setBh("OPENTM200763001");
		tmp = this.sysweixintemplateDao.getSystemTemplateByBh(tmp);
		
		//2.获取会员openid
		Member member = memberDao.get(order.getMemberid());
		String toUserOpenId = member.getWxopenid();
		
		//只有当消息模板开启，并且会员接收微信消息标识开启时，才能接收消息 
		if(!"1".equals(tmp.getIson()) || !"1".equals(member.getIsWechat())){
			return;
		}		
		
		//3.组织推送消息体
		JSONObject datajson = new JSONObject();
		
		JSONObject first = new JSONObject();
		first.put("value", "您好，有机汇团购订单已取消。");
		first.put("color", "#173177");
		datajson.put("first", first);
		
		JSONObject keyword1 = new JSONObject();
		keyword1.put("value", sdf.format(order.getCreateDate()));
		keyword1.put("color", "#173177");
		datajson.put("keyword1", keyword1);
		
		JSONObject keyword2 = new JSONObject();
		keyword2.put("value", "¥"+order.getSfprice());
		keyword2.put("color", "#173177");
		datajson.put("keyword2", keyword2);
		
		JSONObject keyword3 = new JSONObject();
		keyword3.put("value", sb.toString());
		keyword3.put("color", "#173177");
		datajson.put("keyword3", keyword3);
		
		JSONObject keyword4 = new JSONObject();
		keyword4.put("value", "已发货");
		keyword4.put("color", "#173177");
		datajson.put("keyword4", keyword4);
		
		JSONObject keyword5 = new JSONObject();
		keyword5.put("value", order.getZuofeireason());
		keyword5.put("color", "#173177");
		datajson.put("keyword5", keyword5);
		
		JSONObject remark = new JSONObject();
		remark.put("value", "订单号："+order.getDdh()+"，"+tkje+"如有疑问，请咨询400-007-0011。");
		remark.put("color", "#173177");
		datajson.put("remark",remark);
		
		//4.创建消息推送对象
		List<Sysaccesstoken> tokenlist = sysaccesstokenDao.findAllList();
		List<Sysweixin> wxlist = sysweixinDao.findAllList();
		Sysweixin wx = wxlist.get(0);
		
		String accesstoken = null;
		String tokentime = "0";
		String expiresin = "0";
		if(tokenlist.size()>0){
			Sysaccesstoken token = tokenlist.get(0);
			accesstoken = token.getAccesstoken();
			tokentime = token.getTokentime()==null ? "0" : token.getTokentime();
			expiresin = token.getExpiresin()==null ? "0" : String.valueOf(token.getExpiresin());
		}
		
		WeixinMessageUtil tms = new WeixinMessageUtil(wx.getWeixinappid(),wx.getWeixinappsecret(),
				accesstoken, Long.parseLong(tokentime), Long.parseLong(expiresin), toUserOpenId, 
				tmp.getTemplateid(), datajson, Global.getConfig("wxurl")+"/Member/Myorder");
		
		//5.推送消息
		Map<String,Object> rtnmap = tms.sendText();
	    if(rtnmap!=null){ //是否更新access_token
	    	updateAccessToken(rtnmap);
	    }
	}
	
	/**
	 * 6. 佣金提现打款时，推送模板消息 ，模板编号：OPENTM407297066
	 * 模板内容
	 *  {{first.DATA}}
		金额：{{keyword1.DATA}}
		到账时间：{{keyword2.DATA}}
		{{remark.DATA}}
	 * @param order
	 */
	@Transactional(readOnly = false)
	public void send_yjtx(Yjtx yjtx) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		//1.获取消息模板ID
		Sysweixintemplate tmp = new Sysweixintemplate();
		tmp.setBh("OPENTM407297066");
		tmp = this.sysweixintemplateDao.getSystemTemplateByBh(tmp);
		
		//2.获取会员openid
		String toUserOpenId = yjtx.getWxopenid();	
		
		//3.组织推送消息体
		JSONObject datajson = new JSONObject();
		
		JSONObject first = new JSONObject();
		first.put("value", "您好，您的佣金提现已经打款。");
		first.put("color", "#173177");
		datajson.put("first", first);
		
		JSONObject keyword1 = new JSONObject();
		keyword1.put("value", "¥"+yjtx.getJine());
		keyword1.put("color", "#173177");
		datajson.put("keyword1", keyword1);
		
		JSONObject keyword2 = new JSONObject();
		keyword2.put("value", sdf.format(new Date()));
		keyword2.put("color", "#173177");
		datajson.put("keyword2", keyword2);
		
		StringBuffer sb = new StringBuffer();
		sb.append("款已打入银行卡(").append(yjtx.getCardid().substring(0, 3))
			.append("*********")
			.append(yjtx.getCardid().substring(yjtx.getCardid().length()-4))	// 报错
		  .append(")，户名(")
		  .append(yjtx.getUsername().substring(0, 1))
		  .append("*")
		  .append(yjtx.getUsername().substring(yjtx.getUsername().length()-1))
		  .append("),请注意查收。如有疑问，请咨询400-007-0011。");
		
		JSONObject remark = new JSONObject();
		remark.put("value", sb.toString());
		remark.put("color", "#173177");
		datajson.put("remark",remark);
		
		//4.创建消息推送对象
		List<Sysaccesstoken> tokenlist = sysaccesstokenDao.findAllList();
		List<Sysweixin> wxlist = sysweixinDao.findAllList();
		Sysweixin wx = wxlist.get(0);
		
		String accesstoken = null;
		String tokentime = "0";
		String expiresin = "0";
		if(tokenlist.size()>0){
			Sysaccesstoken token = tokenlist.get(0);
			accesstoken = token.getAccesstoken();
			tokentime = token.getTokentime()==null ? "0" : token.getTokentime();
			expiresin = token.getExpiresin()==null ? "0" : String.valueOf(token.getExpiresin());
		}
		
		WeixinMessageUtil tms = new WeixinMessageUtil(wx.getWeixinappid(),wx.getWeixinappsecret(),
				accesstoken, Long.parseLong(tokentime), Long.parseLong(expiresin), toUserOpenId, 
				tmp.getTemplateid(), datajson, Global.getConfig("wxurl")+"/Member/Myorder");
		
		//5.推送消息
		Map<String,Object> rtnmap = tms.sendText();
	    if(rtnmap!=null){ //是否更新access_token
	    	updateAccessToken(rtnmap);
	    }
	}
	
	/**
	 * 更新数据库中存储的accessToken
	 */
	private void updateAccessToken(Map<String,Object> rtnmap){
	  if(rtnmap.get("isRequest").toString().equals("1")){
		  //将access_token信息写入数据库
		  String accesstokenOut = rtnmap.get("accesstoken").toString();
		  long expireinOut = Long.parseLong(rtnmap.get("expirein").toString());
		  long tokentimeOut = Long.parseLong(rtnmap.get("tokentime").toString());
		  
		  if(sysaccesstokenDao.findAllList().size()==0){
			  Sysaccesstoken token = new Sysaccesstoken();
			  token.preInsert();
			  token.setAccesstoken(accesstokenOut);
			  token.setExpiresin(expireinOut);
			  token.setTokentime(String.valueOf(tokentimeOut));
			  sysaccesstokenDao.insert(token);
		  }else{
			  Sysaccesstoken token = new Sysaccesstoken();
			  token.preUpdate();
			  token.setAccesstoken(accesstokenOut);
			  token.setExpiresin(expireinOut);
			  token.setTokentime(String.valueOf(tokentimeOut));
			  sysaccesstokenDao.update(token);
		  }
	   }
	}
	//------------------------------------------------微信公众号 发送模板消息  END ----------------------------------------------------------------
	@Transactional(readOnly = false)
	public void executeTask() {
		List<Tuangou> tglist = tuangouDao.findTuangouList();
		for(Tuangou tg : tglist){
			int qituansl = tg.getMinnum(); //起团数量
			
			TuangouOrder order = new TuangouOrder();
			order.setTuangou(tg);
			List<TuangouOrder> realorderlist = tuangouOrderDao.findRealList(order); //真正支付的参团人数
			List<TuangouOrder> allorderlist = tuangouOrderDao.findList(order); //所有参团人数，包括后台添加的
			int realcantuansl = realorderlist.size();
			int allcantuansl = allorderlist.size();
			
			if(realcantuansl>=1 && allcantuansl<qituansl){
				//插入相差人数的会员记录和参团记录
				int count = qituansl - allcantuansl;
				int seed = new Random().nextInt(90)%(90-10+1)+10;
				
				for(int i=1;i<=count;i++){
					//插入会员信息
					Member m = new Member();
					m.preInsert();
					m.setNickname(WxNickImgUtils.nicknames[(seed+i)%100]);
					m.setHeadimgurl(Global.getConfig("hosturl")+"static/wxheadimg/img%20("+((seed+i)%100)+").jpg");
					memberDao.insert(m);
					
					//插入团购订单信息
					TuangouOrder tgorder = new TuangouOrder();
					tgorder.preInsert();
					tgorder.setTuangou(tg);
					tgorder.setDdh("000000000000000");		// 订单号，写死
					tgorder.setFlag("1");		// 虚拟人标识
					tgorder.setMemberid(m.getId());
					tuangouOrderDao.insert(tgorder);
				}
				System.out.println("-----------执行自动成团---------------");
			}
		}
	}
	
}
