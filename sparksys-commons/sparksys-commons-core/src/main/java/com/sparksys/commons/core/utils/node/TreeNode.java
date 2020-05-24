package com.sparksys.commons.core.utils.node;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * description: 树型节点类
 *
 * @Author zhouxinlei
 * @Date 2020-05-24 13:10:11
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class TreeNode extends BaseNode {

    private String title;

    private Integer key;

    private Integer value;

}
