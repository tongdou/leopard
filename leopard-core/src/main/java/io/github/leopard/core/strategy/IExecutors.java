package io.github.leopard.core.strategy;

import io.github.leopard.common.utils.security.EncryptorUtil;
import io.github.leopard.core.strategy.model.UserSecretDTO;
import io.github.leopard.system.domain.BizMerchantConfig;
import io.github.leopard.system.service.IBizMerchantConfigService;

import javax.annotation.Resource;

/**
 * @author meteor
 */
public class IExecutors {


    @Resource
    IBizMerchantConfigService merchantConfigService;

    /**
     * 构造商户的秘钥信息
     *
     * @param uid
     * @return
     */
    protected UserSecretDTO buildUserSecret(String uid) {
        BizMerchantConfig bizMerchantConfig = merchantConfigService.selectBizMerchantConfigByUId(Long.parseLong(uid));
        String gateApiKey = EncryptorUtil.decrypt(bizMerchantConfig.getGateApiKey());
        String gateSecret = EncryptorUtil.decrypt(bizMerchantConfig.getGateSecret());
        String wxUid = EncryptorUtil.decrypt(bizMerchantConfig.getWxUid());
        return new UserSecretDTO(gateApiKey, gateSecret, wxUid);
    }

}
