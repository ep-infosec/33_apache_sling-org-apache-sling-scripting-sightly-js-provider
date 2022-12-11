/*******************************************************************************
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 ******************************************************************************/
package org.apache.sling.scripting.sightly.js.impl;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.scripting.LazyBindings;
import org.apache.sling.scripting.api.resource.ScriptingResourceResolverProvider;
import org.apache.sling.scripting.core.ScriptNameAwareReader;
import org.apache.sling.scripting.sightly.SightlyException;
import org.apache.sling.scripting.sightly.js.impl.async.AsyncContainer;
import org.apache.sling.scripting.sightly.js.impl.async.AsyncExtractor;
import org.apache.sling.scripting.sightly.js.impl.jsapi.ProxyAsyncScriptableFactory;
import org.apache.sling.scripting.sightly.js.impl.rhino.JsValueAdapter;
import org.apache.sling.scripting.sightly.js.impl.use.DependencyResolver;
import org.apache.sling.scripting.sightly.render.RenderContext;
import org.apache.sling.scripting.sightly.use.ProviderOutcome;
import org.apache.sling.scripting.sightly.use.UseProvider;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.AttributeDefinition;

/**
 * Use provider for JavaScript Use-API objects.
 */
@Component(
        service = UseProvider.class,
        configurationPid = "org.apache.sling.scripting.sightly.js.impl.JsUseProvider",
        property = {
                Constants.SERVICE_RANKING + ":Integer=80"
        }
)
public class JsUseProvider implements UseProvider {

    @interface Configuration {

        @AttributeDefinition(
                name = "Service Ranking",
                description = "The Service Ranking value acts as the priority with which this Use Provider is queried to return an " +
                        "Use-object. A higher value represents a higher priority."
        )
        int service_ranking() default 80;

    }

    private static final String JS_ENGINE_NAME = "rhino";
    private static final JsValueAdapter jsValueAdapter = new JsValueAdapter(new AsyncExtractor());

    @Reference
    private ScriptEngineManager scriptEngineManager;

    @Reference
    private ProxyAsyncScriptableFactory proxyAsyncScriptableFactory;

    @Reference
    private ScriptingResourceResolverProvider scriptingResourceResolverProvider;

    @Override
    public ProviderOutcome provide(String identifier, RenderContext renderContext, Bindings arguments) {
        Bindings globalBindings = new LazyBindings();
        globalBindings.putAll(renderContext.getBindings());
        if (!Utils.isJsScript(identifier)) {
            return ProviderOutcome.failure();
        }
        ScriptEngine jsEngine = scriptEngineManager.getEngineByName(JS_ENGINE_NAME);
        if (jsEngine == null) {
            return ProviderOutcome.failure(new SightlyException("Failed to obtain a " + JS_ENGINE_NAME + " JavaScript engine."));
        }
        JsEnvironment environment = null;
        try {
            ResourceResolver slingScriptingResolver = scriptingResourceResolverProvider.getRequestScopedResourceResolver();
            DependencyResolver dependencyResolver = new DependencyResolver(slingScriptingResolver);
            environment = new JsEnvironment(jsEngine, dependencyResolver);
            environment.initialize();
            ScriptNameAwareReader reader = dependencyResolver.resolve(globalBindings, identifier);
            if (reader != null) {
                proxyAsyncScriptableFactory.registerProxies(slingScriptingResolver, environment, globalBindings);
                AsyncContainer asyncContainer = environment.runScript(reader, globalBindings, arguments);
                return ProviderOutcome.success(jsValueAdapter.adapt(asyncContainer));
            }
            return ProviderOutcome.failure();
        } catch (Exception e) {
            return ProviderOutcome.failure(e);
        } finally {
            if (environment != null) {
                environment.cleanup();
            }
        }
    }
}
