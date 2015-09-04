package com.kazav.gabi.emt;

import org.apache.http.NameValuePair;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.zip.GZIPInputStream;

/**
 * Created by gabik on 1/5/15.
 * This class handles http get and post on AsyncTasks
 */
public class HttpHandler {
    public URL url = null;
    public String method;
    private HttpURLConnection conn = null;

    public HttpHandler(String url, String method)  {
        try {
            this.url = new URL(url);
            this.conn = (HttpURLConnection) this.url.openConnection();
            conn.setReadTimeout(60000);
            conn.setConnectTimeout(30000);
            conn.setRequestMethod(method);
            conn.setDoInput(true);
            conn.setDoOutput(true);

            this.method = method;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String makeCall() throws AppFlow.NoNetException{ return this.makeCall(""); }

    public String makeCall(List<NameValuePair> params) throws AppFlow.NoNetException{
        try {
            String params_str = getQuery(params);
            return makeCall(params_str);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String makeCallZ(String params) throws AppFlow.NoNetException, IOException {
        StringBuilder response = new StringBuilder();
        BufferedReader reader=null;
        try {
            if (params != null) {
                OutputStream out = new BufferedOutputStream(conn.getOutputStream());
                out.write(params.getBytes());
                out.flush();
                out.close();
            }
            InputStream instream=conn.getInputStream();
            String contentEncoding=conn.getHeaderField("Content-Encoding");
            if (contentEncoding != null && contentEncoding.equalsIgnoreCase("gzip")) {
                reader=new BufferedReader(new InputStreamReader(new GZIPInputStream(instream),"UTF-8"));
            }
            else {
                reader=new BufferedReader(new InputStreamReader(instream,"UTF-8"));
            }
            String line;
            while ((line=reader.readLine()) != null) {
                response.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new AppFlow.NoNetException("");
        } finally {
            if (reader != null) { reader.close(); }
            if (conn != null) { conn.disconnect(); }
        }
        return response.toString();
    }

    public String makeCall(String params) throws AppFlow.NoNetException{
        try {
            return makeCallZ(params);
        } catch (IOException e) {
            e.printStackTrace();
            throw new AppFlow.NoNetException("");
        }
    }

    public static String getQuery(List<NameValuePair> params) throws UnsupportedEncodingException {
        if (params == null) { return null; }

        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (NameValuePair pair : params)
        {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
        }

        return result.toString();
    }
}
