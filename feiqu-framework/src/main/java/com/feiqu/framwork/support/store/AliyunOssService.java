package com.feiqu.framwork.support.store;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.feiqu.framwork.constant.CommonConstant;
import com.jeesuite.filesystem.FileSystemClient;
import com.jeesuite.filesystem.UploadTokenParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

/**
 * Created by ZhangShuzheng on 2017/5/15.
 */
@Service
public class AliyunOssService {

	private static final Logger LOGGER = LoggerFactory.getLogger(AliyunOssService.class);

	/**
	 * 签名生成
	 * @return
	 */
	public JSONObject policy() {

		FileSystemClient fileSystemClient = FileSystemClient.getClient("aliyun");
		UploadTokenParam uploadTokenParam = new UploadTokenParam();
		// 存储目录
		String dir = DateUtil.format(new Date(),"yyyy/MM/dd");
		uploadTokenParam.setUploadDir(dir);
		// 签名有效期
		long expireEndTime = CommonConstant.ALIYUN_OSS_EXPIRE;
		uploadTokenParam.setExpires(expireEndTime);
		uploadTokenParam.setCallbackUrl(CommonConstant.ALIYUN_OSS_CALLBACK);
		uploadTokenParam.setFsizeMax(CommonConstant.ALIYUN_OSS_MAX_SIZE * 1024 * 1024);
		Map<String, Object> map =  fileSystemClient.createUploadToken(uploadTokenParam);
// 提交节点
		String action = "http://" + CommonConstant.ALIYUN_OSS_BUCKET_NAME + "." + CommonConstant.ALIYUN_OSS_ENDPOINT;
		map.put("action",action);
		map.put("myDomain", CommonConstant.ALIOSS_URL_PREFIX);
		JSONObject result = new JSONObject(map);
		return result;

		/*Date expiration = new Date(expireEndTime);
		// 文件大小
		long maxSize = CommonConstant.ALIYUN_OSS_MAX_SIZE * 1024 * 1024;
		// 回调
		JSONObject callback = new JSONObject();
		callback.put("callbackUrl", CommonConstant.ALIYUN_OSS_CALLBACK);
		callback.put("callbackBody", "filename=${object}&size=${size}&mimeType=${mimeType}&height=${imageInfo.height}&width=${imageInfo.width}");
		callback.put("callbackBodyType", "application/x-www-form-urlencoded");
		// 提交节点
		String action = "http://" + CommonConstant.ALIYUN_OSS_BUCKET_NAME + "." + CommonConstant.ALIYUN_OSS_ENDPOINT;
		try {
			PolicyConditions policyConds = new PolicyConditions();
			policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, maxSize);
			policyConds.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, dir);
			String postPolicy = aliyunOssClient.generatePostPolicy(expiration, policyConds);
			byte[] binaryData = postPolicy.getBytes("utf-8");
			String policy = BinaryUtil.toBase64String(binaryData);
			String signature = aliyunOssClient.calculatePostSignature(postPolicy);
			String callbackData = BinaryUtil.toBase64String(callback.toString().getBytes("utf-8"));
			// 返回结果
			result.put("OSSAccessKeyId", aliyunOssClient.getCredentialsProvider().getCredentials().getAccessKeyId());
			result.put("policy", policy);
			result.put("signature", signature);
			result.put("dir", dir);
			result.put("callback", callbackData);
			result.put("myDomain", CommonConstant.ALIOSS_URL_PREFIX);
			result.put("action", action);
		} catch (Exception e) {
			LOGGER.error("签名生成失败", e);
		}
		return result;*/
	}

}
