package com.nags.happntech.controllers;

import java.util.Arrays;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class DensityCli implements ApplicationListener<ApplicationReadyEvent> {

    private final ApplicationArguments appArgs;

    public DensityCli(ApplicationArguments appArgs) {
        this.appArgs = appArgs;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        System.out.println("App Args: " + Arrays.asList(appArgs.getSourceArgs()));
    }

}
