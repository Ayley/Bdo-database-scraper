CREATE TABLE IF NOT EXISTS 'item' (
    id INTEGER PRIMARY KEY,
    name TEXT,
    grade INTEGER
);

CREATE TABLE IF NOT EXISTS 'item_detail' (
    id INTEGER PRIMARY KEY,
    level INTEGER,
    type TEXT,
    bound TEXT,
    description TEXT,
    e_description TEXT,
    durability float,
    weight float,
    FOREIGN KEY(id) REFERENCES item(id)
    );

CREATE TABLE IF NOT EXISTS 'item_stat' (
    id INTEGER PRIMARY KEY,
    attack TEXT,
    defense TEXT,
    accuracy TEXT,
    evasion TEXT,
    damageReduction TEXT,
    FOREIGN KEY(id) REFERENCES item(id)
);