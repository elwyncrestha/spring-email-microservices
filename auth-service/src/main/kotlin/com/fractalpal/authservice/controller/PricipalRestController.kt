package com.fractalpal.authservice.controller

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@RestController
class PricipalRestController{

    @RequestMapping("/user")
    fun principal(principal: Principal) : Principal = principal
}