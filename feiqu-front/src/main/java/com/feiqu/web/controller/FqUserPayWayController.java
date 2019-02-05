package com.feiqu.web.controller;


import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.feiqu.common.base.BaseResult;
import com.feiqu.common.enums.ResultEnum;
import com.feiqu.common.enums.YesNoEnum;
import com.feiqu.framwork.constant.CommonConstant;
import com.feiqu.framwork.util.WebUtil;
import com.feiqu.framwork.web.base.BaseController;
import com.feiqu.system.model.FqUserPayWay;
import com.feiqu.system.model.FqUserPayWayExample;
import com.feiqu.system.model.UploadImgRecord;
import com.feiqu.system.model.UploadImgRecordExample;
import com.feiqu.system.pojo.cache.FqUserCache;
import com.feiqu.system.service.FqUserPayWayService;
import com.feiqu.system.service.UploadImgRecordService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jeesuite.filesystem.FileSystemClient;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.Date;
import java.util.List;


/**
 * FqUserPayWaycontroller
 * Created by cwd on 2019/2/2.
 */
@Controller
@RequestMapping("/fqUserPayWay")
public class FqUserPayWayController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(FqUserPayWayController.class);

    @Resource
    private FqUserPayWayService fqUserPayWayService;
    @Resource
    private UploadImgRecordService uploadImgRecordService;

    /**
     * 跳转到FqUserPayWay首页
     */
    @RequestMapping("")
    public String index() {
        return "/fqUserPayWay/index.html";
    }

    /**
     * 添加FqUserPayWay页面
     */
    @RequestMapping("/fqUserPayWay_add")
    public String fqUserPayWay_add() {
        return "/fqUserPayWay/add.html";
    }

    /**
     * 上传二维码
     * @param request
     * @param file
     * @param model
     * @param payWay com.feiqu.common.enums.PayWayEnum
     * @return
     */
    @PostMapping("uploadQrCode")
    @ResponseBody
    public Object uploadQrCode(HttpServletRequest request, MultipartFile file,Model model,Integer payWay) {
        BaseResult result = new BaseResult();
        String fileName = "";
        File localFile;
        try {
            long size = file.getSize();
            int limit = 100 * 1024;//头像不超过50kb=>100kb
            if (size > limit) {
                result.setResult(ResultEnum.FILE_TOO_LARGE);
                result.setMessage("二维码图片大小不能超过100KB");
                return result;
            }
            FqUserCache user = getCurrentUser();

            if (user == null) {
                result.setResult(ResultEnum.FAIL);
                return result;
            }
            String picUrl;
            Date now = new Date();
            String time = DateFormatUtils.format(now,"yyyy-MM-dd");
            String path = CommonConstant.FILE_UPLOAD_TEMP_PATH+File.separator+time;
            File fileParent = new File(path);
            if(!fileParent.exists()){
                fileParent.mkdirs();
            }
            String extName = FileUtil.extName(file.getOriginalFilename());
            List<String> picExtList = CommonConstant.picExtList;
            if(!picExtList.contains(extName)){
                result.setResult(ResultEnum.PIC_EXTNAME_NOT_RIGHT);
                return result;
            }
            fileName = CommonConstant.FILE_NAME_PREFIX + DateUtil.format(now, "yyyyMMddHHmmss") + "." + extName;
            localFile = new File(fileParent, fileName);
            //MultipartFile自带的解析方法
            file.transferTo(localFile);

            //记录图片md5
            String picMd5 = DigestUtil.md5Hex(localFile);
            //根据md5查数据库有没有
            UploadImgRecordExample imgRecordExample = new UploadImgRecordExample();
            imgRecordExample.createCriteria().andPicMd5EqualTo(picMd5).andPicSizeEqualTo(size);
            UploadImgRecord uploadImgRecord = uploadImgRecordService.selectFirstByExample(imgRecordExample);
            if (uploadImgRecord != null && StringUtils.isNotBlank(uploadImgRecord.getPicUrl())) {
                picUrl = uploadImgRecord.getPicUrl();
            } else {
                picUrl = FileSystemClient.getClient("aliyun").upload("icon/"+fileName,localFile);
                picUrl+="?x-oss-process=image/resize,m_fixed,h_300,w_200";
                uploadImgRecord = new UploadImgRecord(picUrl, picMd5, new Date(), WebUtil.getIP(request), user.getId(), size);
                uploadImgRecordService.insert(uploadImgRecord);
            }
            FqUserPayWayExample fqUserPayWayExample = new FqUserPayWayExample();
            fqUserPayWayExample.createCriteria().andUserIdEqualTo(user.getId()).andDelFlagEqualTo(YesNoEnum.NO.getValue());
            FqUserPayWay fqUserPayWay = fqUserPayWayService.selectFirstByExample(fqUserPayWayExample);
            if(fqUserPayWay == null){
                fqUserPayWay = new FqUserPayWay();
                fqUserPayWay.setPayImgUrl(picUrl);
                fqUserPayWay.setGmtCreate(new Date());
                fqUserPayWay.setUserId(user.getId());
                fqUserPayWay.setDelFlag(YesNoEnum.NO.getValue());
                fqUserPayWay.setPayWay(payWay);
                fqUserPayWayService.insert(fqUserPayWay);
            }else {
                FqUserPayWay  toUpdate = new FqUserPayWay();
                toUpdate.setId(fqUserPayWay.getId());
                toUpdate.setPayImgUrl(picUrl);
                fqUserPayWayService.updateByPrimaryKeySelective(toUpdate);
            }
            result.setData(picUrl);
        } catch (Exception e) {
            logger.error("上传二维码失败", e);
            result.setCode(CommonConstant.SYSTEM_ERROR_CODE);
            return result;
        }
        return result;
    }


    /**
     * ajax删除FqUserPayWay
     */
    @ResponseBody
    @RequestMapping("/delete")
    public Object delete(@RequestParam Long id) {
        BaseResult result = new BaseResult();
        try {
            fqUserPayWayService.deleteByPrimaryKey(id);
        } catch (Exception e) {
            logger.error("error", e);
            result.setCode("1");
        }
        return result;
    }

    /**
     * 更新FqUserPayWay页面
     */
    @RequestMapping("/edit/{fqUserPayWayId}")
    public Object fqUserPayWayEdit(@PathVariable Long fqUserPayWayId, Model model) {
        FqUserPayWay fqUserPayWay = fqUserPayWayService.selectByPrimaryKey(fqUserPayWayId);
        model.addAttribute("fqUserPayWay", fqUserPayWay);
        return "/fqUserPayWay/edit.html";
    }

    /**
     * ajax更新FqUserPayWay
     */
    @ResponseBody
    @PostMapping("/save")
    public Object save(FqUserPayWay fqUserPayWay) {
        BaseResult result = new BaseResult();
        try {
            FqUserCache fqUserCache = getCurrentUser();
            if (fqUserCache == null) {
                result.setResult(ResultEnum.USER_NOT_LOGIN);
                return result;
            }
            if (fqUserPayWay.getId() == null) {
                fqUserPayWayService.insert(fqUserPayWay);
            } else {
                fqUserPayWayService.updateByPrimaryKeySelective(fqUserPayWay);
            }
        } catch (Exception e) {
            logger.error("error", e);
            result.setCode("1");
        }
        return result;
    }


    /**
     * 查询FqUserPayWay首页
     */
    @RequestMapping("list")
    @ResponseBody
    public Object list(@RequestParam(defaultValue = "0") Integer index,
                       @RequestParam(defaultValue = "10") Integer size) {
        BaseResult result = new BaseResult();
        try {
            PageHelper.startPage(index, size);
            FqUserPayWayExample example = new FqUserPayWayExample();
            example.setOrderByClause("create_time desc");
            List<FqUserPayWay> list = fqUserPayWayService.selectByExample(example);
            PageInfo page = new PageInfo(list);
            result.setData(page);
        } catch (Exception e) {
            logger.error("error", e);
            result.setCode("1");
        }
        return result;
    }
}