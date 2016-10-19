/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.android.documentsui.queries;

import android.os.Build;
import android.support.annotation.VisibleForTesting;
import android.text.TextUtils;
import android.util.Log;

import com.android.documentsui.base.DebugFlags;
import com.android.documentsui.base.EventHandler;

import java.util.ArrayList;
import java.util.List;

final class DebugCommandProcessor implements EventHandler<String> {

    /**
     *
     */
    private static final String COMMAND_PREFIX = "debug:";

    private static final String TAG = "DebugCommandProcessor";

    private final List<EventHandler<String[]>> mCommands = new ArrayList<>();

    public DebugCommandProcessor() {
        if (Build.IS_DEBUGGABLE) {
            mCommands.add(DebugCommandProcessor::quickViewer);
            mCommands.add(DebugCommandProcessor::gestureScale);
        }
    }

    @VisibleForTesting
    DebugCommandProcessor(EventHandler<String[]>... commands) {
        for (EventHandler<String[]> c : commands) {
            mCommands.add(c);
        }
    }

    @Override
    public boolean accept(String query) {
        if (query.length() > COMMAND_PREFIX.length() && query.startsWith(COMMAND_PREFIX)) {
            String[] tokens = query.substring(COMMAND_PREFIX.length()).split("\\s+");
            for (EventHandler<String[]> command : mCommands) {
                if (command.accept(tokens)) {
                    return true;
                }
            }
            Log.d(SearchViewManager.TAG, "Unrecognized debug command: " + query);
        }
        return false;
    }

    private static boolean quickViewer(String[] tokens) {
        if ("qv".equals(tokens[0])) {
            if (tokens.length == 2 && !TextUtils.isEmpty(tokens[1])) {
                DebugFlags.setQuickViewer(tokens[1]);
                Log.i(TAG, "Set quick viewer to: " + tokens[1]);
                return true;
            } else {
                Log.w(TAG, "Invalid command structure: " + TextUtils.join(" ", tokens));
            }
        } else if ("deqv".equals(tokens[0])) {
            Log.i(TAG, "Unset quick viewer");
            DebugFlags.setQuickViewer(null);
            return true;
        }
        return false;
    }

    private static boolean gestureScale(String[] tokens) {
        if ("gs".equals(tokens[0])) {
            if (tokens.length == 1) {
                DebugFlags.setGestureScaleEnabled(true);
                Log.i(TAG, "Set gesture scale enabled to: " + true);
                return true;
            }

            if (tokens.length == 2 && !TextUtils.isEmpty(tokens[1])) {
                boolean enabled = Boolean.valueOf(tokens[1]);
                DebugFlags.setGestureScaleEnabled(enabled);
                Log.i(TAG, "Set gesture scale enabled to: " + enabled);
                return true;
            }
            Log.w(TAG, "Invalid command structure: " + TextUtils.join(" ", tokens));
        }
        return false;
    }
}
