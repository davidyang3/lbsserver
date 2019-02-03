package com.tjufe.graduate.lbsserver.Bean;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tagActivityMapping")
@Data
public class TagActivityMapping {

    @Id
    Integer id;

    int tagId;

    int activityId;

    public TagActivityMapping (int tagId, int activityId) {
        this.activityId = activityId;
        this.tagId = tagId;
    }

}
