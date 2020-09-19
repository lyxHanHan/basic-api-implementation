package com.thoughtworks.rslist.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "vote")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VotePO  {
    @Id
    @GeneratedValue
    private int id;

    private  UserPO userP0;

    private RsEventPO rsEvent;

    private int voteNum;

    private LocalDateTime voteTime;
    private int restNum;

    @ManyToOne
    @Column(name = "RsEventId")
    private RsEventPO rsEventPO;

    @ManyToOne
    @Column(name = "UserId")
    private UserPO userPO;



}
