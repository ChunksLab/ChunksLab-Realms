package com.chunkslab.realms.paper;

import io.papermc.paper.plugin.loader.PluginClasspathBuilder;
import io.papermc.paper.plugin.loader.PluginLoader;
import io.papermc.paper.plugin.loader.library.impl.MavenLibraryResolver;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.repository.RemoteRepository;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

public class PaperRealmsPluginLoader implements PluginLoader {

    @Override
    public void classloader(@NotNull PluginClasspathBuilder classpathBuilder) {
        final MavenLibraryResolver resolver = new MavenLibraryResolver();

        resolveLibraries(classpathBuilder).stream()
                .map(DefaultArtifact::new)
                .forEach(artifact -> resolver.addDependency(new Dependency(artifact, null)));


        resolver.addRepository(new RemoteRepository.Builder(
                "central", "default", "https://repo.maven.apache.org/maven2/"
        ).build());
        resolver.addRepository(new RemoteRepository.Builder(
                "xenondevs", "default", "https://repo.xenondevs.xyz/releases/"
        ).build());

        classpathBuilder.addLibrary(resolver);
    }

    @NotNull
    private static List<String> resolveLibraries(@NotNull PluginClasspathBuilder classpathBuilder) {
        try (InputStream input = getLibraryListFile()) {
            if (input == null) {
                throw new IllegalStateException("Failed to read libraries file");
            }

            Yaml yaml = new Yaml();
            Map<String, Object> data = yaml.load(input);

            Object librariesObj = data.get("libraries");
            if (librariesObj instanceof List) {
                @SuppressWarnings("unchecked")
                List<String> libraries = (List<String>) librariesObj;
                return libraries;
            }
        } catch (Throwable e) {
            classpathBuilder.getContext().getLogger().error("Failed to resolve libraries", e);
        }
        return List.of();
    }

    @Nullable
    private static InputStream getLibraryListFile() {
        return PaperRealmsPlugin.class.getClassLoader().getResourceAsStream("paper-libraries.yml");
    }
}