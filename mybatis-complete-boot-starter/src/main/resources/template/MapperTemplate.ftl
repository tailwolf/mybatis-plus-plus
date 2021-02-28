<#--
    mapper层的模板
    author: tailwolf
    date: 2021-01-27
-->
package ${packageName};

import com.tailwolf.mybatis.core.common.dao.EntityOptMapper;
import ${entityReference};

public interface ${mapperName} extends EntityOptMapper<${entityName}> {
}