/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.brooklyn.core.effector;

import java.util.List;
import java.util.Map;

import org.apache.brooklyn.api.effector.ParameterType;
import org.apache.brooklyn.api.entity.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;

import groovy.lang.Closure;

/**
 * @deprecated since 0.11.0; explicit groovy utilities/support will be deleted 
 *             (currently only used via {@link #create(String, Class, List, String, Closure)}, 
 *             so class is not deemed useful enough - extend {@link AbstractEffector} directly 
 *             if really required).
 */
@Deprecated
public abstract class ExplicitEffector<I,T> extends AbstractEffector<T> {
    
    private static final Logger LOG = LoggerFactory.getLogger(ExplicitEffector.class);

    public ExplicitEffector(String name, Class<T> type, String description) {
        this(name, type, ImmutableList.<ParameterType<?>>of(), description);
    }
    public ExplicitEffector(String name, Class<T> type, List<ParameterType<?>> parameters, String description) {
        super(name, type, parameters, description);
    }

    @Override
    public T call(Entity entity, Map parameters) {
        return invokeEffector((I) entity, (Map<String,?>)parameters );
    }

    public abstract T invokeEffector(I trait, Map<String,?> parameters);
    
    /** convenience to create an effector supplying a closure; annotations are preferred,
     * and subclass here would be failback, but this is offered as 
     * workaround for bug GROOVY-5122, as discussed in test class CanSayHi.
     * 
     * @deprecated since 0.11.0; explicit groovy utilities/support will be deleted.
     */
    @Deprecated
    public static <I,T> ExplicitEffector<I,T> create(String name, Class<T> type, List<ParameterType<?>> parameters, String description, Closure body) {
        LOG.warn("Use of groovy.lang.Closure is deprecated, in ExplicitEffector.create()");
        return new ExplicitEffectorFromClosure<I,T>(name, type, parameters, description, body);
    }

    /**
     * @deprecated since 0.11.0; explicit groovy utilities/support will be deleted.
     */
    @Deprecated
    private static class ExplicitEffectorFromClosure<I,T> extends ExplicitEffector<I,T> {
        private static final long serialVersionUID = -5771188171702382236L;
        final Closure<T> body;
        public ExplicitEffectorFromClosure(String name, Class<T> type, List<ParameterType<?>> parameters, String description, Closure<T> body) {
            super(name, type, parameters, description);
            this.body = body;
        }
        @Override
        public T invokeEffector(I trait, Map<String,?> parameters) { return body.call(trait, parameters); }
        
        @Override
        public int hashCode() {
            return Objects.hashCode(super.hashCode(), body);
        }
        
        @Override
        public boolean equals(Object other) {
            return super.equals(other) && Objects.equal(body, ((ExplicitEffectorFromClosure<?,?>)other).body);
        }
        
    }
}
