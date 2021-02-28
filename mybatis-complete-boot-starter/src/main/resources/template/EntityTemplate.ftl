<#--
    实体类的模板
    author: tailwolf
    date: 2021-01-27
-->
package ${packageName};

<#if entityParentRef??>import ${entityParentRef};</#if>
<#list  imports as impt>
import ${impt};
</#list>
<#if methodFormat == 2>
import lombok.Data;
import lombok.EqualsAndHashCode;
</#if>
import com.tailwolf.mybatis.core.annotation.Table;
import java.io.Serializable;

<#if methodFormat == 2>
@Data
@EqualsAndHashCode(<#if entityParentName??>callSuper = true</#if>)
</#if>
@Table(tableName = "${tableName}")
public class ${entityName} <#if entityParentName??>extends ${entityParentName} <#else>implements Serializable</#if>{
<#list fields as field>
    <#if field.comment??>
    //${field.comment}
    </#if>
    private ${field.fieldType} ${field.fieldName};

</#list>

<#if methodFormat == 1>
<#list fields as field>
    public ${field.fieldType} get${field.fieldName?cap_first}(){
        return ${field.fieldName};
    }

    public void set${field.fieldName?cap_first}(${field.fieldType} ${field.fieldName}){
        this.${field.fieldName} = ${field.fieldName};
    }
</#list>
</#if>
}