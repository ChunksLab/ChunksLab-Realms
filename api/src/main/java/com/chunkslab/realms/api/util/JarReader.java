/*
 * This file is part of ChunksLab-Gestures, licensed under the Apache License 2.0.
 *
 * Copyright (c) amownyy <amowny08@gmail.com>
 * Copyright (c) contributors
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

package com.chunkslab.realms.api.util;

import com.chunkslab.realms.api.RealmsAPI;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class JarReader {

    public static boolean checkIsLeaked() {
        JarFile jarFile = RealmsAPI.getJarFile();
        if (jarFile == null) return false;
        Enumeration<JarEntry> entries = jarFile.entries();

        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            String entryName = entry.getName();

            if (!entryName.endsWith(".class")) continue;
            if (entryName.contains("/")) continue;

            entryName = entry.getName().substring(0, 10);

            if (StringPatternMatching.calculateStringSimilarity(entryName,"DirectLeaks") > 0.8) return true;
            if (StringPatternMatching.calculateStringSimilarity(entryName,"module-info") > 0.8) return true;
        }
        return false;
    }

    public static String getManifestContent() {
        JarFile jarFile = RealmsAPI.getJarFile();
        if (jarFile == null) return "";
        Enumeration<JarEntry> entries = jarFile.entries();
        String manifest = "";

        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            String entryName = entry.getName();
            if (!entryName.contains("MANIFEST")) continue;
            InputStream manifestStream;
            try {
                manifestStream = jarFile.getInputStream(entry);
            } catch (Exception e) {
                e.printStackTrace();
                return "";
            }

            try {
                manifest = IOUtils.toString(manifestStream, StandardCharsets.UTF_8);
            } catch (IOException ignored) {
            }
        }
        return manifest;
    }

    public static class StringPatternMatching {
        public static double calculateStringSimilarity(String str1, String str2) {
            int str1Length = str1.length();
            int str2Length = str2.length();

            if (str1Length == 0 && str2Length == 0) {
                return 1.0;
            }

            int matchingDistance = Math.max(str1Length, str2Length) / 2 - 1;
            boolean[] str1Matches = new boolean[str1Length];
            boolean[] str2Matches = new boolean[str2Length];

            int matchingCount = 0;
            for (int i = 0; i < str1Length; i++) {
                int start = Math.max(0, i - matchingDistance);
                int end = Math.min(i + matchingDistance + 1, str2Length);

                for (int j = start; j < end; j++) {
                    if (!str2Matches[j] && str1.charAt(i) == str2.charAt(j)) {
                        str1Matches[i] = true;
                        str2Matches[j] = true;
                        matchingCount++;
                        break;
                    }
                }
            }

            if (matchingCount == 0) {
                return 0.0;
            }

            int transpositionCount = 0;
            int k = 0;
            for (int i = 0; i < str1Length; i++) {
                if (str1Matches[i]) {
                    int j;
                    for (j = k; j < str2Length; j++) {
                        if (str2Matches[j]) {
                            k = j + 1;
                            break;
                        }
                    }

                    if (str1.charAt(i) != str2.charAt(j)) {
                        transpositionCount++;
                    }
                }
            }

            transpositionCount /= 2;

            double jaroSimilarity = (double) matchingCount / str1Length;
            double jaroWinklerSimilarity = jaroSimilarity + (0.1 * transpositionCount * (1 - jaroSimilarity));

            return jaroWinklerSimilarity;
        }
    }


}