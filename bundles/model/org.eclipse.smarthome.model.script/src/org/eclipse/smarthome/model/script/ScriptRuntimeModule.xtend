/**
 * Copyright (c) 2014,2018 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package
/*
 * generated by Xtext
 */
org.eclipse.smarthome.model.script

import org.eclipse.smarthome.model.script.interpreter.ScriptInterpreter
import org.eclipse.smarthome.model.script.scoping.ActionClassLoader
import org.eclipse.smarthome.model.script.scoping.ScriptImplicitlyImportedTypes
import org.eclipse.smarthome.model.script.scoping.ScriptImportSectionNamespaceScopeProvider
import org.eclipse.xtext.common.types.access.IJvmTypeProvider
import org.eclipse.xtext.common.types.access.reflect.ReflectionTypeProviderFactory
import org.eclipse.xtext.common.types.access.reflect.ReflectionTypeScopeProvider
import org.eclipse.xtext.common.types.xtext.AbstractTypeScopeProvider
import org.eclipse.xtext.generator.IGenerator
import org.eclipse.xtext.generator.IGenerator.NullGenerator
import org.eclipse.xtext.linking.lazy.LazyURIEncoder
import org.eclipse.xtext.scoping.IScopeProvider
import org.eclipse.xtext.scoping.impl.AbstractDeclarativeScopeProvider
import org.eclipse.xtext.xbase.interpreter.IExpressionInterpreter
import org.eclipse.xtext.xbase.scoping.batch.ImplicitlyImportedFeatures
import com.google.inject.Binder
import com.google.inject.name.Names
import org.eclipse.xtext.xbase.typesystem.computation.ITypeComputer
import org.eclipse.smarthome.model.script.jvmmodel.ScriptTypeComputer
import org.eclipse.xtext.parser.IEncodingProvider
import org.eclipse.smarthome.model.script.internal.ScriptEncodingProvider
import org.eclipse.xtext.service.DispatchingProvider

/** 
 * Use this class to register components to be used at runtime / without the Equinox extension registry.
 * @author Oliver Libutzki - Initial contribution
 */
@SuppressWarnings("restriction") class ScriptRuntimeModule extends org.eclipse.smarthome.model.script.AbstractScriptRuntimeModule {
    def Class<? extends ImplicitlyImportedFeatures> bindImplicitlyImportedTypes() {
        return ScriptImplicitlyImportedTypes
    }
    
    def Class<? extends ITypeComputer> bindITypeComputer() {
        return ScriptTypeComputer
    }

    override configureRuntimeEncodingProvider(Binder binder) {
        binder.bind(IEncodingProvider).annotatedWith(DispatchingProvider.Runtime).to(ScriptEncodingProvider)
    }

    override Class<? extends IExpressionInterpreter> bindIExpressionInterpreter() {
        return ScriptInterpreter
    }

    override Class<? extends IGenerator> bindIGenerator() {
        return NullGenerator
    }

    override void configureIScopeProviderDelegate(Binder binder) {
        binder.bind(IScopeProvider).annotatedWith(Names.named(AbstractDeclarativeScopeProvider.NAMED_DELEGATE)).to(
            ScriptImportSectionNamespaceScopeProvider)
    }

    override Class<? extends IJvmTypeProvider.Factory> bindIJvmTypeProvider$Factory() {
        return ReflectionTypeProviderFactory
    }

    override Class<? extends AbstractTypeScopeProvider> bindAbstractTypeScopeProvider() {
        return ReflectionTypeScopeProvider
    }

    override ClassLoader bindClassLoaderToInstance() {
        return new ActionClassLoader(super.bindClassLoaderToInstance())
    }

    override void configureUseIndexFragmentsForLazyLinking(Binder binder) {
        binder.bind(Boolean.TYPE).annotatedWith(Names.named(LazyURIEncoder.USE_INDEXED_FRAGMENTS_BINDING)).toInstance(
            Boolean.FALSE)
    }
    
}
