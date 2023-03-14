package com.mentoring.mentoringprj.controller;

import jakarta.websocket.server.PathParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping ("/own")
public class OwnController {
    //http://localhost:8080/own/hello?firstName=Joe&secondName=
    @RequestMapping(method = RequestMethod.GET, path = "/helloPerson/{fName}/{sName}")
    public ResponseEntity<String> helloPerson(@PathVariable("fName") String firstName,
                                        @PathVariable(name="sName") String secondName){ //Injection of var name from path to String firstName
        return ResponseEntity.of(Optional.of("hello "+firstName+ " "+secondName));

    }

    @RequestMapping(method = RequestMethod.GET, path = "/hello")
    public ResponseEntity<String> hello(@RequestParam(name="firstName", required=false) String firstName,
                                        @RequestParam(name="secondName", required=true) String secondName){ //requestPAram name is not needed
        return ResponseEntity.of(Optional.of("hello you "+firstName+" "+secondName ));

    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<String> home(){
        return ResponseEntity.of(Optional.of("welcome home"));

    }

}
