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
package com.intellij.refactoring;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.psi.PsiFile;
import com.intellij.refactoring.introduceField.BaseExpressionToFieldHandler;
import com.intellij.refactoring.introduceParameter.IntroduceParameterHandler;
import nz.ac.waikato.modeljunit.*;
import nz.ac.waikato.modeljunit.coverage.ActionCoverage;
import nz.ac.waikato.modeljunit.coverage.StateCoverage;
import nz.ac.waikato.modeljunit.coverage.TransitionCoverage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ExtractAdapter extends LightRefactoringTestCase {

  private static final String BEFORE_TEST_FILE_ENDING = ".java";
  private static final String AFTER_TEST_FILE_ENDING = ".after.java";

  private ExtractModel model;

  @Before
  public void setUp() throws Exception {
    super.setUp();
    model = new ExtractModel();
  }

  @After
  public void tearDown() throws Exception {
    super.tearDown();
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

  @Test
  public void testParameter() throws Exception {
    model.parameter();
    model.refactorField();
  }

  @Test
  public void test() throws Exception {
    choiceInput(ExtractModel.Choice.Parameter);
    refactorParameter();
  }

  public void choiceInput(ExtractModel.Choice choice) throws Exception {
   if (choice != null) {
     configureByFile(choice.getFileName() + BEFORE_TEST_FILE_ENDING);
   } else {
     throw new IllegalStateException("No choice selected");
   }
  }

  public void refactorVariable(String name) {
    new MockIntroduceVariableHandler(name, false, false, true, "boolean").invoke(getProject(), getEditor(), getFile(), null);
    validateResults(ExtractModel.Choice.Variable);
  }

  public void refactorConstant(String name) {
    new MockIntroduceConstantHandler(null, name).invoke(getProject(), getEditor(), getFile(), null);
    validateResults(ExtractModel.Choice.Constant);
  }

  public void refactorField() {
    new MockIntroduceFieldHandler(BaseExpressionToFieldHandler.InitializationPlace.IN_CURRENT_METHOD, false)
      .invoke(getProject(), myEditor, myFile, null);
    validateResults(ExtractModel.Choice.Field);
  }

  public void refactorParameter() {
      boolean enabled = true;
      try {
        enabled = myEditor.getSettings().isVariableInplaceRenameEnabled();
        myEditor.getSettings().setVariableInplaceRenameEnabled(false);
        new IntroduceParameterHandler().invoke(getProject(), myEditor, myFile, DataContext.EMPTY_CONTEXT);
        validateResults(ExtractModel.Choice.Parameter);
      }
      finally {
        myEditor.getSettings().setVariableInplaceRenameEnabled(enabled);
      }
  }

  public void refactorFunctionalParameter() {

  }

  public void refactorParameterObject() {

  }

  public void refactorMethod(){

  }

  public void refactorMethodObject(){

  }

  public void refactorDelegate(){

  }

  public void refactorInterface(){

  }

  public void refactorSuperclass(){

  }

  private void validateResults(ExtractModel.Choice choice) {
    checkResultByFile(choice.getFileName() + AFTER_TEST_FILE_ENDING);
  }

}
