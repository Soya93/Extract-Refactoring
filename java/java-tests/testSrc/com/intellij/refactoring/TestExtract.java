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

import com.intellij.ide.ui.AppearanceOptionsTopHitProvider;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import junit.framework.TestCase;
import nz.ac.waikato.modeljunit.RandomTester;
import nz.ac.waikato.modeljunit.StopOnFailureListener;
import nz.ac.waikato.modeljunit.Tester;
import nz.ac.waikato.modeljunit.VerboseListener;
import nz.ac.waikato.modeljunit.coverage.ActionCoverage;
import nz.ac.waikato.modeljunit.coverage.StateCoverage;
import nz.ac.waikato.modeljunit.coverage.TransitionCoverage;

public class TestExtract extends TestCase {

  private ExtractModel model;

  protected void setUp() {
    model = new ExtractModel();
  }

  public void tearDown() {
    model = null;
  }

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

  public void testVariable() throws Exception {
    model = new ExtractModel();
    model.variable();
    model.refactorVariable();
  }

  public void testConstant() throws Exception {
    model = new ExtractModel();
    model.constant();
    model.refactorConstant();
  }

  public void testField() throws Exception {
    model = new ExtractModel();
    model.field();
    model.refactorField();
  }

  public void testRegression2() throws Exception {
    //model.setText();
  }
}