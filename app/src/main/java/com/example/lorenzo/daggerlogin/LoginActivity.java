package com.example.lorenzo.daggerlogin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lorenzo.daggerlogin.http.TwitchAPI;
import com.example.lorenzo.daggerlogin.http.stream.Datum;
import com.example.lorenzo.daggerlogin.http.stream.Stream;
import com.example.lorenzo.daggerlogin.http.twitch.Game;
import com.example.lorenzo.daggerlogin.http.twitch.Twitch;
import com.example.lorenzo.daggerlogin.login.LoginActivityMVP;
import com.example.lorenzo.daggerlogin.root.App;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

public class LoginActivity extends AppCompatActivity implements LoginActivityMVP.View {


    @Inject
    LoginActivityMVP.Presenter presenter;

    @Inject
    TwitchAPI twitchAPI;


    EditText firstName, lastName;
    Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((App) getApplication()).getComponent().inject(this);

        firstName = findViewById(R.id.nameEt);
        lastName = findViewById(R.id.passEt);
        loginButton = findViewById(R.id.buttonLogin);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.loginButtonClicked();
            }
        });

        //ejemplo de uso api twitch bien villero y bloqueando el hilo principal. for learning purposes TODO: pasar a modelo mvp
//        Call<Twitch> call = twitchAPI.getTopGames("tyoghmzdr0lf3c7284rwtrmuidfqli");
//
//        call.enqueue(new Callback<Twitch>() {
//            @Override
//            public void onResponse(Call<Twitch> call, Response<Twitch> response) {
//                List<Game> topGames = response.body().getGame();
//                for (Game game : topGames){
//                    System.out.println(game.getName());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Twitch> call, Throwable t) {
//                t.printStackTrace();
//            }
//        });

        twitchAPI.getTopGamesObservable("tyoghmzdr0lf3c7284rwtrmuidfqli")
                .flatMap(new Function<Twitch, Observable<Game>>() {
                    @Override
                    public Observable<Game> apply(Twitch twitch){
                        return Observable.fromIterable(twitch.getGame());
                    }
                })
                .flatMap(new Function<Game, Observable<String>>() {
                    @Override
                    public Observable<String> apply(Game game) {
                        // TODO TODO TODO
                        return Observable.just(game.getName());
                    }
                }).filter(new Predicate<String>() {
                    @Override
                    public boolean test(String s){
                        return s.contains("w") || s.contains("W");
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String s) {
                        System.out.println("Rx java says:"+ s );
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }
                });


//        twitchAPI.getStreamObservable("tyoghmzdr0lf3c7284rwtrmuidfqli")
//                .flatMap((Function<Stream, Observable<Datum>>) stream -> Observable.fromIterable(stream.getStreams()))
//                .flatMap( datum -> getDatumTitle(datum) )
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<String>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//
//                    }
//
//                    @Override
//                    public void onNext(String s) {
//                        System.out.println("Rx java says:"+ s );
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        e.printStackTrace();
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                });

        twitchAPI.getStreamObservable("tyoghmzdr0lf3c7284rwtrmuidfqli")
                .flatMap((Function<Stream, Observable<Datum>>) stream -> Observable.fromIterable(stream.getStreams()))
                .retry(2)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Datum>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Datum stream) {
                        printStreamInfo(stream);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }
                });


    }

    private void printStreamInfo(Datum stream) {
        Log.d("tuvieja", stream.toString());

        twitchAPI
                .getGameByIdObservable("tyoghmzdr0lf3c7284rwtrmuidfqli" , stream.getGameId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        game -> {
                            Log.d("tuvieja", game.toString());
                            System.out.println("Stream Title:" + stream.getTitle() + "\nPlaying" + game.getName() + "\n Streamer Username:" + stream.getUserName());
                        }

                );
    }


    @Override
    protected void onResume() {
        super.onResume();
        presenter.setView(this);
        presenter.getCurrentUser();
    }

    @Override
    public String getFirstName() {
        return this.firstName.getText().toString();
    }

    @Override
    public String getLastName() {
        return this.lastName.getText().toString();
    }

    @Override
    public void showUserNotAvailable() {
        Toast.makeText(this, "Error, el usuario no esta disponible", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showInputError() {
        Toast.makeText(this, "Error, el nombre y apellido tienen que estar completos", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showUserSaved() {
        Toast.makeText(this, "Usuario guardado correctamente", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setFirstName(String firstName) {
        this.firstName.setText(firstName);
    }

    @Override
    public void setLastName(String lastName) {
        this.lastName.setText(lastName);
    }
}
