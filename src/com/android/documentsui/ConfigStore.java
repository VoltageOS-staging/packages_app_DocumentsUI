/*
 * Copyright (C) 2024 The Android Open Source Project
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

package com.android.documentsui;

import com.android.modules.utils.build.SdkLevel;

public interface ConfigStore {
    boolean DEFAULT_PICKER_PRIVATE_SPACE_ENABLED = true;

    /**
     * @return if the Private-Space-in-DocsUI is enabled
     */
    default boolean isPrivateSpaceInDocsUIEnabled() {
        return DEFAULT_PICKER_PRIVATE_SPACE_ENABLED;
    }

    /**
     * Implementation of the {@link ConfigStore} that reads "real" configs from
     * {@link android.provider.DeviceConfig}. Meant to be used by the "production" code.
     */
    class ConfigStoreImpl implements ConfigStore {
        @Override
        public boolean isPrivateSpaceInDocsUIEnabled() {
            return SdkLevel.isAtLeastS();
        }

    }
}
