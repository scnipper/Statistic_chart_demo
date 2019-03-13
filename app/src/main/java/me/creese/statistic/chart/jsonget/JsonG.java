/*
 * Copyright 2018 creese
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.creese.statistic.chart.jsonget;


public class JsonG {

    private int index;

    public JsonG() {
    }


    public JsonEntity generateRoot(String json) {

        char[] chars = new char[json.length()];
        json.getChars(0, json.length(), chars, 0);

        while (index < chars.length) {
            char currChar = chars[index];

            if (currChar == '[') {

                index++;
                return readArray(chars);
            }
            if (currChar == '{') {

                index++;
                return readObject(chars);
            }

        }

        return null;
    }

    private JsonEntity readArray(char[] chars) {
        StringBuilder sb = new StringBuilder();
        JsonArray jsonArray = new JsonArray();

        while (index < chars.length) {
            char currChar = chars[index];


            switch (currChar) {
                case '[':
                    index++;
                    jsonArray.add(readArray(chars));
                    break;

                case '{':
                    index++;
                    jsonArray.add(readObject(chars));
                    break;
                case ']':
                    String s = sb.toString();
                    String[] split = s.split(",");

                    for (int i = 0; i < split.length; i++) {
                        if (!split[i].equals("")) jsonArray.add(new JsonEntity(split[i]));
                    }
                    return jsonArray;
                default:
                    if (currChar != '\n' && currChar != ' ' && currChar != '"') {
                        sb.append(currChar);
                    }

            }
            index++;

        }
        return jsonArray;
    }

    private JsonEntity readObject(char[] chars) {
        StringBuilder sb = new StringBuilder();
        JsonObject jsonObject = new JsonObject();

        while (index < chars.length) {
            char currChar = chars[index];

            switch (currChar) {
                case '[':


                    if (index > 0) {
                        StringBuilder str = new StringBuilder();
                        for (int i = index; i >= 0; i--) {
                            char ch = chars[i];

                            if (ch == ',' || ch == '{') {
                                sb = sb.deleteCharAt(sb.length() - 1);
                                break;
                            }
                            if (ch != ':' && ch != '"' && ch != '\n' && ch != ' ' && ch != '[') {
                                str.append(ch);

                                sb = sb.deleteCharAt(sb.length() - 1);
                            }

                        }

                        if (sb.length() > 0) {
                            if (sb.charAt(sb.length() - 1) == ',') {
                                sb = sb.deleteCharAt(sb.length() - 1);
                            }
                        }
                        index++;
                        JsonEntity value = readArray(chars);
                        jsonObject.addField(str.reverse().toString(), value);


                    }
                    break;

                case '{':
                    if (index > 0) {
                        StringBuilder str = new StringBuilder();
                        for (int i = index; i >= 0; i--) {
                            char ch = chars[i];

                            if (ch == ',') {
                                sb = sb.deleteCharAt(sb.length() - 1);
                                break;
                            }
                            if (ch != ':' && ch != '"' && ch != '\n' && ch != ' ' && ch != '[' && ch != '{') {
                                str.append(ch);

                                sb = sb.deleteCharAt(sb.length() - 1);
                            }

                        }

                        if (sb.length() > 0) {
                            if (sb.charAt(sb.length() - 1) == ',') {
                                sb = sb.deleteCharAt(sb.length() - 1);
                            }
                        }
                        index++;
                        JsonEntity value = readObject(chars);
                        jsonObject.addField(str.reverse().toString(), value);


                    }
                    break;
                case '}':
                    String s = sb.toString();
                    String[] split = s.split(",");

                    for (int i = 0; i < split.length; i++) {

                        if (!split[i].equals("")) {
                            String[] pair = split[i].split(":");

                            jsonObject.addField(pair[0], new JsonEntity(pair[1]));
                        }
                    }
                    return jsonObject;
                default:
                    if (currChar != '\n' && currChar != ' ' && currChar != '"') {
                        sb.append(currChar);
                    }

            }

            index++;

        }
        return jsonObject;
    }

}
