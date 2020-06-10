package com.persoff68.fatodo.config.database.migrations;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.github.cloudyrock.mongock.driver.mongodb.springdata.v3.decorator.impl.MongockTemplate;
import com.persoff68.fatodo.config.constant.AuthorityType;
import com.persoff68.fatodo.model.Authority;

@ChangeLog(order = "001")
public class InitialMigration {

    @ChangeSet(author = "initiator", id = "01-authorities", order = "01")
    public void addAuthorities(MongockTemplate mongockTemplate) {
        for (AuthorityType authorityType : AuthorityType.values()) {
            Authority authority = new Authority(authorityType.getValue());
            mongockTemplate.save(authority);
        }
    }

}
