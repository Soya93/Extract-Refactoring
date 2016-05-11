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
package com.intellij.vcs.log.ui.actions;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.util.registry.Registry;
import com.intellij.vcs.log.impl.VcsLogContentProvider;
import com.intellij.vcs.log.impl.VcsLogManager;
import com.intellij.vcs.log.ui.VcsLogUiImpl;

public class OpenAnotherLogTabAction extends DumbAwareAction {
  protected OpenAnotherLogTabAction() {
    super("Open Another Log Tab", "Open Another Log Tab", AllIcons.General.Add);
  }

  @Override
  public void update(AnActionEvent e) {
    if (e.getProject() == null || !Registry.is("vcs.log.open.another.log.visible")) {
      e.getPresentation().setEnabledAndVisible(false);
      return;
    }
    VcsLogUiImpl mainLogUi = VcsLogManager.getInstance(e.getProject()).getMainLogUi();
    if (mainLogUi == null) {
      e.getPresentation().setEnabledAndVisible(false);
      return;
    }

    e.getPresentation().setEnabledAndVisible(true);
  }

  @Override
  public void actionPerformed(AnActionEvent e) {
    VcsLogContentProvider.openAnotherLogTab(e.getProject());
  }
}