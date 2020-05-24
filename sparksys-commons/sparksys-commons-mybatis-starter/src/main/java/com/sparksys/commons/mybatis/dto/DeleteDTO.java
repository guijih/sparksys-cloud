package com.sparksys.commons.mybatis.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

/**
 * description: 删除入参
 *
 * @Author zhouxinlei
 * @Date 2020-05-24 13:21:12
 */
@Data
@ApiModel(value = "删除入参", description = "删除入参")
public class DeleteDTO {

    private List<Long> ids;

}
