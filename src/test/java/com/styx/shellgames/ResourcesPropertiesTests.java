package com.styx.shellgames;

import com.styx.shellgames.utils.ApplicationProperties;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class ResourcesPropertiesTests {

    @Test
    void languageAvailableProperty_exist() {
        ApplicationProperties.loadProperties();
        String languagesAvailable = ApplicationProperties.getProperty("languages.available");
        assertThat(languagesAvailable).isNotNull();
    }

}
