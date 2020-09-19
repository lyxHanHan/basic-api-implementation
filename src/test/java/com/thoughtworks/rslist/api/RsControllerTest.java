package com.thoughtworks.rslist.api;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.po.RsEventPO;
import com.thoughtworks.rslist.po.UserPO;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RsControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RsEventRepository rsEventRepository;
    @Autowired
    RsEventPO rsEventPO;
    @Autowired
    UserPO userPO;

    @BeforeEach
    public void setUp (){
        userRepository.deleteAll();
        rsEventRepository.deleteAll();
    }

    @Test
    @Order(1)
    public void should_get_rs_event_list() throws Exception{
        rsEventPO = RsEventPO.builder().eventName("第一条事件").keyWord("无标签").build();
        rsEventPO = rsEventRepository.save(rsEventPO);
        rsEventPO = RsEventPO.builder().eventName("第二条事件").keyWord("无标签").build();
        rsEventPO = rsEventRepository.save(rsEventPO);
        rsEventPO = RsEventPO.builder().eventName("第三条事件").keyWord("无标签").build();
        rsEventPO = rsEventRepository.save(rsEventPO);

        List<RsEventPO> rsList = rsEventRepository.findAll();
        mockMvc.perform(get("/rs/list")).andExpect(status().isOk());

        assertEquals(3,rsList.size());
        assertEquals("第一条事件",rsList.get(0).getEventName());
        assertEquals("无标签",rsList.get(1).getKeyWord());
    }

    @Test
    @Order(2)
    public void should_get_one_rs_event() throws Exception {
        rsEventPO = RsEventPO.builder().eventName("第一条事件").keyWord("无标签").build();
        rsEventPO = rsEventRepository.save(rsEventPO);
        rsEventPO = RsEventPO.builder().eventName("第二条事件").keyWord("无标签").build();
        rsEventPO = rsEventRepository.save(rsEventPO);
        rsEventPO = RsEventPO.builder().eventName("第三条事件").keyWord("无标签").build();
        rsEventPO = rsEventRepository.save(rsEventPO);

        mockMvc.perform(get("/rs/1")).andExpect(status().isOk());
        List<RsEventPO> rsList = rsEventRepository.findAll();
        assertEquals(1, rsList.size());
        assertEquals("第二条事件", rsList.get(0).getKeyWord());
    }

    @Test
    @Order(3)
    public void should_get_rs_event_between() throws Exception{
        rsEventPO = RsEventPO.builder().eventName("第一条事件").keyWord("无标签").build();
        rsEventPO = rsEventRepository.save(rsEventPO);
        rsEventPO = RsEventPO.builder().eventName("第二条事件").keyWord("无标签").build();
        rsEventPO = rsEventRepository.save(rsEventPO);
        rsEventPO = RsEventPO.builder().eventName("第三条事件").keyWord("无标签").build();
        rsEventPO = rsEventRepository.save(rsEventPO);

        List<RsEventPO> rsList = rsEventRepository.findAll();
        mockMvc.perform(get("/rs/list?start=1&end=2")).andExpect(status().isOk());

        assertEquals(2,rsList.size());
        assertEquals("第一条事件",rsList.get(0).getEventName());
        assertEquals("无标签",rsList.get(1).getKeyWord());
    }
    @Test
    @Order(4)
    public void should_add_rs_event_when_user_exist() throws Exception{
        UserPO savedUser = userRepository.save(UserPO.builder().userName("xiaoli").age(13).phone("18888888888")
                .email("a@3.com").gender("female").voteNum(10).build());
        String jsonString = "{\"eventName\":\"房价终于降了\",\"keyWord\":\"经济\",\"userId\":" + savedUser.getId() + "}";
        mockMvc.perform(post("/rs/event").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        List<RsEventPO> all = rsEventRepository.findAll();
        assertNotNull(all);
        assertEquals(1,all.size());
        assertEquals("房价终于降了",all.get(0).getEventName());
        assertEquals("经济",all.get(0).getKeyWord());
        assertEquals(savedUser.getId(),all.get(0).getUserPO().getId());

    }
    @Test
    @Order(5)
    public void should_not_add_rs_event_when_user_not_exist() throws Exception{
        String jsonString = "{\"eventName\":\"房价终于降了\",\"keyWord\":\"经济\",\"userId\":12}";
        mockMvc.perform(post("/rs/event").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
    @Test
    @Order(6)
    public void should_modify_rs_event_eventName_and_keyWord() throws Exception {
        UserPO userPO = UserPO.builder().userName("xiaowang").voteNum(10).phone("18888888888")
                .email("1@3.com").gender("female").age(13).build();
        userRepository.save(userPO);
        RsEventPO rsEventPO = RsEventPO.builder().keyWord("经济").eventName("猪肉涨价了").userPO(userPO).build();
        rsEventRepository.save(rsEventPO);
        List<RsEventPO> rsEvent = rsEventRepository.findAll();

        String jsonString1 = "{\"eventName\":\"房价大跌了\",\"keyWord\":\"民生\"}";
        mockMvc.perform(patch("/rs/1").contentType(jsonString1))
                .andExpect(status().isOk());
        assertEquals("猪肉涨价了",rsEvent.get(0).getEventName());
        assertEquals("民生",rsEvent.get(0).getKeyWord());
    }

    @Test
    @Order(7)
    public void should_delete_rs_event() throws Exception {
        rsEventPO = RsEventPO.builder().eventName("第一条事件").keyWord("无标签").build();
        rsEventPO = rsEventRepository.save(rsEventPO);
        rsEventPO = RsEventPO.builder().eventName("第二条事件").keyWord("无标签").build();
        rsEventPO = rsEventRepository.save(rsEventPO);


        List<RsEventPO> rsEvent = rsEventRepository.findAll();
        mockMvc.perform(delete("/rs/1")).andExpect(status().isOk());
        assertEquals(1,rsEvent.size());
    }


    @Test
    @Order(8)
    public void should_only_update_rsEvent_keyword() throws Exception {
        UserPO userPO = UserPO.builder().userName("xiaowang").voteNum(10).phone("18888888888")
                .email("1@3.com").gender("female").age(13).build();
        userRepository.save(userPO);
        RsEventPO rsEventPO = RsEventPO.builder().keyWord("经济").eventName("猪肉涨价了").userPO(userPO).build();
        rsEventRepository.save(rsEventPO);
        List<RsEventPO> all = rsEventRepository.findAll();

        String jsonString1 = "{\"keyWord\":\"民生\",\"userId\":" + userPO.getId() + "}";
        mockMvc.perform(patch("/rs/event/1").contentType(jsonString1)).andExpect(status().isOk());
        assertEquals("猪肉涨价了",all.get(0).getEventName());
        assertEquals("民生",all.get(0).getKeyWord());
    }

    @Test
    @Order(9)
    public void should_only_update_rsEvent_eventName() throws Exception {
        UserPO userPO = UserPO.builder().userName("xiaowang").voteNum(10).phone("18888888888")
                .email("1@3.com").gender("female").age(13).build();
        userRepository.save(userPO);
        RsEventPO rsEventPO = RsEventPO.builder().keyWord("经济").eventName("猪肉涨价了").userPO(userPO).build();
        rsEventRepository.save(rsEventPO);
        List<RsEventPO> all = rsEventRepository.findAll();

        String jsonString1 = "{\"eventName\":\"房价大跌了\",\"keyWord\":\"经济\",\"userId\":" + userPO.getId() + "}";
        mockMvc.perform(patch("/rs/event/1").contentType(jsonString1)).andExpect(status().isOk());
        assertEquals("房价大跌了",all.get(0).getEventName());
        assertEquals("经济",all.get(0).getKeyWord());
    }
}



