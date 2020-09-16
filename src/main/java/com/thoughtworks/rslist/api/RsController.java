package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.RsEvent;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class RsController {
  private List<RsEvent> rsList = initRsEventList();
  private List<RsEvent> initRsEventList(){
    List<RsEvent> rsEventList = new ArrayList<>();
    User user = new User("lyx","female",18,"1@2.com","12222222222");
    rsEventList.add(new RsEvent("第一条事件","无标签",user));
    rsEventList.add(new RsEvent("第二条事件","无标签",user));
    rsEventList.add(new RsEvent("第三条事件","无标签",user));
    return rsEventList;
  }

  @GetMapping("/rs/{index}")
  public  ResponseEntity getOneRsEvent(@PathVariable int index){
    return ResponseEntity.ok(rsList.get(index -1));
  }

  @GetMapping("/rs/list")
  public ResponseEntity getRsEventBetween(@RequestParam(required = false) Integer start,@RequestParam(required = false) Integer end){
    if(start == null || end == null){
      return ResponseEntity.ok(rsList);
    }
    return  ResponseEntity.ok(rsList);
  }

  @PostMapping("/rs/event")
  public ResponseEntity<Object> addRsEvent(@RequestBody String rsEvent) throws JsonProcessingException {
    ObjectMapper objectMapper = new ObjectMapper();
    RsEvent event = objectMapper.readValue(rsEvent,RsEvent.class);
    rsList.add(event);

    return ResponseEntity.created(null).build();
  }

  @DeleteMapping ("/rs/{index}")
  public ResponseEntity deleteRsEvent(@PathVariable int index)  {
    rsList.remove(index-1);
    return ResponseEntity.created(null).build();

  }

  @PatchMapping("/rs/{index}")
  public ResponseEntity modifyRsEvent(@PathVariable int index,@RequestBody RsEvent rsEvent )  {
    String eventName = rsEvent.getEventName();
    String keyWord = rsEvent.getKeyWord();
    rsList.get(index - 1).setEventName(eventName);
    rsList.get(index - 1).setKeyWord(keyWord);
    return ResponseEntity.created(null).build();
  }
}
