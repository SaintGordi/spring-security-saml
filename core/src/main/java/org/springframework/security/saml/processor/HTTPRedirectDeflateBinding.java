/* Copyright 2010 Mandus Elfving
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.security.saml.processor;

import java.util.List;

import net.shibboleth.utilities.java.support.xml.ParserPool;
import org.opensaml.compat.security.SAMLProtocolMessageXMLSignatureSecurityPolicyRule;
import org.opensaml.compat.security.SecurityPolicyRule;
import org.opensaml.compat.transport.InTransport;
import org.opensaml.compat.transport.OutTransport;
import org.opensaml.compat.transport.http.HTTPInTransport;
import org.opensaml.compat.transport.http.HTTPTransport;
import org.opensaml.messaging.decoder.MessageDecoder;
import org.opensaml.messaging.encoder.MessageEncoder;
import org.opensaml.saml.common.xml.SAMLConstants;
import org.opensaml.saml.saml2.binding.decoding.impl.HTTPRedirectDeflateDecoder;
import org.opensaml.saml.saml2.binding.encoding.impl.HTTPRedirectDeflateEncoder;
import org.opensaml.saml2.binding.security.SAML2HTTPRedirectDeflateSignatureRule;
import org.opensaml.ws.transport.http.HTTPOutTransport;
import org.opensaml.xmlsec.signature.support.SignatureTrustEngine;
import org.springframework.security.saml.context.SAMLMessageContext;

/**
 * Http redirect binding.
 *
 * @author Mandus Elfving
 */
public class HTTPRedirectDeflateBinding extends SAMLBindingImpl {

    /**
     * Creates binding with default encoder and decoder.
     *
     * @param parserPool parser pool
     */
    public HTTPRedirectDeflateBinding(ParserPool parserPool) {
        this(new HTTPRedirectDeflateDecoder(parserPool), new HTTPRedirectDeflateEncoder());
    }

    /**
     * Constructor with customized encoder and decoder
     *
     * @param decoder decoder
     * @param encoder encoder
     */
    public HTTPRedirectDeflateBinding(MessageDecoder decoder, MessageEncoder encoder) {
        super(decoder, encoder);
    }

    public boolean supports(InTransport transport) {
        if (transport instanceof HTTPInTransport) {
            HTTPTransport t = (HTTPTransport) transport;
            return "GET".equalsIgnoreCase(t.getHTTPMethod()) && (t.getParameterValue("SAMLRequest") != null || t.getParameterValue("SAMLResponse") != null);
        } else {
            return false;
        }
    }

    public boolean supports(OutTransport transport) {
        return transport instanceof HTTPOutTransport;
    }

    public String getBindingURI() {
        return SAMLConstants.SAML2_REDIRECT_BINDING_URI;
    }

    @Override
    public void getSecurityPolicy(List<SecurityPolicyRule> securityPolicy, SAMLMessageContext samlContext) {

        SignatureTrustEngine engine = samlContext.getLocalTrustEngine();
        securityPolicy.add(new SAML2HTTPRedirectDeflateSignatureRule(engine));
        securityPolicy.add(new SAMLProtocolMessageXMLSignatureSecurityPolicyRule(engine));

    }

}