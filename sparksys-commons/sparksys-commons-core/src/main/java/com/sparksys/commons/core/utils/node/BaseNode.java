package com.sparksys.commons.core.utils.node;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * description: 节点基类
 *
 * @Author zhouxinlei
 * @Date 2020-05-24 13:09:26
 */
@Data
public class BaseNode implements INode {

    /**
     * 主键ID
     */
    protected Integer id;

    /**
     * 父节点ID
     */
    protected Integer parentId;

    /**
     * 子孙节点
     */
    protected List<INode> children = new ArrayList<>();

}
