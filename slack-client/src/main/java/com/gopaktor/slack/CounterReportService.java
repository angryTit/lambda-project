package com.gopaktor.slack;

import allbegray.slack.SlackTextBuilder;
import allbegray.slack.webapi.SlackWebApiClient;
import allbegray.slack.webapi.method.chats.ChatPostMessageMethod;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static java.util.Comparator.comparing;

/**
 * @author Mikhail Tyamin <a href="mailto:mikhail.tiamine@gmail.com>mikhail.tiamine@gmail.com</a>
 */
@AllArgsConstructor
public class CounterReportService {
    private final Logger log = LoggerFactory.getLogger(getClass());

    private final SlackWebApiClient webApiClient;

    public void postCounter(String country, Map<String, Object> counters, String channelName) {
        final SlackTextBuilder builder = SlackTextBuilder.create();
        builder.bold(country.toUpperCase());
        builder.newline();
        counters.entrySet().stream().sorted(comparing(Map.Entry::getKey)).forEach(each -> {
            builder.italic(each.getKey() + " : " + each.getValue());
            builder.newline();
        });

        String message = builder.build();

        ChatPostMessageMethod chatPostMessageMethod = new ChatPostMessageMethod(channelName, message);
        chatPostMessageMethod.setAs_user(true);
        webApiClient.postMessage(chatPostMessageMethod);
        log.info("Successfully posted message : {}", message);
    }
}
