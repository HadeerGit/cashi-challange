SET MODE REGULAR;
CREATE TABLE fee_types
(
    id    INTEGER  NOT NULL,
    type VARCHAR(50) NOT NULL,
    description VARCHAR(255)
);

CREATE TABLE transactions
(
    transaction_id VARCHAR(60) NOT NULL,
    amount         decimal,
    asset VARCHAR (10) NOT NULL,
    asset_type     VARCHAR(10) NOT NULL,
    type_id        INTEGER     NOT NULL,
    state          VARCHAR(50) NOT NULL,
    fee            decimal,
    created_at     timestamp   NOT NULL,
    PRIMARY KEY (transaction_id),
    FOREIGN KEY (type_id) references fee_types (id)
);
