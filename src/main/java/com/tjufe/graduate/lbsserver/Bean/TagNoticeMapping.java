package com.tjufe.graduate.lbsserver.Bean;


import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "tagNoticeMapping")
@Data
public class TagNoticeMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    int tagId;

    int noticeId;

    public TagNoticeMapping(int tagId, int noticeId) {
        this.tagId = tagId;
        this.noticeId = noticeId;
    }

}