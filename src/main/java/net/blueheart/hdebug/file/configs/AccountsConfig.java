
package net.blueheart.hdebug.file.configs;

import com.google.gson.Gson;
import net.blueheart.hdebug.file.FileConfig;
import net.blueheart.hdebug.file.FileManager;
import net.blueheart.hdebug.utils.login.MinecraftAccount;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class AccountsConfig extends FileConfig {
    private final List<MinecraftAccount> accounts = new ArrayList<>();
    public final List<MinecraftAccount> altManagerMinecraftAccounts = new ArrayList<>();

    public void removeAccount(MinecraftAccount account) {
        accounts.remove(account);
    }
    /**
     * Constructor of config
     *
     * @param file of config
     */
    public AccountsConfig(final File file) {
        super(file);
    }

    public List<MinecraftAccount> getAccounts() {
        return accounts;
    }
    /**
     * Load config from file
     *
     * @throws IOException
     */
    @Override
    protected void loadConfig() throws IOException {
        final List<String> accountList = new Gson().fromJson(new BufferedReader(new FileReader(getFile())), List.class);

        if(accountList == null)
            return;

        altManagerMinecraftAccounts.clear();

        for(final String account : accountList) {
            final String[] information = account.split(":");

            if(information.length >= 3)
                altManagerMinecraftAccounts.add(new MinecraftAccount(information[0], information[1], information[2]));
            else if(information.length == 2)
                altManagerMinecraftAccounts.add(new MinecraftAccount(information[0], information[1]));
            else
                altManagerMinecraftAccounts.add(new MinecraftAccount(information[0]));
        }
    }

    /**
     * Save config to file
     *
     * @throws IOException
     */
    @Override
    protected void saveConfig() throws IOException {
        final List<String> accountList = new ArrayList<>();

        for(final MinecraftAccount minecraftAccount : altManagerMinecraftAccounts)
            accountList.add(minecraftAccount.getName() + ":" + (minecraftAccount.getPassword() == null ? "" : minecraftAccount.getPassword()) + ":" + (minecraftAccount.getAccountName() == null ? "" : minecraftAccount.getAccountName()));

        final PrintWriter printWriter = new PrintWriter(new FileWriter(getFile()));
        printWriter.println(FileManager.PRETTY_GSON.toJson(accountList));
        printWriter.close();
    }
}
