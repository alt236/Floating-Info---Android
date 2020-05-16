/*
 * Copyright 2016 Alexandros Schillings
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
package uk.co.alt236.floatinginfo.util;

import android.util.Log;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class ExecTerminal {
    final String TAG = this.getClass().getName();

    public String exec(final String cmd) {
        Log.d(TAG, "^ Executing '" + cmd + "'");
        try {
            final Process process = Runtime.getRuntime().exec("sh");
            final DataInputStream is = new DataInputStream(process.getInputStream());
            final DataOutputStream os = new DataOutputStream(process.getOutputStream());
            os.writeBytes(cmd + "\n");
            os.writeBytes("exit\n");
            os.flush();
            os.close();

            final BufferedReader reader = new BufferedReader(
                    new InputStreamReader(is));
            try {
                StringBuilder fullOutput = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    fullOutput.append(line).append("\n");
                }
                return fullOutput.toString();
            } catch (final IOException e) {
                Log.e(TAG, "^ exec, IOException 1");
                e.printStackTrace();
            }

            process.waitFor();

        } catch (final IOException e) {
            Log.e(TAG, "^ exec, IOException 2");
            e.printStackTrace();

        } catch (final InterruptedException e) {
            Log.e(TAG, "^ exec, InterruptedException");
            e.printStackTrace();
        }
        return "";
    }

    public String execSu(final String cmd) {
        Log.d(TAG, "^ Executing as SU '" + cmd + "'");
        try {
            final Process process = Runtime.getRuntime().exec("su");
            final DataInputStream is = new DataInputStream(process.getInputStream());
            final DataOutputStream os = new DataOutputStream(process
                    .getOutputStream());
            os.writeBytes(cmd + "\n");
            os.writeBytes("exit\n");
            os.flush();
            os.close();

            final BufferedReader reader = new BufferedReader(new InputStreamReader(is));

            try {
                String fullOutput = "";
                String line;
                while ((line = reader.readLine()) != null) {
                    fullOutput = fullOutput + line + "\n";
                }
                return fullOutput;
            } catch (final IOException e) {// It seems IOException is thrown when it reaches EOF.
                e.printStackTrace();
                Log.e(TAG, "^ execSU, IOException 1");
            }
            process.waitFor();

        } catch (final IOException e) {
            e.printStackTrace();
            Log.e(TAG, "^ execSU, IOException 2");
        } catch (final InterruptedException e) {
            e.printStackTrace();
            Log.e(TAG, "^ execSU, InterruptedException");
        }
        return "";
    }
}
