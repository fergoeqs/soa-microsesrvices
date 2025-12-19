package org.fergoeqs.web.config;

import org.fergoeqs.service.OrganizationServiceRemote;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Properties;

@Singleton
@Startup
public class EJBClientConfig {

    private OrganizationServiceRemote organizationServiceRemote;
    private Context context;

    @PostConstruct
    public void init() {
        try {
            String ejbServerHost = getEnv("EJB_SERVER_HOST", "ejb-server");
            String ejbServerPort = getEnv("EJB_SERVER_PORT", "8080");
            String ejbServerUrl = "http-remoting://" + ejbServerHost + ":" + ejbServerPort;

            Properties props = new Properties();
            props.put(Context.INITIAL_CONTEXT_FACTORY, "org.wildfly.naming.client.WildFlyInitialContextFactory");
            props.put(Context.PROVIDER_URL, ejbServerUrl);

            String ejbUsername = getEnv("EJB_USERNAME", "admin");
            String ejbPassword = getEnv("EJB_PASSWORD", "admin");
            props.put(Context.SECURITY_PRINCIPAL, ejbUsername);
            props.put(Context.SECURITY_CREDENTIALS, ejbPassword);

            System.out.println("=== EJB REMOTE CLIENT INITIALIZATION ===");
            System.out.println("EJB Server URL: " + ejbServerUrl);
            System.out.println("EJB Username: " + ejbUsername);
            System.out.println("Using remote JNDI lookup with authentication");

            context = new InitialContext(props);

            String jndiName = "ejb:/organization-ejb/OrganizationServiceImpl!org.fergoeqs.service.OrganizationServiceRemote";

            System.out.println("Attempting lookup with JNDI name: " + jndiName);
            
            try {
                Object lookupResult = context.lookup(jndiName);
                System.out.println("Lookup returned object of type: " + lookupResult.getClass().getName());

                if (lookupResult instanceof Context) {
                    System.out.println("Lookup returned a Context, navigating to bean...");
                    Context subContext = (Context) lookupResult;

                    String[] beanPaths = {
                        "OrganizationServiceImpl!org.fergoeqs.service.OrganizationServiceRemote",
                        "OrganizationServiceImpl",
                        "."
                    };
                    
                    for (String beanPath : beanPaths) {
                        try {
                            Object bean = subContext.lookup(beanPath);
                            System.out.println("Found bean at path '" + beanPath + "': " + bean.getClass().getName());
                            
                            if (bean instanceof OrganizationServiceRemote) {
                                organizationServiceRemote = (OrganizationServiceRemote) bean;
                                break;
                            } else if (bean instanceof Context) {
                                Context nestedContext = (Context) bean;
                                try {
                                    Object nestedBean = nestedContext.lookup(".");
                                    if (nestedBean instanceof OrganizationServiceRemote) {
                                        organizationServiceRemote = (OrganizationServiceRemote) nestedBean;
                                        break;
                                    }
                                } catch (NamingException e) {
                                }
                            }
                        } catch (NamingException e) {
                            continue;
                        }
                    }
                    
                    if (organizationServiceRemote == null) {
                        throw new NamingException("Could not find EJB bean in context");
                    }
                } else if (lookupResult instanceof OrganizationServiceRemote) {
                    organizationServiceRemote = (OrganizationServiceRemote) lookupResult;
                } else {
                    throw new ClassCastException("Lookup result is not an EJB proxy: " + lookupResult.getClass().getName());
                }
                
            } catch (Exception e) {
                System.err.println("Failed to lookup EJB with JNDI name: " + jndiName);
                System.err.println("Error: " + e.getMessage());
                e.printStackTrace();
            }
        } catch (Exception e) {
            System.err.println("Failed to initialize EJB remote service: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @PreDestroy
    public void destroy() {
        if (context != null) {
            try {
                context.close();
            } catch (NamingException e) {
                e.printStackTrace();
            }
        }
    }

    public OrganizationServiceRemote getOrganizationServiceRemote() {
        if (organizationServiceRemote == null) {
            throw new RuntimeException("EJB Remote service is not initialized.");
        }
        return organizationServiceRemote;
    }

    private String getEnv(String name, String defaultValue) {
        String value = System.getenv(name);
        return (value != null && !value.isEmpty()) ? value : defaultValue;
    }
}

