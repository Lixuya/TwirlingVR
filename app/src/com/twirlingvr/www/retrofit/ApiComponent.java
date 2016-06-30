package com.twirlingvr.www.retrofit;

import com.twirlingvr.www.activity.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by zsj on 2016/3/5.
 */

@Singleton
@Component(modules = ApiModule.class)
public interface ApiComponent {
    void inject(MainActivity mainActivity);

    final class Initializer {
        private Initializer() {

        }

        public static ApiComponent init() {
            return null;
//            return DaggerApiComponent.builder().girlApiModule(new ApiModule()).build();
        }
    }
}