package com.feiqu.framwork.support.plugin;

import com.feiqu.framwork.util.AESUtil;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

//import com.zheng.common.util.AESUtil;

/**
 * 支持加密配置文件插件
 * Created by ZhangShuzheng on 2017/2/4.
 */
public class EncryptPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {

	private String[] propertyNames = {
		"cwd.cache.password","master.password"
	};

	/**
	 * 解密指定propertyName的加密属性值
	 * @param propertyName
	 * @param propertyValue
	 * @return
	 */
	@Override
	protected String convertProperty(String propertyName, String propertyValue) {
		for (String p : propertyNames) {
			if (p.equalsIgnoreCase(propertyName)) {
				return AESUtil.aesDecode(propertyValue);
			}
		}
		return super.convertProperty(propertyName, propertyValue);
	}

}
