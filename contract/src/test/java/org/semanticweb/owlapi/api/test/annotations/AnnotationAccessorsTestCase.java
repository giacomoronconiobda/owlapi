/* This file is part of the OWL API.
 * The contents of this file are subject to the LGPL License, Version 3.0.
 * Copyright 2014, The University of Manchester
 * 
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program.  If not, see http://www.gnu.org/licenses/.
 *
 * Alternatively, the contents of this file may be used under the terms of the Apache License, Version 2.0 in which case, the provisions of the Apache License Version 2.0 are applicable instead of those above.
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License. */
package org.semanticweb.owlapi.api.test.annotations;

import static java.util.stream.Collectors.toSet;
import static org.junit.Assert.assertTrue;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.*;
import static org.semanticweb.owlapi.search.Searcher.annotations;

import java.util.Arrays;
import java.util.Collection;

import javax.annotation.Nonnull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.semanticweb.owlapi.api.test.baseclasses.TestBase;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAnnotationValue;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLPrimitive;

/**
 * @author Matthew Horridge, The University of Manchester, Bio-Health
 *         Informatics Group
 * @since 3.1.0
 */
@SuppressWarnings("javadoc")
@RunWith(Parameterized.class)
public class AnnotationAccessorsTestCase extends TestBase {

    @Nonnull
    private static final IRI SUBJECT = IRI
            .create("http://owlapi.sourceforge.net/ontologies/test#X");

    @Nonnull
    @Parameterized.Parameters
    public static Collection<Object[]> getData() {
        return Arrays.asList(new Object[] { Class(SUBJECT) },
                new Object[] { NamedIndividual(SUBJECT) },
                new Object[] { DataProperty(SUBJECT) },
                new Object[] { ObjectProperty(SUBJECT) },
                new Object[] { Datatype(SUBJECT) },
                new Object[] { AnnotationProperty(SUBJECT) },
                new Object[] { AnonymousIndividual() });
    }

    private OWLPrimitive e;

    public AnnotationAccessorsTestCase(OWLPrimitive e) {
        this.e = e;
    }

    @Nonnull
    private OWLAnnotationAssertionAxiom createAnnotationAssertionAxiom() {
        OWLAnnotationProperty prop = AnnotationProperty(iri("prop"));
        OWLAnnotationValue value = Literal("value");
        return AnnotationAssertion(prop, SUBJECT, value);
    }

    @Test
    public void testClassAccessor() {
        OWLOntology ont = getOWLOntology("ontology");
        OWLAnnotationAssertionAxiom ax = createAnnotationAssertionAxiom();
        ont.getOWLOntologyManager().addAxiom(ont, ax);
        assertTrue(ont.annotationAssertionAxioms(SUBJECT).anyMatch(
                a -> a.equals(ax)));
        if (e instanceof OWLEntity) {
            assertTrue(ont.annotationAssertionAxioms(((OWLEntity) e).getIRI())
                    .anyMatch(a -> a.equals(ax)));
            assertTrue(annotations(
                    ont.annotationAssertionAxioms(((OWLEntity) e).getIRI())
                            .collect(toSet())).contains(ax.getAnnotation()));
        }
    }
}
