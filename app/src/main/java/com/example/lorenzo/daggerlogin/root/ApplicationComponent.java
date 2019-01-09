package com.example.lorenzo.daggerlogin.root;

import com.example.lorenzo.daggerlogin.http.TwitchModule;
import com.example.lorenzo.daggerlogin.LoginActivity;
import com.example.lorenzo.daggerlogin.login.LoginModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class, LoginModule.class, TwitchModule.class})
public interface ApplicationComponent {

    void inject(LoginActivity target);
}
