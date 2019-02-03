package com.tjufe.graduate.lbsserver.Model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Pager {

    int start;

    int end;

    int pageSize;

    int totalNum;

}
