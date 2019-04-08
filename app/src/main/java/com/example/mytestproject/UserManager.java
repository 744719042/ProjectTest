package com.example.mytestproject;

import java.util.ArrayList;
import java.util.List;

public class UserManager {
    public interface UserLoginListener {
        void onLogin();
    }

    private List<UserLoginListener> mUserListeners = new ArrayList<>();

    private static class UserManagerHolder {
        private static final UserManager INSTANCE = new UserManager();
    }

    public static UserManager getInstance() {
        return UserManagerHolder.INSTANCE;
    }

    public void registerListener(UserLoginListener listener) {
        mUserListeners.add(listener);
    }

    public void unregisterListener(UserLoginListener listener) {
        mUserListeners.remove(listener);
    }

    public void login() {
        notifyLogin();
    }

    private void notifyLogin() {
        for (UserLoginListener listener : mUserListeners) {
            listener.onLogin();
        }
    }
}
