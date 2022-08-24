package me.prism3.loggercore.database.entity;

import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

@Entity
@Table(name = "book_editing")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class BookEditing extends AbstractAction {

    @Column(name = "world", length = 100)
    private String world;

    @Column(name = "page_count")
    private Integer pageCount;

    @Column(name = "page_content", length = 250)
    private String pageContent;

    @Column(name = "signed_by", length = 25)
    private String signedBy;

    @Column(name = "is_staff")
    private Boolean isStaff;

    public String getWorld() {
        return this.world;
    }

    public void setWorld(String world) {
        this.world = world;
    }

    public Integer getPageCount() {
        return this.pageCount;
    }

    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
    }

    public String getPageContent() {
        return this.pageContent;
    }

    public void setPageContent(String pageContent) {
        this.pageContent = pageContent;
    }

    public String getSignedBy() {
        return this.signedBy;
    }

    public void setSignedBy(String signedBy) {
        this.signedBy = signedBy;
    }

    public Boolean isStaff() {
        return this.isStaff;
    }

    public void isStaff(Boolean staff) {
        this.isStaff = staff;
    }

    @Override
    public String getAction() { return this.entityPlayer.getPlayerName() + " book editing"; }
}