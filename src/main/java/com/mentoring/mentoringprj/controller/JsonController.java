package com.mentoring.mentoringprj.controller;

import com.mentoring.mentoringprj.domain.Message;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/json")
public class JsonController {

    @RequestMapping(method = RequestMethod.GET, path = "/hello", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Message> hello(@RequestParam(name="firstName") String firstName,
                                        @RequestParam(name="secondName") String secondName){
        Message message = new Message("hello "+firstName+" "+secondName);
        return ResponseEntity.of(Optional.of(message));

    }
}
