package com.dhuer.mallchat.common.user.service.cache;

import com.dhuer.mallchat.common.common.constant.RedisKey;
import com.dhuer.mallchat.common.common.service.cache.AbstractRedisStringCache;
import com.dhuer.mallchat.common.user.dao.UserBackpackDao;
import com.dhuer.mallchat.common.user.domain.dto.SummaryInfoDTO;
import com.dhuer.mallchat.common.user.domain.entity.*;
import com.dhuer.mallchat.common.user.domain.enums.ItemTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Description:用户所有信息缓存
 * Author: Jintao Li
 * Date: 2024/8/9
 */
@Component
public class UserSummaryCache extends AbstractRedisStringCache<Long, SummaryInfoDTO> {
    @Autowired
    private UserInfoCache userInfoCache;
    @Autowired
    private ItemCache itemCache;
    @Autowired
    private UserBackpackDao userBackpackDao;

    @Override
    protected String getKey(Long uid) {
        return RedisKey.getKey(RedisKey.USER_SUMMARY_STRING, uid);
    }

    @Override
    protected Long getExpireSeconds() {
        return 10 * 60L;
    }

    @Override
    protected Map<Long, SummaryInfoDTO> load(List<Long> uidList) {
        // 用户基本信息
        Map<Long, User> userMap = userInfoCache.getBatch(uidList);
        // 用户徽章信息
        List<ItemConfig> itemConfigs = itemCache.getByType(ItemTypeEnum.BADGE.getType());
        List<Long> itemIds = itemConfigs.stream().map(ItemConfig::getId).collect(Collectors.toList());
        List<UserBackpack> backpacks = userBackpackDao.getByItemIds(uidList, itemIds);
        Map<Long, List<UserBackpack>> userBadgeMap = backpacks.stream().collect(Collectors.groupingBy(UserBackpack::getUid));
        // 用户最后一次更新时间
        return uidList.stream().map(uid -> {
            SummaryInfoDTO summaryInfoDTO = new SummaryInfoDTO();
            User user = userMap.get(uid);
            if (Objects.isNull(user)) {
                return null;
            }
            List<UserBackpack> userBackpacks = userBadgeMap.getOrDefault(user.getId(), new ArrayList<>());
            summaryInfoDTO.setUid(user.getId());
            summaryInfoDTO.setName(user.getName());
            summaryInfoDTO.setAvatar(user.getAvatar());
            summaryInfoDTO.setLocPlace(Optional.ofNullable(user.getIpInfo()).map(IpInfo::getUpdateIpDetail).map(IpDetail::getCity).orElse(null));
            summaryInfoDTO.setWearingItemId(user.getItemId());
            summaryInfoDTO.setItemIds(userBackpacks.stream().map(UserBackpack::getItemId).collect(Collectors.toList()));
            return summaryInfoDTO;
        })
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(SummaryInfoDTO::getUid, Function.identity()));
    }
}
