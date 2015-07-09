package com.example.yamaguchi.arduinotest.network;

import android.os.AsyncTask;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;

/**
 */
public class OAuthRequestAsyncTask extends AsyncTask<Void, Void, Void> {

    private RequestCallback mCallback;
    private String mCallbackUrl;
    private String mAuthUrl;
    private OAuthConsumer mConsumer;
    private OAuthProvider mProvider;

    OAuthRequestAsyncTask(OAuthConsumer comsumer, OAuthProvider provider, String callbackUrl, RequestCallback callback) {
        mConsumer = comsumer;
        mProvider = provider;
        mCallbackUrl = callbackUrl;
        mCallback = callback;
    }

    @Override
    protected Void doInBackground(Void... arg0) {
        //OAuth認証
        try {
            mAuthUrl = mProvider.retrieveRequestToken(mConsumer, mCallbackUrl);
        } catch (OAuthMessageSignerException e) {
            e.printStackTrace();
        } catch (OAuthNotAuthorizedException e) {
            e.printStackTrace();
        } catch (OAuthExpectationFailedException e) {
            e.printStackTrace();
        } catch (OAuthCommunicationException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void v) {
        mCallback.onComplete(mAuthUrl);
    }
}
