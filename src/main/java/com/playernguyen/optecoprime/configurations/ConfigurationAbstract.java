package com.playernguyen.optecoprime.configurations;

import com.google.common.base.Preconditions;
import com.osiris.dyml.DYModule;
import com.osiris.dyml.DreamYaml;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

import java.io.File;

public abstract class ConfigurationAbstract<T extends ConfigurationSectionModel> {

    private final DreamYaml dreamYaml;

    public ConfigurationAbstract(Plugin plugin,
                                 String fileName,
                                 Class<T> modelClass,
                                 @Nullable String parentFolder)
            throws Exception {
        // Setup plugin global folder
        Preconditions.checkState(
                plugin.getDataFolder().exists()
                        && plugin.getDataFolder().mkdir()
        );

        // Set a current base for plugin
        File parent = parentFolder == null
                ? plugin.getDataFolder()
                : new File(plugin.getDataFolder(), parentFolder);
        Preconditions.checkState(parent.exists() && parent.mkdir());
        File configFile = new File(parent, fileName);
        // Set the dream yaml object reference to parent
        this.dreamYaml = new DreamYaml(configFile);

        // Initialize configuration from model
        Preconditions.checkState(modelClass.isEnum());

        // Append all data to dreamYaml
        for (T item : modelClass.getEnumConstants()) {
            setInstanceData(item.getPath(),
                    item.getInstance(),
                    item.getComments()
            );
        }
    }

    public DreamYaml getDreamYaml() {
        return dreamYaml;
    }

    private void setInstanceData(String name, Object data, String... comments)
            throws Exception {
        this.dreamYaml
                .put(name.split("\\."))
                .setDefValues(data.toString())
                .setDefComments(comments);
    }

    /**
     * Get a {@link DYModule} which was configured contains persisted data.
     *
     * @param modelType a module type
     * @return a {@link DYModule} persisted data.
     */
    public DYModule get(T modelType) {
        return this.dreamYaml
                .get(modelType.getPath()
                        .split("\\."));
    }

}
