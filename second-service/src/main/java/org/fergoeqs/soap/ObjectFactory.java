package org.fergoeqs.soap;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

@XmlRegistry
public class ObjectFactory {

    private static final QName _FilterByTurnoverRequest_QNAME = new QName("http://fergoeqs.org/orgdirectory", "FilterByTurnoverRequest");
    private static final QName _FilterByTurnoverResponse_QNAME = new QName("http://fergoeqs.org/orgdirectory", "FilterByTurnoverResponse");
    private static final QName _OrderOrganizationsRequest_QNAME = new QName("http://fergoeqs.org/orgdirectory", "OrderOrganizationsRequest");
    private static final QName _OrderOrganizationsResponse_QNAME = new QName("http://fergoeqs.org/orgdirectory", "OrderOrganizationsResponse");

    public ObjectFactory() {
    }

    @XmlElementDecl(namespace = "http://fergoeqs.org/orgdirectory", name = "FilterByTurnoverRequest")
    public JAXBElement<FilterByTurnoverRequest> createFilterByTurnoverRequest(FilterByTurnoverRequest value) {
        return new JAXBElement<>(_FilterByTurnoverRequest_QNAME, FilterByTurnoverRequest.class, null, value);
    }

    @XmlElementDecl(namespace = "http://fergoeqs.org/orgdirectory", name = "FilterByTurnoverResponse")
    public JAXBElement<FilterByTurnoverResponse> createFilterByTurnoverResponse(FilterByTurnoverResponse value) {
        return new JAXBElement<>(_FilterByTurnoverResponse_QNAME, FilterByTurnoverResponse.class, null, value);
    }

    @XmlElementDecl(namespace = "http://fergoeqs.org/orgdirectory", name = "OrderOrganizationsRequest")
    public JAXBElement<OrderOrganizationsRequest> createOrderOrganizationsRequest(OrderOrganizationsRequest value) {
        return new JAXBElement<>(_OrderOrganizationsRequest_QNAME, OrderOrganizationsRequest.class, null, value);
    }

    @XmlElementDecl(namespace = "http://fergoeqs.org/orgdirectory", name = "OrderOrganizationsResponse")
    public JAXBElement<OrderOrganizationsResponse> createOrderOrganizationsResponse(OrderOrganizationsResponse value) {
        return new JAXBElement<>(_OrderOrganizationsResponse_QNAME, OrderOrganizationsResponse.class, null, value);
    }
}
