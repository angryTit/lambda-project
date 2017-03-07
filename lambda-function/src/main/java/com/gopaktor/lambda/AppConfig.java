package com.gopaktor.lambda;

import allbegray.slack.SlackClientFactory;
import allbegray.slack.webapi.SlackWebApiClient;
import com.gopaktor.lambda.service.CronService;
import com.gopaktor.lambda.service.Neo4jCounterService;
import com.gopaktor.neo4j.service.CounterService;
import com.gopaktor.slack.CounterReportService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @author Mikhail Tyamin <a href="mailto:mikhail.tiamine@gmail.com>mikhail.tiamine@gmail.com</a>
 */
@Configuration
@ComponentScan
public class AppConfig {

    @Value("${SLACK_BOT_TOKEN}")
    private String slackBotToken;

    @Value("${SLACK_CHANNEL_NAME}")
    private String slackChannel;

    @Value("${NEO4J_COUNTRIES}")
    private String countries;

    @Bean
    public AppProperties appProperties() {
        return new AppProperties(slackBotToken, slackChannel, countries);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public SlackWebApiClient slackWebApiClient() {
        return SlackClientFactory.createWebApiClient(appProperties().getSlackBotToken());
    }

    @Bean
    public CounterService counterService() {
        return new CounterService(restTemplate());
    }

    @Bean
    public CounterReportService reportService() {
        return new CounterReportService(slackWebApiClient());
    }

    @Bean("neo4jCounterReport")
    public CronService cronNeo4jService() {
        return new Neo4jCounterService(counterService(), reportService(), appProperties());
    }
}
