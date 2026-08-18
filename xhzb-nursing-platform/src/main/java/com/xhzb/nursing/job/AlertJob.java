package com.xhzb.nursing.job;

import com.xhzb.nursing.service.IAlertRuleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AlertJob {

    @Autowired
    private IAlertRuleService alertRuleService;

    public void deviceDataAlertFilter(){
       log.info("定时器执行!");
    }
}