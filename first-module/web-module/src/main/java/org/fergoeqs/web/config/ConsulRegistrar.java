package org.fergoeqs.web.config;


import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.agent.model.NewService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;

@Singleton
@Startup
public class ConsulRegistrar {

    private ConsulClient consulClient;
    private String serviceId;
    private String serviceName;
    private String serviceAddress;
    private int servicePort;

    @PostConstruct
    public void register() {
        try {
            serviceId = getEnv("SERVICE_ID", "organization-service-" + System.getenv("HOSTNAME"));
            serviceName = getEnv("SERVICE_NAME", "organization-service");
            serviceAddress = getEnv("SERVICE_ADDRESS", System.getenv("HOSTNAME"));
            servicePort = Integer.parseInt(getEnv("SERVICE_PORT", "8080"));

            consulClient = new ConsulClient("consul", 8500);

            NewService service = new NewService();
            service.setId(serviceId);
            service.setName(serviceName);
            service.setAddress(serviceAddress);
            service.setPort(servicePort);

            NewService.Check check = new NewService.Check();
            check.setHttp("http://" + serviceAddress + ":" + servicePort + "/web-module/api/organizations/health");
            check.setInterval("10s");
            check.setDeregisterCriticalServiceAfter("1m");

            service.setCheck(check);

            consulClient.agentServiceRegister(service);

            System.out.println("REGISTERED IN CONSUL: " + serviceId);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PreDestroy
    public void deregister() {
        try {
            consulClient.agentServiceDeregister(serviceId);
            System.out.println("DEREGISTERED FROM CONSUL: " + serviceId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getEnv(String name, String defaultValue) {
        String v = System.getenv(name);
        return (v != null) ? v : defaultValue;
    }
}
