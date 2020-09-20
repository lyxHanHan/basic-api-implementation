package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;

import com.thoughtworks.rslist.po.RsEventPO;
import com.thoughtworks.rslist.po.UserPO;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
<<<<<<< HEAD
=======
import static org.junit.jupiter.api.Assertions.assertNotNull;
>>>>>>> 38119634b157c0c1dce345e8d3ad822b149ec472
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RsEventRepository rsEventRepository;

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
    public void should_not_get_userInfor_for_not_exist_index() throws Exception {
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
        UserPO savedUser = userRepository.save(UserPO.builder().userName("xiaoli").age(13).phone("18888888888")
                .email("a@3.com").gender("female").voteNum(10).build());
        String jsonString = "{\"eventName\":\"房价终于降了\",\"keyWord\":\"经济\",\"userId\":" + savedUser.getId() + "}";
        List<RsEventPO> all = rsEventRepository.findAll();
        mockMvc.perform(delete("/rs/1").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        assertEquals(0,all.size());
    }

    @Test
    public void name_should_less_than_8() throws Exception{
        User user = new User("lyx111111","female",18,"1@2.com","12222222222");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user")
                .content(jsonString)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void age_should_between_18_and_100() throws Exception{
        User user = new User("lyx","female",118,"1@2.com","12222222222");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user")
                .content(jsonString)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void email_should_right() throws Exception{
        User user = new User("lyx","female",28,".com","12222222222");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user")
                .content(jsonString)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void phone_should_right() throws Exception{
        User user = new User("lyx","female",28,".com","3312222222222");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user")
                .content(jsonString)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void should_throw_user_not_valid_exception() throws Exception{
        User user = new User("lyx11111111111","female",28,".com","12222222222");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user")
                .content(jsonString)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error",is("invalid user")));;
    }
    @Test
    public void should_delete_user() throws Exception{
        UserPO userPO = UserPO.builder().userName("xiaowang").voteNum(10).phone("18888888888")
                .email("1@3.com").gender("female").age(13).build();
        userRepository.save(userPO);
        RsEventPO rsEventPO = RsEventPO.builder().keyWord("经济").eventName("猪肉涨价了").userPO(userPO).build();
        rsEventRepository.save(rsEventPO);
        mockMvc.perform(delete("/user/{id}")).andExpect(status().isOk());
        assertEquals(0,rsEventRepository.findAll().size());
        assertEquals(0,userRepository.findAll().size());
    }
}