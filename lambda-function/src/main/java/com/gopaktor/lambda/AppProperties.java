package com.gopaktor.lambda;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

/**
 * @author Mikhail Tyamin <a href="mailto:mikhail.tiamine@gmail.com>mikhail.tiamine@gmail.com</a>
 */
@Data
@AllArgsConstructor
public class AppProperties {
    private static final String BASE_URL = "http://neo4j-XX.dev.gopaktor.com";

    private String slackBotToken;
    private String slackChannel;
    private String countries;

    public Map<String, String> getUrlMapping() {
        String[] arr = countries.split(",");
        return Stream.of(arr).collect(toMap(each -> each, each -> BASE_URL.replaceFirst("XX", each)));
    }
}
