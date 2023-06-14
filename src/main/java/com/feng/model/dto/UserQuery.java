package com.feng.model.dto;


import com.feng.common.PageRequest;
import lombok.Data;

import java.util.List;

/**
 * @author feng
 * @create 2023-02-10 22:22
 */
@Data
public class UserQuery extends PageRequest {
    String searchText;
    List<Long> ids;
}
