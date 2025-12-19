package org.fergoeqs.web.provider;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.cfg.ConstructorDetector;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.annotation.Priority;
import jakarta.ws.rs.ext.ContextResolver;
import jakarta.ws.rs.ext.Provider;

@Provider
@Priority(1) // Higher priority than default ResteasyJackson2Provider (which has priority 0)
public class ObjectMapperProvider implements ContextResolver<ObjectMapper> {

    private final ObjectMapper objectMapper;

    public ObjectMapperProvider() {
        objectMapper = new ObjectMapper();
        // Explicitly register JavaTimeModule (don't use findAndRegisterModules to avoid conflicts)
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        
        // Enable support for Java records
        // Jackson 2.12+ has built-in support for records, but we need to configure it properly
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE, false);
        
        // Enable constructor detection for records (Jackson 2.15+)
        // This helps Jackson properly deserialize Java records
        try {
            objectMapper.setConstructorDetector(ConstructorDetector.USE_PROPERTIES_BASED);
        } catch (Exception e) {
            // If not available in this version, continue without it
            System.out.println("ConstructorDetector.USE_PROPERTIES_BASED not available: " + e.getMessage());
        }
        
        // Additional configuration for better record support
        // Allow Jackson to access record components for serialization/deserialization
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES, false);
    }

    @Override
    public ObjectMapper getContext(Class<?> type) {
        return objectMapper;
    }
}