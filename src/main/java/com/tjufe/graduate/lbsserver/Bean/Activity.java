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
    Integer activityId;

    String title;

    String content;

    int adminId;

    String accessor;

    String publisher;

    String picturePath;

    Integer buildingId;

    double longitude;

    double latitude;

    Date startTime;

    Date endTime;

    Date createTime;

    int status;

    @Transient
    List<ActivityImage> imageList;

    @Transient
    List<Tag> tagList;

}
