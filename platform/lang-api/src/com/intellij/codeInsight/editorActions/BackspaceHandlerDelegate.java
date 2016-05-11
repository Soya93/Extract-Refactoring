/*
 * Copyright 2000-2009 JetBrains s.r.o.
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

package com.intellij.codeInsight.editorActions;

import com.intellij.openapi.extensions.ExtensionPointName;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiFile;

/**
 * @author yole
 */
public abstract class BackspaceHandlerDelegate {
  public static final ExtensionPointName<BackspaceHandlerDelegate> EP_NAME = ExtensionPointName.create("com.intellij.backspaceHandlerDelegate");

  public abstract void beforeCharDeleted(char c, PsiFile file, Editor editor);
  public abstract boolean charDeleted(final char c, final PsiFile file, final Editor editor);
}