package com.sparksys.commons.core.utils.node;

import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * description: 森林节点类
 *
 * @Author zhouxinlei
 * @Date 2020-05-24 13:09:37
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ForestNode extends BaseNode {

    /**
     * 节点内容
     */
    private Object content;

    public ForestNode(Integer id, Integer parentId, Object content) {
        this.id = id;
        this.parentId = parentId;
        this.content = content;
    }

}
