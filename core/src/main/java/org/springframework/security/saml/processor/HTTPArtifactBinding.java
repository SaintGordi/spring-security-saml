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
import org.apache.velocity.app.VelocityEngine;
import org.opensaml.compat.security.SAMLProtocolMessageXMLSignatureSecurityPolicyRule;
import org.opensaml.compat.security.SecurityPolicyRule;
import org.opensaml.compat.transport.InTransport;
import org.opensaml.compat.transport.OutTransport;
import org.opensaml.compat.transport.http.HTTPInTransport;
import org.opensaml.messaging.decoder.MessageDecoder;
import org.opensaml.messaging.encoder.MessageEncoder;
import org.opensaml.saml.common.xml.SAMLConstants;
import org.opensaml.saml.saml2.binding.encoding.impl.HTTPArtifactEncoder;
import org.opensaml.saml2.binding.decoding.HTTPArtifactDecoderImpl;
import org.opensaml.ws.transport.http.HTTPOutTransport;
import org.opensaml.xmlsec.signature.support.SignatureTrustEngine;
import org.springframework.security.saml.context.SAMLMessageContext;
import org.springframework.security.saml.websso.ArtifactResolutionProfile;

/**
 * Http artifact binding.
 *
 * @author Mandus Elfving, Vladimir Schaefer
 */
public class HTTPArtifactBinding extends SAMLBindingImpl {

    /**
     * Creates default implementation of the binding.
     *
     * @param parserPool      parserPool for message deserialization
     * @param velocityEngine  engine for message formatting
     * @param artifactProfile profile used to retrieven the artifact message
     */
    public HTTPArtifactBinding(ParserPool parserPool, VelocityEngine velocityEngine, ArtifactResolutionProfile artifactProfile) {
        HTTPArtifactEncoder artifactEncoder = new HTTPArtifactEncoder();
        artifactEncoder.setVelocityEngine(velocityEngine);
        artifactEncoder.setVelocityTemplateId("/templates/saml2-post-artifact-binding.vm");
        this(new HTTPArtifactDecoderImpl(artifactProfile, parserPool), artifactEncoder);
    }

    /**
     * Implementation of the binding with custom encoder and decoder.
     *
     * @param decoder custom decoder implementation
     * @param encoder custom encoder implementation
     */
    public HTTPArtifactBinding(MessageDecoder decoder, MessageEncoder encoder) {
        super(decoder, encoder);
    }

    public boolean supports(InTransport transport) {
        if (transport instanceof HTTPInTransport) {
            HTTPInTransport t = (HTTPInTransport) transport;
            return t.getParameterValue("SAMLart") != null;
        } else {
            return false;
        }
    }

    public boolean supports(OutTransport transport) {
        return transport instanceof HTTPOutTransport;
    }

    public String getBindingURI() {
        return SAMLConstants.SAML2_ARTIFACT_BINDING_URI;
    }

    @Override
    public void getSecurityPolicy(List<SecurityPolicyRule> securityPolicy, SAMLMessageContext samlContext) {

        SignatureTrustEngine engine = samlContext.getLocalTrustEngine();
        securityPolicy.add(new SAMLProtocolMessageXMLSignatureSecurityPolicyRule(engine));

    }

}