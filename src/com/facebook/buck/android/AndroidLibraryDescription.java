/*
 * Copyright 2014-present Facebook, Inc.
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

package com.facebook.buck.android;

import com.facebook.buck.java.AnnotationProcessingParams;
import com.facebook.buck.java.JavaCompilerEnvironment;
import com.facebook.buck.java.JavaLibraryDescription;
import com.facebook.buck.java.JavacOptions;
import com.facebook.buck.rules.BuildRuleParams;
import com.facebook.buck.rules.BuildRuleType;
import com.facebook.buck.rules.Description;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

import java.nio.file.Path;

public class AndroidLibraryDescription implements Description<AndroidLibraryDescription.Arg> {

  public static final BuildRuleType TYPE = new BuildRuleType("android_library");
  private final JavaCompilerEnvironment javacEnv;

  public AndroidLibraryDescription(JavaCompilerEnvironment javacEnv) {
    this.javacEnv = Preconditions.checkNotNull(javacEnv);
  }

  @Override
  public BuildRuleType getBuildRuleType() {
    return TYPE;
  }

  @Override
  public Arg createUnpopulatedConstructorArg() {
    return new Arg();
  }

  @Override
  public AndroidLibrary createBuildable(BuildRuleParams params, Arg args) {
    JavacOptions.Builder javacOptions = JavaLibraryDescription.getJavacOptions(args, javacEnv);

    AnnotationProcessingParams annotationParams =
        args.buildAnnotationProcessingParams(params.getBuildTarget());
    javacOptions.setAnnotationProcessingData(annotationParams);

    return new AndroidLibrary(
        params,
        args.srcs.get(),
        args.resources.get(),
        args.proguardConfig,
        args.postprocessClassesCommands.get(),
        args.exportedDeps.get(),
        javacOptions.build(),
        args.manifest
    );

  }

  public static class Arg extends JavaLibraryDescription.Arg {
    public Optional<Path> manifest;
  }
}
