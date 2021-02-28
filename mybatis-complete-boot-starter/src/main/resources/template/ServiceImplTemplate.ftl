<#--
    serviceImpl层的模板
    author: tailwolf
    date: 2021-01-28
-->
package ${packageName};

import com.tailwolf.mybatis.core.common.service.impl.EntityOptServiceImpl;
import org.springframework.stereotype.Service;
import ${entityReference};
import ${mapperReference};
import ${sericeReference};

@Service
public class ${serviceImplName} extends EntityOptServiceImpl<${entityName}, ${mapperName}> implements ${serviceName}{
}