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
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.refactoring.extractclass.usageInfo.MakeMethodDelegate;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.Nullable;
import org.junit.Test;

import java.io.File;

public class ExtractAdapter extends LightRefactoringTestCase {

  private static final String BEFORE_TEST_FILE_ENDING = ".java";
  private static final String AFTER_TEST_FILE_ENDING = ".after.java";

  @Test
  public void test() throws Exception {
    // TODO Remove
    choiceInput(ExtractModel.Choice.Variable);
    refactorVariable("b");
  }

  public void choiceInput(ExtractModel.Choice choice) throws Exception {
   if (choice != null) {
      configureByFile(choice.getFileName() + BEFORE_TEST_FILE_ENDING);
    } else {
     throw new IllegalStateException("No choice selected");
   }
  }

  public void refactorVariable(String name) {
    MockIntroduceVariableHandler handler = new MockIntroduceVariableHandler(name, false, false, true, "boolean");
    handler.invoke(getProject(), getEditor(), getFile(), null);
    checkResultByFile(ExtractModel.Choice.Variable.getFileName() + AFTER_TEST_FILE_ENDING);
  }

  public void refactorConstant() {

  }

  public void refactorField() {

  }

  public void refactorParameter() {

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

}
