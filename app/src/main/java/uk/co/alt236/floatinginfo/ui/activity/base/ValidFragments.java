/*
 * Copyright 2017 Alexandros Schillings
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

package uk.co.alt236.floatinginfo.ui.activity.base;

import java.util.HashSet;
import java.util.Set;

import uk.co.alt236.floatinginfo.ui.activity.main.AppearancePreferenceFragment;
import uk.co.alt236.floatinginfo.ui.activity.main.EnabledInfoPreferenceFragment;
import uk.co.alt236.floatinginfo.ui.activity.main.InfoPreferenceFragment;

/*package*/ class ValidFragments {
    private final Set<String> validClasses;

    public ValidFragments() {
        validClasses = new HashSet<>();
        validClasses.add(AppearancePreferenceFragment.class.getName());
        validClasses.add(EnabledInfoPreferenceFragment.class.getName());
        validClasses.add(InfoPreferenceFragment.class.getName());
    }

    public boolean isValidFragment(final String name) {
        return validClasses.contains(name);
    }

}
