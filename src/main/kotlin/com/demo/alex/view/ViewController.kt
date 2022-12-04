package com.demo.alex.view

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class ViewController {

    @GetMapping("/comments")
    fun commentPage() : String {
        return "comment"
    }
}