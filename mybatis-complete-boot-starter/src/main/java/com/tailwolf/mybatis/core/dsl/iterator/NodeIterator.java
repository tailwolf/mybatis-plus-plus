package com.tailwolf.mybatis.core.dsl.iterator;

import com.tailwolf.mybatis.constant.MontageSqlConstant;
import com.tailwolf.mybatis.core.exception.MybatisCompleteRuntimeException;
import com.tailwolf.mybatis.core.dsl.node.ConditionNode;

import java.util.ArrayList;
import java.util.List;

/**
 * 节点迭代器
 * @author tailwolf
 * @date 2020-06-23
 */
public class NodeIterator<T> implements Iterator<T> {

    private int orEndCount;

    /**
     * 当前元素的数量
     */
    private int size;
    /**
     * 当前节点的下标
     */
    private int index;

    /**
     * 保存节点的数组
     */
    private ArrayList<T> nodeList = new ArrayList<>();

    /**
     * 是否为空
     * @return
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * 是否是第一个节点
     */
    public boolean isFirst(){
        return index == 0;
    }

    /**
     * 是否是最后一个节点
     */
    public boolean isLast(){
        //如果当前没有节点，也当是最后一个节点
        if(isEmpty()){
            return true;
        }
        return index == size - 1;
    }

    /**
     * 获得当前节点
     * @return
     */
    @Override
    public T currentNode() {
        if(isEmpty() || index >= size){
            return null;
        }
        return nodeList.get(index);
    }

    /**
     * 下标往下移
     */
    @Override
    public NodeIterator<T> nextNonius() {
        if(index < size){
            index++;
        }
        return this;
    }

    /**
     * 下标往上移
     */
    @Override
    public NodeIterator<T> preNonius() {
        if(index >= 0){
            index--;
        }
        return this;
    }

    /**
     * 获取前一个元素
     * @return
     */
    @Override
    public T preNode() {
        if(index == 0){
            return null;
        }
        return nodeList.get(index-1);
    }

    /**
     * 获取下一个元素
     * @return
     */
    @Override
    public T nextNode() {
        if(index == size-1){
            return null;
        }
        return nodeList.get(index+1);
    }

    /**
     * 增加一个节点到最后
     */
    public void add(T node){
        if(node == null){
            throw new NullPointerException();
        }

        nodeList.add(node);
        size++;
    }

    /**
     * 增加一个集合到最后
     */
    public void addAll(List<T> nodeList){
        if(nodeList == null){
            throw new NullPointerException();
        }

        this.nodeList.addAll(nodeList);
        this.size = this.size + nodeList.size();
    }

    /**
     * 获取第一个节点
     */
    public T getFirst(){
        if(size == 0){
            throw new MybatisCompleteRuntimeException("当前迭代器无元素数量！");
        }
        return nodeList.get(0);
    }









    /**
     * 当前节点是否AND的开始节点
     * @param conditionKeywork
     * @return
     */
    public boolean isAndStart(String conditionKeywork){
        return MontageSqlConstant.AND_START.equals(conditionKeywork);
    }

    /**
     * 当前节点是否OR的开始节点
     * @param conditionKeywork
     * @return
     */
    public boolean isOrStart(String conditionKeywork){
        return MontageSqlConstant.OR_START.equals(conditionKeywork);
    }

    /**
     * 当前节点是否AND的结束节点
     * @param conditionKeywork
     * @return
     */
    public boolean isAndEnd(String conditionKeywork){
        return MontageSqlConstant.AND_END.equals(conditionKeywork);
    }

    /**
     * 当前节点是否OR的结束节点
     * @param conditionKeywork
     * @return
     */
    public boolean isOrEnd(String conditionKeywork){
        return MontageSqlConstant.OR_END.equals(conditionKeywork);
    }

    /**
     * 当前节点是否开始节点
     * @param conditionKeywork
     * @return
     */
    private boolean isStart(String conditionKeywork){
        return isAndStart(conditionKeywork) || isOrStart(conditionKeywork);
    }


    /**
     * 当前节点是否结束节点
     * @param conditionKeywork
     * @return
     */
    public boolean isEnd(String conditionKeywork){
        if(MontageSqlConstant.OR_END.equals(conditionKeywork)){
            orEndCount++;
            return true;
        }
        return MontageSqlConstant.AND_END.equals(conditionKeywork);
    }

    /**
     * 是否应该加OR
     */
    public boolean isOr(NodeIterator<ConditionNode> conditionsIterator){
        if(conditionsIterator.isFirst()){
            return false;
        }
        ConditionNode conditionNode = conditionsIterator.preNode();
        String conditionKeywork = conditionNode.getConditionKeywork();
        if(isOrEnd(conditionKeywork)){
            //如果是第一次or_end，且第一个节点为or_star，则应该加OR
            ConditionNode firstNode = conditionsIterator.getFirst();
            if(MontageSqlConstant.OR_START.equals(firstNode.getConditionKeywork())
                    && orEndCount == 1){
                return true;
            }
        }
        return false;
    }

    /**
     * 是否应该加AND
     */
    public boolean isAnd(NodeIterator<ConditionNode> conditionsIterator){
        if(conditionsIterator.isFirst()){
            return false;
        }
        ConditionNode conditionNode = conditionsIterator.preNode();
        String conditionKeywork = conditionNode.getConditionKeywork();
        if(isStart(conditionKeywork)){
            return false;
        }
        //如果上一个节点只是or，则不应该加and
        ConditionNode preNode = conditionsIterator.preNode();
        if(MontageSqlConstant.OR.equals(preNode.getConditionKeywork())){
            return false;
        }
        //是否是OR
        return !isOr(conditionsIterator);
    }

    private boolean isConditionKeyWord(NodeIterator<ConditionNode> conditionsIterator){
        if(conditionsIterator.isFirst()){
            return false;
        }
        ConditionNode conditionNode = conditionsIterator.preNode();
        String conditionKeywork = conditionNode.getConditionKeywork();
        if(isStart(conditionKeywork)){
            return false;
        }
        return true;
    }
}
