create table if not exists USER (
    id  BIGINT AUTO_INCREMENT ,
    name VARCHAR(50) NOT NULL,
    username VARCHAR(50) NOT NULL UNIQUE ,
    password VARCHAR(250) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    primary key (id)
);

create table if not exists NOTE (
    id BIGINT AUTO_INCREMENT ,
    user_id BIGINT,
    name VARCHAR(50) NOT NULL,
    content TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    primary key (id)
);

alter table NOTE
add constraint fk_note_user foreign key (user_id) references USER(id);
