package com.example.physiplay.singletons;

import com.example.physiplay.Account;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class LoginRegisterSingleton {
    private static LoginRegisterSingleton instance;
    private Gson gson = new Gson();
    public List<Account> accountList = new ArrayList<>();
    public static LoginRegisterSingleton getInstance() {
        if (instance == null) {
            instance = new LoginRegisterSingleton();
        }
        return instance;
    }

    public void updateAccountList() {
        try {
            File file = new File("login.info");
            if (!file.createNewFile()) {
                try (FileReader reader = new FileReader("login.info")) {
                    Type accountType = new TypeToken<List<Account>>() {}.getType();
                    accountList = gson.fromJson(reader, accountType);
                    if (accountList == null) accountList = new ArrayList<>();
                }
                catch (Exception e) {
                    SettingsSingleton.getInstance().displayAlertErrorMessage("Something went wrong, please try again");
                }
            }
        }
        catch (IOException e) {
            SettingsSingleton.getInstance().displayAlertErrorMessage("Something went wrong, please try again");
        }
    }
    private LoginRegisterSingleton() {}
}
