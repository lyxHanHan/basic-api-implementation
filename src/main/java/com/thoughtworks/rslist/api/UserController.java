package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.exception.Error;
import com.thoughtworks.rslist.exception.RsEventNotValidException;
import com.thoughtworks.rslist.po.UserPO;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class UserController{
    List<User> userList = new ArrayList<>();
    @Autowired
    UserRepository userRepository;
    @Autowired
    RsEventRepository rsEventRepository;

    @PostMapping("/user")
    public void register(@RequestBody @Valid User user) {
        UserPO userPO = new UserPO();
        userPO.setUserName(user.getName());
        userPO.setGender(user.getGender());
        userPO.setAge(user.getAge());
        userPO.setEmail(user.getEmail());
        userPO.setPhone(user.getPhone());
        userPO.setVoteNum(user.getVoteNum());
        userRepository.save(userPO);
    }

    @GetMapping("/user/{index}")
    public ResponseEntity getOneIndexUser(@RequestBody @Valid int index){
        userRepository.deleteById(index);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/user")
    public List<User> getUserList(){
        return userList;
    }

    @DeleteMapping("/user/{index}")
    public ResponseEntity deleteUser(@RequestBody @Valid int index){
        Optional <UserPO>  user = userRepository.findById(index);
        userRepository.deleteById(index);
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler({RsEventNotValidException.class, MethodArgumentNotValidException.class})
    public ResponseEntity rsExceptionHandler (Exception e){
        String errorMessage;
        if(e instanceof  MethodArgumentNotValidException){
            errorMessage = "invalid user";
        }else {
            errorMessage = e.getMessage();
        }
        Error error = new Error();
        error.setError(errorMessage);
        return ResponseEntity.badRequest().body(error);
    }

}