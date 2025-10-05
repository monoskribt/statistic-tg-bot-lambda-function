package com.dataart.lambdaparserfunction;

import com.dataart.lambdaparserfunction.props.AWSProps;
import com.dataart.lambdaparserfunction.props.S3Props;
import com.dataart.lambdaparserfunction.props.SQSProps;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(value = {AWSProps.class, S3Props.class, SQSProps.class})
public class LambdaParserFunctionApplication {

    public static void main(String[] args) {
        SpringApplication.run(LambdaParserFunctionApplication.class, args);
    }

}
