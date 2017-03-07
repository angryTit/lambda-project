package com.gopaktor.neo4j.service;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

/**
 * @author Mikhail Tyamin <a href="mailto:mikhail.tiamine@gmail.com>mikhail.tiamine@gmail.com</a>
 */
@AllArgsConstructor
public class CounterService {
    private final Logger log = LoggerFactory.getLogger(getClass());

    private final RestTemplate restTemplate;

    private Map<String, Object> translate(Map<String, Object> source) {
        Map<String, Object> result = new HashMap<>();
        for (String key : source.keySet()) {
            if (Objects.equals("UPDATE_USER", key)) {
                result.put("Update commands", source.get(key));
            } else if (Objects.equals("DELETE_USER", key)) {
                result.put("Delete user commands", source.get(key));
            } else if (Objects.equals("UPDATE_RELATIONSHIPS", key)) {
                result.put("Create relationship commands", source.get(key));
            } else if (Objects.equals("counter_id", key)) {
                result.put("Affected date", source.get(key));
            } else {
                result.put(key, source.get(key));
            }
        }

        log.info("Successfully transformed source : {} into target : {}", source, result);
        return result;
    }

    public Map<String, Object> fetchCounter(String baseUri) {
        try {
            String finalUrl = baseUri + "/v1/counter";
            log.info("Make http request to url : {}", finalUrl);
            ResponseEntity<Map> resp = restTemplate.getForEntity(finalUrl, Map.class);

            if (resp.getStatusCode().value() == 200) {
                log.info("Successfully fetch counter from : {}, counter : {}", baseUri, resp.getBody());
                return translate((Map<String, Object>) resp.getBody());
            } else {
                log.error("Error response from counter, error code : {}, reason : {}",
                        resp.getStatusCode().value(), resp.getStatusCode().getReasonPhrase());
            }
        } catch (Exception e) {
            log.error("Error while ask counter", e);
        }
        return Collections.emptyMap();
    }


    public List<Map<String, Object>> fetchAll(String baseUri) {
        try {
            ResponseEntity<List> resp = restTemplate.getForEntity(baseUri + "/v1/counters", List.class);

            if (resp.getStatusCode().value() == 200) {
                log.info("Successfully fetch counters from : {}, counter : {}", baseUri, resp.getBody());
                List<Map<String, Object>> result = (List<Map<String, Object>>) resp.getBody();
                return result.stream().map(each -> translate(each)).collect(toList());
            } else {
                log.error("Error response from counters, error code : {}, reason : {}",
                        resp.getStatusCode().value(), resp.getStatusCode().getReasonPhrase());
            }
        } catch (Exception e) {
            log.error("Error while ask counter", e);
        }
        return Collections.emptyList();
    }
}
