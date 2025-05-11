CREATE TABLE IF NOT EXISTS inventory_item (
  id TEXT       PRIMARY KEY,
  type TEXT     NOT NULL,
  available_count INTEGER NOT NULL,
  price REAL    NOT NULL
);
