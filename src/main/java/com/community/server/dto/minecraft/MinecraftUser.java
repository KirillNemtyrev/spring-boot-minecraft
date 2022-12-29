package com.community.server.dto.minecraft;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.community.server.utils.Base64Utils.base64Encoded;
import static com.community.server.utils.KeyUtils.sign;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MinecraftUser {
    private String uuid;
    private String name;
    private ModelType model = ModelType.STEVE;
    private Map<TextureType, Texture> textures;
    private Set<TextureType> uploadableTextures;

    @SuppressWarnings("unchecked")
    public Map<String, Object> toCompleteResponse(boolean signed) {
        LinkedHashMap texturesResponse = new LinkedHashMap<>();
        textures.forEach((type, texture) -> texturesResponse.put(type, type.getMetadata(this)
                .map(metadata -> {
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("url", texture.url);
                    map.put("metadata", metadata);
                    return map;
                })
                .orElseGet(() -> {
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("url", texture.url);
                    return map;
                })
        ));

        ArrayList<Pair<String, String>> properties = new ArrayList<>();
        properties.add(
                Pair.of("textures", base64Encoded(
                        Pair.of("timestamp", System.currentTimeMillis()),
                        Pair.of("profileId", uuid),
                        Pair.of("profileName", name),
                        Pair.of("textures", texturesResponse)
                ))
        );

        if (!uploadableTextures.isEmpty()) {
            properties.add(
                    Pair.of("uploadableTextures",
                            uploadableTextures.stream()
                                    .map(type -> type.name().toLowerCase())
                                    .collect(joining(","))
                    )
            );
        }

        HashMap<String, Object> map = new HashMap<>();
        map.put("id", uuid);
        map.put("name", name);
        map.put("properties", mapProperties(signed, properties));
        return map;
    }

    public static List<?> mapProperties(boolean isSign, ArrayList<Pair<String, String>> entries) {
        return entries.stream()
                .map(entry -> {
                    LinkedHashMap<String, Object> property = new LinkedHashMap<>();
                    property.put("name", entry.getFirst());
                    property.put("value", entry.getSecond());
                    if (isSign) {
                        property.put("signature", sign(entry.getSecond()));
                    }
                    return property;
                })
                .collect(toList());
    }


}
