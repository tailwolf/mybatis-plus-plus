package com.tailwolf.config;

import com.tailwolf.mybatis.fill.FillFieldHandler;
import com.tailwolf.mybatis.fill.InsertFill;
import com.tailwolf.mybatis.fill.UpdateFill;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * 关于mybatis-complete的填充指定字段的配置
 */
@Configuration
public class MyBatisCompleteConfig {
    @Bean
    public FillFieldHandler fillFieldHandler() {
        return new FillFieldHandler() {
            @Override
            public void insertFill(InsertFill insertFill) {
                insertFill.fillColumn("create_on", new Date());
                insertFill.fillColumn("create_by", "test");
                insertFill.fillColumn("update_on", new Date());
                insertFill.fillColumn("update_by", "test");
            }

            @Override
            public void updateFill(UpdateFill updateFill) {
                updateFill.fillColumn("update_on", new Date());
                updateFill.fillColumn("update_by", "test");
            }
        };
    }
}