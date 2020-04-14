package com.idea.church.commons;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import static com.idea.church.commons.Constants.ACCOUNT_TYPE;
import static com.idea.church.commons.Constants.AUTH_TOKEN_TYPE;

public class HeaderInterceptor implements Interceptor {
    private Context context;

    public HeaderInterceptor(Context context) {
       this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        if (context != null) {
            AccountManager accountManager = AccountManager.get(context);
            Account[] accounts = accountManager.getAccountsByType(ACCOUNT_TYPE);
            if (accounts.length > 0) {
                Account account = accounts[0];
                String authToken = accountManager.peekAuthToken(account, AUTH_TOKEN_TYPE);
                Request request = chain.request()
                        .newBuilder()
                        .addHeader("Authorization", "Token " + authToken)
                        .build();
                Response response = chain.proceed(request);
                if (response.code() == 401) {
                    //                accountManager.removeAccount(account, null, null, null);
                }
                return response;
            }
        }
        return chain.proceed(chain.request());
    }
}
