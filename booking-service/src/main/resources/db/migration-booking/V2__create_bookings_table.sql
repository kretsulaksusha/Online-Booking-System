CREATE TABLE booking (
  id          TEXT    PRIMARY KEY,
  created_at  INTEGER NOT NULL,         -- було TIMESTAMP, стало INTEGER
  item_id     TEXT    NOT NULL,
  quantity    INTEGER NOT NULL
);