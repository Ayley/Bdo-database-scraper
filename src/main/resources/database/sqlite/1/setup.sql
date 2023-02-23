CREATE TABLE IF NOT EXISTS `item_ids` (
    id INTEGER PRIMARY KEY,
);

CREATE TABLE IF NOT EXISTS `languages` (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    item_id INTEGER,
    language_key INTEGER,
    meta_tag INTEGER,
    val TEXT,
    FOREIGN KEY(item_id) REFERENCES item(id)
);

CREATE TABLE IF NOT EXISTS `metadata` (
  id INTEGER PRIMARY KEY,
  item_id INTEGER,
  meta_tag INTEGER,
  val,
  FOREIGN KEY(id) REFERENCES item(id)
);
