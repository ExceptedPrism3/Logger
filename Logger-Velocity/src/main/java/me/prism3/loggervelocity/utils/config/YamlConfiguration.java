package me.prism3.loggervelocity.utils.config;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.constructor.SafeConstructor;
import org.yaml.snakeyaml.representer.Representer;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class YamlConfiguration extends ConfigurationProvider {

    private final ThreadLocal<Yaml> yaml = ThreadLocal.withInitial(() -> {
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);

        Representer representer = new Representer(options) {
            {
                representers.put(Configuration.class, data -> represent(((Configuration) data).self));
            }
        };

        return new Yaml(new SafeConstructor(new LoaderOptions()), representer, options);
    });

    @Override
    public void save(Configuration config, File file) throws IOException {
        try (Writer writer = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8)) {
            save(config, writer);
        }
    }

    @Override
    public void save(Configuration config, Writer writer) {
        yaml.get().dump(config.self, writer);
    }

    @Override
    public Configuration load(File file) throws IOException {
        return load(file, null);
    }

    @Override
    public Configuration load(File file, Configuration defaults) throws IOException {
        try (FileInputStream is = new FileInputStream(file)) {
            return load(is, defaults);
        }
    }

    @Override
    public Configuration load(Reader reader) {
        return load(reader, null);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Configuration load(Reader reader, Configuration defaults) {
        Object loaded = yaml.get().load(reader);
        Map<String, Object> map = (loaded instanceof Map) ? (Map<String, Object>) loaded : new LinkedHashMap<>();
        return new Configuration(map, defaults);
    }

    @Override
    public Configuration load(InputStream is) {
        return load(is, null);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Configuration load(InputStream is, Configuration defaults) {
        Object loaded = yaml.get().load(is);
        Map<String, Object> map = (loaded instanceof Map) ? (Map<String, Object>) loaded : new LinkedHashMap<>();
        return new Configuration(map, defaults);
    }

    @Override
    public Configuration load(String string) {
        return load(string, null);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Configuration load(String string, Configuration defaults) {
        Object loaded = yaml.get().load(string);
        Map<String, Object> map = (loaded instanceof Map) ? (Map<String, Object>) loaded : new LinkedHashMap<>();
        return new Configuration(map, defaults);
    }
}
