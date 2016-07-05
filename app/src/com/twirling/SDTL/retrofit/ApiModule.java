package com.twirling.SDTL.retrofit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by zsj on 2016/3/5.
 */
@Module
public class ApiModule {

    @Provides
    @Singleton
    public Api provideApi() {
        return new RetrofitManager().getService();
    }
}