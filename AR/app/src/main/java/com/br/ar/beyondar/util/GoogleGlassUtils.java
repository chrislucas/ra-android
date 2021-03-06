/*
 * Copyright (C) 2014 BeyondAR
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
package com.br.ar.beyondar.util;

import android.os.Build;

public class GoogleGlassUtils {

	private static Boolean sIsGoogleGlass = null;

	public static boolean isGoogleGlass() {
		if (sIsGoogleGlass != null) {
			return sIsGoogleGlass;
		}
		String model = Build.MODEL.toLowerCase();

		if (model.contains("glass")) {
			sIsGoogleGlass = true;
			return true;
		}
		try {
			Class.forName("com.google.android.glass.timeline.TimelineManager");
			sIsGoogleGlass = true;
			return true;
		} catch (ClassNotFoundException e) {
			sIsGoogleGlass = false;
			return false;
		}
	}

}
