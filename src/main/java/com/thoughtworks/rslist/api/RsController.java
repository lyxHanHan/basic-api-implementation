package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.exception.Error;
import com.thoughtworks.rslist.exception.RsEventNotValidException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.thoughtworks.rslist.domain.RsEvent;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.awt.*;
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
    if (index <= 0 || index > rsList.size()){
      throw new RsEventNotValidException("invalid index");
    }
    return ResponseEntity.ok(rsList.get(index -1));
  }

  @GetMapping("/rs/list")
  public ResponseEntity getRsEventBetween(@RequestParam(required = false) Integer start,@RequestParam(required = false) Integer end){
    if(start == null || end == null){
      return ResponseEntity.ok(rsList);
    }
    if((end - start +1) >rsList.size() || start > rsList.size()){
      throw new RsEventNotValidException("invalid request param");
    }
    return  ResponseEntity.ok(rsList.subList(start - 1,end));
  }

  @PostMapping("/rs/event")
  public ResponseEntity<Object> addRsEvent(@RequestBody @Valid RsEvent rsEvent) throws JsonProcessingException {
    rsList.add(rsEvent);
    return ResponseEntity.created(null).build();
  }

  @DeleteMapping ("/rs/{index}")
  public ResponseEntity deleteRsEvent(@PathVariable int index)  {
    rsList.remove(index-1);
    return ResponseEntity.ok(rsList.remove(index-1));

  }

  @PatchMapping("/rs/{index}")
  public ResponseEntity modifyRsEvent(@PathVariable int index,@RequestBody RsEvent rsEvent )  {
    String eventName = rsEvent.getEventName();
    String keyWord = rsEvent.getKeyWord();
    rsList.get(index - 1).setEventName(eventName);
    rsList.get(index - 1).setKeyWord(keyWord);
    return ResponseEntity.ok(null);
  }

  @ExceptionHandler({RsEventNotValidException.class, MethodArgumentNotValidException.class})
  public ResponseEntity rsExceptionHandler (Exception e){
    String errorMessage;
    if(e instanceof  MethodArgumentNotValidException){
      errorMessage = "invalid param";
    }else {
      errorMessage = e.getMessage();
    }
    Error error = new Error();
    error.setError(errorMessage);
    return ResponseEntity.badRequest().body(error);
  }
}
