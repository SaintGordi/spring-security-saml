/*
 * Copyright 2002-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package org.springframework.security.saml.boot;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.security.saml.provider.SamlServerConfiguration;

@ConfigurationProperties(prefix = "spring.security.saml2")
public class SamlConfiguration {

	@NestedConfigurationProperty
	private LocalServiceProviderConfiguration serviceProvider;

	@NestedConfigurationProperty
	private LocalIdentityProviderConfiguration identityProvider;

	public LocalServiceProviderConfiguration getServiceProvider() {
		return serviceProvider;
	}

	public void setServiceProvider(LocalServiceProviderConfiguration serviceProvider) {
		this.serviceProvider = serviceProvider;
	}

	public LocalIdentityProviderConfiguration getIdentityProvider() {
		return identityProvider;
	}

	public void setIdentityProvider(LocalIdentityProviderConfiguration identityProvider) {
		this.identityProvider = identityProvider;
	}

	public SamlServerConfiguration toSamlServerConfiguration() {
		throw new UnsupportedOperationException();
	}
}
