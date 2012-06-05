/*
 * <copyright>
 * 
 * Copyright 2000-2004 Cougaar Software, Inc. under sponsorship of the Defense
 * Advanced Research Projects Agency (DARPA).
 * 
 * You can redistribute this software and/or modify it under the terms of the
 * Cougaar Open Source License as published on the Cougaar Open Source Website
 * (www.cougaar.org).
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * 
 * </copyright>
 */
package com.cougaarsoftware.config.service;
import org.cougaar.core.component.BindingSite;
import org.cougaar.core.component.Component;
import org.cougaar.core.component.ServiceBroker;
import org.cougaar.core.component.ServiceProvider;
import org.cougaar.util.GenericStateModelAdapter;
/**
 * @author mabrams
 */
public class ConfigurationServiceComponent extends GenericStateModelAdapter
		implements
			Component {
	private ServiceBroker sb;
	private ConfigurationService configurationService;
	private ConfigurationServiceProvider configurationServiceSP;
	public void setBindingSite(BindingSite bs) {
		this.sb = bs.getServiceBroker();
	}
	public void setParameter(Object o) {
	}
	public void load() {
		super.load();
		this.configurationService = new ConfigurationServiceImpl(sb);
		this.configurationServiceSP = new ConfigurationServiceProvider();
		sb.addService(ConfigurationService.class, configurationServiceSP);
	}
	public void unload() {
		// revoke our service
		if (configurationServiceSP != null) {
			sb.revokeService(ConfigurationService.class, configurationServiceSP);
			configurationServiceSP = null;
		}
		super.unload();
	}
	private class ConfigurationServiceProvider implements ServiceProvider {
		/**
		 * @see org.cougaar.core.component.ServiceProvider#getService(org.cougaar.core.component.ServiceBroker,
		 *      java.lang.Object, java.lang.Class)
		 */
		public Object getService(ServiceBroker sb, Object requestor,
				Class serviceClass) {
			if (ConfigurationService.class.isAssignableFrom(serviceClass)) {
				return configurationService;
			} else {
				return null;
			}
		}
		/**
		 * @see org.cougaar.core.component.ServiceProvider#releaseService(org.cougaar.core.component.ServiceBroker,
		 *      java.lang.Object, java.lang.Class, java.lang.Object)
		 */
		public void releaseService(ServiceBroker sb, Object requestor,
				Class serviceClass, Object service) {
			configurationService = null;
		}
	}
}