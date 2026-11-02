ALTER TABLE restaurants
ADD COLUMN owner_user_id BIGINT NULL;

ALTER TABLE restaurants
ADD CONSTRAINT fk_restaurants_owner
FOREIGN KEY (owner_user_id)
REFERENCES users(id)
ON DELETE SET NULL;

CREATE INDEX idx_restaurants_owner_user_id
ON restaurants(owner_user_id);