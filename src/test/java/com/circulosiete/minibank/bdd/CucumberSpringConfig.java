package com.circulosiete.minibank.bdd;

import com.circulosiete.minibank.TestcontainersConfiguration;
import io.cucumber.junit.platform.engine.Constants;
import io.cucumber.spring.CucumberContextConfiguration;
import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@CucumberContextConfiguration
@Import(TestcontainersConfiguration.class)
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features") // looks under src/test/resources/features
@ConfigurationParameter(
    key = Constants.GLUE_PROPERTY_NAME,
    value = "com.circulosiete.minibank.bdd"
)
@ConfigurationParameter(
    key = Constants.PLUGIN_PROPERTY_NAME,
    value = "pretty, html:build/RunCucumberTests.html"
)
public class CucumberSpringConfig {

}
