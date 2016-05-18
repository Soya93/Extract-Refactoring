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

import com.intellij.ide.ui.AppearanceOptionsTopHitProvider;
import com.intellij.idea.IdeaTestApplication;
import com.intellij.openapi.application.Application;
import nz.ac.waikato.modeljunit.Action;
import nz.ac.waikato.modeljunit.FsmModel;
import org.junit.Test;

import java.util.Random;

public class ExtractModel implements FsmModel {

  public enum Choice {
    Variable,
    Constant,
    Field,
    Parameter,
    FunctionalParameter,
    ParameterObject,
    Method,
    MethodObject,
    Delegate,
    Interface,
    Superclass;

    private String fileName;

    private Choice() {
      this.fileName = "/refactoring/testExtract/Refactor" + this.name();
    }

    public String getFileName() {
      return fileName;
    }
  }

  private enum State {
    Home,
    Variable,
    Constant,
    Field,
    Parameter,
    FunctionalParameter,
    ParameterObject,
    Method,
    MethodObject,
    Delegate,
    Interface,
    Superclass,
    RefactorVariable,
    RefactorConstant,
    RefactorField,
    RefactorParameter,
    RefactorFunctionalParameter,
    RefactorParameterObject,
    RefactorMethod,
    RefactorMethodObject,
    RefactorDelegate,
    RefactorInterface,
    RefactorSuperclass
  }

  private State state = State.Home;
  private ExtractAdapter adapter = new ExtractAdapter();
  private Random random = new Random();

  @Override
  public Object getState() {
    return state;
  }

  @Override
  public void reset(boolean b) {
    state = State.Home;
  }

  @Test
  public void test() throws Exception {
    this.variable();
    this.refactorVariable();
  }

  @Action
  public void variable() throws Exception {
    state = State.Variable;
    if(variableGuard()) {
      adapter.choiceInput(Choice.Variable);
      state = State.RefactorVariable;
    } else {
      state = State.Home;
    }
  }

  private boolean variableGuard(){
    return state == State.Variable && isExpression();
  }

  @Action
  public void refactorVariable() {
    if(refactorVariableGuard()) {
      adapter.refactorVariable("b");
      state = State.Home;
    }
  }

  private boolean refactorVariableGuard(){
    return state == State.RefactorVariable;
  }

  @Action
  public void constant() throws Exception {
    if(constantGuard()) {
      state = State.RefactorConstant;
      adapter.choiceInput(Choice.Constant);
    } else {
      state = State.Home;
    }
  }

  private boolean constantGuard(){
    return state == State.Constant && (isExpression() || isLocalVar());
  }

  @Action
  public void refactorConstant() {
    if(refactorConstantGuard()){
      adapter.refactorConstant("CONSTANT");
      state = State.Home;
    }
  }

  private boolean refactorConstantGuard(){
    return state == State.RefactorConstant;
  }

  @Action
  public void field() throws Exception {
    if(fieldGuard()) {
      state = State.RefactorField;
      adapter.choiceInput(Choice.Field);
    } else {
      state = State.Home;
    }
  }

  private boolean fieldGuard(){
    return state == State.Field && !isVoid();
  }

  @Action
  public void refactorField() {
    if(refactorFieldGuard()){
      adapter.refactorField();
      state = State.Home;
    }
  }

  private boolean refactorFieldGuard(){
    return state == State.RefactorConstant;
  }

  @Action
  public void parameter() throws Exception {
    if(parameterGuard()) {
      state = State.RefactorParameter;
      adapter.choiceInput(Choice.Parameter);
    } else {
      state = State.Home;
    }
  }

  private boolean parameterGuard(){
    return state == State.Parameter && (isExpression() || isLocalVar());
  }

  @Action
  public void refactorParameter() {
    if(refactorFieldGuard()) {
      adapter.refactorParameter();
      state = State.Home;
    }
  }

  private boolean refactorParameterGuard(){
    return state == State.RefactorParameter;
  }

  @Action
  public void functionalParameter() {
    if(functionalParameterGuard()) {
      state = State.RefactorFunctionalParameter;
    } else {
      state = State.Home;
    }
  }

  private boolean functionalParameterGuard(){
    return state == State.FunctionalParameter && isSupportedContext();
  }

  public void refactorFunctionalParameter() {
    if(refactorFunctionalParameterGuard()) {
      adapter.refactorFunctionalParameter();
      state = State.Home;
    }
  }

  private boolean refactorFunctionalParameterGuard(){
    return state == State.RefactorFunctionalParameter;
  }

  @Action
  public void parameterObject() {
    if(parameterObjectGuard()) {
      state = State.RefactorParameterObject;
    } else {
      state = State.Home;
    }
  }

  private boolean parameterObjectGuard(){
    return state == State.ParameterObject && (isMethod() && methodHasParams());
  }

  public void refactorParameterObject() {
    if(refactorParameterObjectGuard()) {
      adapter.refactorParameterObject();
      state = State.Home;
    }
  }

  private boolean refactorParameterObjectGuard(){
    return state == State.RefactorParameterObject;
  }

  @Action
  public void method() {
    if(methodGuard()) {
      state = State.RefactorMethod;
    } else {
      state = State.Home;
    }
  }

  private boolean methodGuard(){
    return state == State.Method &&  (isExpression() || isStatement());
  }

  @Action
  public void refactorMethod() {
    if(refactorMethodGuard()) {
      adapter.refactorMethod();
      state = State.Home;
    }
  }

  private boolean refactorMethodGuard(){
    return state == State.RefactorMethod;
  }

  @Action
  public void methodObject() {
    if(methodObjectGuard()) {
      state = State.RefactorMethodObject;
    } else {
      state = State.Home;
    }
  }

  private boolean methodObjectGuard(){
    return state == State.MethodObject &&  (isExpression() || isStatement());
  }

  @Action
  public void refactorMethodObject() {
    if(refactorMethodObjectGuard()) {
      adapter.refactorMethodObject();
      state = State.Home;
    }
  }

  private boolean refactorMethodObjectGuard(){
    return state == State.RefactorMethodObject;
  }

  @Action
  public void delegate() {
    if(delegateGuard()) {
      // adapter.delegate(); //setName, setPackage, setMembers, setVisibility
      state = State.RefactorDelegate;
    } else { //cancel()
      state = State.Home;
    }
  }

  private boolean delegateGuard(){
    return state == State.Delegate;
  }

  @Action
  public void refactorDelegate() {
    if(refactorDelegateGuard()) {
      adapter.refactorDelegate();
      state = State.Home;
    }
  }

  private boolean refactorDelegateGuard(){
    return state == State.RefactorDelegate;
  }

  @Action
  public void iinterface() {
    if(iinterfaceGuard()) {
      //adapter.iinterface(); // selectMethodFromClass, setName, setPackage, setMembers
      state = State.RefactorInterface;
    } else { //cancel()
      state = State.Home;
    }
  }

  private boolean iinterfaceGuard(){
    return state == State.Interface;
  }

  @Action
  public void refactorInterface() {
    if(refactorInterfaceGuard()) {
      adapter.refactorInterface();
      state = State.Home;
    }
  }

  private boolean refactorInterfaceGuard(){
    return state == State.RefactorInterface;
  }

  @Action
  public void superclass() {
    if(superclassGuard()) {
      //adapter.superclass(); // selectMethodFromClass, setName, setPackage, setMembers
      state = State.RefactorSuperclass;
    } else { //cancel()
      state = State.Home;
    }
  }

  private boolean superclassGuard(){
    return state == State.Superclass;
  }

  @Action
  public void refactorSuperclass() {
    if(refactorSuperclassGuard()) {
      adapter.refactorSuperclass();
      state = State.Home;
    }
  }

  private boolean refactorSuperclassGuard(){
    return state == State.Superclass;
  }

  //Used in variable, constant, parameter, method, method object,
  private boolean isExpression(){
    return random.nextBoolean();
  }

  //Used in constant, parameter,
  private boolean isLocalVar(){
    return random.nextBoolean();
  }

  //Used in functional parameter
  private boolean isSupportedContext(){
    return random.nextBoolean();
  }

  //Used in field
  private boolean isVoid(){
    return random.nextBoolean();
  }

  //Used in parameter object,
  private boolean isMethod(){
    return random.nextBoolean();
  }

  //Used in parameter object,
  private boolean methodHasParams(){
    return random.nextBoolean();
  }

  //Used in method, method object,
  private boolean isStatement(){
    return random.nextBoolean();
  }

}
