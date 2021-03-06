package com.tjufe.graduate.lbsserver.Bean;


import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "tagActivityMapping")
@Data
public class TagActivityMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private int tagId;

    private int activityId;

    public TagActivityMapping (int tagId, int activityId) {
        this.activityId = activityId;
        this.tagId = tagId;
    }

}
