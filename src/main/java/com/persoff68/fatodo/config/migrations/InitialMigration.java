package com.persoff68.fatodo.config.migrations;

import com.github.mongobee.changeset.ChangeLog;
import com.github.mongobee.changeset.ChangeSet;
import com.persoff68.fatodo.config.constant.AuthorityType;
import com.persoff68.fatodo.model.Authority;
import org.springframework.data.mongodb.core.MongoTemplate;

@ChangeLog(order = "001")
public class InitialMigration {

    @ChangeSet(author = "initiator", id = "01-authorities", order = "01")
    public void addAuthorities(MongoTemplate mongoTemplate) {
        for (AuthorityType authorityType : AuthorityType.values()) {
            Authority authority = new Authority(authorityType.getValue());
            mongoTemplate.save(authority);
        }
    }

}
