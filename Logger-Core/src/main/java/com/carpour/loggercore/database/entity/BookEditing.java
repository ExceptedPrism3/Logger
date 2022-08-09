package com.carpour.loggercore.database.entity;

import javax.persistence.*;
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


    @Column(name = "page_count")
    private Integer pageCount;

    @Column(name = "page_content", length = 250)
    private String pageContent;

    @Column(name = "signed_by", length = 25)
    private String signedBy;

    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "entity_player_id", nullable = false)
    private EntityPlayer entityPlayer;

    public EntityPlayer getEntityPlayer() {
        return entityPlayer;
    }

    public void setEntityPlayer(EntityPlayer entityPlayer) {
        this.entityPlayer = entityPlayer;
    }

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


}