package com.example.userservice.controller;

import com.ctc.wstx.shaded.msv_core.reader.GrammarReader;
import com.example.userservice.dto.UserDto;
import com.example.userservice.repository.UserEntity;
import com.example.userservice.service.UserService;
import com.example.userservice.vo.Greeting;
import com.example.userservice.vo.RequestUser;
import com.example.userservice.vo.ResponseOrder;
import com.example.userservice.vo.ResponseUser;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/")
public class UserController {

    private Environment env;
    private Greeting greeting;
    private UserService userService;

    @Autowired
    public UserController(Environment env, UserService userService,Greeting greeting){
        this.env = env;
        this.userService = userService;
        this.greeting = greeting;
    }

    @GetMapping("/health_check")
    public String Status(){

        return "it's working. on Port "+env.getProperty("local.server.port") +
                "\n token Time: "+env.getProperty("token.expiration_time")+
                "\n token secret "+env.getProperty("token.secret");
    }


    @GetMapping("/welcome")
    public String Welcome(){
        return env.getProperty("greeting.message");
    }

    @GetMapping("/welcome2")
    public String Welcome2(){
        return greeting.getMessage();
    }


    @PostMapping("/users")
    public ResponseEntity createUser(@RequestBody RequestUser params){

        log.info("params"+params.toString());
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserDto userDto = mapper.map(params,UserDto.class);
        userService.createUser(userDto);

        ResponseUser responseUser = mapper.map(userDto,ResponseUser.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseUser);

    }
    @GetMapping("/users")
    public ResponseEntity<List<ResponseUser>> getUsers(){
        Iterable<UserEntity> userList = userService.getUserByAll();

        List<ResponseUser> result = new ArrayList<>();

        userList.forEach(v->{
            result.add(new ModelMapper().map(v,ResponseUser.class));
        });

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<ResponseUser> getUser(@PathVariable("userId") String userId){
        UserDto userDto = userService.getUserByUserId(userId);
        ResponseUser returnValue = new ModelMapper().map(userDto,ResponseUser.class);

        return ResponseEntity.status(HttpStatus.OK).body(returnValue);
    }
}
