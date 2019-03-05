package com.tjufe.graduate.lbsserver.Bean;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "activity")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer activityId;

    private String title;

    private String content;

    private int adminId;

    private String accessor;

    private String publisher;

    private String picturePath;

    private Integer buildingId;

    private double longitude;

    private double latitude;

    private Date startTime;

    private Date endTime;

    private Date createTime;

    private int status;

    @Transient
    private List<ActivityImage> imageList;

    @Transient
    private List<Tag> tagList;

}
