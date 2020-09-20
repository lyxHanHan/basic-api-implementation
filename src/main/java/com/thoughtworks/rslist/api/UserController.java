package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.exception.Error;
import com.thoughtworks.rslist.exception.RsEventNotValidException;
import com.thoughtworks.rslist.po.UserPO;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
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
    public void addUser(@RequestBody @Valid User user) {
        UserPO userPO = new UserPO();
        userPO.setUserName(user.getName());
        userPO.setGender(user.getGender());
        userPO.setAge(user.getAge());
        userPO.setEmail(user.getEmail());
        userPO.setPhone(user.getPhone());
        userPO.setVoteNum(user.getVoteNum());
        userRepository.save(userPO);
    }

<<<<<<< HEAD
    @DeleteMapping("/user/{id}")
    public ResponseEntity deleteUser(@RequestBody @Valid int id){
        userRepository.deleteById(id);
        return ResponseEntity.ok().build();
=======
    @GetMapping("/user/{index}")
    public ResponseEntity getOneIndexUser(@RequestBody @Valid int index){
        Optional <UserPO>  user = userRepository.findById(index);
        if(user.get()!= null){
            return ResponseEntity.ok(user.get());
        }
        else{
            return ResponseEntity.badRequest().build();
        }
>>>>>>> 38119634b157c0c1dce345e8d3ad822b149ec472
    }

    @GetMapping("/user")
    public List<User> getUserList(){
        return userList;
    }

    @DeleteMapping("/user/{index}")
    public ResponseEntity deleteUser(@RequestBody @Valid int index){
        Optional <UserPO>  user = userRepository.findById(index);
        if(user.isPresent()){
            userRepository.deleteById(index);
            return ResponseEntity.ok().build();
        }
        else{
            return ResponseEntity.badRequest().build();
        }
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