package com.xhzb.nursing.domain.vo;

import lombok.Data;

@Data
public class ElderVisitInfoVO {

    /**
     * 老人姓名
     */
    private String name;
    /**
     * 探访时间
     */
    private String visitTime;
    /**
     * 探访人
     */
    private String visitor;

}