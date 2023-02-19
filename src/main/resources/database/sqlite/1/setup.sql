CREATE TABLE IF NOT EXIST item (
    id INTEGER PRIMARY KEY,
    name TEXT,
    grade INTEGER
);

CREATE TABLE IF NOT EXIST item_detail (
    id INTEGER PRIMARY KEY,
    level INTEGER,
    type TEXT,
    bound TEXT,
    description TEXT,
    eDescription TEXT,
    durability TEXT,
    weight TEXT,
    FOREIGN KEY(id) REFERENCES item(id)
    );

CREATE TABLE IF NOT EXIST item_stat (
    id INTEGER PRIMARY KEY,
    attack TEXT,
    defense TEXT,
    accuracy TEXT,
    evasion TEXT,
    damageReduction TEXT,
    FOREIGN KEY(id) REFERENCES item(id)
);