package com.project.app.dto;

import lombok.Data;

@Data
public class PermissionRequest {
    private Long userId;
    private String permissionName;
}
