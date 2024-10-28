
    create table note (
        id bigint not null auto_increment,
        insert_at datetime(6),
        update_at datetime(6),
        content varchar(255),
        title varchar(255),
        primary key (id)
    ) engine=InnoDB;
