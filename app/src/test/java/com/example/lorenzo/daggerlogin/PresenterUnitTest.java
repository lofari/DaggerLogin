package com.example.lorenzo.daggerlogin;

import com.example.lorenzo.daggerlogin.login.LoginActivityMVP;
import com.example.lorenzo.daggerlogin.login.LoginActivityPresenter;
import com.example.lorenzo.daggerlogin.login.User;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class PresenterUnitTest {

    LoginActivityPresenter presenter;
    User user;

    LoginActivityMVP.Model mockedModel;
    LoginActivityMVP.View mockedView;

    @Before
    public void initialization(){
        mockedModel = mock(LoginActivityMVP.Model.class);
        mockedView = mock(LoginActivityMVP.View.class);

        user = new User("Mocked","Dude");

//        when(mockedModel.getUser()).thenReturn(user);

//        when(mockedView.getFirstName()).thenReturn("Mocked");
//        when(mockedView.getLastName()).thenReturn("Dude");

        presenter = new LoginActivityPresenter(mockedModel);
        presenter.setView(mockedView);

    }

    @Test
    public void noExistInteractionWithView(){
        presenter.getCurrentUser();

        verify(mockedView, times(1)).showUserNotAvailable();

    }

    @Test
    public void loadUserFromRepoWhenValidUserIsPresent(){
        when(mockedModel.getUser()).thenReturn(user);

        presenter.getCurrentUser();

        // testeo la interaccion con el modelo de datos
        verify(mockedModel, times(1)).getUser();

        //testeo la interaccion con la vista
        verify(mockedView, times(1)).setFirstName("Mocked");
        verify(mockedView,times(1)).setLastName("Dude");
        verify(mockedView,never()).showUserNotAvailable();

    }

    @Test
    public void ShowErrorMessageWhenUserIsNull(){
        when(mockedModel.getUser()).thenReturn(null);

        presenter.getCurrentUser();

        // testeo la interaccion con el modelo de datos
        verify(mockedModel, times(1)).getUser();

        //testeo la interaccion con la vista
        verify(mockedView,never()).setFirstName("Mocked");
        verify(mockedView,never()).setLastName("Dude");
        verify(mockedView,times(1)).showUserNotAvailable();
    }
}
