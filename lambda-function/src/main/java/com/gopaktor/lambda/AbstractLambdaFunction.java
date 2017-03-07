package com.gopaktor.lambda;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author Mikhail Tyamin <a href="mailto:mikhail.tiamine@gmail.com>mikhail.tiamine@gmail.com</a>
 */
public abstract class AbstractLambdaFunction {

    protected final ApplicationContext appContext =
            new AnnotationConfigApplicationContext(com.gopaktor.lambda.AppConfig.class);

}
