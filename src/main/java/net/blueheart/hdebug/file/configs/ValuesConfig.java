package net.blueheart.hdebug.file.configs;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.blueheart.hdebug.HDebug;
import net.blueheart.hdebug.features.module.Module;
import net.blueheart.hdebug.features.module.modules.misc.IRC;
import net.blueheart.hdebug.features.special.AntiForge;
import net.blueheart.hdebug.features.special.BungeeCordSpoof;
import net.blueheart.hdebug.file.FileConfig;
import net.blueheart.hdebug.file.FileManager;
import net.blueheart.hdebug.ui.client.GuiBackground;
import net.blueheart.hdebug.ui.client.altmanager.sub.GuiDonatorCape;
import net.blueheart.hdebug.utils.EntityUtils;
import net.blueheart.hdebug.value.Value;

import java.io.*;
import java.util.Iterator;
import java.util.Map;

public class ValuesConfig extends FileConfig {

    /**
     * Constructor of config
     *
     * @param file of config
     */
    public ValuesConfig(final File file) {
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

        final JsonObject jsonObject = (JsonObject) jsonElement;

        final Iterator<Map.Entry<String, JsonElement>> iterator = jsonObject.entrySet().iterator();
        while(iterator.hasNext()) {
            final Map.Entry<String, JsonElement> entry = iterator.next();

            if (entry.getKey().equalsIgnoreCase("CommandPrefix")) {
                HDebug.commandManager.setPrefix(entry.getValue().getAsCharacter());
            } else if (entry.getKey().equalsIgnoreCase("targets")) {
                JsonObject jsonValue = (JsonObject) entry.getValue();

                if (jsonValue.has("TargetPlayer"))
                    EntityUtils.targetPlayer = jsonValue.get("TargetPlayer").getAsBoolean();
                if (jsonValue.has("TargetMobs"))
                    EntityUtils.targetMobs = jsonValue.get("TargetMobs").getAsBoolean();
                if (jsonValue.has("TargetAnimals"))
                    EntityUtils.targetAnimals = jsonValue.get("TargetAnimals").getAsBoolean();
                if (jsonValue.has("TargetInvisible"))
                    EntityUtils.targetInvisible = jsonValue.get("TargetInvisible").getAsBoolean();
                if (jsonValue.has("TargetDead"))
                    EntityUtils.targetDead = jsonValue.get("TargetDead").getAsBoolean();
            } else if (entry.getKey().equalsIgnoreCase("features")) {
                JsonObject jsonValue = (JsonObject) entry.getValue();

                if (jsonValue.has("AntiForge"))
                    AntiForge.enabled = jsonValue.get("AntiForge").getAsBoolean();
                if (jsonValue.has("AntiForgeFML"))
                    AntiForge.blockFML = jsonValue.get("AntiForgeFML").getAsBoolean();
                if (jsonValue.has("AntiForgeProxy"))
                    AntiForge.blockProxyPacket = jsonValue.get("AntiForgeProxy").getAsBoolean();
                if (jsonValue.has("AntiForgePayloads"))
                    AntiForge.blockPayloadPackets = jsonValue.get("AntiForgePayloads").getAsBoolean();
                if (jsonValue.has("BungeeSpoof"))
                    BungeeCordSpoof.enabled = jsonValue.get("BungeeSpoof").getAsBoolean();
            } else if (entry.getKey().equalsIgnoreCase("liquidchat")) {
                JsonObject jsonValue = (JsonObject) entry.getValue();

                if (jsonValue.has("token"))
                    IRC.Companion.setJwtToken(jsonValue.get("token").getAsString());
            } else if (entry.getKey().equalsIgnoreCase("DonatorCape")) {
                JsonObject jsonValue = (JsonObject) entry.getValue();

                if (jsonValue.has("TransferCode"))
                    GuiDonatorCape.Companion.setTransferCode(jsonValue.get("TransferCode").getAsString());

                if (jsonValue.has("CapeEnabled"))
                    GuiDonatorCape.Companion.setCapeEnabled(jsonValue.get("CapeEnabled").getAsBoolean());
            } else if (entry.getKey().equalsIgnoreCase("Background")) {
                JsonObject jsonValue = (JsonObject) entry.getValue();

                if (jsonValue.has("Enabled"))
                    GuiBackground.Companion.setEnabled(jsonValue.get("Enabled").getAsBoolean());

                if (jsonValue.has("Particles"))
                    GuiBackground.Companion.setParticles(jsonValue.get("Particles").getAsBoolean());
            } else {
                final Module module = HDebug.moduleManager.getModule(entry.getKey());

                if(module != null) {
                    final JsonObject jsonModule = (JsonObject) entry.getValue();

                    for(final Value moduleValue : module.getValues()) {
                        final JsonElement element = jsonModule.get(moduleValue.getName());

                        if(element != null) moduleValue.fromJson(element);
                    }
                }
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

        jsonObject.addProperty("CommandPrefix", HDebug.commandManager.getPrefix());

        final JsonObject jsonTargets = new JsonObject();
        jsonTargets.addProperty("TargetPlayer", EntityUtils.targetPlayer);
        jsonTargets.addProperty("TargetMobs", EntityUtils.targetMobs);
        jsonTargets.addProperty("TargetAnimals", EntityUtils.targetAnimals);
        jsonTargets.addProperty("TargetInvisible", EntityUtils.targetInvisible);
        jsonTargets.addProperty("TargetDead", EntityUtils.targetDead);
        jsonObject.add("targets", jsonTargets);

        final JsonObject jsonFeatures = new JsonObject();
        jsonFeatures.addProperty("AntiForge", AntiForge.enabled);
        jsonFeatures.addProperty("AntiForgeFML", AntiForge.blockFML);
        jsonFeatures.addProperty("AntiForgeProxy", AntiForge.blockProxyPacket);
        jsonFeatures.addProperty("AntiForgePayloads", AntiForge.blockPayloadPackets);
        jsonFeatures.addProperty("BungeeSpoof", BungeeCordSpoof.enabled);
        jsonObject.add("features", jsonFeatures);

        final JsonObject liquidChatObject = new JsonObject();
        liquidChatObject.addProperty("token", IRC.Companion.getJwtToken());
        jsonObject.add("liquidchat", liquidChatObject);

        final JsonObject capeObject = new JsonObject();
        capeObject.addProperty("TransferCode", GuiDonatorCape.Companion.getTransferCode());
        capeObject.addProperty("CapeEnabled", GuiDonatorCape.Companion.getCapeEnabled());
        jsonObject.add("DonatorCape", capeObject);

        final JsonObject backgroundObject = new JsonObject();
        backgroundObject.addProperty("Enabled", GuiBackground.Companion.getEnabled());
        backgroundObject.addProperty("Particles", GuiBackground.Companion.getParticles());
        jsonObject.add("Background", backgroundObject);

        HDebug.moduleManager.getModules().stream().filter(module -> !module.getValues().isEmpty()).forEach(module -> {
            final JsonObject jsonModule = new JsonObject();
            module.getValues().forEach(value -> jsonModule.add(value.getName(), value.toJson()));
            jsonObject.add(module.getName(), jsonModule);
        });

        final PrintWriter printWriter = new PrintWriter(new FileWriter(getFile()));
        printWriter.println(FileManager.PRETTY_GSON.toJson(jsonObject));
        printWriter.close();
    }
}