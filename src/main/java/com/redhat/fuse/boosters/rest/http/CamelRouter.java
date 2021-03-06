package com.redhat.fuse.boosters.rest.http;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.stereotype.Component;
import org.apache.camel.Exchange;

/**
 * A simple Camel REST DSL route that implements the greetings service.
 * 
 */
@Component
public class CamelRouter extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        // @formatter:off
        restConfiguration()
                .apiContextPath("/api-doc")
                .apiProperty("api.title", "Save File REST API")
                .apiProperty("api.version", "1.0")
                .apiProperty("cors", "true")
                .apiProperty("base.path", "camel/")
                .apiProperty("api.path", "/")
                .apiProperty("host", "")
                .apiContextRouteId("doc-api")
            .component("servlet")
            .bindingMode(RestBindingMode.json);
        
        rest("/save/file").description("Save File in Filesystem")
            .get("/pdf").consumes("text/plain").produces("application/json")
            .route().routeId("save-file-api")
            .to("direct:save-file");

        from("direct:save-file").description("Save File as PDF")
            .streamCaching()
            .setHeader(Exchange.CONTENT_TYPE, constant("application/pdf"))
            .to("file://files/out/pdf?fileName=sampleFile.pdf")
            .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
            .setBody(constant("{'status':'File transformed ok'}"));

    }

}