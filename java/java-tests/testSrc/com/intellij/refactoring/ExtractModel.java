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

import com.intellij.refactoring.ExtractAdapter;
import nz.ac.waikato.modeljunit.Action;
import nz.ac.waikato.modeljunit.FsmModel;

public class ExtractModel implements FsmModel {

  public enum Choice implements GuardInterface {
    Variable(State.Variable) {
      @Override
      public boolean guard(State state) {
        return state.equals(State.Extract);
      }
    }; // TODO Similar for all other choices.

    private State nextState;

    private Choice(State nextState) {
      this.nextState = nextState;
    }
  }

  private enum State {
    Home,
    Extract,
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

  private String selectedText = "";
  private State state = State.Home;
  private Choice choice;
  private ExtractAdapter adapter = new ExtractAdapter();

  @Override
  public Object getState() {
    return state;
  }

  @Override
  public void reset(boolean b) {
    if (b) {
      state = State.Home;
      selectedText = "";
      choice = null;
    }
  }

  @Action
  public void selectTextInput() {
    adapter.selectTextInput(selectedText);
    state = State.Extract;
  }

  @Action
  public void choiceInput() throws Exception {
    choice = Choice.Variable;
    if (choiceInputGuard()) {
      adapter.choiceInput(choice);
      state = State.valueOf(choice.name());
    }
    reset(true);
  }

  public boolean choiceInputGuard() {
    return choice.guard(state) && selectedText != null && !selectedText.isEmpty();
  }

  // Variable
  @Action
  public void variable() {
    if(variableGuard()) {
      state = State.RefactorVariable;
    } else {
      state = State.Home;
    }
  }

  public boolean variableGuard(){
    return state == State.Variable && isExpression(selectedText);
  }

  @Action
  public void refactorVariable() {
    if(refactorVariableGuard()) {
      adapter.refactorVariable();
      state = State.Home;
    }
  }

  public boolean refactorVariableGuard(){
    return state == State.RefactorVariable;
  }


  // Constant
  @Action
  public void constant() {
    if(constantGuard()) {
      state = State.RefactorConstant;
    } else {
      state = State.Home;
    }
  }

  public boolean constantGuard(){
    return state == State.Constant && (isExpression(selectedText) || isLocalVar(selectedText));
  }

  @Action
  public void refactorConstant() {
    if(refactorConstantGuard()){
      adapter.refactorConstant();
      state = State.Home;
    }
  }

  public boolean refactorConstantGuard(){
    return state == State.RefactorConstant;
  }

  // Field
  @Action
  public void field() {
    if(fieldGuard()) {
      state = State.RefactorField;
    } else {
      state = State.Home;
    }
  }

  public boolean fieldGuard(){
    return state == State.Field && !isVoid(selectedText);
  }

  @Action
  public void refactorField() {
    if(refactorFieldGuard()){
      adapter.refactorField();
      state = State.Home;
    }
  }

  public boolean refactorFieldGuard(){
    return state == State.RefactorConstant;
  }


  //Parameter
  @Action
  public void parameter() {
    if(parameterGuard()) {
      state = State.RefactorParameter;
    } else {
      state = State.Home;
    }
  }

  public boolean parameterGuard(){
    return state == State.Parameter && (isExpression(selectedText) || isLocalVar(selectedText));
  }

  @Action
  public void refactorParameter() {
    if(refactorFieldGuard()) {
      adapter.refactorParameter();
      state = State.Home;
    }
  }

  public boolean refactorParameterGuard(){
    return state == State.RefactorParameter;
  }


  //Functional Parameter
  @Action
  public void functionalParameter() {
    if(functionalParameterGuard()) {
      state = State.RefactorFunctionalParameter;
    } else {
      state = State.Home;
    }
  }

  public boolean functionalParameterGuard(){
    return state == State.FunctionalParameter && isSupportedContext(selectedText);
  }

  public void refactorFunctionalParameter() {
    if(refactorFunctionalParameterGuard()) {
      adapter.refactorFunctionalParameter();
      state = State.Home;
    }
  }

  public boolean refactorFunctionalParameterGuard(){
    return state == State.RefactorFunctionalParameter;
  }


  //Refactor Parameter Object
  @Action
  public void parameterObject() {
    if(parameterObjectGuard()) {
      state = State.RefactorParameterObject;
    } else {
      state = State.Home;
    }
  }

  public boolean parameterObjectGuard(){
    return state == State.ParameterObject && (isMethod(selectedText) && methodHasParams(selectedText));
  }

  public void refactorParameterObject() {
    if(refactorParameterObjectGuard()) {
      adapter.refactorParameterObject();
      state = State.Home;
    }
  }

  public boolean refactorParameterObjectGuard(){
    return state == State.RefactorParameterObject;
  }


  //Method
  @Action
  public void method() {
    if(methodGuard()) {
      state = State.RefactorMethod;
    } else {
      state = State.Home;
    }
  }

  public boolean methodGuard(){
    return state == State.Method &&  (isExpression(selectedText) || isStatement(selectedText));
  }


  @Action
  public void refactorMethod() {
    if(refactorMethodGuard()) {
      adapter.refactorMethod();
      state = State.Home;
    }
  }

  public boolean refactorMethodGuard(){
    return state == State.RefactorMethod;
  }

  //Method Object

  @Action
  public void methodObject() {
    if(methodObjectGuard()) {
      state = State.RefactorMethodObject;
    } else {
      state = State.Home;
    }
  }

  public boolean methodObjectGuard(){
    return state == State.MethodObject &&  (isExpression(selectedText) || isStatement(selectedText));
  }

  @Action
  public void refactorMethodObject() {
    if(refactorMethodObjectGuard()) {
      adapter.refactorMethodObject();
      state = State.Home;
    }
  }

  public boolean refactorMethodObjectGuard(){
    return state == State.RefactorMethodObject;
  }


  //Delegate
  @Action
  public void delegate() {
    if(delegateGuard()) {
      //adapter.delegate(); //setName, setPackage, setMembers, setVisibility
      state = State.RefactorDelegate;
    } else { //cancel()
      state = State.Home;
    }
  }

  public boolean delegateGuard(){
    return state == State.Delegate;
  }

  @Action
  public void refactorDelegate() {
    if(refactorDelegateGuard()) {
      adapter.refactorDelegate();
      state = State.Home;
    }
  }

  public boolean refactorDelegateGuard(){
    return state == State.RefactorDelegate;
  }


  //Interface
  @Action
  public void iinterface() {
    if(iinterfaceGuard()) {
      //adapter.iinterface(); // selectMethodFromClass, setName, setPackage, setMembers
      state = State.RefactorInterface;
    } else { //cancel()
      state = State.Home;
    }
  }

  public boolean iinterfaceGuard(){
    return state == State.Interface;
  }

  @Action
  public void refactorInterface() {
    if(refactorInterfaceGuard()) {
      adapter.refactorInterface();
      state = State.Home;
    }
  }

  public boolean refactorInterfaceGuard(){
    return state == State.RefactorInterface;
  }

  //Superclass
  @Action
  public void superclass() {
    if(superclassGuard()) {
      //adapter.superclass(); // selectMethodFromClass, setName, setPackage, setMembers
      state = State.RefactorSuperclass;
    } else { //cancel()
      state = State.Home;
    }
  }

  public boolean superclassGuard(){
    return state == State.Superclass;
  }

  @Action
  public void refactorSuperclass() {
    if(refactorSuperclassGuard()) {
      adapter.refactorSuperclass();
      state = State.Home;
    }
  }

  public boolean refactorSuperclassGuard(){
    return state == State.Superclass;
  }

  public void setState(String choice){
    state = State.valueOf(choice);
  }

  private interface GuardInterface {
    public boolean guard(State state);
  }

  private boolean isExpression(String selectedText){
    //TODO
    return true;
  }

  private boolean isLocalVar(String selectedText){
    //TODO
    return true;
  }

  private boolean isVoid(String text) {
    // TODO
    return true;
  }

  private boolean isSupportedContext(String selectedText){
    //TODO
    return true;
  }

  private boolean isMethod(String selectedText){
    //TODO
    return true;
  }

  private boolean methodHasParams(String selectedText){
    //TODO
    return true;
  }

  private boolean isStatement(String selectedText){
    //TODO
    return true;
  }

}
