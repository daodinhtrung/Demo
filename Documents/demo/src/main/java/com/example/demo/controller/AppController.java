package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.FtpBatchService;

@RestController
public class AppController {

    @Autowired
    private FtpBatchService ftpBatchService;

    @GetMapping("/run-sync")
    public String startSync() {
        return ftpBatchService.syncLargeData();
    }
}
