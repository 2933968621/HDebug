
package net.blueheart.hdebug.file.configs;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.blueheart.hdebug.HDebug;
import net.blueheart.hdebug.features.module.Module;
import net.blueheart.hdebug.file.FileConfig;
import net.blueheart.hdebug.file.FileManager;

import java.io.*;
import java.util.Iterator;
import java.util.Map;

public class ModulesConfig extends FileConfig {

    /**
     * Constructor of config
     *
     * @param file of config
     */
    public ModulesConfig(final File file) {
        super(file);
    }

    /**
     * Load config from file
     *
     * @throws IOException
     */
    @Override
    protected void loadConfig() throws IOException {
        final JsonElement jsonElement = new JsonParser().parse(new BufferedReader(new FileReader(getFile())));

        if(jsonElement instanceof JsonNull)
            return;

        final Iterator<Map.Entry<String, JsonElement>> entryIterator = jsonElement.getAsJsonObject().entrySet().iterator();
        while(entryIterator.hasNext()) {
            final Map.Entry<String, JsonElement> entry = entryIterator.next();
            final Module module = HDebug.moduleManager.getModule(entry.getKey());

            if(module != null) {
                final JsonObject jsonModule = (JsonObject) entry.getValue();

                module.setState(jsonModule.get("State").getAsBoolean());
                module.setKeyBind(jsonModule.get("KeyBind").getAsInt());
            }
        }
    }

    /**
     * Save config to file
     *
     * @throws IOException
     */
    @Override
    protected void saveConfig() throws IOException {
        final JsonObject jsonObject = new JsonObject();

        for (final Module module : HDebug.moduleManager.getModules()) {
            final JsonObject jsonMod = new JsonObject();
            jsonMod.addProperty("State", module.getState());
            jsonMod.addProperty("KeyBind", module.getKeyBind());
            jsonObject.add(module.getName(), jsonMod);
        }

        final PrintWriter printWriter = new PrintWriter(new FileWriter(getFile()));
        printWriter.println(FileManager.PRETTY_GSON.toJson(jsonObject));
        printWriter.close();
    }
}
