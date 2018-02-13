package com.github.caoyouxin.taoke.api;

import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


public class TaoKeRetrofit {

    public final static String CDN_HOST = "http://192.168.0.136:8070/";
//    public final static String CDN_HOST = "http://192.168.1.115:8070/";
//    public final static String CDN_HOST = "http://server.tkmqr.com:8070/";

    public final static String HOST = "http://192.168.0.136:8080/api/";
//    public final static String HOST = "http://192.168.1.115:8080/api/";
//    public final static String HOST = "http://server.tkmqr.com:8080/api/";

    private static TaoKeRetrofit instance;

    private TaoKeService service;

    private TaoKeRetrofit() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
//                .hostnameVerifier((hostname, session) -> true)
//                .sslSocketFactory(createSSLSocketFactory())
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(HOST)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(TaoKeService.class);
//        service = new TaoKeTestService();
    }

    static TaoKeService getService() {
        if (instance == null) {
            instance = new TaoKeRetrofit();
        }
        return instance.service;
    }

    public static SSLSocketFactory createSSLSocketFactory() {
        SSLSocketFactory ssfFactory = null;

        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[]{new TrustAllCerts()}, new SecureRandom());

            ssfFactory = sc.getSocketFactory();
        } catch (Exception ignored) {
        }

        return ssfFactory;
    }

    public static class TrustAllCerts implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }
}
