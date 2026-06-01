package com.chat.bot.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "intents")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Intent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String pattern;

    /** The text the bot sends */
    @Column(columnDefinition = "TEXT")
    private String response;

    /**
     * "text" → plain reply, no menu rendered
     * "menu" → reply + clickable child items shown below
     */
    @Column(name = "response_type", length = 10)
    private String responseType = "text";

    /** NULL = root intent; otherwise points to parent menu intent */
    @Column(name = "parent_id")
    private Long parentId;

    /** Sibling sort order inside the same parent */
    @Column(name = "display_order")
    private int displayOrder = 0;

    @Column(name = "is_active")
    private boolean isActive = true;

    // ── Convenience: child intents (not mapped as FK, loaded by service) ──
    @Transient
    private List<Intent> children;
}