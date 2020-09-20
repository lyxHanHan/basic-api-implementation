package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.Vote;
import com.thoughtworks.rslist.po.RsEventPO;
import com.thoughtworks.rslist.po.UserPO;
import com.thoughtworks.rslist.po.VotePO;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRespository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.SQLException;
import java.time.LocalDateTime;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class VoteControllerTest {
    private final RsEventRepository rsEventRepository;
    private final UserRepository userRepository;
    private final MockMvc mockMvc;
    private final VoteRespository voteRespository;
    UserPO userPO;
    RsEventPO rsEventPO;

    public VoteControllerTest(RsEventRepository rsEventRepository, UserRepository userRepository, MockMvc mockMvc, VoteRespository voteRespository) throws SQLException {
        this.rsEventRepository = rsEventRepository;
        this.userRepository = userRepository;
        this.mockMvc = mockMvc;
        this.voteRespository = voteRespository;
    }

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
                .userP0(userPO).rsEventPO(rsEventPO).build();
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
                .userP0(userPO).rsEventPO(rsEventPO).build();
        voteRespository.save(votePO);
        mockMvc.perform(post("/rs/vote/{rsEventId}")
                .param("rsEventId",String.valueOf((rsEventPO.getId()))))
                .andExpect(jsonPath("$",hasSize(1)))
                .andExpect(status().isBadRequest());
    }

@Test
    public void should_get_vote_Record() throws Exception{
    for (int i = 0;i < 8;i++){
        VotePO votePO = VotePO.builder().voteNum(i+1).voteTime(LocalDateTime.now())
                .userP0(userPO).rsEventPO(rsEventPO).build();
        voteRespository.save(votePO);
    }
    mockMvc.perform(post("/voteRecord")
            .param("rsEventId",String.valueOf((rsEventPO.getId())))
            .param("userId",String.valueOf((userPO.getId())))
            .param("pageIndex","1"))
            .andExpect(jsonPath("$",hasSize(5)))
            .andExpect(jsonPath("$[0].userId",is(userPO.getId())))
            .andExpect(jsonPath("$[0].rsEventId",is(rsEventPO.getId())))
            .andExpect(jsonPath("$[0].voteNum",is(1)))
            .andExpect(jsonPath("$[1].voteNum",is(2)))
            .andExpect(jsonPath("$[2].voteNum",is(3)))
            .andExpect(jsonPath("$[3].voteNum",is(4)))
            .andExpect(jsonPath("$[5].voteNum",is(5)));
    mockMvc.perform(post("/voteRecord")
            .param("rsEventId",String.valueOf((rsEventPO.getId())))
            .param("userId",String.valueOf((userPO.getId())))
            .param("pageIndex","2"))
            .andExpect(jsonPath("$",hasSize(3)))
            .andExpect(jsonPath("$[0].userId",is(userPO.getId())))
            .andExpect(jsonPath("$[0].rsEventId",is(rsEventPO.getId())))
            .andExpect(jsonPath("$[0].voteNum",is(6)))
            .andExpect(jsonPath("$[1].voteNum",is(7)))
            .andExpect(jsonPath("$[2].voteNum",is(8)));
    }
}
