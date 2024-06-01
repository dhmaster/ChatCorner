package com.dhuer.mallchat.common.user.service.impl;

import cn.hutool.core.thread.NamedThreadFactory;
import cn.hutool.http.HttpUtil;
import com.dhuer.mallchat.common.common.domain.vo.resp.ApiResult;
import com.dhuer.mallchat.common.common.utils.JsonUtils;
import com.dhuer.mallchat.common.user.dao.UserDao;
import com.dhuer.mallchat.common.user.domain.entity.IpDetail;
import com.dhuer.mallchat.common.user.domain.entity.IpInfo;
import com.dhuer.mallchat.common.user.domain.entity.User;
import com.dhuer.mallchat.common.user.service.IpService;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Description:
 * Author: Jintao Li
 * Date: 2024/5/31
 */
@Service
@Slf4j
public class IpServiceImpl implements IpService , DisposableBean {
    // 给线程池加前缀 prefix，后期如果 cpu 占用高，我们就可以排查哪个业务有问题
    private static ExecutorService executor = new ThreadPoolExecutor(1, 1,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>(500), new NamedThreadFactory("refresh-ipDetail", false));
    @Autowired
    private UserDao userDao;

    @Override
    public void refreshIpDetailAsync(Long uid) {
        executor.execute(() -> {
            User user = userDao.getById(uid);
            IpInfo ipInfo = user.getIpInfo();
            if (Objects.isNull(ipInfo)) {
                return;
            }
            String ip = ipInfo.needRefreshIp();
            IpDetail ipDetail = tryGetIpDetailOrNullThreeTimes(ip);
            if (Objects.nonNull(ipDetail)) {
                ipInfo.refreshIpDetail(ipDetail);
                User update = new User();
                update.setId(uid);
                update.setIpInfo(ipInfo);
                userDao.updateById(update);
            }
        });
    }

    private static IpDetail tryGetIpDetailOrNullThreeTimes(String ip) {
        for (int i = 0; i < 3; i++) {
            IpDetail ipDetail = getIpDetailOrNull(ip);
            if (Objects.nonNull(ipDetail)) {
                return ipDetail;
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                log.error("tryGetIpDetailOrNullThreeTimes InterruptedException", e);
            }
        }
        return null;
    }

    private static IpDetail getIpDetailOrNull(String ip) {
        try {
            String url = "https://ip.taobao.com/outGetIpInfo?ip=" + ip + "&accessKey=alibaba-inc";
            String data = HttpUtil.get(url);
            ApiResult<IpDetail> result = JsonUtils.toObj(data, new TypeReference<ApiResult<IpDetail>>() {});
            IpDetail detail = result.getData();
            return detail;
        } catch (Exception e) {
            return null;
        }
    }

    public static void main(String[] args) {
        Date begin = new Date();
        for (int i = 0; i < 100; i++) {
            int finalI = i;
            executor.execute(() -> {
                IpDetail ipDetail = tryGetIpDetailOrNullThreeTimes("117.2.5.2");
                if (Objects.nonNull(ipDetail)) {
                    Date date = new Date();
                    System.out.println(String.format("%d times.", finalI));
                }
            });
        }
        Date end = new Date();
        System.out.println(end.getTime() - begin.getTime());
    }

    @Override
    public void destroy() throws InterruptedException {
        // 不在接收新任务
        executor.shutdown();
        if (!executor.awaitTermination(30, TimeUnit.SECONDS)) {//最多等30秒，处理不完就拉倒
            if (log.isErrorEnabled()) {
                log.error("Timed out while waiting for executor [{}] to terminate", executor);
            }
        }
    }
}
