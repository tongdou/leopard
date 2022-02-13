package io.github.leopard.system.service.impl;

import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.zjiecode.wxpusher.client.bean.CreateQrcodeReq;
import com.zjiecode.wxpusher.client.bean.CreateQrcodeResp;
import com.zjiecode.wxpusher.client.bean.Result;
import com.zjiecode.wxpusher.client.bean.ResultCode;
import io.github.leopard.common.core.text.Convert;
import io.github.leopard.common.utils.DateUtils;
import io.github.leopard.common.utils.StringUtils;
import io.github.leopard.common.utils.security.EncryptorUtil;
import io.github.leopard.system.domain.BizMerchantConfig;
import io.github.leopard.system.mapper.BizMerchantConfigMapper;
import io.github.leopard.system.service.DictService;
import io.github.leopard.system.service.IBizMerchantConfigService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 商户信息配置Service业务层处理
 *
 * @author liuxin
 * @date 2022-02-12
 */
@Service
public class BizMerchantConfigServiceImpl implements IBizMerchantConfigService {
    @Autowired
    private DictService dictService;

    @Autowired
    private BizMerchantConfigMapper bizMerchantConfigMapper;

    /**
     * 查询商户信息配置
     *
     * @param id 商户信息配置主键
     * @return 商户信息配置
     */
    @Override
    public BizMerchantConfig selectBizMerchantConfigById(Long id) {
        BizMerchantConfig bizMerchantConfig = bizMerchantConfigMapper.selectBizMerchantConfigById(id);
        String url = dictService.getLabel("leopard_wx_pusher", "WXPUSHER_CREATE_QRCODE_URL");
        String appToken = dictService.getLabel("leopard_wx_pusher", "WXPUSHER_APP_TOKEN");

        CreateQrcodeReq createQrcodeReq = new CreateQrcodeReq();
        createQrcodeReq.setAppToken(appToken);
        createQrcodeReq.setExtra(String.valueOf(id));
        createQrcodeReq.setValidTime(60 * 60 * 24 * 30);
        String resultData = HttpRequest.post(url).body(JSON.toJSONString(createQrcodeReq)).execute().body();
        if (StringUtils.isNotNull(resultData)) {
            Result<CreateQrcodeResp> qrcodeRespResult = JSON.parseObject(resultData,
                    new TypeReference<Result<CreateQrcodeResp>>() {
                    });
            if(qrcodeRespResult.getCode().equals(ResultCode.SUCCESS.getCode())){
                bizMerchantConfig.setQrCodeUrl(qrcodeRespResult.getData().getUrl());
            }
        }
        return bizMerchantConfig;
    }

    @Override
    public BizMerchantConfig selectBizMerchantConfigByUId(Long uid) {
        BizMerchantConfig bizMerchantConfig  = new BizMerchantConfig();
        bizMerchantConfig.setUid(uid);
        List<BizMerchantConfig> bizMerchantConfigList = bizMerchantConfigMapper.selectBizMerchantConfigList(bizMerchantConfig);
        if(CollectionUtils.isNotEmpty(bizMerchantConfigList)){
            return bizMerchantConfigList.get(0);
        }
        return null;
    }

    /**
     * 查询商户信息配置列表
     *
     * @param bizMerchantConfig 商户信息配置
     * @return 商户信息配置
     */
    @Override
    public List<BizMerchantConfig> selectBizMerchantConfigList(BizMerchantConfig bizMerchantConfig) {
        return bizMerchantConfigMapper.selectBizMerchantConfigList(bizMerchantConfig);
    }

    /**
     * 新增商户信息配置
     *
     * @param bizMerchantConfig 商户信息配置
     * @return 结果
     */
    @Override
    public int insertBizMerchantConfig(BizMerchantConfig bizMerchantConfig) {
        //秘钥信息加密
        bizMerchantConfig.setGateApiKey(EncryptorUtil.encrypt(bizMerchantConfig.getGateApiKey()));
        bizMerchantConfig.setGateSecret(EncryptorUtil.encrypt(bizMerchantConfig.getGateSecret()));
        bizMerchantConfig.setWxUid(EncryptorUtil.encrypt(bizMerchantConfig.getWxUid()));
        bizMerchantConfig.setCreateTime(DateUtils.getNowDate());
        return bizMerchantConfigMapper.insertBizMerchantConfig(bizMerchantConfig);
    }

    /**
     * 修改商户信息配置
     *
     * @param bizMerchantConfig 商户信息配置
     * @return 结果
     */
    @Override
    public int updateBizMerchantConfig(BizMerchantConfig bizMerchantConfig) {
        //秘钥信息加密
        bizMerchantConfig.setGateApiKey(EncryptorUtil.encrypt(bizMerchantConfig.getGateApiKey()));
        bizMerchantConfig.setGateSecret(EncryptorUtil.encrypt(bizMerchantConfig.getGateSecret()));
        bizMerchantConfig.setWxUid(EncryptorUtil.encrypt(bizMerchantConfig.getWxUid()));
        bizMerchantConfig.setUpdateTime(DateUtils.getNowDate());
        return bizMerchantConfigMapper.updateBizMerchantConfig(bizMerchantConfig);
    }

    /**
     * 根据商户id更新wxuid
     * @param merchantConfigId
     * @param wxUid
     */
    @Override
    public void updateWxUidById(String merchantConfigId, String wxUid) {
        BizMerchantConfig bizMerchantConfig = new BizMerchantConfig();
        bizMerchantConfig.setId(Long.valueOf(merchantConfigId));
        bizMerchantConfig.setWxUid(EncryptorUtil.encrypt(wxUid));
        bizMerchantConfigMapper.updateBizMerchantConfig(bizMerchantConfig);
    }

    /**
     * 批量删除商户信息配置
     *
     * @param ids 需要删除的商户信息配置主键
     * @return 结果
     */
    @Override
    public int deleteBizMerchantConfigByIds(String ids) {
        return bizMerchantConfigMapper.deleteBizMerchantConfigByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除商户信息配置信息
     *
     * @param id 商户信息配置主键
     * @return 结果
     */
    @Override
    public int deleteBizMerchantConfigById(Long id) {
        return bizMerchantConfigMapper.deleteBizMerchantConfigById(id);
    }
}
