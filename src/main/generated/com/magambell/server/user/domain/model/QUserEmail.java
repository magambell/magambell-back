package com.magambell.server.user.domain.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QUserEmail is a Querydsl query type for UserEmail
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserEmail extends EntityPathBase<UserEmail> {

    private static final long serialVersionUID = 2125844627L;

    public static final QUserEmail userEmail = new QUserEmail("userEmail");

    public final com.magambell.server.common.QBaseTimeEntity _super = new com.magambell.server.common.QBaseTimeEntity(this);

    public final StringPath authCode = createString("authCode");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath email = createString("email");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final EnumPath<com.magambell.server.user.domain.enums.VerificationStatus> verificationStatus = createEnum("verificationStatus", com.magambell.server.user.domain.enums.VerificationStatus.class);

    public QUserEmail(String variable) {
        super(UserEmail.class, forVariable(variable));
    }

    public QUserEmail(Path<? extends UserEmail> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUserEmail(PathMetadata metadata) {
        super(UserEmail.class, metadata);
    }

}

