package com.tjufe.graduate.lbsserver.Bean;


import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "NoticeTag")
@Data
public class TagNoticeMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "notice_id")
    private int tagId;

    @Column(name = "tag_id")
    private int noticeId;

    public TagNoticeMapping(int tagId, int noticeId) {
        this.tagId = tagId;
        this.noticeId = noticeId;
    }

}
