package com.tailwolf.mybatis.generator.properties;

/**
 * 生成控制层的配置属性
 * @author tailwolf
 * @date 2021-01-27
 */
public class ControllerProperties extends BaseProperties{
    //service类引用路径
    private String serviceReference;
    //service文件名
    private String serviceName;
    //controller文件名
    private String controllerName;
    //父类引用
    private String controllerParentRef;
    //父类名称
    private String controllerParentName;

    public ControllerProperties(String packageName, String entityName, String serviceReference, String serviceName, String controllerName){
        super(packageName, entityName);
        this.serviceReference = serviceReference;
        this.serviceName = serviceName;
        this.controllerName = controllerName;
    }

    public String getControllerParentRef() {
        return controllerParentRef;
    }

    public void setControllerParentRef(String controllerParentRef) {
        this.controllerParentRef = controllerParentRef;
    }

    public String getControllerParentName() {
        return controllerParentName;
    }

    public void setControllerParentName(String controllerParentName) {
        this.controllerParentName = controllerParentName;
    }

    public String getServiceReference() {
        return serviceReference;
    }

    public void setServiceReference(String serviceReference) {
        this.serviceReference = serviceReference;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getControllerName() {
        return controllerName;
    }

    public void setControllerName(String controllerName) {
        this.controllerName = controllerName;
    }
}
