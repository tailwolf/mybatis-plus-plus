<#--
    控制层的模板
    author: tailwolf
    date: 2021-01-27
-->
package ${packageName};

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
<#if controllerParentRef??>import ${controllerParentRef}</#if>;
import ${serviceReference};

@RestController
@RequestMapping("/${entityName?uncap_first}")
public class ${controllerName} <#if controllerParentName??>extends ${controllerParentName}</#if>{
    @Autowired
    private ${serviceName} ${serviceName?uncap_first};
}