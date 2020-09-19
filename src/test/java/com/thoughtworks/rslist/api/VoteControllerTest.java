package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.Vote;
import com.thoughtworks.rslist.po.RsEventPO;
import com.thoughtworks.rslist.po.UserPO;
import com.thoughtworks.rslist.po.VotePO;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRespository;
import org.apache.tomcat.jni.Local;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class VoteControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RsEventRepository rsEventRepository;
    @Autowired
    VoteRespository voteRespository;
    UserPO userPO;
    RsEventPO rsEventPO;
    VotePO votePO;
    RsEventPO rsEventPOs;
    VotePO votePOs;
    UserPO userPOs;

@BeforeEach
    public void setUp(){
        userPO = userPO.builder().userName("xiaoli").age(27).gender("female")
                .phone("12222222222").voteNum(3).build();
        userPO=userRepository.save(userPO);

        RsEventPO rsEventPO1 = RsEventPO.builder().eventName("event name 1").keyWord("keyword")
                .voteNum(3).build();
        rsEventPO = rsEventRepository.save(rsEventPO1);
        RsEventPO rsEventPO2 = RsEventPO.builder().eventName("event name 2").keyWord("keyword")
            .voteNum(3).build();
        rsEventPO = rsEventRepository.save(rsEventPO2);
    }

@Test
    public void should_vote_if_vote_number_less_than_user_has() throws Exception{
        VotePO votePO = VotePO.builder().voteNum(5).voteTime(LocalDateTime.now())
                .userP0(userPO).rsEvent(rsEventPO).build();
        voteRespository.save(votePO);
        mockMvc.perform(post("/rs/vote/{rsEventId}")
                .param("rsEventId",String.valueOf((rsEventPO.getId()))))
                .andExpect(jsonPath("$",hasSize(1)))
                .andExpect(jsonPath("$[0].voteNum",is(5)))
                .andExpect(status().isOk());
}
@Test
    public void should_not_vote_if_vote_number_less_than_user_has() throws Exception{
        VotePO votePO = VotePO.builder().voteNum(9).voteTime(LocalDateTime.now())
                .userP0(userPO).rsEvent(rsEventPO).build();
        voteRespository.save(votePO);
        mockMvc.perform(post("/rs/vote/{rsEventId}")
                .param("rsEventId",String.valueOf((rsEventPO.getId()))))
                .andExpect(jsonPath("$",hasSize(1)))
                .andExpect(status().isBadRequest());
    }
}
