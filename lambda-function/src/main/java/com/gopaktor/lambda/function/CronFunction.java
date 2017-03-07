package com.gopaktor.lambda.function;

import com.amazonaws.services.lambda.runtime.Context;
import com.gopaktor.lambda.AbstractLambdaFunction;
import com.gopaktor.lambda.AppProperties;
import com.gopaktor.lambda.service.CronService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mikhail Tyamin <a href="mailto:mikhail.tiamine@gmail.com>mikhail.tiamine@gmail.com</a>
 */
public class CronFunction extends AbstractLambdaFunction {
    private final Logger log = LoggerFactory.getLogger(getClass());

    public void handle(Object request, Context context) {
        log.info("AppProperties : {}", appContext.getBean(AppProperties.class));
        String functionName = context.getFunctionName();
        CronService service = appContext.getBean(functionName, CronService.class);
        service.execute();
        log.info("Successfully executed function : {}", functionName);
    }
}
