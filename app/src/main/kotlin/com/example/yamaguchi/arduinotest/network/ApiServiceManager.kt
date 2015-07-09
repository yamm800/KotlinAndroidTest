package com.example.yamaguchi.arduinotest.network

import com.squareup.okhttp.OkHttpClient
import oauth.signpost.OAuthConsumer
import oauth.signpost.OAuthProvider
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer
import oauth.signpost.commonshttp.CommonsHttpOAuthProvider
import retrofit.RequestInterceptor
import retrofit.RestAdapter.Builder
import retrofit.client.OkClient
import java.util.concurrent.TimeUnit

/**

 */
public class ApiServiceManager {

    companion object {
        private val END_POINT = ""
        private val HTTP_CONNECTION_TIMEOUT_SEC = 30L

        private val CONSUMER_KEY = "<<Zaim開発センター登録時の情報>>";
        private val CONSUMER_SECRET = "<<Zaim開発センター登録時の情報>>";
        private val REQUEST_TOKEN_URL = "<<Zaim開発センター登録時の情報>>";
        private val AUTHORIZE_URL = "<<Zaim開発センター登録時の情報>>";
        private val ACCESS_TOKEN_URL = "<<Zaim開発センター登録時の情報>>";
    }

    /**
     * 全Apiサービスが利用するhttpクライアント。
     */
    private var mHttpClient: OkHttpClient? = null

    private var mService: ApiService? = null

    private var mConsumer : OAuthConsumer? = null
    private var mProvider : OAuthProvider? = null

    synchronized public fun getOkHttpClient(): OkHttpClient {
        if (mHttpClient != null) {
            return mHttpClient as OkHttpClient
        }

        mHttpClient = OkHttpClient()
        mHttpClient!!.setConnectTimeout(HTTP_CONNECTION_TIMEOUT_SEC, TimeUnit.SECONDS)
        mHttpClient!!.setReadTimeout(HTTP_CONNECTION_TIMEOUT_SEC, TimeUnit.SECONDS)
        mHttpClient!!.setWriteTimeout(HTTP_CONNECTION_TIMEOUT_SEC, TimeUnit.SECONDS)

        return mHttpClient as OkHttpClient
    }

    /** 共通ヘッダーを追加する interceptor  */
    public fun getRestRequestInterceptor(): RequestInterceptor {
        return object : RequestInterceptor {
            override fun intercept(request: RequestInterceptor.RequestFacade?) {

            }
        }
    }

    synchronized public fun doOAuthLogin() {
        mConsumer = CommonsHttpOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET)
        mProvider = CommonsHttpOAuthProvider(REQUEST_TOKEN_URL, ACCESS_TOKEN_URL, AUTHORIZE_URL)

    }

    synchronized public fun getApiService(): ApiService {
        if (mService != null) {
            return mService as ApiService
        }

        val builder = Builder().setEndpoint(END_POINT).setClient(OkClient(getOkHttpClient())).setRequestInterceptor(getRestRequestInterceptor())

        //        if (BuildConfig.DEBUG) {
        //            // debug build の場合ログを出す
        //            builder
        //                    .setLogLevel(LogLevel.FULL)
        //                    .setLog(new AndroidLog("ApiService"));
        //        }

        mService = builder.build().create<ApiService>(javaClass<ApiService>())
        return mService as ApiService
    }

    public interface ApiService

}
