/**
 * Copyright [2017] [Yahya Hassanzadeh-Nazarabadi]
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
public class Main {

    public static final int KUSIS_ID = 54561;
    public static final int DD = 05;
    public static final int TOTAL_NUMBER_OF_PORTS = 65535;
    public static final int SSL_PORT = (KUSIS_ID + DD) % TOTAL_NUMBER_OF_PORTS;

    public static void main(String args[]) {
        CertificateConnection certificateConnection = new CertificateConnection(4444);
        certificateConnection.start();

        SSLServer s = new SSLServer(SSL_PORT);
    }

}
