/**
 * Copyright 2016 JustWayward Team
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.wydlb.first.view.loadding;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import androidx.core.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.wydlb.first.R;


public class CustomDialog extends Dialog {

    public CustomDialog(Context context) {
        this(context, 0);
    }

    public CustomDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public static CustomDialog instance(Activity activity) {
        FrameLayout v = (FrameLayout) View.inflate(activity, R.layout.common_progress_view, null);
        LoadingView loadingView=v.findViewById(R.id.dialog_loading_view);
        loadingView.setColor(ContextCompat.getColor(activity, R.color.white_text_color));
        CustomDialog dialog = new CustomDialog(activity, R.style.loading_dialog);
        dialog.setContentView(v,
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT)
        );
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(true);
        return dialog;
    }
}
