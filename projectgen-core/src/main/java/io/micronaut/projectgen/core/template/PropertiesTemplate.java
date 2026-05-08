/*
 * Copyright 2017-2022 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.micronaut.projectgen.core.template;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.HexFormat;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static io.micronaut.projectgen.core.feature.config.Configuration.BLANK_LINE_PREFIX;
import static io.micronaut.projectgen.core.feature.config.Configuration.COMMENT_PREFIX;

public class PropertiesTemplate extends DefaultTemplate {

    private final Properties properties;

    public PropertiesTemplate(String path, Map<String, Object> config) {
        super(path);
        this.properties = transform(new LinkedProperties(), "", config);
    }

    @Override
    public void write(OutputStream outputStream) throws IOException {
        properties.store(outputStream, null);
    }

    private Properties transform(Properties finalConfig, String prefix, Map<String, Object> config) {
        for (Map.Entry<String, Object> entry : config.entrySet()) {
            transform(finalConfig, prefix + entry.getKey(), entry.getValue());
        }
        return finalConfig;
    }

    private void transform(Properties finalConfig, String prefix, Object value) {
        if (value instanceof Map) {
            transform(finalConfig, prefix + ".", (Map<String, Object>) value);
        } else if (value instanceof List list) {
            for (int i = 0; i < list.size(); i++) {
                transform(finalConfig, prefix + "[" + i + "]", list.get(i));
            }
        } else {
            finalConfig.put(prefix, value.toString());
        }
    }

    public class LinkedProperties extends Properties {
        private final HashSet<Object> keys = new LinkedHashSet<>();

        public LinkedProperties() {
        }

        public Iterable<Object> orderedKeys() {
            return Collections.list(keys());
        }

        public Enumeration<Object> keys() {
            return Collections.enumeration(keys);
        }

        public Object put(Object key, Object value) {
            keys.add(key);
            return super.put(key, value);
        }

        // Override Properties `store` method to avoid printing date
        public void store(OutputStream out, String comments)
            throws IOException {
            store0(new BufferedWriter(new OutputStreamWriter(out, StandardCharsets.ISO_8859_1)),
                comments,
                true);
        }

        private void store0(BufferedWriter bw, String comments, boolean escUnicode)
            throws IOException
        {
            if (comments != null) {
                writeComments(bw, comments);
            }
            //bw.write("#" + new Date().toString());
            //bw.newLine();
            synchronized (this) {
                for (Object keyObj : this.orderedKeys()) {
                    String key = keyObj.toString();
                    String val = getProperty(key);
                    if (key.startsWith(COMMENT_PREFIX)) {
                        bw.write("# " + val);
                        bw.newLine();

                    } else if (key.startsWith(BLANK_LINE_PREFIX)) {
                        bw.newLine();

                    } else {
                        key = saveConvert(key, true, escUnicode);
                        /* No need to escape embedded and trailing spaces for value, hence
                         * pass false to flag.
                         */
                        val = saveConvert(val, false, escUnicode);
                        bw.write(key + "=" + val);
                        bw.newLine();
                    }
                }
            }
            bw.flush();
        }
    }

    private String saveConvert(String theString,
                               boolean escapeSpace,
                               boolean escapeUnicode) {
        int len = theString.length();
        int bufLen = len * 2;
        if (bufLen < 0) {
            bufLen = Integer.MAX_VALUE;
        }
        StringBuilder outBuffer = new StringBuilder(bufLen);
        HexFormat hex = HexFormat.of().withUpperCase();
        for(int x=0; x<len; x++) {
            char aChar = theString.charAt(x);
            // Handle common case first, selecting largest block that
            // avoids the specials below
            if ((aChar > 61) && (aChar < 127)) {
                if (aChar == '\\') {
                    outBuffer.append('\\'); outBuffer.append('\\');
                    continue;
                }
                outBuffer.append(aChar);
                continue;
            }
            switch(aChar) {
                case ' ':
                    if (x == 0 || escapeSpace)
                        outBuffer.append('\\');
                    outBuffer.append(' ');
                    break;
                case '\t':outBuffer.append('\\'); outBuffer.append('t');
                    break;
                case '\n':outBuffer.append('\\'); outBuffer.append('n');
                    break;
                case '\r':outBuffer.append('\\'); outBuffer.append('r');
                    break;
                case '\f':outBuffer.append('\\'); outBuffer.append('f');
                    break;
                case '=': // Fall through
                case ':': // Fall through
                case '#': // Fall through
                case '!':
                    outBuffer.append('\\'); outBuffer.append(aChar);
                    break;
                default:
                    if (((aChar < 0x0020) || (aChar > 0x007e)) & escapeUnicode ) {
                        outBuffer.append("\\u");
                        outBuffer.append(hex.toHexDigits(aChar));
                    } else {
                        outBuffer.append(aChar);
                    }
            }
        }
        return outBuffer.toString();
    }

    private static void writeComments(BufferedWriter bw, String comments)
        throws IOException {
        HexFormat hex = HexFormat.of().withUpperCase();
        bw.write("#");
        int len = comments.length();
        int current = 0;
        int last = 0;
        while (current < len) {
            char c = comments.charAt(current);
            if (c > '\u00ff' || c == '\n' || c == '\r') {
                if (last != current)
                    bw.write(comments.substring(last, current));
                if (c > '\u00ff') {
                    bw.write("\\u");
                    bw.write(hex.toHexDigits(c));
                } else {
                    bw.newLine();
                    if (c == '\r' &&
                        current != len - 1 &&
                        comments.charAt(current + 1) == '\n') {
                        current++;
                    }
                    if (current == len - 1 ||
                        (comments.charAt(current + 1) != '#' &&
                            comments.charAt(current + 1) != '!'))
                        bw.write("#");
                }
                last = current + 1;
            }
            current++;
        }
        if (last != current)
            bw.write(comments.substring(last, current));
        bw.newLine();
    }

}
