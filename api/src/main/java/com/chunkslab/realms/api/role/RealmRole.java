package com.chunkslab.realms.api.role;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class RealmRole {
    private String id;
    private String name;
    private int weight;
    private List<RealmPermission> permissions;
}