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

// Located under: intellij/platform/platform-tests/testSrc/com/intellij/history/core
package com.intellij.history.core;

import com.intellij.history.core.storage.TestContent;
import junit.framework.TestCase;
import org.junit.Test;

public class ContentTest extends TestCase {

  @Test
  public void testToString() throws Exception {
    String testContent = "testContent";
    Content content = getContent(testContent);
    assertEquals(testContent, content.toString());
  }

  @Test
  public void testEquals() throws Exception {
    Content content1 = getContent("test1");
    Content content2 = getContent("test2");
    assertTrue(content1.equals(content2));
    assertTrue(content2.equals(content1));
    Content content3 = new TestContent("test".getBytes());
    assertFalse(content1.equals(content3));
  }

  private Content getContent(String content) {
    return new Content() {
      @Override
      public byte[] getBytes() {
        return content.getBytes();
      }

      @Override
      public boolean isAvailable() {
        return false;
      }

      @Override
      public void release() {

      }
    };
  }
}