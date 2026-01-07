package org.fergoeqs.soap;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.fergoeqs.dtos.FilterConditionDTO;
import org.fergoeqs.dtos.FilterRequestDTO;
import org.fergoeqs.dtos.SortOptionDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import org.springframework.web.reactive.function.client.WebClient;

import javax.xml.bind.JAXBElement;
import java.util.ArrayList;
import java.util.List;

@Endpoint
@RequiredArgsConstructor
public class OrgDirectoryEndpoint {

    private static final String NAMESPACE_URI = "http://fergoeqs.org/orgdirectory";

    private final WebClient webClient;
    private final ObjectFactory objectFactory;
    private final ObjectMapper objectMapper;

    @Value("${first-service.base-url}")
    private String firstServiceBaseUrl;

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "FilterByTurnoverRequest")
    @ResponsePayload
    public JAXBElement<FilterByTurnoverResponse> filterByTurnover(@RequestPayload JAXBElement<FilterByTurnoverRequest> requestElement) {
        System.out.println(">>> SOAP FilterByTurnoverRequest received");
        System.out.println(">>> firstServiceBaseUrl = " + firstServiceBaseUrl);

        FilterByTurnoverRequest request = requestElement.getValue();
        int min = request.getMinAnnualTurnover();
        int max = request.getMaxAnnualTurnover();
        
        if (min > max) {
            throw new IllegalArgumentException("Invalid turnover range: min > max");
        }

        List<FilterConditionDTO> filters = new ArrayList<>();
        
        if (request.getFilters() != null && request.getFilters().getFilter() != null) {
            for (FilterCondition filter : request.getFilters().getFilter()) {
                filters.add(new FilterConditionDTO(
                        filter.getField(),
                        filter.getOperator(),
                        filter.getValue()
                ));
            }
        }

        filters.add(new FilterConditionDTO("annualTurnover", "between", List.of(min, max)));

        List<SortOptionDTO> sort = new ArrayList<>();
        if (request.getSort() != null && request.getSort().getSortOption() != null) {
            for (SortOption so : request.getSort().getSortOption()) {
                sort.add(new SortOptionDTO(
                        so.getField(),
                        so.getDirection(),
                        so.getPriority()
                ));
            }
        }

        Integer page = request.getPage() != null ? request.getPage() : 0;
        Integer size = request.getSize() != null ? request.getSize() : 20;

        FilterRequestDTO requestDTO = new FilterRequestDTO(filters, sort, page, size);
        
        Object result = webClient.post()
                .uri(firstServiceBaseUrl + "/search")
                .bodyValue(requestDTO)
                .retrieve()
                .bodyToMono(Object.class)
                .block();

        String resultJson;
        try {
            resultJson = objectMapper.writeValueAsString(result);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize result to JSON", e);
        }

        FilterByTurnoverResponse response = new FilterByTurnoverResponse();
        response.setResult(resultJson);

        return objectFactory.createFilterByTurnoverResponse(response);
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "OrderOrganizationsRequest")
    @ResponsePayload
    public JAXBElement<OrderOrganizationsResponse> orderOrganizations(@RequestPayload JAXBElement<OrderOrganizationsRequest> requestElement) {
        System.out.println(">>> SOAP OrderOrganizationsRequest received");
        
        OrderOrganizationsRequest request = requestElement.getValue();
        System.out.println(">>> request.getSort() = " + request.getSort());
        System.out.println(">>> request.getPage() = " + request.getPage());
        System.out.println(">>> request.getSize() = " + request.getSize());

        if (request.getSort() == null || request.getSort().getSortOption() == null || request.getSort().getSortOption().isEmpty()) {
            System.err.println(">>> ERROR: Missing sort criteria");
            throw new IllegalArgumentException("Missing sort criteria");
        }
        
        System.out.println(">>> sort options count: " + request.getSort().getSortOption().size());

        List<FilterConditionDTO> filters = new ArrayList<>();
        if (request.getFilters() != null && request.getFilters().getFilter() != null) {
            for (FilterCondition filter : request.getFilters().getFilter()) {
                filters.add(new FilterConditionDTO(
                        filter.getField(),
                        filter.getOperator(),
                        filter.getValue()
                ));
            }
        }

        List<SortOptionDTO> sort = new ArrayList<>();
        for (SortOption so : request.getSort().getSortOption()) {
            sort.add(new SortOptionDTO(
                    so.getField(),
                    so.getDirection(),
                    so.getPriority()
            ));
        }

        Integer page = request.getPage() != null ? request.getPage() : 0;
        Integer size = request.getSize() != null ? request.getSize() : 20;

        FilterRequestDTO requestDTO = new FilterRequestDTO(filters, sort, page, size);
        
        Object result = webClient.post()
                .uri(firstServiceBaseUrl + "/search")
                .bodyValue(requestDTO)
                .retrieve()
                .bodyToMono(Object.class)
                .block();

        String resultJson;
        try {
            resultJson = objectMapper.writeValueAsString(result);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize result to JSON", e);
        }

        OrderOrganizationsResponse response = new OrderOrganizationsResponse();
        response.setResult(resultJson);

        return objectFactory.createOrderOrganizationsResponse(response);
    }
}
