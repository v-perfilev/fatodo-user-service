package com.persoff68.fatodo.config.database.migrations;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
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
