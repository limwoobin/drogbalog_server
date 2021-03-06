package com.drogbalog.server.domain.posts.domain.entity;

import com.drogbalog.server.global.code.Status;
import com.drogbalog.server.global.entity.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.math.BigInteger;

@Entity
@Getter
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "posts")
public class Posts extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false , length = 20)
    private long id;

    @Column(name = "category_id", length = 20, nullable = false)
    private long categoryId;

    @Column(nullable = false , length = 30)
    private String email;

    @Column(nullable = false , length = 50)
    private String subject;

    @Lob
    private String contents;

    @Column(length = 7)
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column
    private BigInteger views;

    @ManyToOne
    @JoinColumn(name = "category_id", insertable = false, updatable = false)
    private Categories categories;

    @Builder
    public Posts(String email , long categoryId , String subject , String contents) {
        this.email = email;
        this.categoryId = categoryId;
        this.subject = subject;
        this.contents = contents;
        this.status = Status.ACTIVE;
    }

    public void update(long categoryId , String subject , String contents) {
        this.categoryId = categoryId;
        this.subject = subject;
        this.contents = contents;
    }

    public Posts updateStatus(Status status) {
        this.status = status;
        return this;
    }

    public void addPostsViews() {
        this.views = views.add(BigInteger.ONE);
    }
}
