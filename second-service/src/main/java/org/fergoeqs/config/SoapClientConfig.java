package org.fergoeqs.config;

import org.fergoeqs.soap.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.WebServiceTemplate;

@Configuration
public class SoapClientConfig {

    @Value("${server.port:8081}")
    private String serverPort;

    @Value("${server.address:127.0.0.1}")
    private String serverAddress;

    @Bean
    public Jaxb2Marshaller marshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setClassesToBeBound(
                FilterByTurnoverRequest.class,
                FilterByTurnoverResponse.class,
                OrderOrganizationsRequest.class,
                OrderOrganizationsResponse.class,
                FilterConditions.class,
                FilterCondition.class,
                SortOptions.class,
                SortOption.class,
                ObjectFactory.class
        );
        return marshaller;
    }

    @Bean
    public WebServiceTemplate webServiceTemplate(Jaxb2Marshaller marshaller) {
        WebServiceTemplate webServiceTemplate = new WebServiceTemplate();
        webServiceTemplate.setMarshaller(marshaller);
        webServiceTemplate.setUnmarshaller(marshaller);
        webServiceTemplate.setDefaultUri("http://" + serverAddress + ":" + serverPort + "/ws");
        return webServiceTemplate;
    }
}
