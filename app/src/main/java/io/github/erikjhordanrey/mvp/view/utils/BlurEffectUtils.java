/**
 * Copyright 2015 Erik Jhordan Rey.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.erikjhordanrey.mvp.view.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import com.squareup.picasso.Transformation;

public class BlurEffectUtils implements Transformation {

  private static final int UP_LIMIT = 25;
  private static final int LOW_LIMIT = 1;
  protected final Context context;
  private final int blurRadius;

  public BlurEffectUtils(Context context, int radius) {
    this.context = context;

    if (radius < LOW_LIMIT) {
      this.blurRadius = LOW_LIMIT;
    } else if (radius > UP_LIMIT) {
      this.blurRadius = UP_LIMIT;
    } else {
      this.blurRadius = radius;
    }
  }

  @Override public Bitmap transform(Bitmap source) {

    Bitmap blurredBitmap;
    blurredBitmap = Bitmap.createBitmap(source);

    RenderScript renderScript = RenderScript.create(context);

    Allocation input =
        Allocation.createFromBitmap(renderScript, source, Allocation.MipmapControl.MIPMAP_FULL,
            Allocation.USAGE_SCRIPT);

    Allocation output = Allocation.createTyped(renderScript, input.getType());

    ScriptIntrinsicBlur script =
        ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript));

    script.setInput(input);
    script.setRadius(blurRadius);

    script.forEach(output);
    output.copyTo(blurredBitmap);

    return blurredBitmap;
  }

  @Override public String key() {
    return "blurred";
  }
}
