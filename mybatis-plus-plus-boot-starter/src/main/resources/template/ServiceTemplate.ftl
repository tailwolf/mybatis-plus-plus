<#--
    service层的模板
    author: tailwolf
    date: 2021-01-27
-->
package ${packageName};

import com.tailwolf.mybatis.core.common.service.EntityOptService;
import ${entityReference};

public interface ${serviceName} extends EntityOptService<${entityName}>{
}