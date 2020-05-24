package com.sparksys.commons.core.utils.node;

import java.util.List;

/**
 * description: 节点接口类
 *
 * @Author zhouxinlei
 * @Date 2020-05-24 13:09:16
 */
public interface INode {

    /**
     * 主键
     *
     * @return Integer
     */
    Integer getId();

    /**
     * 父主键
     *
     * @return Integer
     */
    Integer getParentId();

    /**
     * 子孙节点
     *
     * @return List
     */
    List<INode> getChildren();

}
