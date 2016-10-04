package org.jsystem;

import junit.framework.SystemTestCase4;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.http.HttpHeader;
import org.eclipse.jetty.http.HttpMethod;
import org.junit.Assert;
import org.junit.Test;


/**
 * Created by Nadav on 10/4/2016.
 */
public class HelloWorldTests extends SystemTestCase4 {
    @Test
    public void server_defaultEndpoint_shouldReturnHelloWorld() throws Exception {
		HttpClient client = new HttpClient();
        client.start();

        Request res = client.newRequest("http://localhost:8081");
        res.header(HttpHeader.HOST , "text/plain");
        res.method(HttpMethod.GET);

        ContentResponse response = res.send();

        String actual = response.getContentAsString();
        Assert.assertEquals("Hello World", actual);

    }
}
