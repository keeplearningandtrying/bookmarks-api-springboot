package com.sivalabs.bookmarksapi.bookmarks.entities;

import com.sivalabs.bookmarksapi.common.entities.BaseEntity;
import com.sivalabs.bookmarksapi.users.entities.User;
import java.io.Serializable;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "bookmarks")
public class Bookmark extends BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bookmark_id_generator")
    @SequenceGenerator(
            name = "bookmark_id_generator",
            sequenceName = "bookmark_id_seq",
            allocationSize = 100)
    private Long id;

    @Column(nullable = false)
    @NotEmpty()
    private String url;

    @Column(nullable = false)
    @NotEmpty()
    private String title;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "bookmark_tag",
            joinColumns = {@JoinColumn(name = "bookmark_id", referencedColumnName = "ID")},
            inverseJoinColumns = {@JoinColumn(name = "tag_id", referencedColumnName = "ID")})
    private Set<Tag> tags;

    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;
}
