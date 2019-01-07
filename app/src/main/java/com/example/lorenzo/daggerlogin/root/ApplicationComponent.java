package com.example.lorenzo.daggerlogin.root;

import com.example.lorenzo.daggerlogin.login.LoginActivity;
import com.example.lorenzo.daggerlogin.login.LoginModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class, LoginModule.class})
public interface ApplicationComponent {

    void inject(LoginActivity target);
}
