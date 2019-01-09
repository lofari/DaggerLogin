package com.example.lorenzo.daggerlogin.http;

import com.example.lorenzo.daggerlogin.http.stream.Stream;
import com.example.lorenzo.daggerlogin.http.twitch.Game;
import com.example.lorenzo.daggerlogin.http.twitch.Twitch;


import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TwitchAPI {

    @GET("games/top")
    Call<Twitch> getTopGames(@Header("Client-Id") String clientId);

    @GET("games/top")
    Observable<Twitch> getTopGamesObservable (@Header("Client-Id") String clientId);

    @GET("streams?first=20")
    Observable<Stream> getStreamObservable (@Header("Client-Id") String clientId);

    @GET("games")
    Observable<Game> getGameByIdObservable (@Header("Client-Id") String clientId, @Query(value = "id", encoded = true) String id);
    //@Path("id") String id  ,

}
