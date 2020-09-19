package com.thoughtworks.rslist.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Valid
public class Vote {
    private int userId;
    private int rsEventId;
    private int voteNum;
    private String voteTime;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getRsEventId() {
        return rsEventId;
    }

    public void setRsEventId(int rsEventId) {
        this.rsEventId = rsEventId;
    }

    public int getVoteNum() {
        return voteNum;
    }

    public void setVoteNum(int voteNum) {
        this.voteNum = voteNum;
    }

    public String getVoteTime() {
        return voteTime;
    }

    public void setVoteTime(String voteTime) {
        this.voteTime = voteTime;
    }
}
