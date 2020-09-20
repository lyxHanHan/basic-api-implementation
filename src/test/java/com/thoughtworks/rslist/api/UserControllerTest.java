package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.User;

import com.thoughtworks.rslist.po.RsEventPO;
import com.thoughtworks.rslist.po.UserPO;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.SQLException;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
    private final RsEventRepository rsEventRepository;
    private final UserRepository userRepository;
    private final MockMvc mockMvc;
    public UserControllerTest(RsEventRepository rsEventRepository,UserRepository userRepository,MockMvc mockMvc) throws SQLException {
        this.rsEventRepository = rsEventRepository;
        this.userRepository = userRepository;
        this.mockMvc = mockMvc;
    }


    @BeforeEach
    public void setUp (){
        userRepository.deleteAll();
        rsEventRepository.deleteAll();
    }
    @Test
    public void should_register_user() throws Exception {
        User user = new User("lyx","female",18,"1@2.com","12222222222");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user")
                .content(jsonString)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        List<UserPO> all = userRepository.findAll();
        assertEquals(1,all.size());
        assertEquals("lyx",all.get(0).getUserName());
        assertEquals("female",all.get(0).getGender());
    }


    @Test
    public void should_return_user_list() throws Exception {
        UserPO userPO = UserPO.builder().userName("111").voteNum(10).phone("18888888881")
                .email("1@3.com").gender("female").age(13).build();
        userRepository.save(userPO);
        UserPO userP1 = UserPO.builder().userName("222").voteNum(10).phone("18888888882")
                .email("1@3.com").gender("female").age(13).build();
        userRepository.save(userP1);

        mockMvc.perform(post("/user/list")).andExpect(status().isOk());
        List<UserPO> allUser = userRepository.findAll();

        assertEquals(3,allUser.size());
        assertEquals("111",allUser.get(0).getUserName());
    }

    @Test
    public void should_get_userInfor() throws Exception {
        User user = new User("lyx","female",18,"1@2.com","12222222222");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/user")
                .content(jsonString)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        List<UserPO> all = userRepository.findAll();

        mockMvc.perform(get("/user/1")
                .content(jsonString)
                .contentType(MediaType.APPLICATION_JSON));

        assertEquals(1,all.size());
        assertEquals("lyx",all.get(0).getUserName());
        assertEquals("female",all.get(0).getGender());
    }

    @Test
    public void should_not_get_user_Infor_for_not_exist_index() throws Exception {
        User user = new User("lyx","female",18,"1@2.com","12222222222");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/user")
                .content(jsonString)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        List<UserPO> all = userRepository.findAll();

        mockMvc.perform(get("/user/5")
                .content(jsonString)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void should_delete_user() throws Exception {
        UserPO userPO = UserPO.builder().userName("xiaowang").voteNum(10).phone("18888888888")
                .email("1@3.com").gender("female").age(13).build();
        userRepository.save(userPO);
        RsEventPO rsEventPO = RsEventPO.builder().keyWord("经济").eventName("猪肉涨价了").userPO(userPO).build();
        rsEventRepository.save(rsEventPO);
        mockMvc.perform(delete("/user/1")).andExpect(status().isOk());
        assertEquals(0,rsEventRepository.findAll().size());
        assertEquals(0,userRepository.findAll().size());
    }

    @Test
    public void should_not_delete_user_for_false_index() throws Exception {
        UserPO userPO = UserPO.builder().userName("xiaowang").voteNum(10).phone("18888888888")
                .email("1@3.com").gender("female").age(13).build();
        userRepository.save(userPO);
        RsEventPO rsEventPO = RsEventPO.builder().keyWord("经济").eventName("猪肉涨价了").userPO(userPO).build();
        rsEventRepository.save(rsEventPO);
        mockMvc.perform(delete("/user/4")).andExpect(status().isBadRequest());
        assertEquals(1,rsEventRepository.findAll().size());
        assertEquals(0,userRepository.findAll().size());
    }
}