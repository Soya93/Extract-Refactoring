/*
 * Copyright 2000-2015 JetBrains s.r.o.
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
package com.intellij.openapi.externalSystem.model.task.event;

import org.jetbrains.annotations.NotNull;

/**
 * @author Vladislav.Soroka
 * @since 12/16/2015
 */
public class TaskOperationDescriptorImpl extends OperationDescriptorImpl implements TaskOperationDescriptor {
  private static final long serialVersionUID = 1L;

  private final String myTaskName;

  public TaskOperationDescriptorImpl(String displayName, long eventTime, String taskName) {
    super(displayName, eventTime);
    myTaskName = taskName;
  }

  @NotNull
  @Override
  public String getTaskName() {
    return myTaskName;
  }
}