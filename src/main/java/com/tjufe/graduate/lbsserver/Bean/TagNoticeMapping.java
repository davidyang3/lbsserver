package com.tjufe.graduate.lbsserver.Bean;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "NoticeTag")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TagNoticeMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "tag_id")
    private int tagId;

    @Column(name = "notice_id")
    private int noticeId;

    public TagNoticeMapping(int tagId, int noticeId) {
        this.tagId = tagId;
        this.noticeId = noticeId;
    }

}
