package com.example.lorenzo.daggerlogin.login;

public class LoginActivityModel implements LoginActivityMVP.Model{

    private LoginRepository repository;

    public LoginActivityModel(LoginRepository repository){
        this.repository = repository;
    }

    @Override
    public void createUser(String firstName, String lastName) {
        //LOGICA DE negocio aca
        repository.saveUser(new User(firstName, lastName));
    }

    @Override
    public User getUser() {
        //LOGICA DE negocio aza
        return repository.getUser();
    }
}
