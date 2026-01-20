package org.fergoeqs.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.fergoeqs.soap.*;
import org.springframework.stereotype.Service;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.client.core.SoapActionCallback;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SoapClientService {

    private final WebServiceTemplate webServiceTemplate;
    private final ObjectFactory objectFactory;
    private final ObjectMapper objectMapper;

    public Object filterByTurnover(Integer min, Integer max, List<Map<String, Object>> filters, 
                                   List<Map<String, Object>> sort, Integer page, Integer size) {
        FilterByTurnoverRequest request = new FilterByTurnoverRequest();
        request.setMinAnnualTurnover(min);
        request.setMaxAnnualTurnover(max);
        
        if (page != null) {
            request.setPage(page);
        } else {
            request.setPage(0);
        }
        
        if (size != null) {
            request.setSize(size);
        } else {
            request.setSize(20);
        }

        if (filters != null && !filters.isEmpty()) {
            FilterConditions filterConditions = new FilterConditions();
            for (Map<String, Object> f : filters) {
                FilterCondition filterCondition = new FilterCondition();
                filterCondition.setField((String) f.get("field"));
                filterCondition.setOperator((String) f.get("operator"));
                filterCondition.setValue(f.get("value"));
                filterConditions.getFilter().add(filterCondition);
            }
            request.setFilters(filterConditions);
        }

        if (sort != null && !sort.isEmpty()) {
            SortOptions sortOptions = new SortOptions();
            for (Map<String, Object> s : sort) {
                SortOption sortOption = new SortOption();
                sortOption.setField((String) s.get("field"));
                sortOption.setDirection((String) s.get("direction"));
                if (s.get("priority") != null) {
                    Object priority = s.get("priority");
                    if (priority instanceof Number) {
                        sortOption.setPriority(((Number) priority).intValue());
                    } else if (priority instanceof String) {
                        sortOption.setPriority(Integer.parseInt((String) priority));
                    }
                }
                sortOptions.getSortOption().add(sortOption);
            }
            request.setSort(sortOptions);
        }

        javax.xml.bind.JAXBElement<FilterByTurnoverRequest> requestElement = 
                objectFactory.createFilterByTurnoverRequest(request);
        
        javax.xml.bind.JAXBElement<FilterByTurnoverResponse> response = 
                (javax.xml.bind.JAXBElement<FilterByTurnoverResponse>) webServiceTemplate
                        .marshalSendAndReceive(
                                requestElement,
                                new SoapActionCallback("http://fergoeqs.org/orgdirectory/FilterByTurnoverRequest")
                        );

        Object result = response.getValue().getResult();
        
        if (result instanceof String) {
            try {
                return objectMapper.readValue((String) result, Object.class);
            } catch (JsonProcessingException e) {
                System.err.println(">>> Warning: Failed to parse JSON result, returning as string: " + e.getMessage());
                return result;
            }
        }
        
        return result;
    }

    public Object orderOrganizations(List<Map<String, Object>> sort, 
                                     List<Map<String, Object>> filters, 
                                     Integer page, Integer size) {
        OrderOrganizationsRequest request = new OrderOrganizationsRequest();
        
        if (sort == null || sort.isEmpty()) {
            throw new IllegalArgumentException("sort is required");
        }
        
        SortOptions sortOptions = new SortOptions();
        for (Map<String, Object> s : sort) {
            SortOption sortOption = new SortOption();
            sortOption.setField((String) s.get("field"));
            sortOption.setDirection((String) s.get("direction"));
            if (s.get("priority") != null) {
                Object priority = s.get("priority");
                if (priority instanceof Number) {
                    sortOption.setPriority(((Number) priority).intValue());
                } else if (priority instanceof String) {
                    sortOption.setPriority(Integer.parseInt((String) priority));
                }
            }
            sortOptions.getSortOption().add(sortOption);
        }
        request.setSort(sortOptions);

        if (filters != null && !filters.isEmpty()) {
            FilterConditions filterConditions = new FilterConditions();
            for (Map<String, Object> f : filters) {
                FilterCondition filterCondition = new FilterCondition();
                filterCondition.setField((String) f.get("field"));
                filterCondition.setOperator((String) f.get("operator"));
                filterCondition.setValue(f.get("value"));
                filterConditions.getFilter().add(filterCondition);
            }
            request.setFilters(filterConditions);
        }
        
        if (page != null) {
            request.setPage(page);
        } else {
            request.setPage(0);
        }
        
        if (size != null) {
            request.setSize(size);
        } else {
            request.setSize(20);
        }

        System.out.println(">>> Creating OrderOrganizationsRequest SOAP call");
        System.out.println(">>> sort options count: " + (request.getSort() != null && request.getSort().getSortOption() != null ? request.getSort().getSortOption().size() : 0));
        System.out.println(">>> page: " + request.getPage());
        System.out.println(">>> size: " + request.getSize());
        
        javax.xml.bind.JAXBElement<OrderOrganizationsRequest> requestElement = 
                objectFactory.createOrderOrganizationsRequest(request);
        
        try {
            javax.xml.bind.JAXBElement<OrderOrganizationsResponse> response = 
                    (javax.xml.bind.JAXBElement<OrderOrganizationsResponse>) webServiceTemplate
                            .marshalSendAndReceive(
                                    requestElement,
                                    new SoapActionCallback("http://fergoeqs.org/orgdirectory/OrderOrganizationsRequest")
                            );

            Object result = response.getValue().getResult();
        
        if (result instanceof String) {
            try {
                return objectMapper.readValue((String) result, Object.class);
            } catch (JsonProcessingException e) {
                System.err.println(">>> Warning: Failed to parse JSON result, returning as string: " + e.getMessage());
                return result;
            }
        }
        
        return result;
        } catch (Exception e) {
            System.err.println(">>> Error in SOAP call: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
}
