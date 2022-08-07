package com.carpour.loggercore.database.entity;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "book_editing")
public class BookEditing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "server_name", length = 30)
    private String serverName;

    @Column(name = "date", nullable = false)
    private Instant date;

    @Column(name = "world", length = 100)
    private String world;

    @Column(name = "player_name", length = 100)
    private String playerName;

    @Column(name = "page_count")
    private Integer pageCount;

    @Column(name = "page_content", length = 250)
    private String pageContent;

    @Column(name = "signed_by", length = 25)
    private String signedBy;

    @Column(name = "is_staff")
    private Boolean isStaff;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public String getWorld() {
        return world;
    }

    public void setWorld(String world) {
        this.world = world;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public Integer getPageCount() {
        return pageCount;
    }

    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
    }

    public String getPageContent() {
        return pageContent;
    }

    public void setPageContent(String pageContent) {
        this.pageContent = pageContent;
    }

    public String getSignedBy() {
        return signedBy;
    }

    public void setSignedBy(String signedBy) {
        this.signedBy = signedBy;
    }

    public Boolean getIsStaff() {
        return isStaff;
    }

    public void setIsStaff(Boolean isStaff) {
        this.isStaff = isStaff;
    }

}