package com.dhuer.mallchat.common.user.domain.entity;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

/**
 * Description:
 * Author: Jintao Li
 * Date: 2024/5/31
 */
@Data
public class IpInfo implements Serializable {
    // 注册时的 ip
    private String createIp;
    // 注册时的 ip 详情
    private IpDetail createIpDetail;
    // 最新登录的 ip
    private String updateIp;
    // 最新登录的 ip 详情
    private IpDetail updateIpDetail;

    public void refreshIp(String ip) {
        if (StringUtils.isBlank(ip)) {
            return;
        }
        // 第一次注册
        if (StringUtils.isBlank(createIp)) {
            createIp = ip;
        }
        updateIp = ip;
    }

    /**
     * 通过比较 updateIp 和 updateIpDetail 中的 ip 是否一致，如果一致，不需更新
     */
    public String needRefreshIp() {
        boolean notNeedRefresh = Optional.ofNullable(updateIpDetail)
                .map(IpDetail::getIp)
                .filter(ip -> Objects.equals(ip, updateIp))
                .isPresent();
        return notNeedRefresh ? null : updateIp;
    }

    public void refreshIpDetail(IpDetail ipDetail) {
        if (Objects.equals(createIp, ipDetail.getIp())) {
            createIpDetail = ipDetail;
        }
        if (Objects.equals(updateIp, ipDetail.getIp())) {
            updateIpDetail = ipDetail;
        }
    }
}
