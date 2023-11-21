package com.unfbx.chatgpt.entity.billing;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 描述：账户信息
 *
 * @author https:www.unfbx.com
 * @since  2023-04-08
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Subscription {
    @JsonProperty("object")
    private String object;
    @JsonProperty("has_payment_method")
    private boolean hasPaymentMethod;
    @JsonProperty("canceled")
    private boolean canceled;
    @JsonProperty("canceled_at")
    private Object canceledAt;
    @JsonProperty("delinquent")
    private Object delinquent;
    @JsonProperty("access_until")
    private long accessUntil;
    @JsonProperty("soft_limit")
    private long softLimit;
    @JsonProperty("hard_limit")
    private long hardLimit;
    @JsonProperty("system_hard_limit")
    private long systemHardLimit;
    @JsonProperty("soft_limit_usd")
    private double softLimitUsd;
    @JsonProperty("hard_limit_usd")
    private double hardLimitUsd;
    @JsonProperty("system_hard_limit_usd")
    private double systemHardLimitUsd;
    @JsonProperty("plan")
    private Plan plan;
    @JsonProperty("account_name")
    private String accountName;
    @JsonProperty("po_number")
    private Object poNumber;
    @JsonProperty("billing_email")
    private Object billingEmail;
    @JsonProperty("tax_ids")
    private Object taxIds;
    @JsonProperty("billing_address")
    private Object billingAddress;
    @JsonProperty("business_address")
    private Object businessAddress;
    @JsonProperty("primary")
    private Boolean primary;
}
