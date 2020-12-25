package com.landray.kmss.km.review.webservice;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * This class was generated by Apache CXF 2.4.2
 * 2020-12-01T18:13:43.634+08:00
 * Generated source version: 2.4.2
 * 
 */
@WebServiceClient(name = "IKmReviewWebserviceServiceService", 
                  wsdlLocation = "http://localhost:8080/ekp/sys/webservice/kmReviewWebserviceService?wsdl",
                  targetNamespace = "http://webservice.review.km.kmss.landray.com/") 
public class IKmReviewWebserviceServiceService extends Service {

    public final static URL WSDL_LOCATION;

    public final static QName SERVICE = new QName("http://webservice.review.km.kmss.landray.com/", "IKmReviewWebserviceServiceService");
    public final static QName IKmReviewWebserviceServicePort = new QName("http://webservice.review.km.kmss.landray.com/", "IKmReviewWebserviceServicePort");
    static {
        URL url = null;
        try {
            url = new URL("http://localhost:8080/ekp/sys/webservice/kmReviewWebserviceService?wsdl");
        } catch (MalformedURLException e) {
            java.util.logging.Logger.getLogger(IKmReviewWebserviceServiceService.class.getName())
                .log(java.util.logging.Level.INFO, 
                     "Can not initialize the default wsdl from {0}", "http://localhost:8080/ekp/sys/webservice/kmReviewWebserviceService?wsdl");
        }
        WSDL_LOCATION = url;
    }

    public IKmReviewWebserviceServiceService(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public IKmReviewWebserviceServiceService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public IKmReviewWebserviceServiceService() {
        super(WSDL_LOCATION, SERVICE);
    }
    
    //This constructor requires JAX-WS API 2.2. You will need to endorse the 2.2
    //API jar or re-run wsdl2java with "-frontend jaxws21" to generate JAX-WS 2.1
    //compliant code instead.
    public IKmReviewWebserviceServiceService(WebServiceFeature ... features) {
        super(WSDL_LOCATION, SERVICE, features);
    }

    //This constructor requires JAX-WS API 2.2. You will need to endorse the 2.2
    //API jar or re-run wsdl2java with "-frontend jaxws21" to generate JAX-WS 2.1
    //compliant code instead.
    public IKmReviewWebserviceServiceService(URL wsdlLocation, WebServiceFeature ... features) {
        super(wsdlLocation, SERVICE, features);
    }

    //This constructor requires JAX-WS API 2.2. You will need to endorse the 2.2
    //API jar or re-run wsdl2java with "-frontend jaxws21" to generate JAX-WS 2.1
    //compliant code instead.
    public IKmReviewWebserviceServiceService(URL wsdlLocation, QName serviceName, WebServiceFeature ... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     *
     * @return
     *     returns IKmReviewWebserviceService
     */
    @WebEndpoint(name = "IKmReviewWebserviceServicePort")
    public IKmReviewWebserviceService getIKmReviewWebserviceServicePort() {
        return super.getPort(IKmReviewWebserviceServicePort, IKmReviewWebserviceService.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns IKmReviewWebserviceService
     */
    @WebEndpoint(name = "IKmReviewWebserviceServicePort")
    public IKmReviewWebserviceService getIKmReviewWebserviceServicePort(WebServiceFeature... features) {
        return super.getPort(IKmReviewWebserviceServicePort, IKmReviewWebserviceService.class, features);
    }

}
