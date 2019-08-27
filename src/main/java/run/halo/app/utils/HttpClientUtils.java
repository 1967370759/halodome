package run.halo.app.utils;


import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.lang.NonNull;

import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

public class HttpClientUtils {
    private final static int TIMEOUT = 5000;

    private HttpClientUtils() {
    }


    /**
     * 创建https端。
     *
     * @param timeout 连接超时(ms)
     * @throws KeyStoreException        密钥存储异常
     * @throws NoSuchAlgorithmException 没有这样的算法例外
     * @throws KeyManagementException   密钥管理的例外
     * @returnhttps端
     */
    @NonNull
    public static CloseableHttpClient createHttpsClient(int timeout) throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        SSLContext sslContext = new SSLContextBuilder().
                loadTrustMaterial(null, (certificate, authType) -> true).build();
        return HttpClients.custom()
                .setSSLContext(sslContext).setSSLHostnameVerifier(new NoopHostnameVerifier())
                .setDefaultRequestConfig(getReqeustConfig(timeout)).build();
    }

    /**
     * 请求配置。
     *
     * @param timeout 连接超时(ms)
     * @return 请求配置
     */
    private static RequestConfig getReqeustConfig(int timeout) {
        return RequestConfig.custom().setConnectTimeout(timeout)
                .setConnectionRequestTimeout(timeout)
                .setSocketTimeout(timeout)
                .build();
    }

    public static class MultipartFileResource extends ByteArrayResource{
        private final String filename;
        public MultipartFileResource(byte[] byteArray,String filename) {
            super(byteArray);
            this.filename=filename;

        }

        @Override
        public String getFilename() {
            return this.filename;
        }
    }


}
