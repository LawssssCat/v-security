package cn.vshop.security.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author alan smith
 * @version 1.0
 * @date 2020/3/31 23:24
 */
@Slf4j
@RequestMapping("/filter")
@RestController
public class FilterController {
    @RequestMapping
    public String ok(String id) {
        log.info("ok ! id={}"  , id);
        return "filter ok !";
    }
}
