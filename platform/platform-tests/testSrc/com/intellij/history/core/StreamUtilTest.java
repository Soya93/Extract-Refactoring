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
package com.intellij.history.core;

import com.intellij.history.core.tree.FileEntry;
import com.intellij.history.integration.IntegrationTestCase;
import com.intellij.util.io.DataOutputStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class StreamUtilTest extends IntegrationTestCase {

  public void testWriteEntry() throws Exception {
    String name = "test";
    String testContent = "content";
    boolean isAvailable = false;
    Content content = new Content() {
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

    FileEntry fileEntry = new FileEntry(name, content, 1L, false);
    File file = new File("testFile");
    try (FileOutputStream output = new FileOutputStream(file);
         DataOutputStream dos = new DataOutputStream(output)) {

      // Function under test
      StreamUtil.writeEntry(dos, fileEntry);

      // Read the file we just wrote the entry to.
      FileInputStream input = new FileInputStream(file);
      // Read the id
      assertEquals(0, input.read()); // 0 corresponds to FileEntryChange.
      input.read(); // Skip a byte;
      // Name of file should start here
      char[] chars = new char[name.length()];
      for (int i = 0; i < name.length(); i++) {
        chars[i] = (char) input.read();
      }
      assertEquals(name, new String(chars));
      // Rest of file is encoded.
      file.delete();
    }
  }

}