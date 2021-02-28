package com.tailwolf.mybatis.core;

import com.tailwolf.mybatis.core.api.build.EntityCrudStatementBuild;
import com.tailwolf.mybatis.core.api.build.EntityDaoMapperStatementBuild;
import com.tailwolf.mybatis.core.dsl.build.DslMappedStatementBuild;
import org.apache.ibatis.session.Configuration;
import com.tailwolf.mybatis.core.common.seat.DslSeat;
import com.tailwolf.mybatis.core.common.seat.EntityCrudSeat;

import java.util.List;

/**
 * MappedStatement工厂
 * @author tailwolf
 * @date 2020-09-22
 */
public class MappedStatementFactory {
    private MappedStatementFactory(){}

    public static MappedStatementBuild createMappedStatementBuild(Object obj, List<String> dslMapperList, List<String> daoMapperList, Configuration configuration){
        if(obj instanceof DslSeat){
            return new DslMappedStatementBuild(obj.getClass(), dslMapperList, configuration);
        }else if(obj instanceof EntityCrudSeat){
            return new EntityDaoMapperStatementBuild(obj.getClass(), daoMapperList, configuration);
        }else{
            return new EntityCrudStatementBuild(obj.getClass(), configuration);
        }

    }
}
