package com.intellij.refactoring;
/*
 * Copyright 2000-2016 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import nz.ac.waikato.modeljunit.RandomTester;
import nz.ac.waikato.modeljunit.StopOnFailureListener;
import nz.ac.waikato.modeljunit.Tester;
import nz.ac.waikato.modeljunit.VerboseListener;
import nz.ac.waikato.modeljunit.coverage.ActionCoverage;
import nz.ac.waikato.modeljunit.coverage.StateCoverage;
import nz.ac.waikato.modeljunit.coverage.TransitionCoverage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestExtract {

  private ExtractModel model;

  @Before
  public void setUp() {
    model = new ExtractModel();
  }

  @After
  public void tearDown() {
    model = null;
  }

  @Test
  public void testExtractModel() throws Exception {
    Tester tester = new RandomTester(model);

    tester.buildGraph();
    tester.addListener(new VerboseListener());
    tester.addListener(new StopOnFailureListener());
    tester.addCoverageMetric(new TransitionCoverage());
    tester.addCoverageMetric(new StateCoverage());
    tester.addCoverageMetric(new ActionCoverage());

    tester.generate(200000);
    tester.printCoverage();
  }

  @Test
  public void testVariable() throws Exception {
    model.variable();
    model.refactorVariable();
  }

  @Test
  public void testConstant() throws Exception {
    model.constant();
    model.refactorConstant();
  }

  @Test
  public void testField() throws Exception {
    model.field();
    model.refactorField();
  }

}