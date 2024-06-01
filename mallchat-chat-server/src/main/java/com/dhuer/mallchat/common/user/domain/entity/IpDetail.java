package com.dhuer.mallchat.common.user.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;

/**
 * Description:
 * Author: Jintao Li
 * Date: 2024/5/31
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class IpDetail implements Serializable {
    private String ip;
    private String isp;
    private String isp_id;
    private String city;
    private String city_id;
    private String country;
    private String country_id;
    private String region;
    private String region_id;
}
