/*
 * Copyright (C) 2021 dreamn(dream@dreamn.cn)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package cn.dreamn.qianji_auto.utils.tools;


import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.util.TypedValue;
import android.widget.TextView;

public class StyleUtil {

    public static final float TEXT_SIZE_DEFAULT = 15.0f;
    public static final float TEXT_SIZE_BIG = 17.0f;
    public static final float TEXT_SIZE_SMALL = 13.0f;

    public static final int TEXT_COLOR_DEFAULT = Color.BLACK;
    public static final int TEXT_COLOR_SECONDARY = 0xFF8A9899;

    public static final int LINE_COLOR_DEFAULT = 0xFFE5E5E5;

    public static void apply(TextView textView) {
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, TEXT_SIZE_DEFAULT);
        textView.setTextColor(TEXT_COLOR_DEFAULT);
    }

    public static boolean isDarkMode(Context context) {
        return Configuration.UI_MODE_NIGHT_YES == (context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK);
    }
}
