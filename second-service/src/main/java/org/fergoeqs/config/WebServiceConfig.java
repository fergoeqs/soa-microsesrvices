package org.fergoeqs.config;

import org.fergoeqs.soap.ObjectFactory;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

@EnableWs
@Configuration
public class WebServiceConfig extends WsConfigurerAdapter {

    @Bean
    public ServletRegistrationBean<MessageDispatcherServlet> messageDispatcherServlet(ApplicationContext applicationContext) {
        MessageDispatcherServlet servlet = new MessageDispatcherServlet();
        servlet.setApplicationContext(applicationContext);
        servlet.setTransformWsdlLocations(true);
        ServletRegistrationBean<MessageDispatcherServlet> registration = new ServletRegistrationBean<>(servlet, "/ws/*");
        registration.setName("messageDispatcherServlet");
        registration.setLoadOnStartup(1);
        registration.setOrder(1);
        return registration;
    }

    @Bean(name = "orgdirectory")
    public DefaultWsdl11Definition defaultWsdl11Definition(XsdSchema orgdirectorySchema) {
        DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
        wsdl11Definition.setPortTypeName("OrgDirectoryPort");
        wsdl11Definition.setLocationUri("/ws");
        wsdl11Definition.setTargetNamespace("http://fergoeqs.org/orgdirectory");
        wsdl11Definition.setSchema(orgdirectorySchema);
        wsdl11Definition.setCreateSoap11Binding(true);
        return wsdl11Definition;
    }

    @Bean
    public XsdSchema orgdirectorySchema() {
        return new SimpleXsdSchema(new ClassPathResource("orgdirectory.xsd"));
    }

    @Bean
    public ObjectFactory objectFactory() {
        return new ObjectFactory();
    }
}
