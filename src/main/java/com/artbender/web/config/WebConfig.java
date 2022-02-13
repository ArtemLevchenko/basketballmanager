package com.artbender.web.config;

import com.artbender.service.ServiceConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan("com.artbender.web")
@Import(value = ServiceConfig.class)
public class WebConfig {
}
