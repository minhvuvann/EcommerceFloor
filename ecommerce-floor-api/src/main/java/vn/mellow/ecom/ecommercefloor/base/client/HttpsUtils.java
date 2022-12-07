package vn.mellow.ecom.ecommercefloor.base.client;

import com.google.gson.Gson;
import vn.mellow.ecom.ecommercefloor.base.exception.ClientException;

import javax.net.ssl.*;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HttpsUtils {

    private static Logger logger = Logger.getLogger(HttpsUtils.class.getSimpleName());

    private static void disableSSL() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{new TrustSSLUtils()};
            // Install the all-trusting trust manager
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HostnameVerifier allHostsValid = new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String get(String url) throws ClientException {
        return pushRequest(url, null, "GET", null);
    }

    public static String post(String url, String body) throws ClientException {
        return pushRequest(url, body, "POST", null);
    }

    public static String put(String url, String body) throws ClientException {
        return pushRequest(url, body, "PUT", null);
    }

    public static String get(String url, String token) throws ClientException {
        return pushRequest(url, null, "GET", token);
    }

    public static String post(String url, String body, String token) throws ClientException {
        return pushRequest(url, body, "POST",  token);
    }

    public static String put(String url, String body, String token) throws ClientException {
        return pushRequest(url, body, "PUT",  token);
    }

    public static String delete(String url, String token) throws ClientException {
        return pushRequest(url, null, "DELETE",  token);
    }

    public static String delete(String url) throws ClientException {
        return pushRequest(url, null, "DELETE",  null);
    }

    private static String pushRequest(String urlRest, String body, String method, String token) throws ClientException {
        HttpURLConnection conn = null;
        InputStream inputStream = null;
        BufferedReader inBuffer = null;
        try {
            URL url = null;
            if (urlRest.startsWith("https")) {
                disableSSL();
                url = new URL(urlRest);
                conn = (HttpsURLConnection) url.openConnection();
            } else {
                url = new URL(urlRest);
                conn = (HttpURLConnection) url.openConnection();
            }
            if (null != token && token.length() > 0) {
                conn.setRequestProperty("Token", token);
            }
            conn.setConnectTimeout(120000);
            conn.setReadTimeout(120000);
            conn.setRequestMethod(method);
            conn.setConnectTimeout(15000);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);
            if (null != body && body.length() > 0) {
                OutputStreamWriter output = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
                output.write(body);
                output.flush();
                output.close();
            }

            String result = "";
            int responseCode = conn.getResponseCode();
            if (responseCode >= 400) {
                logger.log(Level.INFO, method + " " + urlRest);
                inputStream = conn.getErrorStream();
                String errorContent = readStream(inputStream);
                try {
                    Gson gson = new Gson();
                    HashMap<String, Object> errorData = gson.fromJson(errorContent, HashMap.class);
                    if (errorData.containsKey("errorCode")) {
                        String errorCode = (String) errorData.get("errorCode");
                        String errorMessage = (String) errorData.get("errorMessage");
                        String errorDetail = (String) errorData.get("errorDetail");
                        ClientException clientException = new ClientException(null, null, errorCode, errorMessage, errorDetail);
                        throw clientException;
                    } else if (errorData.containsKey("message")) {
                        String errorCode = String.valueOf(errorData.get("status"));
                        String errorMessage = (String) errorData.get("message");
                        String errorDetail = errorMessage;
                        ClientException clientException = new ClientException(null, null, errorCode, errorMessage, errorDetail);
                        throw clientException;
                    }
                } catch (Exception e) {
                    if (e instanceof ClientException) {
                        throw e;
                    }
                }
                logger.log(Level.INFO, "Error:" + errorContent);
                throw new Exception(errorContent);
            }
            inputStream = conn.getInputStream();
            result = readStream(inputStream);
            inputStream.close();
//            logger.log(Level.INFO,"Result:" + result);
            inputStream = null;
            return result;

        } catch (Exception ex) {
            if (ex instanceof ClientException) {
                throw (ClientException) ex;
            }
            throw new ClientException(ex.getMessage(), ex, "client_error", "Có lỗi trong quá trình thực hiện " + method + " " + urlRest, "Error sent request");
        } finally {
            if (null != conn) {
                conn.disconnect();
            }
            try {
                if (null != inBuffer) inBuffer.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (null != inputStream) inputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static String readStream(InputStream inputStream) throws Exception {
        String file = "";
        String UTF8 = "utf8";
        int BUFFER_SIZE = 8192;
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream,
                UTF8), BUFFER_SIZE);
        String str;
        while ((str = br.readLine()) != null) {
            file += str;
        }
        return file;
    }
}
