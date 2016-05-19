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

import nz.ac.waikato.modeljunit.Action;
import nz.ac.waikato.modeljunit.FsmModel;

import java.util.ArrayList;
import java.util.List;
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

  private static final List<Choice> HOME_CHOICES;

  static {
    HOME_CHOICES = new ArrayList<>();
    HOME_CHOICES.add(Choice.Variable);
    HOME_CHOICES.add(Choice.Constant);
    HOME_CHOICES.add(Choice.Field);
    HOME_CHOICES.add(Choice.Parameter);
    // TODO Future implementation
    //HOME_CHOICES.add(Choice.FunctionalParameter);
    //HOME_CHOICES.add(Choice.ParameterObject);
    //HOME_CHOICES.add(Choice.Method);
    //HOME_CHOICES.add(Choice.MethodObject);
    //HOME_CHOICES.add(Choice.Delegate);
    //HOME_CHOICES.add(Choice.Interface);
    //HOME_CHOICES.add(Choice.Superclass);
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

  @Action
  public void home() throws Exception {
    if (state == State.Home) {
      Choice choice = HOME_CHOICES.get(random.nextInt(HOME_CHOICES.size()));
      adapter.choiceInput(choice); // "select text"
      state = State.valueOf(choice.name());
    }
  }

  @Action
  public void variable() throws Exception {
    if (state == State.Variable) {
      if(isExpression()) {
        adapter.choiceInput(Choice.Variable);
        state = State.RefactorVariable;
      } else {
        state = State.Home;
      }
    }
  }

  @Action
  public void refactorVariable() {
    if(state == State.RefactorVariable) {
      adapter.refactorVariable("b");
      state = State.Home;
    }
  }

  @Action
  public void constant() throws Exception {
    if (state == State.Constant) {
      if(isExpression() || isLocalVar()) {
        state =  State.RefactorConstant;
        adapter.choiceInput(Choice.Constant);
      } else {
        state = State.Home;
      }
    }
  }

  @Action
  public void refactorConstant() {
    if (state == State.RefactorConstant) {
      adapter.refactorConstant("CONSTANT");
      state = State.Home;
    }
  }

  @Action
  public void field() throws Exception {
    if(state == State.Field) {
      if (isNotVoid()) {
        state = State.RefactorField;
        adapter.choiceInput(Choice.Field);
      } else {
        state = State.Home;
      }
    }
  }

  @Action
  public void refactorField() {
    if(state == State.RefactorField){
      adapter.refactorField();
      state = State.Home;
    }
  }

  @Action
  public void parameter() throws Exception {
    if (state == State.Parameter) {
      if(isExpression() || isLocalVar()) {
        state = State.RefactorParameter;
        adapter.choiceInput(Choice.Parameter);
      } else {
        state = State.Home;
      }
    }
  }

  @Action
  public void refactorParameter() {
    if(state == State.RefactorParameter) {
      adapter.refactorParameter();
      state = State.Home;
    }
  }

  @Action
  public void functionalParameter() {
    if (state == State.FunctionalParameter) {
      if(isSupportedContext()) {
        state = State.RefactorFunctionalParameter;
      } else {
        state = State.Home;
      }
    }
  }

  public void refactorFunctionalParameter() {
    if(state == State.RefactorFunctionalParameter) {
      adapter.refactorFunctionalParameter();
      state = State.Home;
    }
  }

  @Action
  public void parameterObject() {
    if(state == State.ParameterObject) {
      if (isMethod() && methodHasParams()) {
        state = State.RefactorParameterObject;
      } else {
        state = State.Home;
      }
    }
  }

  public void refactorParameterObject() {
    if(state == State.RefactorParameterObject) {
      adapter.refactorParameterObject();
      state = State.Home;
    }
  }

  @Action
  public void method() {
    if(state == State.Method) {
      if (isExpression() || isStatement()) {
        state = State.RefactorMethod;
      } else {
        state = State.Home;
      }
    }
  }

  @Action
  public void refactorMethod() {
    if(state == State.RefactorMethod) {
      adapter.refactorMethod();
      state = State.Home;
    }
  }

  @Action
  public void methodObject() {
    if(state == State.MethodObject) {
      if (isExpression() || isStatement()) {
        state = State.RefactorMethodObject;
      } else {
        state = State.Home;
      }
    }
  }

  @Action
  public void refactorMethodObject() {
    if(state == State.RefactorMethodObject) {
      adapter.refactorMethodObject();
      state = State.Home;
    }
  }

  @Action
  public void delegate() {
    if(state == State.Delegate) {
      if (!isCancel()) {
        // adapter.delegate(); //setName, setPackage, setMembers, setVisibility
        state = State.RefactorDelegate;
      } else { //cancel()
        state = State.Home;
      }
    }
  }

  @Action
  public void refactorDelegate() {
    if(state == State.RefactorDelegate) {
      adapter.refactorDelegate();
      state = State.Home;
    }
  }

  @Action
  public void iinterface() {
    if(state == State.Interface) {
      if (!isCancel()) {
        //adapter.iinterface(); // selectMethodFromClass, setName, setPackage, setMembers
        state = State.RefactorInterface;
      } else { //cancel()
        state = State.Home;
      }
    }
  }

  @Action
  public void refactorInterface() {
    if(state == State.RefactorInterface) {
      adapter.refactorInterface();
      state = State.Home;
    }
  }

  @Action
  public void superclass() {
    if(state == State.Superclass) {
      if (!isCancel()) {
        //adapter.superclass(); // selectMethodFromClass, setName, setPackage, setMembers
        state = State.RefactorSuperclass;
      } else { //cancel()
        state = State.Home;
      }
    }
  }

  @Action
  public void refactorSuperclass() {
    if(state == State.RefactorSuperclass) {
      adapter.refactorSuperclass();
      state = State.Home;
    }
  }

  //Used in variable, constant, parameter, method, method object,
  private boolean isExpression(){
    return getBoolean();
  }

  //Used in constant, parameter,
  private boolean isLocalVar(){
    return getBoolean();
  }

  //Used in functional parameter
  private boolean isSupportedContext(){
    return getBoolean();
  }

  //Used in field
  private boolean isNotVoid(){
    return getBoolean();
  }

  //Used in parameter object,
  private boolean isMethod(){
    return getBoolean();
  }

  //Used in parameter object,
  private boolean methodHasParams(){
    return getBoolean();
  }

  //Used in method, method object,
  private boolean isStatement(){
    return getBoolean();
  }

  private boolean isCancel(){
    return getBoolean();
  }

  private boolean getBoolean() {
    return true;
  }

}
