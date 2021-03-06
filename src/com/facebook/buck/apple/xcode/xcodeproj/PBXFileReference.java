/*
 * Copyright 2013-present Facebook, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain
 * a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package com.facebook.buck.apple.xcode.xcodeproj;

import com.facebook.buck.apple.xcode.XcodeprojSerializer;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import com.google.common.io.Files;

/**
 * Reference to a concrete file.
 */
public class PBXFileReference extends PBXReference {
  private static final ImmutableMap<String, String> FILE_TYPE_TO_FILE_TYPE_IDENTIFIERS =
      ImmutableMap.<String, String>builder()
          .put("a", "archive.ar")
          .put("app", "wrapper.application")
          .put("bdic", "file")
          .put("bin", "archive.macbinary")
          .put("bmp", "image.bmp")
          .put("bundle", "wrapper.cfbundle")
          .put("c", "sourcecode.c.c")
          .put("cc", "sourcecode.cpp.cpp")
          .put("cpp", "sourcecode.cpp.cpp")
          .put("css", "text.css")
          .put("cxx", "sourcecode.cpp.cpp")
          .put("dart", "sourcecode")
          .put("dylib", "compiled.mach-o.dylib")
          .put("exp", "sourcecode.exports")
          .put("framework", "wrapper.framework")
          .put("fsh", "sourcecode.glsl")
          .put("gyp", "sourcecode")
          .put("gypi", "text")
          .put("h", "sourcecode.c.h")
          .put("hxx", "sourcecode.cpp.h")
          .put("icns", "image.icns")
          .put("java", "sourcecode.java")
          .put("jar", "archive.jar")
          .put("jpeg", "image.jpeg")
          .put("jpg", "image.jpeg")
          .put("js", "sourcecode.javascript")
          .put("json", "text.json")
          .put("m", "sourcecode.c.objc")
          .put("mm", "sourcecode.cpp.objcpp")
          .put("nib", "wrapper.nib")
          .put("o", "compiled.mach-o.objfile")
          .put("octest", "wrapper.cfbundle")
          .put("pdf", "image.pdf")
          .put("pl", "text.script.perl")
          .put("plist", "text.plist.xml")
          .put("pm", "text.script.perl")
          .put("png", "image.png")
          .put("proto", "text")
          .put("py", "text.script.python")
          .put("r", "sourcecode.rez")
          .put("rez", "sourcecode.rez")
          .put("rtf", "text.rtf")
          .put("s", "sourcecode.asm")
          .put("storyboard", "file.storyboard")
          .put("strings", "text.plist.strings")
          .put("tif", "image.tiff")
          .put("tiff", "image.tiff")
          .put("tcc", "sourcecode.cpp.cpp")
          .put("ttf", "file")
          .put("vsh", "sourcecode.glsl")
          .put("xcassets", "folder.assetcatalog")
          .put("xcconfig", "text.xcconfig")
          .put("xcodeproj", "wrapper.pb-project")
          .put("xcdatamodel", "wrapper.xcdatamodel")
          .put("xcdatamodeld", "wrapper.xcdatamodeld")
          .put("xctest", "wrapper.cfbundle")
          .put("xib", "file.xib")
          .put("y", "sourcecode.yacc")
          .put("zip", "archive.zip")
          .build();

  private Optional<String> explicitFileType;

  public PBXFileReference(String name, String path, SourceTree sourceTree) {
    super(name, path, sourceTree);

    // this is necessary to prevent O(n^2) behavior in xcode project loading
    String fileType = FILE_TYPE_TO_FILE_TYPE_IDENTIFIERS.get(Files.getFileExtension(name));
    explicitFileType = Optional.fromNullable(fileType);
  }

  public Optional<String> getExplicitFileType() {
    return explicitFileType;
  }

  public void setExplicitFileType(Optional<String> explicitFileType) {
    this.explicitFileType = explicitFileType;
  }

  @Override
  public String isa() {
    return "PBXFileReference";
  }

  @Override
  public void serializeInto(XcodeprojSerializer s) {
    super.serializeInto(s);

    if (explicitFileType.isPresent()) {
      s.addField("explicitFileType", explicitFileType.get());
    }
  }
}
