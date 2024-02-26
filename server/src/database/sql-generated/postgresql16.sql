
    create sequence calendar_seq_id start with 1 increment by 1;

    create sequence event_seq_id start with 1 increment by 1;

    create sequence eventmod_seq_id start with 1 increment by 1;

    create sequence eventref_seq_id start with 1 increment by 1;

    create sequence eventrep_seq_id start with 1 increment by 1;

    create table Calendar (
        ca_id bigint not null,
        ca_key uuid not null,
        ca_name varchar(255) not null,
        ca_owner varchar(255),
        ca_version bigint,
        primary key (ca_id),
        constraint calendar_uq_key unique (ca_key)
    );

    create table Event (
        ev_id bigint not null,
        ev_description text,
        ev_end timestamp(6) with time zone not null,
        ev_fullday boolean,
        ev_key uuid not null,
        ev_start timestamp(6) with time zone not null,
        ev_tags varchar(255) array,
        ev_title varchar(255) not null,
        ev_version bigint,
        ev_fk_calendar bigint not null,
        ev_fk_repeat_pattern bigint unique,
        primary key (ev_id),
        constraint event_uq_key unique (ev_key)
    );

    create table EventModification (
        em_type integer not null,
        em_id bigint not null,
        em_date date not null,
        em_end timestamp(6) with time zone,
        em_start timestamp(6) with time zone,
        em_description varchar(255),
        em_fk_event bigint not null,
        primary key (em_id)
    );

    create table EventReference (
        er_id bigint not null,
        er_fk_calendar bigint not null,
        er_fk_event bigint not null,
        primary key (er_id)
    );

    create table EventRepeat (
        er_type integer not null,
        er_id bigint not null,
        er_end timestamp(6) with time zone,
        er_interval smallint not null,
        er_recurrence_tz varchar(255) not null,
        er_start timestamp(6) with time zone not null,
        er_days_of_week smallint array,
        er_month smallint check (er_month between 0 and 11),
        er_day_of_month smallint,
        primary key (er_id)
    );

    alter table if exists Event 
       add constraint event_fkey_calendar 
       foreign key (ev_fk_calendar) 
       references Calendar;

    alter table if exists Event 
       add constraint event_fkey_repeatpattern 
       foreign key (ev_fk_repeat_pattern) 
       references EventRepeat;

    alter table if exists EventModification 
       add constraint eventmod_fkey_event 
       foreign key (em_fk_event) 
       references Event;

    alter table if exists EventReference 
       add constraint eventref_fkey_calendar 
       foreign key (er_fk_calendar) 
       references Calendar;

    alter table if exists EventReference 
       add constraint eventref_fkey_event 
       foreign key (er_fk_event) 
       references Event;
