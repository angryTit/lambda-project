package com.gopaktor.lambda.service;

import com.gopaktor.lambda.AppProperties;
import com.gopaktor.neo4j.service.CounterService;
import com.gopaktor.slack.CounterReportService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @author Mikhail Tyamin <a href="mailto:mikhail.tiamine@gmail.com>mikhail.tiamine@gmail.com</a>
 */
@AllArgsConstructor
public class Neo4jCounterService implements CronService {
    private final Logger log = LoggerFactory.getLogger(getClass());

    private final CounterService counterService;
    private final CounterReportService reportService;
    private final AppProperties properties;


    @Override
    public void execute() {
        properties.getUrlMapping().entrySet().stream().forEach(each -> {
            String country = each.getKey();
            Map<String, Object> counter = counterService.fetchCounter(each.getValue());
            reportService.postCounter(country, counter, properties.getSlackChannel());
        });
        log.info("Method was successfully executed");
    }
}
