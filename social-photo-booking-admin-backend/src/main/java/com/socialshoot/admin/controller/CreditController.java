package com.socialshoot.admin.controller;

import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/credit")
public class CreditController {

    @GetMapping("/rules")
    public Map<String, Object> getCreditRules() {
        Map<String, Object> rules = new HashMap<>();
        rules.put("initialScore", 100);
        rules.put("onTimeScore", 5);
        rules.put("completeScore", 10);
        rules.put("lateScore", -10);
        rules.put("cancelScore", -15);

        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "success");
        response.put("data", rules);
        return response;
    }

    @PostMapping("/rules")
    public Map<String, Object> saveCreditRules(@RequestBody Map<String, Object> rules) {
        // 这里可以实现保存信用规则的逻辑
        // 目前只是返回成功
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "success");
        return response;
    }
}
