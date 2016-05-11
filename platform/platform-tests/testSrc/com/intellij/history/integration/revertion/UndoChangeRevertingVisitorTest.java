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
package com.intellij.history.integration.revertion;

import com.intellij.history.core.Content;
import com.intellij.history.core.changes.ContentChange;
import com.intellij.history.core.changes.DeleteChange;
import com.intellij.history.core.changes.RenameChange;
import com.intellij.history.core.tree.FileEntry;
import com.intellij.history.integration.IntegrationTestCase;
import com.intellij.openapi.vfs.VirtualFile;
import org.junit.Test;

public class UndoChangeRevertingVisitorTest extends IntegrationTestCase {

  @Test
  public void testShouldRevert_invalidTest() throws Exception {
    UndoChangeRevertingVisitor ucrv = new UndoChangeRevertingVisitor(this.myGateway, 123L, 321L);
    long id = 1L;
    String testContent = "testContent";
    VirtualFile file = this.createFile("test.txt", testContent);
    Content content = getContent(testContent, false);
    boolean shouldRevert = ucrv.shouldRevert(new ContentChange(id, file.getPath(), content, 1L));
    assertEquals(false, shouldRevert);
  }

  @Test
  public void testShouldRevert_successful() throws Exception {
    long id = 1L;
    UndoChangeRevertingVisitor ucrv = new UndoChangeRevertingVisitor(this.myGateway, id, null);
    String fileName = "test.txt";
    String testContent = "testContent";
    VirtualFile file = this.createFile(fileName, testContent);
    boolean shouldRevert = ucrv.shouldRevert(new RenameChange(id, file.getPath(), "txt.test"));
    assertEquals(true, shouldRevert);
  }

  @Test
  public void testVisit() {
    try {
      long id = 1L;
      UndoChangeRevertingVisitor ucrv = new UndoChangeRevertingVisitor(this.myGateway, id, null);
      String fileName = "test.txt";
      String testContent = "testContent";
      VirtualFile file = this.createFile(fileName, testContent);
      DeleteChange change = new DeleteChange(id, file.getPath(), new FileEntry(fileName, getContent(testContent, true), 1L, false));
      ucrv.visit(change);
      assertEquals(true, file.exists());
      assertEquals(true, change.isDeletionOf(file.getPath()));
    } catch (Exception e) {
      assertFalse("Test triggered an Exception --> failed test", false);
    }
  }

  private Content getContent(String testContent, boolean isAvailable) {
    return new Content() {
      @Override
      public byte[] getBytes() {
        return testContent.getBytes();
      }

      @Override
      public boolean isAvailable() {
        return isAvailable;
      }

      @Override
      public void release() {
        return;
      }
    };
  }
}
