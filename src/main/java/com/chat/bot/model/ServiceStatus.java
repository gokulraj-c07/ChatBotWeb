package com.chat.bot.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "service_status")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String serviceName;
    private String status;
    private String osVersion;
    private String osName;
    private String osArchitecture;
    private LocalDateTime checkedAt;
}
