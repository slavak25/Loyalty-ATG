drop table loyalty_transaction;
drop table user_loyalty_transactions;

commit work;

CREATE TABLE loyalty_transaction (
        id                      VARCHAR(32)     not null,
        amount                  INTEGER		    null,
        description             LONG VARCHAR    null,
        transaction_date        TIMESTAMP       null,       
        primary key(id)
        );
		
CREATE TABLE user_loyalty_transactions (
        user_id                VARCHAR(32)     not null references dps_user(id),
        loyalty_transaction    VARCHAR(32)     not null references loyalty_transaction(id),        
        primary key(user_id)
);

commit work;		