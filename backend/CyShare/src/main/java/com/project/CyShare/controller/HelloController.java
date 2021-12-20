package com.project.CyShare.controller;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;



/**
 * This Controller was used to test endpoints. Just a quick way to make sure the backend is fired up.
 * Will not be used in final project.
 * @author Hugo Alvarez Valdivia
 */

@Api(tags = "HelloController", value = "HelloController", description = "This controller is used to test endpoints. Quick way to check" +
        " if backend is fired up or not.")
@RestController
public class HelloController {

        @GetMapping("/")
        public String hello()
        {
            return "Oh hi Mark";
        }

        @GetMapping("/test1")
        public String test1()
        {
            return "This is a quick test";
        }

        @GetMapping("/test2")
        public String test2()
        {
            return "This is quick test of alternative mapping";
        }

}
