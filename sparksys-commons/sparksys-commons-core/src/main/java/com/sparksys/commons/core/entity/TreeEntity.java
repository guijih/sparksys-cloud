package com.sparksys.commons.core.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * description: 树形实体封装
 *
 * @Author zhouxinlei
 * @Date 2020-05-24 12:48:27
 */
@Getter
@Setter
@Accessors(chain = true)
@ToString(callSuper = true)
public class TreeEntity<E, T extends Serializable> {

    @ApiModelProperty(value = "主键")
    protected T id;

    @ApiModelProperty(value = "名称")
    protected String label;

    @ApiModelProperty(value = "父ID")
    protected T parentId;

    @ApiModelProperty(value = "排序号")
    protected Integer sortValue;

    @ApiModelProperty(value = "子节点", hidden = true)
    protected List<E> children;

    @ApiModelProperty(value = "创建时间")
    protected String createTime;

    @ApiModelProperty(value = "创建人ID")
    protected T createUserId;

    @ApiModelProperty(value = "最后修改时间")
    protected String updateTime;

    @ApiModelProperty(value = "最后修改人ID")
    protected T updateUserId;

    /**
     * 初始化子类
     */
    public void initChildren() {
        if (getChildren() == null) {
            this.setChildren(new ArrayList<>());
        }
    }
}
