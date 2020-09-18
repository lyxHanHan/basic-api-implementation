package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.exception.Error;
import com.thoughtworks.rslist.exception.RsEventNotValidException;
import com.thoughtworks.rslist.po.RsEventPO;
import com.thoughtworks.rslist.po.UserPO;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class RsController {
  private List<RsEvent> rsList = initRsEventList();
  @Autowired
  RsEventRepository rsEventRepository;
  @Autowired
  UserRepository userRepository;

  public RsController() throws SQLException {
  }

  private List<RsEvent> initRsEventList() throws SQLException {
    //createTableByJdbc();
    List<RsEvent> rsEventList = new ArrayList<>();
    User user = new User("lyx","female",18,"1@2.com","12222222222");
    rsEventList.add(new RsEvent("第一条事件","无标签",1));
    rsEventList.add(new RsEvent("第二条事件","无标签",1));
    rsEventList.add(new RsEvent("第三条事件","无标签",1));
    return rsEventList;
  }

  private static void createTableByJdbc() throws SQLException {
    Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/rsSystem",
            "root","123456");
    DatabaseMetaData metaData = connection.getMetaData();
    ResultSet resultSet = metaData.getTables(null,null,"rsEvent",null);
    if(!resultSet.next()){
      String createTableSql = "create table rsEvent(eventName varchar(200) not null,keyword varchar(100) not null)";
      Statement statement = connection.createStatement();
      statement.execute(createTableSql);
    }
    connection.close();
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
    Optional<UserPO> userPO = userRepository.findById((rsEvent.getUserId()));
    if(!userPO.isPresent()){
      return ResponseEntity.badRequest().build();
    }
    RsEventPO eventPO = RsEventPO.builder().keyWord(rsEvent.getKeyWord()).eventName(rsEvent.getEventName()).userPO(userPO.get()).build();
    rsEventRepository.save(eventPO);
    return ResponseEntity.created(null).build();
  }

  @DeleteMapping ("/rs/{index}")
  public ResponseEntity deleteRsEvent(@PathVariable int index)  {
    Optional<UserPO> user = userRepository.findById(index);
    if(user.isPresent()){
      userRepository.deleteById(index);
      return ResponseEntity.ok().build();
    }
    else{
      return ResponseEntity.badRequest().build();
    }

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
