package com.tailwolf.mybatis.core.dsl.functional.select.join;

import com.tailwolf.mybatis.core.exception.MybatisPlusPlusRuntimeException;
import com.tailwolf.mybatis.core.dsl.node.SelectNode;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;

public class JoinSelect<T, E> {

    /**
     * 条件节点
     */
    private LinkedList<SelectNode> conditionsQueue = new LinkedList<>();

    /**
     * 选择列名
     * @param selectFromTableFunctional
     * @return
     */
    public JoinSelect<T, E> column(JoinSelectFromFunctional<T> selectFromTableFunctional) {
        conditionsQueue.add(SelectNode.newInstance(selectFromTableFunctional, null));
        return this;
    }

    /**
     * 选择列名
     * @param selectJoinTableFunctional
     * @return
     */
    public JoinSelect<T, E> column(JoinSelectJoinFunctional<E> selectJoinTableFunctional) {
        conditionsQueue.add(SelectNode.newInstance(selectJoinTableFunctional, null));
        return this;
    }

    /**
     * 选择表所有的字段
     * @param clazz
     * @return
     */
    public JoinSelect<T, E> column(Class clazz) {
        Object obj = createInstanceByClass(clazz);
        conditionsQueue.add(SelectNode.newInstance(obj, null));
        return this;
    }

//    /**
//     * 函数count(*)
//     * @return
//     */
//    public JoinSelect<T, E> count() {
//        conditionsQueue.add(SelectNode.newInstance(MontageSqlConstant.COUNT_ALL, MontageSqlConstant.COUNT));
//        return this;
//    }
//
//    /**
//     * count函数，选择字段
//     * @return
//     */
//    public JoinSelect<T, E> count(JoinSelectFromFunctional<T> selectFromTableFunctional) {
//        conditionsQueue.add(SelectNode.newInstance(selectFromTableFunctional, MontageSqlConstant.COUNT_TEMPLATE));
//        return this;
//    }
//
//    /**
//     * count函数，选择字段
//     * @return
//     */
//    public JoinSelect<T, E> count(JoinSelectJoinFunctional<E> selectJoinTableFunctional) {
//        conditionsQueue.add(SelectNode.newInstance(selectJoinTableFunctional, MontageSqlConstant.COUNT_TEMPLATE));
//        return this;
//    }

//    /**
//     * count(*)函数，选择表
//     * @return
//     */
//    public JoinSelect<T, E> count(Class clazz) {
//        Object obj = createInstanceByClass(clazz);
//        conditionsQueue.add(SelectNode.newInstance(obj, MontageSqlConstant.COUNT_TEMPLATE));
//        return this;
//    }

    public LinkedList<SelectNode> getConditionsQueue() {
        return conditionsQueue;
    }

    public void setConditionsQueue(LinkedList<SelectNode> conditionsQueue) {
        this.conditionsQueue = conditionsQueue;
    }

    public void clean(){
        this.conditionsQueue.clear();
    }

    /**
     * 根据Class返回创建的实例
     * @param clazz
     * @param <T>
     * @return
     */
    private static <T> T createInstanceByClass(Class clazz){
        Object obj = null;
        try {
            obj = clazz.getDeclaredConstructor().newInstance();
        } catch (InstantiationException e) {
            throw new MybatisPlusPlusRuntimeException("无法实例化类" + clazz.getName());
        } catch (IllegalAccessException e) {
            throw new MybatisPlusPlusRuntimeException("无法访问实体类" + clazz.getName() + "无参构造方法！");
        } catch (InvocationTargetException e) {
            throw new MybatisPlusPlusRuntimeException("调用目标异常！");
        } catch (NoSuchMethodException e) {
            throw new MybatisPlusPlusRuntimeException("实体类" + clazz.getName() + "不存在无参构造方法！");
        }
        return (T)obj;
    }
}
